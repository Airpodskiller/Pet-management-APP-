package com.sys.common;

import com.sys.modules.JsonModel;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLDecoder;

public class HttpBaseRequest {
    private HttpServletRequest request;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpBaseRequest(HttpServletRequest request){
        this.request = request;
    }

    public String getString(String key){
        String value = "";
        try{
            value = request.getParameter(key);
            value = URLDecoder.decode(value,"utf-8");
            if(value == null){
                value = "";
            }
        }catch (Exception e){
            value = "";
        }
        return value;
    }

    public Long getLong(String key){
        Long value = getLong(key,new Long(-1));
        return value;
    }

    public Long getLong(String key,Long dfvalue){
        Long value;
        try{
            value = Long.parseLong(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }
        return value;
    }

    public Long getLong(String key,long dfvalue){
        Long value;
        try{
            value = Long.parseLong(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }
        return value;
    }

    public Integer getInt(String key){
        Integer value = getInt(key,new Integer(-1));
        return value;
    }

    public Integer getInt(String key,Integer dfvalue){
        Integer value;
        try{
            value = Integer.parseInt(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }
        return value;
    }

    public Integer getInt(String key,int dfvalue){
        Integer value;
        try{
            value = Integer.parseInt(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }
        return value;
    }

    public BigDecimal getDecimal(String key){
        BigDecimal value = getDecimal(key,new BigDecimal(-1));
        return value;
    }

    public BigDecimal getDecimal(String key,BigDecimal dfvalue){
        BigDecimal value;
        try{
            value = new BigDecimal(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }

        return value;
    }

    public double getDouble(String key,double dfvalue){
        double value = dfvalue;
        try{
            value = Double.parseDouble(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }

        return value;
    }

    public boolean getBool(String key,boolean dfvalue){
        boolean value = dfvalue;
        try{
            value = Boolean.parseBoolean(request.getParameter(key));
        }catch (Exception e){
            value = dfvalue;
        }

        return value;
    }

    //校验参数是否合格
    private boolean ischecked = true;
    private  String errmsg = "";
    public boolean CheckParam(){
        return this.ischecked;
    }
    public String ErrorMsg(){
        return this.errmsg;
    }

    public JsonModel toError(){
        return RespData.JsonError(this.errmsg);
    }
    //检查是否为空的参数
    public HttpBaseRequest checkNullParam(String key,String msg){
        if(!ischecked){
            return this;
        }
        if(request.getParameter(key) == null || request.getParameter(key).equals("")){
            this.ischecked = false;
            this.errmsg = msg;
        }

        return this;
    }

    public <T> HttpBaseRequest checkDefaultParam(String key,String msg,T dfvalue){
        try{
            if(!this.ischecked){
                return this;
            }
            if (request.getParameter(key) == null){
                this.errmsg = msg;
                this.ischecked = false;
                return this;
            }

            T value = (T)request.getParameter(key);
            if(value == dfvalue){
                this.errmsg = msg;
                this.ischecked = false;
            }
        }catch (Exception e){
            this.errmsg = "参数转换失败";
            this.ischecked = false;
        }

        return this;
    }
}
