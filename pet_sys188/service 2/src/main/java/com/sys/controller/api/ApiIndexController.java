package com.sys.controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.common.HttpBaseRequest;
import com.sys.common.MySqlHelper;
import com.sys.common.RespData;
import com.sys.dao.SysMemberMapper;
import com.sys.dao.SysMemberVipMapper;
import com.sys.dao.SysPetShowerTypeMapper;
import com.sys.entity.SysMember;
import com.sys.entity.SysMemberVip;
import com.sys.entity.SysPetShop;
import com.sys.entity.SysPetShowerType;
import com.sys.modules.JsonModel;
import com.sys.modules.ListGageModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/index")
public class ApiIndexController {
    @Autowired
    SysMemberMapper sysMemberMapper;
    @Autowired
    SysMemberVipMapper sysMemberVipMapper;
    @Autowired
    SysPetShowerTypeMapper sysPetShowerTypeMapper;
    //用户登录
    @RequestMapping("/login")
    @ResponseBody
    public JsonModel login(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        String account = baseRequest.getString("account");
        String password = baseRequest.getString("password");
        if(account.equals("")){
            return RespData.JsonError("请输入账号");
        }
        if(password.equals("")){
            return RespData.JsonError("请输入密码");
        }
        //查询
        SysMember sysMember = sysMemberMapper.selectMemberByAccount(account);
        if(sysMember == null){
            return RespData.JsonError("账号或者密码输入有误");
        }
        if(!sysMember.getPassword().equals(password)){
            return RespData.JsonError("账号或者密码输入有误");
        }
        //查询会员
        if(sysMember.getMemberVip() != null && sysMember.getMemberVip() > 0){
            //查询
            SysMemberVip sysMemberVip = sysMemberVipMapper.selectByPrimaryKey(sysMember.getMemberVip());
            if(sysMemberVip != null){
                sysMember.setVipName(sysMemberVip.getName());
            }
        }
        return RespData.JsonOK(sysMember);
    }

    //注册
    @RequestMapping("/sign")
    @ResponseBody
    public JsonModel sign(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        String account = baseRequest.getString("account");
        String password = baseRequest.getString("password");
        String userName = baseRequest.getString("userName");
        String phone = baseRequest.getString("phone");
        if(phone.equals("")){
            return RespData.JsonError("请输入手机号");
        }
        if(account.equals("")){
            return RespData.JsonError("请输入账号");
        }
        if(password.equals("")){
            return RespData.JsonError("请输入密码");
        }
        if(userName.equals("")){
            return RespData.JsonError("请输入真实姓名");
        }
        //查询
        SysMember sysMember = sysMemberMapper.selectMemberByAccount(account);
        if(sysMember != null){
            return RespData.JsonError("该账号已经存在");
        }

        sysMember = new SysMember();
        sysMember.setUserName(userName);
        sysMember.setPassword(password);
        sysMember.setPhone(phone);
        sysMember.setAccount(account);
        int result = sysMemberMapper.insertSelective(sysMember);
        if(result > 0){
            return RespData.JsonOK("注册成功");
        }else {
            return RespData.JsonError("注册失败");
        }
    }

    //查询所有的商品
    @RequestMapping("/shopList")
    @ResponseBody
    public JsonModel shopList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int petType = baseRequest.getInt("petType");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetShop> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_shop_id desc";
            String strTable = " sys_pet_shop a";
            String strQuery = " a.pet_shop_id as petShopId, a.shop_name as shopName, a.pet_type as petType, a.moneys as moneys, a.image as image, a.create_time as createTime ";
            strWhere += " and a.pet_type = " + petType;

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetShop>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }

    //查询洗澡的配置
    @RequestMapping("/showerTypeList")
    @ResponseBody
    public JsonModel showerTypeList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int petType = baseRequest.getInt("petType");
        List<SysPetShowerType> sysPetShowerTypes = sysPetShowerTypeMapper.selectList(" and pet_type = "+petType);
        return RespData.JsonOK(sysPetShowerTypes);
    }
}
