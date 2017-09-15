package com.pl.gassystem.bean.ht;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * ���췢������
 */

public class HtSendMessage {

    //����ģʽ
    public static final String COMMAND_TYPE_DOOR_STATE = "02";//�鿴����״̬
    public static final String COMMAND_TYPE_OPEN_DOOR = "04";//�򿪷���
    public static final String COMMAND_TYPE_CLOSE_DOOR = "03";//�رշ���
    public static final String COMMAND_TYPE_COPY_FROZEN = "01";//��ȡ������
    public static final String COMMAND_TYPE_COPY_NORMAL = "09";//��ȡʵʱ��

    //����ģʽ
    public static final String WAKE_UP_MARK_00 = "00";//������
    public static final String WAKE_UP_MARK_01 = "01";//ͨѶ�Ż���
    public static final String WAKE_UP_MARK_02 = "02";//ͨѶ�Ź㲥����

    //����ģʽ ���� Ⱥ��
    public static final String COPY_TYPE_SINGLE = "01";//����
    public static final String COPY_TYPE_GROUP = "02";//Ⱥ��

    private String commandType;//��������
    private String bookNo;//�������
    private List<String > bookNos;//������һȺ���
    private String wakeUpMark;//���ѱ�־
    private int wakeUpTime;//����ʱ�� ����ms ת��16����
    private String setTime;//���ó���ʱ��

    public String getCopyType() {
        return copyType;
    }

    public void setCopyType(String copyType) {
        this.copyType = copyType;
    }

    private String copyType;//����ǳ���  ��Ϊ������Ⱥ��




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
