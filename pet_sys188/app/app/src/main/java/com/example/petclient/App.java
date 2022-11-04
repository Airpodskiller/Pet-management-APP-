package com.example.petclient;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.petclient.utils.JsonUtil;
import com.example.petclient.utils.SharedPreferenceUtil;
import com.example.petclient.base.entry.SysMember;
import com.example.petclient.utils.JsonUtil;

public class App extends Application {
    /**
     * 获取用户
     * @param user
     * @param context
     */
    public static void saveMember(SysMember user, Context context){
        String json = JsonUtil.ToJson(user);
        try{
            SharedPreferenceUtil.getInstance(context).putString("user",json);
            App.member = user;
        }catch (Exception e){
            Log.e("保存",e.toString());
        }
    }

    /**
     * 记录登录用户
     */
    private static SysMember member = null;
    public static SysMember getMember(Context context){
        if(member != null){
            return member;
        }
        try{
            String json = SharedPreferenceUtil.getInstance(context).getString("user");
            if(json != null && !"".equals(json)){
                member = JsonUtil.ToModel(json,SysMember.class);
            }

        }catch (Exception e){
            Log.e("初始化",e.toString());
        }
        return member;
    }

    public static void LoginOut(Context context){
        try{
            SharedPreferenceUtil.getInstance(context).putString("user","");
            App.member = null;
        }catch (Exception e){
            Log.e("保存",e.toString());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocationClient.setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(this,true);
//        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
//        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.GCJ02);
    }
}
