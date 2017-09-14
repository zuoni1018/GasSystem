package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * 杭天发送命令
 */

public class HtGetMessage {

    public static String COMMAND_TYPE_DOOR_STATE = "82";//查看阀门状态
    public static String COMMAND_TYPE_OPEN_DOOR = "84";//打开阀门
    public static String COMMAND_TYPE_CLOSE_DOOR = "83";//关闭阀门
    public static String COMMAND_TYPE_COPY_COPYFROZEN = "81";//抄取冻结量
    public static String COMMAND_TYPE_COPY_NORMAL = "89";//抄取实时量

    private static String VALVE_OPEN = "C2";//阀门关闭
    private static String VALVE_CLOSE = "C3";//阀门打开

    private String valveState="";

    private String commandType;//操作类型
    private String bookNo;//操作表号

    private String voltage="00";//电压
    private String signal="00";//信号

    public String getValveState() {
        if(valveState.toUpperCase().equals(HtGetMessage.VALVE_OPEN)){
            return "阀开";
        }else if(valveState.toUpperCase().equals(HtGetMessage.VALVE_CLOSE)){
            return "阀关";
        }else {
            return "未知";
        }
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getCommandType() {
        if(commandType.equals(COMMAND_TYPE_DOOR_STATE)){
            return "查看阀门状态";
        }else   if(commandType.equals(COMMAND_TYPE_OPEN_DOOR)){
            return "打开阀门";
        }else   if(commandType.equals(COMMAND_TYPE_CLOSE_DOOR)){
            return "关闭阀门";
        }else {
            return "未开发";
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
            //16进制转10进制
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
