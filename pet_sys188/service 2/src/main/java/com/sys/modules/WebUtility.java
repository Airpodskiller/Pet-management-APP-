package com.sys.modules;

public class WebUtility {
    //管理员session
    public final static String ADMINLOGIN = "ADMINLOGIN";
    //登录界面
    public final static String ADMINURL = "/admin";

    public final static String ADMINHOME = "/admin/home";

    public static long parseLong(String s,long dfvalue){
        long value = -1;
        try{
            value = Long.parseLong(s);
        }catch (Exception e){
            value = dfvalue;
        }

        return value;
    }

    public static int parseInt(String s,int dfvalue){
        int value = -1;
        try{
            value = Integer.parseInt(s);
        }catch (Exception e){
            value = dfvalue;
        }

        return value;
    }
}
