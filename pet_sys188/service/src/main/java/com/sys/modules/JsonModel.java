package com.sys.modules;

public class JsonModel {
    private int State = -1;
    private String Content = "";
    private Object Data = null;

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

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
