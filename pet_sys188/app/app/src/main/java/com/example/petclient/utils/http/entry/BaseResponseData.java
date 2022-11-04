package com.example.petclient.utils.http.entry;

import com.example.petclient.utils.http.ResponseData;

public class BaseResponseData extends ResponseData {
    private Object Data;

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
