package com.example.petclient.utils.http;

import java.io.Serializable;

public class ResponseData implements Serializable {
    private int State;
    private String Content;

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
