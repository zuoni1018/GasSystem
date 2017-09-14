package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * 杭天发送命令
 */

public class HtSendMessage {

    public static String COMMAND_TYPE_DOOR_STATE = "02";//查看阀门状态
    public static String COMMAND_TYPE_OPEN_DOOR = "04";//打开阀门
    public static String COMMAND_TYPE_CLOSE_DOOR = "03";//关闭阀门
    public static String COMMAND_TYPE_COPY_COPYFROZEN = "01";//抄取冻结量
    public static String COMMAND_TYPE_COPY_NORMAL = "09";//抄取实时量


    public static String WAKE_UP_MARK_00 = "00";//不唤醒
    public static String WAKE_UP_MARK_01 = "01";//通讯号唤醒
    public static String WAKE_UP_MARK_02 = "02";//通讯号广播唤醒


    private String commandType;//操作类型
    private String bookNo;//操作表号
    private String bookNos;//操作的一群表号
    private String wakeUpMark;//唤醒标志
    private int wakeUpTime;//唤醒时间 传入ms 转成16进制

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookNos() {
        return bookNos;
    }

    public void setBookNos(String bookNos) {
        this.bookNos = bookNos;
    }

    public String getWakeUpMark() {
        return wakeUpMark;
    }

    public void setWakeUpMark(String wakeUpMark) {
        this.wakeUpMark = wakeUpMark;
    }

    public int getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(int wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }




}
