package com.sys.common.interceptor.admin;

import com.sys.common.RespData;
import com.sys.common.JsonHelper;
import com.sys.modules.JsonModel;
import com.sys.modules.MsgState;
import com.sys.modules.WebUtility;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//用于判断用户是否登录以及是否有权限
public class AdminPermission {
    private int isJson;
    public AdminPermission(int isJson){
        this.isJson = isJson;
    }

    public boolean Auth(HttpServletRequest request, HttpServletResponse response, Object handler){
        boolean result = false;

        AdminLoginModel user = (AdminLoginModel) request.getSession().getAttribute(WebUtility.ADMINLOGIN);

        if(user == null){
            result = false;
            //判断是否是json
            if(isJson == 0){
                //非json,返回提示
                try
                {
                    response.setContentType("text/html;charset=utf-8");
                    response.getWriter().print(RespData.responseLink("请重新登录",WebUtility.ADMINURL));
                }catch (Exception ex) {
                    //记录日志
                }
            }else{
                //是json
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
            }
        }else{
            result = true;
        }

        return result;
    }
}
