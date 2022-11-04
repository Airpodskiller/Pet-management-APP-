package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysPetTest;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysPetTestListData extends ResponseData {
    private List<SysPetTest> Data;

    public List<SysPetTest> getData() {
        return Data;
    }

    public void setData(List<SysPetTest> data) {
        Data = data;
    }
}
