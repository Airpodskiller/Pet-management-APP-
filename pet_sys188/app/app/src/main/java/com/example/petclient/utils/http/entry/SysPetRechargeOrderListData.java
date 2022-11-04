package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetRechargeOrder;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetRechargeOrderListData extends ResponseData {
    private List<SysPetRechargeOrder> Data;

    public List<SysPetRechargeOrder> getData() {
        return Data;
    }

    public void setData(List<SysPetRechargeOrder> data) {
        Data = data;
    }
}
