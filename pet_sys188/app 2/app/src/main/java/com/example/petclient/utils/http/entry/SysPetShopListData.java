package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetShop;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetShopListData extends ResponseData {
    private List<SysPetShop> Data;

    public List<SysPetShop> getData() {
        return Data;
    }

    public void setData(List<SysPetShop> data) {
        Data = data;
    }
}
