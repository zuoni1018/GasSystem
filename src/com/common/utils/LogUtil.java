package com.common.utils;

import android.util.Log;


public class LogUtil {

    private static boolean isDebug = true;
    private static String TAG = "zangyi";

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, ""+"\n" + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(TAG, ""+"\n"+tag + "\n" + msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, "\n" + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(TAG, tag + "\n" + msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, ""+"\n" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(TAG, tag + "\n" + msg);
        }
    }
}
