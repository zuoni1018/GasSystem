package com.zuoni.zuoni_common.utils.common;

import java.text.DecimalFormat;

/**
 * Created by zangyi_shuai_ge on 2017/8/4
 * 值 工具类
 */

public class ValueUtils {

    /**
     * double 保留howLong位小数
     */
    public static String getDoubleValueString(double value,int howLong) {
        String sLong="######.";
        String a="";
        for (int i = 0; i <howLong ; i++) {
            a=a+"0";
        }
        sLong=sLong+a;
        DecimalFormat df = new DecimalFormat(sLong);
        return df.format(value);
    }


}
