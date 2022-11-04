package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPet;
import com.example.petclient.utils.http.ResponseData;

public class SysPetData extends ResponseData {
    private SysPet Data;

    public SysPet getData() {
        return Data;
    }

    public void setData(SysPet data) {
        Data = data;
    }
}
