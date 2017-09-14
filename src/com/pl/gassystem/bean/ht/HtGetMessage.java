package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * ���췢������
 */

public class HtGetMessage {

    public static String COMMAND_TYPE_DOOR_STATE = "82";//�鿴����״̬
    public static String COMMAND_TYPE_OPEN_DOOR = "84";//�򿪷���
    public static String COMMAND_TYPE_CLOSE_DOOR = "83";//�رշ���
    public static String COMMAND_TYPE_COPY_COPYFROZEN = "81";//��ȡ������
    public static String COMMAND_TYPE_COPY_NORMAL = "89";//��ȡʵʱ��

    private static String VALVE_OPEN = "C2";//���Źر�
    private static String VALVE_CLOSE = "C3";//���Ŵ�

    private String valveState="";

    private String commandType;//��������
    private String bookNo;//�������

    private String voltage="00";//��ѹ
    private String signal="00";//�ź�

    public String getValveState() {
        if(valveState.toUpperCase().equals(HtGetMessage.VALVE_OPEN)){
            return "����";
        }else if(valveState.toUpperCase().equals(HtGetMessage.VALVE_CLOSE)){
            return "����";
        }else {
            return "δ֪";
        }
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getCommandType() {
        if(commandType.equals(COMMAND_TYPE_DOOR_STATE)){
            return "�鿴����״̬";
        }else   if(commandType.equals(COMMAND_TYPE_OPEN_DOOR)){
            return "�򿪷���";
        }else   if(commandType.equals(COMMAND_TYPE_CLOSE_DOOR)){
            return "�رշ���";
        }else {
            return "δ����";
        }
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

    public String getVoltage() {
        if (voltage!=null){
            //16����ת10����
            double mVoltage=Integer.parseInt(voltage,16)/10.00;
            return mVoltage+"V";
        }
        return "";
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }
}
