package com.sys.common;

import java.util.Date;

public class DateUtil {
    public static Date addMinute(Date time,int minute){
        return new Date(time.getTime() + minute * 60000);
    }

    public static boolean compareTo(Date time){
        return time.getTime() <= new Date().getTime();
    }
}
