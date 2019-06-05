package com.hrst.induce.utils;

import android.util.Log;

/**
 * Log日志输出
 * Created by Lin on 2018/6/4.
 */

public class LogUitl {
    private static final String T = "induce-log ";
    private static boolean isDebug = false;

    public static void setIsDebug(boolean isDebug) {
        LogUitl.isDebug = isDebug;
    }

    public static void i(String TAG, String obj) {
        if(isDebug) {
            Log.i(T + TAG, obj);
        }

    }

    public static void i(String obj) {
        Log.i(T, obj);
    }

    public static void d(String TAG, String obj) {
        if(isDebug) {
            Log.d(T + TAG, obj);
        }

    }

    public static void d(String obj) {
        Log.d(T, obj);
    }

    public static void e(String TAG, String obj) {
        if(isDebug) {
            Log.e(T + TAG, obj);
        }

    }

    public static void e(String obj) {
        e(T, obj);
    }
}
