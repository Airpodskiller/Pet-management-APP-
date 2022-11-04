package com.sys.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    //返回请求
    protected HttpBaseRequest baseRequest;
    //
    protected HttpServletResponse response;
    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response){
        this.baseRequest = new HttpBaseRequest(request);
        this.response = response;
    }

}
