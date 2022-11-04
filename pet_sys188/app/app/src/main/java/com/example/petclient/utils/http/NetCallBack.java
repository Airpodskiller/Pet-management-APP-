package com.example.petclient.utils.http;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Response;

public abstract class NetCallBack<T> {
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler(msg);
        }
    };
    public abstract void complete();
    public abstract void success(Response response) throws IOException;
    public abstract void fail(int code);

    /**
     * 回传界面的方法
     * @param msg
     */
    public abstract void handler(Message msg);
}
