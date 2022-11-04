package com.example.petclient.utils.http.entry;

import com.example.petclient.base.entry.SysMember;
import com.example.petclient.utils.http.ResponseData;

public class SysMemberData extends ResponseData {
    private SysMember Data;

    public SysMember getData() {
        return Data;
    }

    public void setData(SysMember data) {
        Data = data;
    }
}
