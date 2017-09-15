package com.pl.gassystem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/8/18
 * Calendar ������
 */

public class CalendarUtils {


    //��ȡ��ǰ��
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    //��ȡ��ǰ��
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    //��ȡ��ǰ��
    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    //��ȡ��ǰСʱ 24Сʱ��
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    //��ȡ��ǰ��
    public static int getMin() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    //��ȡ��ǰ����
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * ��ȡ��ȥ�ڼ��������
     * �б�������������
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
     * ��ȡδ�� �� past �������
     * �б�������������
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
     * ��ȡpast Сʱ���ʱ��
     * �б������������� ʱ����
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
     * ��ȡpast ���Ӻ��ʱ��
     * �б������������� ʱ����
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
     * ���������ջ�ȡ����
     */
    public static String getWeek(int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        //��1��ʼ��7  1��������
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        switch (week) {
            case 1:
                return "��";
            case 2:
                return "һ";
            case 3:
                return "��";
            case 4:
                return "��";
            case 5:
                return "��";
            case 6:
                return "��";
            case 7:
                return "��";
            default:
                return "";
        }
    }

    /**
     * ������ �� ��ȡ��Ӧ���·� ����
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
     * ��ȡ���µ� ����
     */
    public static int getCurrentMonthDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }


    /**
     * 2012-03-12 ���ָ�ʽ��ʱ��ת��Calendar
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
    //��ú���ʱ��
    public static String   getHtTime(){
        String time="";
        String year=getYear()+"";
        time+=year.substring(2,4);//��ȡ����2λ
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
