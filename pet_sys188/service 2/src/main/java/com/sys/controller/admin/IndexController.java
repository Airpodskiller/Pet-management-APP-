package com.sys.controller.admin;

import com.sys.common.HttpBaseRequest;
import com.sys.common.RespData;
import com.sys.common.interceptor.admin.AdminLoginModel;
import com.sys.dao.SysUserMapper;
import com.sys.entity.SysUser;
import com.sys.modules.JsonModel;
import com.sys.modules.WebUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class IndexController {
    @Autowired
    SysUserMapper sysUserMapper;
    @RequestMapping("")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/login");
        return view;
    }

    @ResponseBody
    @RequestMapping("/login")
    public JsonModel login(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        baseRequest.checkNullParam("account","请输入登录账号")
                .checkNullParam("password","请输入登录密码");
        if(!baseRequest.CheckParam()){
            return baseRequest.toError();
        }
        //密码多少数的LMW20210128
        String account = baseRequest.getString("account");
        String password = baseRequest.getString("password");
        SysUser query = new SysUser();
        query.setAccount(account);
        query.setPassword(password);
        SysUser sysUser = sysUserMapper.selectUser(query);
        if(sysUser == null){
            return RespData.JsonError("账户或密码输入有误");
        }
        AdminLoginModel model = new AdminLoginModel();
        model.sysUser = sysUser;
        request.getSession().setAttribute(WebUtility.ADMINLOGIN,model);
        return RespData.JsonOK("登录成功",WebUtility.ADMINHOME);
    }

    @RequestMapping("/loginOut")
    public ModelAndView loginOut(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/login");
        request.getSession().setAttribute(WebUtility.ADMINLOGIN,null);
        return view;
    }
}
