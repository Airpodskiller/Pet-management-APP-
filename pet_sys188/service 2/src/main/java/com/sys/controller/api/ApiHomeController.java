package com.sys.controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.common.HttpBaseRequest;
import com.sys.common.MySqlHelper;
import com.sys.common.RespData;
import com.sys.common.interceptor.api.ApiAnnotation;
import com.sys.common.interceptor.api.ApiContext;
import com.sys.common.interceptor.api.ApiLoginModel;
import com.sys.dao.*;
import com.sys.entity.*;
import com.sys.modules.HuiPageModel;
import com.sys.modules.JsonModel;
import com.sys.modules.ListGageModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/home")
@ApiAnnotation
public class ApiHomeController {
    @Autowired
    private SysPetMapper sysPetMapper;
    @Autowired
    private SysPetTestMapper sysPetTestMapper;
    @Autowired
    private SysPetIllMapper sysPetIllMapper;
    @Autowired
    private SysNotifyMapper sysNotifyMapper;
    @Autowired
    private SysPetVaccineConfigMapper sysPetVaccineConfigMapper;
    @Autowired
    private SysPetVaccineMapper sysPetVaccineMapper;
    @Autowired
    private SysPetVaccineLogMapper sysPetVaccineLogMapper;
    @Autowired
    private SysPetSterilizationMapper sysPetSterilizationMapper;
    @Autowired
    private SysPetShowerTimeMapper sysPetShowerTimeMapper;
    @Autowired
    private SysPetSterilizationConfigMapper sysPetSterilizationConfigMapper;
    @Autowired
    private SysMemberMapper sysMemberMapper;
    @Autowired
    private SysPetShowerMapper sysPetShowerMapper;
    @Autowired
    private SysPetSymptomsMapper sysPetSymptomsMapper;
    //分页查询我的宠物
    @RequestMapping("/petList")
    @ResponseBody
    public JsonModel petList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        String petType = baseRequest.getString("petType");
        String petInfo = baseRequest.getString("petInfo");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPet> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and a.member_id = " + loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_id desc";
            String strTable = " sys_pet a left join sys_member b on a.member_id = b.member_id";
            String strQuery = " a.pet_id as petId,a.pet_name as petName,a.pet_type as petType,a.pet_info as petInfo,a.pet_age as petAge," +
                    "a.pet_color as petColor,a.pet_color as petColor,a.pet_detail as petDetail,a.member_id as memberId," +
                    "a.weight as weight,a.is_sterilization as isSterilization," +
                    "a.gps_info as gpsInfo,a.create_time as createTime,b.account as account,b.user_name as userName ";
            if (!userName.equals(""))
                strWhere += " and b.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and a.pet_name like '%" + petName + "%'";
            if (!petType.equals(""))
                strWhere += " and a.pet_type like '%" + petType + "%'";
            if (!petInfo.equals(""))
                strWhere += " and a.pet_info like '%" + petInfo + "%'";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPet>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }

    @RequestMapping("/petInfo")
    @ResponseBody
    public JsonModel petInfo(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
        if(sysPet == null){
            return RespData.JsonError("不存在该记录");
        }
        return RespData.JsonOK(sysPet);
    }

    //添加或者修改宠物
    @RequestMapping("/addPet")
    @ResponseBody
    public JsonModel addPet(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        long petId = baseRequest.getLong("petId");
        int isSterilization = baseRequest.getInt("isSterilization");
        int weight = baseRequest.getInt("weight");
        String petName = baseRequest.getString("petName");
        String petType = baseRequest.getString("petType");
        String petInfo = baseRequest.getString("petInfo");
        String petAge = baseRequest.getString("petAge");
        String petColor = baseRequest.getString("petColor");
        String petDetail = baseRequest.getString("petDetail");
        String vaccine = baseRequest.getString("vaccine");
        if(petId > 0){
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
            if(sysPet == null){
                return RespData.JsonError("不存在该数据");
            }
            sysPet.setIsSterilization(isSterilization);
            sysPet.setWeight(weight);
            sysPet.setPetName(petName);
            sysPet.setPetAge(petAge);
            sysPet.setPetType(petType);
            sysPet.setPetDetail(petDetail);
            sysPet.setPetInfo(petInfo);
            sysPet.setPetColor(petColor);
            int result = sysPetMapper.updateByPrimaryKeySelective(sysPet);
            if(result > 0){
                if(!vaccine.equals("")){
                    //逗号分割
                    String[]arr = vaccine.split(",");
                    for (int i = 0;i<arr.length;i++){
                        //查询是否已经添加过
                        List<SysPetVaccineLog> sysPetVaccineLogs = sysPetVaccineLogMapper.selectList(" and pet_id = "+sysPet.getPetId()+" and pet_vaccine_config_id = "+arr[i]);
                        if(sysPetVaccineLogs.size() == 0){
                            //添加
                            SysPetVaccineLog sysPetVaccineLog = new SysPetVaccineLog();
                            sysPetVaccineLog.setCreateTime(new Date());
                            sysPetVaccineLog.setPetVaccineConfigId(Long.parseLong(arr[i]));
                            sysPetVaccineLog.setPetId(sysPet.getPetId());
                            sysPetVaccineLogMapper.insertSelective(sysPetVaccineLog);
                        }
                    }
                    //删除没用的
                    List<SysPetVaccineLog> sysPetVaccineLogs = sysPetVaccineLogMapper.selectList(" and pet_id = "+sysPet.getPetId()+" and pet_vaccine_config_id not in ("+vaccine+")");
                    for (int i = 0;i<sysPetVaccineLogs.size();i++){
                        sysPetVaccineLogMapper.deleteByPrimaryKey(sysPetVaccineLogs.get(i).getVaccineLogId());
                    }
                }
                return RespData.JsonOK("保存成功");
            }else {
                return RespData.JsonError("保存失败");
            }
        }else {
            SysPet sysPet = new SysPet();
            sysPet.setIsSterilization(isSterilization);
            sysPet.setWeight(weight);
            sysPet.setPetName(petName);
            sysPet.setPetAge(petAge);
            sysPet.setPetType(petType);
            sysPet.setPetDetail(petDetail);
            sysPet.setPetInfo(petInfo);
            sysPet.setPetColor(petColor);
            sysPet.setMemberId(loginModel.sysMember.getMemberId());
            int result = sysPetMapper.insertSelective(sysPet);
            if(result > 0){
                if(!vaccine.equals("")){
                    //逗号分割
                    String[]arr = vaccine.split(",");
                    for (int i = 0;i<arr.length;i++){
                        //添加
                        SysPetVaccineLog sysPetVaccineLog = new SysPetVaccineLog();
                        sysPetVaccineLog.setCreateTime(new Date());
                        sysPetVaccineLog.setPetVaccineConfigId(Long.parseLong(arr[i]));
                        sysPetVaccineLog.setPetId(sysPet.getPetId());
                        sysPetVaccineLogMapper.insertSelective(sysPetVaccineLog);
                    }
                }
                return RespData.JsonOK("保存成功");
            }else {
                return RespData.JsonError("保存失败");
            }
        }
    }

    //删除宠物
    @RequestMapping("/deletePet")
    @ResponseBody
    public JsonModel deletePet(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        if(petId <= 0){
            return RespData.JsonError("请选择删除数据");
        }
        int result = sysPetMapper.deleteByPrimaryKey(petId);
        if(result > 0){
            return RespData.JsonOK("删除成功");
        }else {
            return RespData.JsonError("删除失败");
        }
    }

    //宠物发起一条体检
    @RequestMapping("/addPetTest")
    @ResponseBody
    public JsonModel addPetTest(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        if(petId <= 0){
            return RespData.JsonError("请选择宠物");
        }
        SysPetTest sysPetTest = new SysPetTest();
        sysPetTest.setIsOver(0);
        sysPetTest.setIsNotify(0);
        sysPetTest.setPetId(petId);
        int result = sysPetTestMapper.insertSelective(sysPetTest);
        if(result > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //宠物发起一条治疗申请
    @RequestMapping("/addPetIll")
    @ResponseBody
    public JsonModel addPetIll(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        //修改
        long testId = baseRequest.getLong("testId");
        SysPetTest sysPetTest = sysPetTestMapper.selectByPrimaryKey(testId);
        if(sysPetTest == null){
            return RespData.JsonError("不存在该记录");
        }
        if(sysPetTest.getTestIll() == null || sysPetTest.getTestIll() == -1){
            return RespData.JsonError("该宠物不需要治疗");
        }

        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetTest.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该记录");
        }
        //查询疾病
        SysPetSymptoms sysPetSymptoms = sysPetSymptomsMapper.selectByPrimaryKey(sysPetTest.getTestIll());
        if(sysPetSymptoms == null){
            return RespData.JsonError("不存在该疾病");
        }
        //判断金额是否够
        if(loginModel.sysMember.getMoneys().doubleValue() < sysPetSymptoms.getMoneys().doubleValue()){
            return RespData.JsonError("您的余额不足，请先充值");
        }
        SysPetIll sysPetIll = new SysPetIll();
        sysPetIll.setIsOver(0);
        sysPetIll.setIsNotify(0);
        sysPetIll.setPetId(sysPet.getPetId());
        sysPetIll.setIllDetail(sysPetSymptoms.getPetIll());
        int result = sysPetIllMapper.insertSelective(sysPetIll);
        if(result > 0){
            sysPetTest.setStatus(1);
            sysPetTestMapper.updateByPrimaryKeySelective(sysPetTest);
            //扣除金额
            SysMember sysMember = loginModel.sysMember;
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() - sysPetSymptoms.getMoneys().doubleValue()));
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("下单成功");
        }else {
            return RespData.JsonError("下单失败");
        }
    }

    @RequestMapping("/cancelIll")
    @ResponseBody
    public JsonModel cancelIll(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long testId = baseRequest.getLong("testId");
        //查询测试
        SysPetTest sysPetTest = sysPetTestMapper.selectByPrimaryKey(testId);
        if(sysPetTest == null){
            return RespData.JsonError("不存在该记录");
        }
        sysPetTest.setStatus(1);
        int result = sysPetTestMapper.updateByPrimaryKeySelective(sysPetTest);
        if(result > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //轮询是否有消息通知
    @RequestMapping("/notifyMessage")
    @ResponseBody
    public JsonModel notifyMessage(){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        List<SysNotify> sysNotifies = sysNotifyMapper.selectByMemberId(loginModel.sysMember.getMemberId());
        //通知过了不要再通知
        for (int i = 0;i<sysNotifies.size();i++){
            sysNotifies.get(i).setStatus(1);
            sysNotifyMapper.updateByPrimaryKeySelective(sysNotifies.get(i));
        }
        return RespData.JsonOK(sysNotifies);
    }

    @RequestMapping("/petIllList")
    @ResponseBody
    public JsonModel petIllList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetIll> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and c.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_ill_id desc";
            String strTable = " sys_pet_ill a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id ";
            String strQuery = " a.pet_ill_id as petIllId,a.pet_id as petId,a.ill_detail as illDetail,a.is_over as isOver,a.is_over as isOver," +
                    "a.is_notify as isNotify,a.create_time as createTime," +
                    "a.status as status,a.moneys as moneys," +
                    "b.pet_name as petName,c.user_name as userName,c.account as account ";

            strWhere += " and a.pet_id = " + petId;
            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetIll>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }

    @RequestMapping("/petTestList")
    @ResponseBody
    public JsonModel petTestList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetTest> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and c.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_test_id desc";
            String strTable = " sys_pet_test a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id " +
                    "left join sys_pet_symptoms d on a.test_ill = d.pet_symptoms_id";
            String strQuery = " a.pet_test_id as petTestId,a.pet_id as petId,a.is_over as isOver,a.is_notify as isNotify," +
                    "a.test_ill as testIll,a.status as status," +
                    "a.create_time as createTime,b.pet_name as petName,c.user_name as userName,c.account as account,d.pet_ill as illName ";
            strWhere += " and a.pet_id = " + petId;
            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetTest>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }

    //分页获取所有消息
    @RequestMapping("/notifyList")
    @ResponseBody
    public JsonModel notifyList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysNotify> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and a.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.notify_id desc";
            String strTable = " sys_notify a ";
            String strQuery = " a.member_id as memberId,a.type as type,a.key_id as keyId,a.title as title,a.message as message,a.status as status,a.create_time as createTime";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysNotify>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }

    //查询宠物是否打疫苗
    @RequestMapping("/petVaccineLog")
    @ResponseBody
    public JsonModel petVaccineLog(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        int petType = baseRequest.getInt("petType",0);
        if(petId > 0){
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
            petType = sysPet.getPetType().equals("猫") ? 0 : 1;
        }
        //查询宠物类型
        //获取
        List<SysPetVaccineConfig> sysPetVaccineConfigs = sysPetVaccineConfigMapper.selectList(" and pet_type = "+petType);
        //查询宠物是否接种
        for (int i = 0;i<sysPetVaccineConfigs.size();i++){
            sysPetVaccineConfigs.get(i).setIsSelect(0);
            if(petId > 0){
                List<SysPetVaccineLog> sysPetVaccineLogs = sysPetVaccineLogMapper.selectList(" and pet_id = "+petId+" and pet_vaccine_config_id = "+sysPetVaccineConfigs.get(i).getPetVaccineConfigId());
                if(sysPetVaccineLogs.size() > 0){
                    sysPetVaccineConfigs.get(i).setIsSelect(1);
                }else {
                    sysPetVaccineConfigs.get(i).setIsOver(-1);
                    //查询订单
                    List<SysPetVaccine> sysPetVaccines = sysPetVaccineMapper.selectList(" and pet_id = "+petId+" and pet_vaccine_config_id = "+sysPetVaccineConfigs.get(i).getPetVaccineConfigId());
                    if(sysPetVaccines.size() > 0){
                        //加上
                        sysPetVaccineConfigs.get(i).setIsOver(sysPetVaccines.get(0).getIsOver());
                        sysPetVaccineConfigs.get(i).setCreateTime(sysPetVaccines.get(0).getCreateTime());
                    }
                }
            }
        }
        return RespData.JsonOK(sysPetVaccineConfigs);
    }

    //洗澡记录
    @RequestMapping("/petShowerList")
    @ResponseBody
    public JsonModel petShowerList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetShower> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and c.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_shower_id desc";
            String strTable = " sys_pet_shower a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id ";
            String strQuery = "a.pet_shower_id as petShowerId, a.pet_id as petId, a.pet_showe_type as petShoweType, a.is_over as isOver, a.is_notify as isNotify," +
                    " a.moneys as moneys, a.status as status, a.create_time as createTime," +
                    "b.pet_name as petName,c.user_name as userName,c.account as account ";
            strWhere += " and a.pet_id = " + petId;
            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetShower>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0;i<list.size();i++){
            //查询宠物
            list.get(i).setTime("");
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(list.get(i).getPetId());
            if(sysPet != null){
                List<SysPetShowerTime> sysPetShowerTimes = sysPetShowerTimeMapper.selectList(" and min_kg <= "+sysPet.getWeight() + " and max_kg >= " + sysPet.getWeight());
                if(sysPetShowerTimes.size() > 0){
                    list.get(i).setTime(sysPetShowerTimes.get(0).getTime()+"分钟");
                }
            }
        }
        return RespData.JsonOK(list);
    }

    //绝育记录
    @RequestMapping("/petSterilizationList")
    @ResponseBody
    public JsonModel petSterilizationList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        List<SysPetSterilization> sysPetSterilizations = sysPetSterilizationMapper.selectList(" and pet_id = " + petId);
        return RespData.JsonOK(sysPetSterilizations);
    }

    //查询绝育价格
    @RequestMapping("/petSterilizationConfig")
    @ResponseBody
    public JsonModel petSterilizationConfig(HttpServletRequest request){
        //查询
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int petType = baseRequest.getInt("petType",0);
        //查询数据
        List<SysPetSterilizationConfig> sysPetSterilizationConfigs = sysPetSterilizationConfigMapper.selectList(" and pet_type = "+petType);
        if(sysPetSterilizationConfigs.size() == 0){
            return RespData.JsonError("不存在该记录");
        }
        //查询价格
        return RespData.JsonOK(sysPetSterilizationConfigs.get(0));
    }

    //添加绝育记录
    @RequestMapping("/addSterilization")
    @ResponseBody
    public JsonModel addSterilization(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        //添加绝育
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
        if(sysPet == null){
            return RespData.JsonError("不存在该记录");
        }
        if(sysPet.getIsSterilization() == 1){
            return RespData.JsonError("该宠物已绝育，请勿重复点击");
        }
        //判断是否已经创建了
        List<SysPetSterilizationConfig> sysPetSterilizationConfigs = sysPetSterilizationConfigMapper.selectList(" and pet_type = "+(sysPet.getPetType().equals("猫") ? 0 : 1));
        if(sysPetSterilizationConfigs.size() == 0){
            return RespData.JsonError("不存在该记录");
        }
        List<SysPetSterilization> sysPetSterilizations = sysPetSterilizationMapper.selectList(" and pet_id = "+sysPet.getPetId());
        if(sysPetSterilizations.size() > 0){
            return RespData.JsonError("您已申请宠物绝育，请勿重复申请");
        }
        if(loginModel.sysMember.getMoneys().doubleValue() < sysPetSterilizationConfigs.get(0).getMoneys().doubleValue()){
            return RespData.JsonError("您的余额不足，请先充值");
        }
        SysPetSterilization sysPetSterilization = new SysPetSterilization();
        sysPetSterilization.setStatus(1);
        sysPetSterilization.setIsOver(0);
        sysPetSterilization.setIsNotify(0);
        sysPetSterilization.setMoneys(sysPetSterilizationConfigs.get(0).getMoneys());
        sysPetSterilization.setPetId(petId);
        sysPetSterilization.setCreateTime(new Date());
        int result = sysPetSterilizationMapper.insertSelective(sysPetSterilization);
        if(result > 0){
            //扣除金额
            SysMember sysMember = loginModel.sysMember;
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() - sysPetSterilization.getMoneys().doubleValue()));
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("下单成功");
        }else {
            return RespData.JsonError("下单失败");
        }
    }

    //添加洗澡的
    @RequestMapping("/addShower")
    @ResponseBody
    public JsonModel addShower(HttpServletRequest request){
        //添加洗澡
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        String shower = baseRequest.getString("shower");
        double moneys = baseRequest.getDouble("moneys",0);
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
        if(sysPet == null){
            return RespData.JsonError("不存在该记录");
        }
        if(loginModel.sysMember.getMoneys().doubleValue() < moneys){
            return RespData.JsonError("您的余额不足，请先充值");
        }
        SysPetShower sysPetShower = new SysPetShower();
        sysPetShower.setStatus(1);
        sysPetShower.setIsOver(0);
        sysPetShower.setIsNotify(0);
        sysPetShower.setMoneys(new BigDecimal(moneys));
        sysPetShower.setPetId(petId);
        sysPetShower.setCreateTime(new Date());
        sysPetShower.setPetShoweType(shower);
        int result = sysPetShowerMapper.insertSelective(sysPetShower);
        if(result > 0){
            //扣除金额
            SysMember sysMember = loginModel.sysMember;
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() - sysPetShower.getMoneys().doubleValue()));
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("下单成功");
        }else {
            return RespData.JsonError("下单失败");
        }
    }

    //添加疫苗
    @RequestMapping("/addVaccine")
    @ResponseBody
    public JsonModel addVaccine(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petId = baseRequest.getLong("petId");
        long configId = baseRequest.getLong("configId");
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(petId);
        if(sysPet == null){
            return RespData.JsonError("不存在该记录");
        }
        //查询配置
        SysPetVaccineConfig config = sysPetVaccineConfigMapper.selectByPrimaryKey(configId);
        if(config == null){
            return RespData.JsonError("不存在该记录");
        }
        if(loginModel.sysMember.getMoneys().doubleValue() < config.getMoneys().doubleValue()){
            return RespData.JsonError("您的余额不足，请先充值");
        }
        //查询是否已经正在接种
        List<SysPetVaccine> sysPetVaccines = sysPetVaccineMapper.selectList(" and pet_id = "+petId+" and pet_vaccine_config_id = "+config.getPetVaccineConfigId());
        if(sysPetVaccines.size() > 0){
            return RespData.JsonError("宠物正在接种，请勿宠物下单");
        }
        SysPetVaccine sysPetVaccine = new SysPetVaccine();
        sysPetVaccine.setStatus(1);
        sysPetVaccine.setIsOver(0);
        sysPetVaccine.setPetId(petId);
        sysPetVaccine.setPetVaccineConfigId(configId);
        sysPetVaccine.setCreateTime(new Date());
        sysPetVaccine.setIsNotify(0);
        sysPetVaccine.setMoneys(config.getMoneys());
        int result = sysPetVaccineMapper.insertSelective(sysPetVaccine);
        if(result > 0){
            //扣除金额
            SysMember sysMember = loginModel.sysMember;
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() - config.getMoneys().doubleValue()));
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("下单成功");
        }else {
            return RespData.JsonError("下单失败");
        }
    }
}
