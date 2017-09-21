package com.zuoni.zuoni_common.utils.common;

/**
 * Created by zangyi_shuai_ge on 2017/8/4
 */

public class DateFormattedUtils {

    public DateFormattedUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 传入 2017-7-6
     * 返回 2017-07-06
     */
    public static String formattedDate(int year, int month, int day) {

        String sYear = year + "";
        String sMonth = month + "";
        String sDay = day + "";
        if (month < 10) {
            sMonth = "0" + sMonth;
        }
        if (day < 10) {
            sDay = "0" + sDay;
        }
        return sYear + "-" + sMonth + "-" + sDay;
    }

    /**
     * 传入 6
     * 返回 06
     */
    public static String formattedDate(int num) {
        String sNum = num + "";
        if (num < 10) {
            sNum = "0" + sNum;
        }
        return sNum;
    }

}
