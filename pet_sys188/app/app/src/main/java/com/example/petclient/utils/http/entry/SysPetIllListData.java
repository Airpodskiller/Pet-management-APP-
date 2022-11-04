package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetIll;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetIllListData extends ResponseData {
    private List<SysPetIll> Data;

    public List<SysPetIll> getData() {
        return Data;
    }

    public void setData(List<SysPetIll> data) {
        Data = data;
    }
}
