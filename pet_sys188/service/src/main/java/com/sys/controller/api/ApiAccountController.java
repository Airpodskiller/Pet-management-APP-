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
import com.sys.dao.SysMemberMapper;
import com.sys.dao.SysMemberVipMapper;
import com.sys.dao.SysPetRechargeConfigMapper;
import com.sys.dao.SysPetRechargeOrderMapper;
import com.sys.entity.SysMember;
import com.sys.entity.SysMemberVip;
import com.sys.entity.SysPetRechargeConfig;
import com.sys.entity.SysPetRechargeOrder;
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
@RequestMapping("/api/home/account")
@ApiAnnotation
public class ApiAccountController {
    @Autowired
    private SysMemberMapper sysMemberMapper;
    @Autowired
    private SysPetRechargeConfigMapper sysPetRechargeConfigMapper;
    @Autowired
    private SysPetRechargeOrderMapper sysPetRechargeOrderMapper;
    @Autowired
    private SysMemberVipMapper sysMemberVipMapper;
    //查询个人信息
    @RequestMapping("/info")
    @ResponseBody
    public JsonModel info(){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        //查询是否是会员
        if(loginModel.sysMember.getMemberVip() != null && loginModel.sysMember.getMemberVip() > 0){
            SysMemberVip sysMemberVip = sysMemberVipMapper.selectByPrimaryKey(loginModel.sysMember.getMemberVip());
            if(sysMemberVip != null){
                loginModel.sysMember.setVipName("您是尊贵的"+sysMemberVip.getName()+"，您在商城享有"+sysMemberVip.getDiscount()+"折扣~");
            }
        }
        return RespData.JsonOK(loginModel.sysMember);
    }

    //修改个人信息
    @RequestMapping("/update")
    @ResponseBody
    public JsonModel update(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        String phone = baseRequest.getString("phone");
        String userName = baseRequest.getString("userName");
        SysMember sysMember = loginModel.sysMember;
        sysMember.setPhone(phone);
        sysMember.setUserName(userName);
        int result = sysMemberMapper.updateByPrimaryKeySelective(sysMember);
        if(result > 0){
            return RespData.JsonOK(sysMember);
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //修改密码
    @RequestMapping("/updatePassword")
    @ResponseBody
    public JsonModel updatePassword(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        String nPassword = baseRequest.getString("nPassword");
        String password = baseRequest.getString("password");
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        if(!loginModel.sysMember.getPassword().equals(password)){
            return RespData.JsonError("旧密码输入有误");
        }
        SysMember sysMember = loginModel.sysMember;
        sysMember.setPassword(nPassword);
        int result = sysMemberMapper.updateByPrimaryKeySelective(sysMember);
        if(result > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //充值接口
    @RequestMapping("/rechargeConfig")
    @ResponseBody
    public JsonModel rechargeConfig(){
        List<SysPetRechargeConfig> sysPetRechargeConfigs = sysPetRechargeConfigMapper.selectList("order by moneys asc");
        return RespData.JsonOK(sysPetRechargeConfigs);
    }

    //点击充值
    @RequestMapping("/recharge")
    @ResponseBody
    public JsonModel recharge(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        SysMember sysMember = loginModel.sysMember;
        double moneys = baseRequest.getDouble("moneys",0);
        if(moneys <= 0){
            return RespData.JsonError("请输入充值金额");
        }
        //查询
        long vip = -1;
        List<SysPetRechargeConfig> sysPetRechargeConfigs = sysPetRechargeConfigMapper.selectList("and moneys <= " + moneys + " order by moneys desc");
        if(sysPetRechargeConfigs.size() > 0){
            vip = sysPetRechargeConfigs.get(0).getVipId();
        }
        //增加充值记录
        SysPetRechargeOrder sysPetRechargeOrder = new SysPetRechargeOrder();
        sysPetRechargeOrder.setMoneys(new BigDecimal(moneys));
        sysPetRechargeOrder.setMemberId(sysMember.getMemberId());
        sysPetRechargeOrder.setCreateTime(new Date());
        sysPetRechargeOrder.setStatus(1);
        int result = sysPetRechargeOrderMapper.insertSelective(sysPetRechargeOrder);
        if(result > 0){
            //增加金额
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() + moneys));
            if(vip != -1 && vip > sysMember.getMemberVip()){
                sysMember.setMemberVip(vip);
            }
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("充值成功");
        }else {
            return RespData.JsonError("充值失败");
        }
    }

    @RequestMapping("/rechargeList")
    @ResponseBody
    public JsonModel rechargeList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetRechargeOrder> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and a.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_recharge_order_id desc";
            String strTable = " sys_pet_recharge_order a left join sys_member b on a.member_id = b.member_id";
            String strQuery = " a.pet_recharge_order_id as petRechargeOrderId, a.member_id as memberId, a.moneys as moneys, a.status as status, a.create_time as createTime, " +
                    "b.account as account,b.user_name as userName ";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetRechargeOrder>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }
}
