package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetSterilizationConfig;
import com.example.petclient.utils.http.ResponseData;

public class SysPetSterilizationConfigData extends ResponseData {
    private SysPetSterilizationConfig Data;

    public SysPetSterilizationConfig getData() {
        return Data;
    }

    public void setData(SysPetSterilizationConfig data) {
        Data = data;
    }
}
