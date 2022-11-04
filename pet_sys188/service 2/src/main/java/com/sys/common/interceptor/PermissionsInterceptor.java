package com.sys.common.interceptor;


import com.sys.common.interceptor.api.ApiAnnotation;
import com.sys.common.interceptor.api.ApiContext;
import com.sys.common.interceptor.api.ApiPermission;
import com.sys.dao.SysMemberMapper;
import com.sys.entity.SysMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sys.common.interceptor.admin.AdminAnnotation;
import com.sys.common.interceptor.admin.AdminPermission;

public class PermissionsInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    SysMemberMapper sysMemberMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //权限控制
        boolean result = true;
        String url = request.getRequestURL().toString().toLowerCase();
        if (url.indexOf("/admin") != -1){
            //先获取控制器上的注解
            if (handler instanceof HandlerMethod){
                HandlerMethod h = (HandlerMethod)handler;
                int isjson = 0;
                AdminAnnotation adminannotation = h.getMethodAnnotation(AdminAnnotation.class);
                //获取类上的注解
                if(adminannotation == null){
                    adminannotation = h.getMethod().getDeclaringClass().getAnnotation(AdminAnnotation.class);
                }
                //获取注解
                ResponseBody responseBody = h.getMethodAnnotation(ResponseBody.class);
                if(responseBody != null){
                    isjson = 1;
                }
                //判断权限
                if(adminannotation != null){
                    result = new AdminPermission(isjson).Auth(request,response,handler);
                }
            }
        }else if(url.indexOf("/api") != -1){
            if (handler instanceof HandlerMethod){
                HandlerMethod h = (HandlerMethod)handler;
                ApiAnnotation apiAnnotation = h.getMethodAnnotation(ApiAnnotation.class);
                //获取类上的注解
                if(apiAnnotation == null){
                    apiAnnotation = h.getMethod().getDeclaringClass().getAnnotation(ApiAnnotation.class);
                }
                //判断权限
                if(apiAnnotation != null){
                    result = new ApiPermission(sysMemberMapper).Auth(request,response,handler);
                }
            }
        } else {
            result = true;
        }

        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ApiContext.LOCAL.set(null);
    }
}
