package com.sys.common.interceptor.api;

import com.sys.common.JsonHelper;
import com.sys.dao.SysMemberMapper;
import com.sys.entity.SysMember;
import com.sys.modules.JsonModel;
import com.sys.modules.MsgState;
import com.sys.modules.WebUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiPermission {
    SysMemberMapper sysMemberMapper;
    public ApiPermission(SysMemberMapper sysMemberMapper){
        this.sysMemberMapper = sysMemberMapper;
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
        ApiContext.LOCAL.set(apiLoginModel);
        return true;
    }
}
