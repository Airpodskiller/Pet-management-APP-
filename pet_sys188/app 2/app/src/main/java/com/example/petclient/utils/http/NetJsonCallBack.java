package com.example.petclient.utils.http;

import com.example.petclient.utils.JsonUtil;

import java.io.IOException;

import okhttp3.Response;

public abstract class NetJsonCallBack<T> extends NetCallBack<T> {
    private Class<T> tClass;
    public NetJsonCallBack(Class<T> tClass){
        this.tClass = tClass;
    }

    @Override
    public void success(Response response) throws IOException {
        T data = JsonUtil.ToModel(response.body().string(),tClass);
        this.success(data);
    }

    public abstract void success(T data);
}
