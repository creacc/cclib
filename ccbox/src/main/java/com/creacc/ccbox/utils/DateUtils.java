package com.creacc.ccbox.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 2016/1/22.
 */
public class DateUtils {

    public static String timeToDate(int time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time * 1000L));
    }

    public static String timeToDateDetail(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String timeToDateDetail(int time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time * 1000L));
    }
}
