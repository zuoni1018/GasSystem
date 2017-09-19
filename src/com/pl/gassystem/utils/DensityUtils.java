package com.pl.gassystem.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zangyi_shuai_ge on 2017/3/22
 * ��λת��������
 */

public class DensityUtils {

    private DensityUtils() {
        //���ܱ� new ����
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dpתpx
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * spתpx
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * pxתdp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * pxתsp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
