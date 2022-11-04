package com.example.petclient.utils.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetClient {
    public NetClient(){
        client = initOkHttpClient();
    }

    public OkHttpClient client;
    private OkHttpClient initOkHttpClient(){
        //初始化的时候可以自定义一些参数
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(NetConfig.ReadTimeOut, TimeUnit.MILLISECONDS)//设置读取超时为10秒
                .connectTimeout(NetConfig.ConnTimeOut, TimeUnit.MILLISECONDS)//设置链接超时为10秒
                .build();
        return okHttpClient;
    }
    private NetRequest request;
    private NetCallBack netCallBack;
    public NetClient Callback(NetCallBack netCallBack){
        this.netCallBack = netCallBack;
        return this;
    }

    public NetClient Request(NetRequest request){
        this.request = request;
        return this;
    }

    public NetRequest Request(){
        return this.request;
    }

    public Call Http(){
        Call call = this.client.newCall(request.builder());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求网络失败，调用自己的接口，通过传回的-1可以知道错误类型
                netCallBack.fail(200);
                netCallBack.complete();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    netCallBack.success(response);
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    netCallBack.fail(response.code());
                }
                netCallBack.complete();
            }
        });
        return call;
    }

    public Call UploadHttp(String filePath,long friendId){
        Call call = this.client.newCall(request.uploadBuilder(filePath,friendId));
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求网络失败，调用自己的接口，通过传回的-1可以知道错误类型
                netCallBack.fail(200);
                netCallBack.complete();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求网络成功说明服务器有响应，但返回的是什么我们无法确定，可以根据响应码判断
                if (response.code() == 200) {
                    //如果是200说明正常，调用MyCallBack的成功方法，传入数据
                    netCallBack.success(response);
                }else{
                    //如果不是200说明异常，调用MyCallBack的失败方法将响应码传入
                    netCallBack.fail(response.code());
                }
                netCallBack.complete();
            }
        });
        return call;
    }
}
