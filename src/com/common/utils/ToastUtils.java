package com.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zangyi_shuai_ge on 2017/4/22
 * ��ֹ�ظ���ʾToast
 * ����Context ���봫�� Application
 */

public class ToastUtils {
    private static String oldMsg;//֮ǰ��ʾ������
    private static Toast toast = null;//Toast����
    private static long oneTime = 0;//��һ��ʱ��
    private static long twoTime = 0;//�ڶ���ʱ��

    /**
     * ��ʾToast
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
