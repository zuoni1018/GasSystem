package com.zuoni.zuoni_common.utils.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zangyi_shuai_ge on 2017/4/22
 * 防止重复显示Toast
 * 这里Context 必须传递 Application
 */

public class ToastUtils {
    private static String oldMsg;//之前显示的内容
    private static Toast toast = null;//Toast对象
    private static long oneTime = 0;//第一次时间
    private static long twoTime = 0;//第二次时间

    /**
     * 显示Toast
     */
    public static void showToast(Context context, String message) {

        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();

            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
