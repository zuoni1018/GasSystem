package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * ���췢������
 */

public class HtSendMessage {

    public static String COMMAND_TYPE_DOOR_STATE = "02";//�鿴����״̬
    public static String COMMAND_TYPE_OPEN_DOOR = "04";//�򿪷���
    public static String COMMAND_TYPE_CLOSE_DOOR = "03";//�رշ���
    public static String COMMAND_TYPE_COPY_COPYFROZEN = "01";//��ȡ������
    public static String COMMAND_TYPE_COPY_NORMAL = "09";//��ȡʵʱ��


    public static String WAKE_UP_MARK_00 = "00";//������
    public static String WAKE_UP_MARK_01 = "01";//ͨѶ�Ż���
    public static String WAKE_UP_MARK_02 = "02";//ͨѶ�Ź㲥����


    private String commandType;//��������
    private String bookNo;//�������
    private String bookNos;//������һȺ���
    private String wakeUpMark;//���ѱ�־
    private int wakeUpTime;//����ʱ�� ����ms ת��16����

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
