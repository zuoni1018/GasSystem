package com.pl.gassystem.bean.ht;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * 杭天发送命令
 */

public class HtSendMessage {

    //命令模式
    public static final String COMMAND_TYPE_DOOR_STATE = "02";//查看阀门状态
    public static final String COMMAND_TYPE_OPEN_DOOR = "04";//打开阀门
    public static final String COMMAND_TYPE_CLOSE_DOOR = "03";//关闭阀门
    public static final String COMMAND_TYPE_COPY_FROZEN = "01";//抄取冻结量
    public static final String COMMAND_TYPE_COPY_NORMAL = "09";//抄取实时量

    //唤醒模式
    public static final String WAKE_UP_MARK_00 = "00";//不唤醒
    public static final String WAKE_UP_MARK_01 = "01";//通讯号唤醒
    public static final String WAKE_UP_MARK_02 = "02";//通讯号广播唤醒

    //抄表模式 单抄 群抄
    public static final String COPY_TYPE_SINGLE = "01";//单抄
    public static final String COPY_TYPE_GROUP = "02";//群抄

    private String commandType;//操作类型
    private String bookNo;//操作表号
    private List<String > bookNos;//操作的一群表号
    private String wakeUpMark;//唤醒标志
    private int wakeUpTime;//唤醒时间 传入ms 转成16进制
    private String setTime;//设置抄表时间

    public String getCopyType() {
        return copyType;
    }

    public void setCopyType(String copyType) {
        this.copyType = copyType;
    }

    private String copyType;//如果是抄表  分为单抄和群抄




    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }



    public List<String> getBookNos() {
        return bookNos;
    }

    public void setBookNos(List<String> bookNos) {
        this.bookNos = bookNos;
    }



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
