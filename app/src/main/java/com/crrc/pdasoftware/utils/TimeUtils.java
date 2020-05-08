package com.crrc.pdasoftware.utils;

import java.text.SimpleDateFormat;

public class TimeUtils {
    //获取时间格式为：“2013-09-22 2:32:36”的时间
    public static  String getCurrentTimeFormat() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sDateFormat.format(new java.util.Date());
    }
}
