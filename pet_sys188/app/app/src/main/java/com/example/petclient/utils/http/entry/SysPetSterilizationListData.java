package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetSterilization;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetSterilizationListData extends ResponseData {
    private List<SysPetSterilization> Data;

    public List<SysPetSterilization> getData() {
        return Data;
    }

    public void setData(List<SysPetSterilization> data) {
        Data = data;
    }
}
