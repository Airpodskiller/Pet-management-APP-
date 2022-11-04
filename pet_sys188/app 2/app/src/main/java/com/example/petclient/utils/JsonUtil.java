package com.example.petclient.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public class JsonUtil {
    private final static String TAG = "JsonUtil";
    public static String ToJson(Object obj){
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T ToModel(String json, Class<T> tclass){
        Log.e(TAG,json);
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.fromJson(json,tclass);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map> ToList(String json){
        try{
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.fromJson(json, List.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
