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
    public static final String COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT = "91";//设置表号或者累计量
    public static final String COMMAND_TYPE_QUERY_PARAMETER = "bc";//查询参数
    public static final String COMMAND_TYPE_SET_PARAMETER = "06";//设置参数
    public static final String COMMAND_TYPE_SET_KEY = "0b";//设置参数

    public static String getCommandString(String commandType) {
        switch (commandType) {
            case COMMAND_TYPE_DOOR_STATE:
                return "查看阀门状态";
            case COMMAND_TYPE_OPEN_DOOR:
                return "打开阀门";
            case COMMAND_TYPE_CLOSE_DOOR:
                return "关闭阀门";
            case COMMAND_TYPE_COPY_FROZEN:
                return "抄取冻结量";
            case COMMAND_TYPE_COPY_NORMAL:
                return "抄取实时量";
            case COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT:
                return "查看阀门状态";
            case COMMAND_TYPE_QUERY_PARAMETER:
                return "查看表具状态";
            case COMMAND_TYPE_SET_KEY:
                return "查看阀门状态";
        }

        return "";
    }

    //唤醒模式
    public static final String WAKE_UP_MARK_00 = "00";//不唤醒
    public static final String WAKE_UP_MARK_01 = "01";//通讯号唤醒
    public static final String WAKE_UP_MARK_02 = "02";//通讯号广播唤醒

    //抄表模式 单抄 群抄
    public static final String COPY_TYPE_SINGLE = "01";//单抄
    public static final String COPY_TYPE_GROUP = "02";//群抄


    private String kuoPinYinZi = "";//扩频因子
    private String kuoPinXinDao = "";//扩频信道
    private String commandType;//操作类型
    private String bookNo;//操作表号 多表操作改变量赋值为 FFFFFFFF
    private List<String> bookNos;//操作的一群表号
    private String wakeUpMark;//唤醒标志
    private int wakeUpTime;//唤醒时间 传入ms 转成16进制
    private String setTime;//设置抄表时间
    private String copyType;//如果是抄表  分为单抄和群抄


    public String getKuoPinYinZi() {
        return kuoPinYinZi;
    }

    public void setKuoPinYinZi(String kuoPinYinZi) {
        this.kuoPinYinZi = kuoPinYinZi;
    }

    public String getKuoPinXinDao() {
        return kuoPinXinDao;
    }

    public void setKuoPinXinDao(String kuoPinXinDao) {
        this.kuoPinXinDao = kuoPinXinDao;
    }

    public String getCopyType() {
        return copyType;
    }

    public void setCopyType(String copyType) {
        this.copyType = copyType;
    }


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
