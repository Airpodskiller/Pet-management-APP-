package com.sys.common.interceptor.api;

public class ApiContext {
    public static final ThreadLocal<ApiLoginModel> LOCAL = new ThreadLocal<ApiLoginModel>();
}
