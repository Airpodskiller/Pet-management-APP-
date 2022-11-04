package com.sys.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.common.HttpBaseRequest;
import com.sys.common.MySqlHelper;
import com.sys.common.RespData;
import com.sys.common.interceptor.admin.AdminAnnotation;
import com.sys.common.interceptor.admin.AdminLoginModel;
import com.sys.dao.*;
import com.sys.entity.*;
import com.sys.modules.HuiPageModel;
import com.sys.modules.JsonModel;
import com.sys.modules.ListGageModule;
import com.sys.modules.WebUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin/home")
@AdminAnnotation()
public class HomeController {
    @Autowired
    private SysPetTestMapper sysPetTestMapper;
    @Autowired
    private SysPetIllMapper sysPetIllMapper;
    @Autowired
    private SysNotifyMapper sysNotifyMapper;
    @Autowired
    private SysPetMapper sysPetMapper;
    @Autowired
    private SysPetSymptomsMapper sysPetSymptomsMapper;
    @Autowired
    private SysPetShowerMapper sysPetShowerMapper;
    @Autowired
    private SysPetSterilizationMapper sysPetSterilizationMapper;
    @Autowired
    private SysPetVaccineMapper sysPetVaccineMapper;
    @Autowired
    private SysMemberMapper sysMemberMapper;
    @Autowired
    private SysPetVaccineLogMapper sysPetVaccineLogMapper;

    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        AdminLoginModel user = (AdminLoginModel) request.getSession().getAttribute(WebUtility.ADMINLOGIN);
        view.addObject("name",user.sysUser.getUserName());
        view.addObject("account",user.sysUser.getAccount());
        view.setViewName("/admin/home");
        return view;
    }

    //宠物信息
    @RequestMapping("/pet")
    public ModelAndView pet(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/pet");
        return view;
    }

    @RequestMapping("/petList")
    @ResponseBody
    public HuiPageModel<SysPet> petList(HttpServletRequest request){

        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPet> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        String petType = baseRequest.getString("petType");
        String petInfo = baseRequest.getString("petInfo");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
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
            List<SysPet> list = gson.fromJson(json, new TypeToken<List<SysPet>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //宠物体检信息

    @RequestMapping("/petTest")
    public ModelAndView petTest(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/petTest");
        return view;
    }

    @RequestMapping("/petTestList")
    @ResponseBody
    public HuiPageModel<SysPetTest> petTestList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetTest> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_test_id desc";
            String strTable = " sys_pet_test a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id " +
                    "left join sys_pet_symptoms d on a.test_ill = d.pet_symptoms_id";
            String strQuery = " a.pet_test_id as petTestId,a.pet_id as petId,a.is_over as isOver,a.is_notify as isNotify," +
                    "a.test_ill as testIll,a.status as status," +
                    "a.create_time as createTime,b.pet_name as petName,c.user_name as userName,c.account as account,d.pet_ill as illName ";
            if (!userName.equals(""))
                strWhere += " and c.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and b.pet_name like '%" + petName + "%'";

            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetTest> list = gson.fromJson(json, new TypeToken<List<SysPetTest>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @RequestMapping("/confirmTest")
    @ResponseBody
    public JsonModel confirmTest(HttpServletRequest request) throws InterruptedException {
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petTestId = baseRequest.getInt("petTestId", 0);
        if(petTestId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetTest sysPetTest = sysPetTestMapper.selectByPrimaryKey(petTestId);
        if(sysPetTest == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetTest.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        Thread.sleep(3000);
        //生成体检的疾病
        Random rand = new Random();
        int n1=rand.nextInt(100);//返回值在范围[0,100) 即[0,99]
        String message = "";
        Long ill = -1l;
        if(n1 > 25){
            //ill
            List<SysPetSymptoms> sysPetSymptoms = sysPetSymptomsMapper.selectList(" and pet_type = "+(sysPet.getPetType().equals("猫") ? 0 : 1));
            int num = rand.nextInt(sysPetSymptoms.size());
            SysPetSymptoms petSymptoms = sysPetSymptoms.get(num);
            ill = petSymptoms.getPetSymptomsId();
            message = "宠物诊断出【"+petSymptoms.getPetIll()+"】";
        }else {
            message = "宠物很健康";
            sysPetTest.setStatus(1);
        }
        sysPetTest.setTestIll(ill);
        sysPetTest.setCreateTime(new Date());
        sysPetTest.setIsOver(1);
        int res = sysPetTestMapper.updateByPrimaryKeySelective(sysPetTest);
        if(res > 0){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetTest.getPetTestId());
            sysNotify.setType(0);
            sysNotify.setTitle("宠物体检通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPet.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成体检，" + message);
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //宠物病情信息
    @RequestMapping("/petIll")
    public ModelAndView petIll(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/petIll");
        return view;
    }

    @RequestMapping("/petIllList")
    @ResponseBody
    public HuiPageModel<SysPetIll> petIllList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetIll> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_ill_id desc";
            String strTable = " sys_pet_ill a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id ";
            String strQuery = " a.pet_ill_id as petIllId,a.pet_id as petId,a.ill_detail as illDetail,a.is_over as isOver,a.is_over as isOver," +
                    "a.is_notify as isNotify,a.create_time as createTime," +
                    "a.status as status,a.moneys as moneys," +
                    "b.pet_name as petName,c.user_name as userName,c.account as account ";
            if (!userName.equals(""))
                strWhere += " and c.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and b.pet_name like '%" + petName + "%'";

            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetIll> list = gson.fromJson(json, new TypeToken<List<SysPetIll>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @RequestMapping("/confirmIll")
    @ResponseBody
    public JsonModel confirmIll(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petIllId = baseRequest.getInt("petIllId", 0);
        if(petIllId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetIll sysPetIll = sysPetIllMapper.selectByPrimaryKey(petIllId);
        if(sysPetIll == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetIll.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetIll.setIsOver(1);
        sysPetIll.setCreateTime(new Date());
        int res = sysPetIllMapper.updateByPrimaryKeySelective(sysPetIll);
        if(res > 0){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetIll.getPetIllId());
            sysNotify.setType(1);
            sysNotify.setTitle("宠物治疗通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPet.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成治疗");
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //宠物洗澡
    @RequestMapping("/petShower")
    public ModelAndView petShower(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/petShower");
        return view;
    }

    @RequestMapping("/petShowerList")
    @ResponseBody
    public HuiPageModel<SysPetShower> petShowerList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetShower> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_shower_id desc";
            String strTable = " sys_pet_shower a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id ";
            String strQuery = "a.pet_shower_id as petShowerId, a.pet_id as petId, a.pet_showe_type as petShoweType, a.is_over as isOver, a.is_notify as isNotify," +
                    " a.moneys as moneys, a.status as status, a.create_time as createTime," +
                    "b.pet_name as petName,c.user_name as userName,c.account as account ";
            if (!userName.equals(""))
                strWhere += " and c.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and b.pet_name like '%" + petName + "%'";

            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetShower> list = gson.fromJson(json, new TypeToken<List<SysPetShower>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //开始洗澡
    @RequestMapping("/confirmShower")
    @ResponseBody
    public JsonModel confirmShower(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petShowerId = baseRequest.getInt("petShowerId", 0);
        if(petShowerId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetShower sysPetShower = sysPetShowerMapper.selectByPrimaryKey(petShowerId);
        if(sysPetShower == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetShower.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetShower.setIsOver(1);
        sysPetShower.setCreateTime(new Date());
        int res = sysPetShowerMapper.updateByPrimaryKeySelective(sysPetShower);
        if(res > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    @RequestMapping("/okShower")
    @ResponseBody
    public JsonModel okShower(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petShowerId = baseRequest.getInt("petShowerId", 0);
        if(petShowerId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetShower sysPetShower = sysPetShowerMapper.selectByPrimaryKey(petShowerId);
        if(sysPetShower == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetShower.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetShower.setIsOver(2);
        sysPetShower.setCreateTime(new Date());
        int res = sysPetShowerMapper.updateByPrimaryKeySelective(sysPetShower);
        if(res > 0){
            //发送通知
            //洗澡完成
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetShower.getPetShowerId());
            sysNotify.setType(4);
            sysNotify.setTitle("宠物洗澡通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPet.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成洗澡");
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //宠物绝育信息
    @RequestMapping("/petSterilization")
    public ModelAndView petSterilization(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/petSterilization");
        return view;
    }

    @RequestMapping("/petSterilizationList")
    @ResponseBody
    public HuiPageModel<SysPetSterilization> petSterilizationList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetSterilization> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_sterilization_id desc";
            String strTable = " sys_pet_sterilization a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id ";
            String strQuery = "a.pet_sterilization_id as petSterilizationId,a.pet_id as petId,a.status as status," +
                    "a.moneys as moneys,a.is_over as isOver,a.is_notify as isNotify,a.create_time as createTime,"+
                    "b.pet_name as petName,c.user_name as userName,c.account as account ";
            if (!userName.equals(""))
                strWhere += " and c.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and b.pet_name like '%" + petName + "%'";

            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetSterilization> list = gson.fromJson(json, new TypeToken<List<SysPetSterilization>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //绝育完成
    @RequestMapping("/confirmPetSterilization")
    @ResponseBody
    public JsonModel confirmPetSterilization(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petSterilizationId = baseRequest.getInt("petSterilizationId", 0);
        if(petSterilizationId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetSterilization sysPetSterilization = sysPetSterilizationMapper.selectByPrimaryKey(petSterilizationId);
        if(sysPetSterilization == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetSterilization.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetSterilization.setIsOver(1);
        int res = sysPetSterilizationMapper.updateByPrimaryKeySelective(sysPetSterilization);
        if(res > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    @RequestMapping("/okPetSterilization")
    @ResponseBody
    public JsonModel okPetSterilization(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petSterilizationId = baseRequest.getInt("petSterilizationId", 0);
        if(petSterilizationId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetSterilization sysPetSterilization = sysPetSterilizationMapper.selectByPrimaryKey(petSterilizationId);
        if(sysPetSterilization == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetSterilization.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetSterilization.setIsOver(2);
        int res = sysPetSterilizationMapper.updateByPrimaryKeySelective(sysPetSterilization);
        if(res > 0){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetSterilization.getPetSterilizationId());
            sysNotify.setType(5);
            sysNotify.setTitle("宠物绝育通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPet.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成绝育");
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //宠物疫苗
    @RequestMapping("/petVaccine")
    public ModelAndView petVaccine(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/petVaccine");
        return view;
    }

    @RequestMapping("/petVaccineList")
    @ResponseBody
    public HuiPageModel<SysPetVaccine> petVaccineList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetVaccine> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String petName = baseRequest.getString("petName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_vaccine_id desc";
            String strTable = " sys_pet_vaccine a left join sys_pet b on a.pet_id = b.pet_id left join sys_member c on b.member_id = c.member_id " +
                    " left join sys_pet_vaccine_config d on a.pet_vaccine_config_id = d.pet_vaccine_config_id";
            String strQuery = "a.pet_vaccine_id as petVaccineId,a.pet_id as petId,a.pet_vaccine_config_id as petVaccineConfigId,a.moneys as moneys" +
                    ",a.status as status,a.is_over as isOver,a.is_notify as isNotify,a.create_time as createTime" +
                    ",b.pet_name as petName,c.user_name as userName,c.account as account,d.name as vaccineName";
            if (!userName.equals(""))
                strWhere += " and c.user_name like '%" + userName + "%'";
            if (!petName.equals(""))
                strWhere += " and b.pet_name like '%" + petName + "%'";

            strWhere += " and a.pet_id in (select pet_id from sys_pet)";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetVaccine> list = gson.fromJson(json, new TypeToken<List<SysPetVaccine>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //绝育完成
    @RequestMapping("/confirmPetVaccine")
    @ResponseBody
    public JsonModel confirmPetVaccine(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petVaccineId = baseRequest.getInt("petVaccineId", 0);
        if(petVaccineId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetVaccine sysPetVaccine = sysPetVaccineMapper.selectByPrimaryKey(petVaccineId);
        if(sysPetVaccine == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetVaccine.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetVaccine.setIsOver(1);
        int res = sysPetVaccineMapper.updateByPrimaryKeySelective(sysPetVaccine);
        if(res > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    @RequestMapping("/okPetVaccine")
    @ResponseBody
    public JsonModel okPetVaccine(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petVaccineId = baseRequest.getInt("petVaccineId", 0);
        if(petVaccineId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetVaccine sysPetVaccine = sysPetVaccineMapper.selectByPrimaryKey(petVaccineId);
        if(sysPetVaccine == null){
            return RespData.JsonError("不存在该数据");
        }
        //查询宠物
        SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetVaccine.getPetId());
        if(sysPet == null){
            return RespData.JsonError("不存在该宠物信息");
        }
        sysPetVaccine.setIsOver(2);
        int res = sysPetVaccineMapper.updateByPrimaryKeySelective(sysPetVaccine);
        if(res > 0){
            //增加日志
            SysPetVaccineLog sysPetVaccineLog = new SysPetVaccineLog();
            sysPetVaccineLog.setPetId(sysPet.getPetId());
            sysPetVaccineLog.setPetVaccineConfigId(sysPetVaccine.getPetVaccineConfigId());
            sysPetVaccineLog.setCreateTime(new Date());
            sysPetVaccineLogMapper.insertSelective(sysPetVaccineLog);
            //接种完成
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetVaccine.getPetVaccineId());
            sysNotify.setType(6);
            sysNotify.setTitle("宠物疫苗通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPet.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成疫苗接种");
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }


    @RequestMapping("/member")
    public ModelAndView member(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/member");
        return view;
    }

    @RequestMapping("/memberList")
    @ResponseBody
    public HuiPageModel<SysMember> memberList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysMember> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.member_id desc";
            String strTable = " sys_member a left join sys_member_vip b on a.member_vip = b.member_vip_id";
            String strQuery = "a.member_id as memberId, a.account as account, a.password as password, a.user_name as userName, a.phone as phone, a.create_time as createTime," +
                    "a.moneys as moneys, a.member_vip as memberVip,b.name as vipName";
            if (!userName.equals(""))
                strWhere += " and a.user_name like '%" + userName + "%'";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysMember> list = gson.fromJson(json, new TypeToken<List<SysMember>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @RequestMapping("/confirmMemberShower")
    @ResponseBody
    public JsonModel confirmMemberShower(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long memberId = baseRequest.getInt("memberId", 0);
        if(memberId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysMember sysMember = sysMemberMapper.selectByPrimaryKey(memberId);
        if(sysMember == null){
            return RespData.JsonError("不存在该数据");
        }
        SysNotify sysNotify = new SysNotify();
        sysNotify.setKeyId(-1L);
        sysNotify.setType(2);
        sysNotify.setTitle("宠物洗澡通知");
        sysNotify.setStatus(0);
        sysNotify.setMemberId(sysMember.getMemberId());
        sysNotify.setMessage("您的宠物需要洗澡啦~");
        int res = sysNotifyMapper.insertSelective(sysNotify);
        if(res > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    @RequestMapping("/confirmMemberTest")
    @ResponseBody
    public JsonModel confirmMemberTest(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long memberId = baseRequest.getInt("memberId", 0);
        if(memberId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysMember sysMember = sysMemberMapper.selectByPrimaryKey(memberId);
        if(sysMember == null){
            return RespData.JsonError("不存在该数据");
        }
        SysNotify sysNotify = new SysNotify();
        sysNotify.setKeyId(-1L);
        sysNotify.setType(3);
        sysNotify.setTitle("宠物体检通知");
        sysNotify.setStatus(0);
        sysNotify.setMemberId(sysMember.getMemberId());
        sysNotify.setMessage("您的宠物需要体检啦~");
        int res = sysNotifyMapper.insertSelective(sysNotify);
        if(res > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }
}
