package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetVaccineConfig;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetVaccineConfigListData extends ResponseData {
    private List<SysPetVaccineConfig> Data;

    public List<SysPetVaccineConfig> getData() {
        return Data;
    }

    public void setData(List<SysPetVaccineConfig> data) {
        Data = data;
    }
}
