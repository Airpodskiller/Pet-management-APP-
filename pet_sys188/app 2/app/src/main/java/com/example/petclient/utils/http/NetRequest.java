package com.example.petclient.utils.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetRequest {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private String method = "get";
    private String url;
    private Request.Builder builder;
    private Map<String,String> headers = new HashMap<>();
    private Map<String,String> parmas = new HashMap<>();
    public NetRequest(String url){
        this.url = url;
    }
    public NetRequest(String url,String method){
        this.url = url;
        this.method = method;
    }
    public NetRequest addHeader(String key,String value){
        this.headers.put(key,value);
        return this;
    }
    public NetRequest addParam(String key,String value){
        this.parmas.put(key,value);
        return this;
    }

    public Request builder(){
        String url = this.url;
        this.builder = new Request.Builder();
        if(method.toLowerCase().equals("get")){
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String,String> item : this.parmas.entrySet()){
                if(sb.length() == 0){
                    sb.append(item.getKey()+"="+item.getValue());
                }else {
                    sb.append("&"+item.getKey()+"="+item.getValue());
                }
            }
            url = this.url + "?" + sb.toString();
        }else {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String,String> item : this.parmas.entrySet()){
                if(sb.length() == 0){
                    sb.append(item.getKey()+"="+item.getValue());
                }else {
                    sb.append("&"+item.getKey()+"="+item.getValue());
                }
            }
            String body = sb.toString();
            RequestBody requestBody = FormBody.create(MEDIA_TYPE, body);
            this.builder.post(requestBody);
        }
        this.builder.url(NetConfig.api+url);
        for (Map.Entry<String,String> item : this.headers.entrySet()){
            this.builder.addHeader(item.getKey(),item.getValue());
        }

        return this.builder.build();
    }

    /**
     * 上传图片
     * @param filePath
     * @param friendId
     * @return
     */
    public Request uploadBuilder(String filePath,long friendId){
        File file = new File(filePath);
        String name = file.getName();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("friendMemberId",friendId+"")
                .addFormDataPart("file",name,RequestBody.create(MediaType.parse("*/*"),file)).build();
        this.builder = new Request.Builder();
        this.builder.url(NetConfig.api+url);
        builder.post(requestBody);
        return this.builder.build();
    }
}
