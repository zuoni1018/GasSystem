package com.pl.gassystem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/8/18
 * Calendar 工具类
 */

public class CalendarUtils {


    //获取当前年
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    //获取当前月
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    //获取当前日
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    //获取当前小时 24小时制
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    //获取当前分
    public static int getMin() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    //获取当前日历
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 获取过去第几天的日期
     * 列表里存的是年月日
     */
    public static List<Integer> getPastDate(int past) {
        List<Integer> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        dateList.add(calendar.get(Calendar.YEAR));
        dateList.add(calendar.get(Calendar.MONTH) + 1);
        dateList.add(calendar.get(Calendar.DAY_OF_MONTH));
        return dateList;
    }

    /**
     * 获取未来 第 past 天的日期
     * 列表里存的是年月日
     */
    public static List<Integer> getFetureDate(int past) {
        List<Integer> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        dateList.add(calendar.get(Calendar.YEAR));
        dateList.add(calendar.get(Calendar.MONTH) + 1);
        dateList.add(calendar.get(Calendar.DAY_OF_MONTH));
        return dateList;
    }

    /**
     * 获取past 小时后的时间
     * 列表里存的是年月日 时分秒
     */
    public static List<Integer> getFetureHourDate(int past) {
        List<Integer> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, past);

        dateList.add(calendar.get(Calendar.YEAR));
        dateList.add(calendar.get(Calendar.MONTH) + 1);
        dateList.add(calendar.get(Calendar.DAY_OF_MONTH));

        dateList.add(calendar.get(Calendar.HOUR_OF_DAY));
        dateList.add(calendar.get(Calendar.MINUTE));
        dateList.add(calendar.get(Calendar.SECOND));

        return dateList;
    }

    /**
     * 获取past 分钟后的时间
     * 列表里存的是年月日 时分秒
     */
    public static List<Integer> getFetureMinDate(int past) {
        List<Integer> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, past);

        dateList.add(calendar.get(Calendar.YEAR));
        dateList.add(calendar.get(Calendar.MONTH) + 1);
        dateList.add(calendar.get(Calendar.DAY_OF_MONTH));

        dateList.add(calendar.get(Calendar.HOUR_OF_DAY));
        dateList.add(calendar.get(Calendar.MINUTE));
        dateList.add(calendar.get(Calendar.SECOND));

        return dateList;
    }


    /**
     * 根据年月日获取星期
     */
    public static String getWeek(int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        //从1开始到7  1代表周天
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        switch (week) {
            case 1:
                return "天";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }


    /**
     * 2012-03-12 这种格式的时间转成Calendar
     */
    public static Calendar string2Calendar(String sTime) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(sTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
    //获得杭天时间
    public static String   getHtTime(){
        String time="";
        String year=getYear()+"";
        time+=year.substring(2,4);//截取后面2位
        int month=getMonth();
        if(month<10){
            time+="0"+month;
        }else {
            time+=month;
        }

        int day=getDay();

        if(day<10){
            time+="0"+day;
        }else {
            time+=day;
        }

        int hour=getHour();

        if(hour<10){
            time+="0"+hour;
        }else {
            time+=hour;
        }

        int min=getMin();

        if(min<10){
            time+="0"+min;
        }else {
            time+=min;
        }
        if(time.length()==10){
            return time+"00";
        }else {
            return "0000000000";
        }
    }
}
