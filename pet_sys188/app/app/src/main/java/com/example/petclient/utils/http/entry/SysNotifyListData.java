package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysNotify;
import com.example.petclient.utils.http.ResponseData;

import java.util.List;

public class SysNotifyListData extends ResponseData {
    private List<SysNotify> Data;

    public List<SysNotify> getData() {
        return Data;
    }

    public void setData(List<SysNotify> data) {
        Data = data;
    }
}
