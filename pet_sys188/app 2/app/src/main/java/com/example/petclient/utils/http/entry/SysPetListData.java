package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPet;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetListData extends ResponseData {
    private List<SysPet> Data;

    public List<SysPet> getData() {
        return Data;
    }

    public void setData(List<SysPet> data) {
        Data = data;
    }
}
