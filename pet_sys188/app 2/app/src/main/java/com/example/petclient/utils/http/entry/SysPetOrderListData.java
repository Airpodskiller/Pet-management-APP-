package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetOrder;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetOrderListData extends ResponseData {
    private List<SysPetOrder> Data;

    public List<SysPetOrder> getData() {
        return Data;
    }

    public void setData(List<SysPetOrder> data) {
        Data = data;
    }
}
