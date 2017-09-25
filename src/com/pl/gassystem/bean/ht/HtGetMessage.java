package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * �����������
 */

public class HtGetMessage {
    //��������
    public static final String COMMAND_TYPE_DOOR_STATE = "82";//�鿴����״̬
    public static final String COMMAND_TYPE_OPEN_DOOR = "84";//�򿪷���
    public static final String COMMAND_TYPE_CLOSE_DOOR = "83";//�رշ���
    public static final String COMMAND_TYPE_COPY_FROZEN = "81";//��ȡ������
    public static final String COMMAND_TYPE_COPY_NORMAL = "89";//��ȡʵʱ��
    public static final String COMMAND_TYPE_QUERY_PARAMETER = "bc";//��ѯ����
    public static final String COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT = "91";//���ñ�Ż����ۼ���
    public static final String COMMAND_TYPE_SET_PARAMETER = "86";//���ñ�߲���
    public static final String COMMAND_TYPE_SET_KEY = "8b";//����Key

    //���ſ���״̬
    private static String VALVE_OPEN = "C3";//���Źر�
    private static String VALVE_CLOSE = "C2";//���Ŵ�

    //����״̬
    private String valveState = "";//���ſ���״̬
    private String commandType = "";//��������
    private String bookNo;//�������

    private String voltage = "00";//��ѹ
    private String signal = "00";//�ź�
    private String frozenTime = "";//��������


    public String getCopyValue() {
        return copyValue;
    }

    public void setCopyValue(String copyValue) {
        this.copyValue = copyValue;
    }

    private String copyValue = "";

    public String getFrozenTime() {
        return frozenTime;
    }

    public void setFrozenTime(String frozenTime) {
        this.frozenTime = frozenTime;
    }

    public String getValveState() {

        if (commandType.equals(COMMAND_TYPE_COPY_NORMAL)
                | commandType.equals(COMMAND_TYPE_COPY_FROZEN)) {
            String valve = hexString2binaryString(valveState);
            if (valve.length() == 8) {
                char[] a = valve.toCharArray();
                String message = "";
                //��7λ �����ֶ� 0

                //��6λ ����״̬�������� 0���� 1���� 1
                if ((a[1] + "").equals("0")) {
                    message += " ���Ź���";
                } else {
                    message += " ��������";
                }

                //�� 5 4 Ϊģ���������� 00 �������  01���� 10 ���� 2 3
                String s54 = (a[2] + "" + a[3]);
                if (s54.equals("00")) {
                    message += " ģ����������: �������";
                } else if (s54.equals("01")) {
                    message += " ģ����������: ����";
                } else {
                    message += " ģ����������: ����";
                }
                //3 2 Ϊ�����ֶ� 4 5
                //1 0 Ϊ����״̬ 6 7
                String s67 = (a[6] + "" + a[7]);
                if (s67.equals("00")) {
                    message += " ����״̬: ����";
                } else if (s54.equals("10")) {
                    message += " ����״̬: �ر�";
                } else {
                    message += " ����״̬: ��";
                }
                return "\n[---  " + message + "  ---]\n";
            } else {
                return "����״̬����ʧ��";
            }

        } else {
            if (valveState.toUpperCase().equals(HtGetMessage.VALVE_OPEN)) {
                return "����";
            } else if (valveState.toUpperCase().equals(HtGetMessage.VALVE_CLOSE)) {
                return "����";
            } else {
                return "δ֪";
            }
        }
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getCommandType() {

        if (commandType.equals(COMMAND_TYPE_DOOR_STATE)) {
            return "�鿴����״̬";
        } else if (commandType.equals(COMMAND_TYPE_OPEN_DOOR)) {
            return "�򿪷���";
        } else if (commandType.equals(COMMAND_TYPE_CLOSE_DOOR)) {
            return "�رշ���";
        } else if (commandType.equals(COMMAND_TYPE_COPY_NORMAL)) {
            return "��ͨ����";
        } else if (commandType.equals(COMMAND_TYPE_COPY_FROZEN)) {
            return "��ȡ������";
        } else if (commandType.toLowerCase().equals(COMMAND_TYPE_QUERY_PARAMETER)) {
            return "��ѯ����";
        } else if (commandType.equals(COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)) {
            return "�޸ı�Ż��ۼ���";
        } else if (commandType.equals(COMMAND_TYPE_SET_PARAMETER)) {
            return "���ñ�߲���";
        } else if (commandType.toLowerCase().equals(COMMAND_TYPE_SET_KEY)) {
            return "�޸���Կ";
        } else {
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
        if (voltage != null) {
            //16����ת10����
            double mVoltage = Integer.parseInt(voltage, 16) / 10.00;
            return mVoltage + "V";
        }
        return "";
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getSignal() {
        return Integer.parseInt(this.signal, 16) + "%";
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }


    public String getResult() {

        if (commandType.equals(COMMAND_TYPE_COPY_NORMAL)
                | commandType.equals(COMMAND_TYPE_COPY_FROZEN)) {
            return "����"
                    + "\n\n�������:\n"
                    + " ��������:" + this.getCommandType()
                    + " ���:" + this.getBookNo()
                    + " \n����״̬:" + this.getValveState()
                    + " ��ص�ѹ:" + this.getVoltage()
                    + " �ź�ǿ��:" + this.getSignal()
                    + " ��������:" + this.getFrozenTime()
                    + " ����ֵ:" + this.getCopyValue();
        } else {
            return "\n\n����"
                    + "�������:\n"
                    + " ��������:" + this.getCommandType()
                    + " ���:" + this.getBookNo()
                    + " \n����״̬:" + this.getValveState()
                    + " ��ص�ѹ:" + this.getVoltage();
//                    + " �ź�ǿ��:" + this.getSignal();
        }

    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
}
