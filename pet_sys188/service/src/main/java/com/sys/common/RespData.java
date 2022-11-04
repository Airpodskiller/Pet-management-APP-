package com.sys.common;

import com.sys.modules.JsonModel;
import com.sys.modules.MsgState;

public class RespData {
    //返回脚本
    public static String responseJs(String js){
        return responseHtml("<script>" + js + "</script>");
    }

    public static String responseMsg(String js,String msg){
        return responseHtml("<script>alert('" + msg + "');" + js + "</script>");
    }

    public static String responseLink(String msg,String url){
        return responseMsg("top.location = '"+url+"';",msg);
    }

    public static String responseMsg(String msg){
        return responseHtml("<script>alert(" + msg + ");</script>");
    }

    //返回基础
    public static String responseHtml(String html){
        return "<!DOCTYPE html>\n" +
                "<html>\t\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\"> \n" +
                "<title>网站提示</title> \n" +
                "</head>\n" +
                "<body>\n" +
                html +
                "</body>\n" +
                "</html>";
    }

    public static JsonModel JsonOK(String msg, Object data){
        JsonModel model = new JsonModel();
        model.setState(MsgState.Success);
        model.setContent(msg);
        model.setData(data);
        return model;
    }

    public static JsonModel JsonOK(String msg){
        JsonModel model = new JsonModel();
        model.setState(MsgState.Success);
        model.setContent(msg);
        return model;
    }

    public static String JsonMsgSuccess(String msg,String data){
        return "{\"State\":" + (int)MsgState.Success + ",\"Content\":\"" + msg + "\",\"Data\":" + data + "}";
    }

    public static JsonModel JsonOK(Object data){
        JsonModel model = new JsonModel();
        model.setState(MsgState.Success);
        model.setContent("");
        model.setData(data);
        return model;
    }

    public static JsonModel JsonError(String msg){
        JsonModel model = new JsonModel();
        model.setState(MsgState.Fail);
        model.setContent(msg);
        return model;
    }

    public static JsonModel JsonError(String msg,Object data){
        JsonModel model = new JsonModel();
        model.setState(MsgState.Fail);
        model.setContent(msg);
        model.setData(data);
        return model;
    }
}
