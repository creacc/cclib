package com.creacc.ccbox.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/12/7.
 */
public class LogUtils {

    private static boolean DEBUG = false;

    public static void setDebug(boolean isDebug) {
        LogUtils.DEBUG = isDebug;
    }

    public static void print(String message) {
        if (DEBUG) {
            Log.e("log", message);
        }
    }

    public static void print(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }
}
