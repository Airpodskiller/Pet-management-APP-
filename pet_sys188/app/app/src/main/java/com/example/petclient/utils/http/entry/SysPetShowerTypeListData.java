package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetShowerType;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetShowerTypeListData extends ResponseData {
    private List<SysPetShowerType> Data;

    public List<SysPetShowerType> getData() {
        return Data;
    }

    public void setData(List<SysPetShowerType> data) {
        Data = data;
    }
}
