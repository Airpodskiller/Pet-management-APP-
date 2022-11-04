package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetShower;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetShowerListData extends ResponseData {
    private List<SysPetShower> Data;

    public List<SysPetShower> getData() {
        return Data;
    }

    public void setData(List<SysPetShower> data) {
        Data = data;
    }
}
