package com.sys.common.interceptor.api;

import com.sys.common.JsonHelper;
import com.sys.dao.SysMemberMapper;
import com.sys.dao.SysMemberVipMapper;
import com.sys.entity.SysMember;
import com.sys.entity.SysMemberVip;
import com.sys.modules.JsonModel;
import com.sys.modules.MsgState;
import com.sys.modules.WebUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiPermission {
    SysMemberMapper sysMemberMapper;
    SysMemberVipMapper sysMemberVipMapper;
    public ApiPermission(SysMemberMapper sysMemberMapper,SysMemberVipMapper sysMemberVipMapper){
        this.sysMemberMapper = sysMemberMapper;
        this.sysMemberVipMapper = sysMemberVipMapper;
    }
    public boolean Auth(HttpServletRequest request, HttpServletResponse response, Object handler){
        String userId = request.getHeader("userId");
        if(userId == null || userId.equals("")){
            JsonModel model = new JsonModel();
            model.setState(MsgState.NoLogin);
            model.setContent("请重新登录");
            model.setData(WebUtility.ADMINURL);
            String json = JsonHelper.ToJson(model);
            try
            {
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().print(json);
            }catch (Exception ex) {
                //记录日志
                ex.printStackTrace();
            }
            return false;
        }
        //查询用户
        SysMember sysMember = sysMemberMapper.selectByPrimaryKey(Long.parseLong(userId));
        if(sysMember == null){
            JsonModel model = new JsonModel();
            model.setState(MsgState.NoLogin);
            model.setContent("请重新登录");
            model.setData(WebUtility.ADMINURL);
            String json = JsonHelper.ToJson(model);
            try
            {
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().print(json);
            }catch (Exception ex) {
                //记录日志
                ex.printStackTrace();
            }
            return false;
        }
        ApiLoginModel apiLoginModel = new ApiLoginModel();
        apiLoginModel.sysMember = sysMember;
        sysMember.setDisCount(0);
        sysMember.setIsVip(0);
        if(sysMember.getMemberVip() != null && sysMember.getMemberVip() > 0){
            SysMemberVip sysMemberVip = sysMemberVipMapper.selectByPrimaryKey(sysMember.getMemberVip());
            if(sysMemberVip != null){
                sysMember.setDisCount(sysMemberVip.getDiscount());
                sysMember.setIsVip(1);
            }
        }
        ApiContext.LOCAL.set(apiLoginModel);
        return true;
    }
}
