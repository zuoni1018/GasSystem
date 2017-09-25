package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * 杭天接收命令
 */

public class HtGetMessage {
    //命令类型
    public static final String COMMAND_TYPE_DOOR_STATE = "82";//查看阀门状态
    public static final String COMMAND_TYPE_OPEN_DOOR = "84";//打开阀门
    public static final String COMMAND_TYPE_CLOSE_DOOR = "83";//关闭阀门
    public static final String COMMAND_TYPE_COPY_FROZEN = "81";//抄取冻结量
    public static final String COMMAND_TYPE_COPY_NORMAL = "89";//抄取实时量
    public static final String COMMAND_TYPE_QUERY_PARAMETER = "bc";//查询参数
    public static final String COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT = "91";//设置表号或者累计量
    public static final String COMMAND_TYPE_SET_PARAMETER = "86";//设置表具参数
    public static final String COMMAND_TYPE_SET_KEY = "8b";//设置Key

    //阀门开关状态
    private static String VALVE_OPEN = "C3";//阀门关闭
    private static String VALVE_CLOSE = "C2";//阀门打开

    //阀门状态
    private String valveState = "";//阀门开关状态
    private String commandType = "";//操作类型
    private String bookNo;//操作表号

    private String voltage = "00";//电压
    private String signal = "00";//信号
    private String frozenTime = "";//冻结日期


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
                //第7位 保留字段 0

                //第6位 量传状态返回数据 0故障 1正常 1
                if ((a[1] + "").equals("0")) {
                    message += " 阀门故障";
                } else {
                    message += " 阀门正常";
                }

                //第 5 4 为模块数据类型 00 光电数据  01摄像 10 脉冲 2 3
                String s54 = (a[2] + "" + a[3]);
                if (s54.equals("00")) {
                    message += " 模块数据类型: 光电数据";
                } else if (s54.equals("01")) {
                    message += " 模块数据类型: 摄像";
                } else {
                    message += " 模块数据类型: 脉冲";
                }
                //3 2 为保留字段 4 5
                //1 0 为阀门状态 6 7
                String s67 = (a[6] + "" + a[7]);
                if (s67.equals("00")) {
                    message += " 阀门状态: 故障";
                } else if (s54.equals("10")) {
                    message += " 阀门状态: 关闭";
                } else {
                    message += " 阀门状态: 打开";
                }
                return "\n[---  " + message + "  ---]\n";
            } else {
                return "阀门状态解析失败";
            }

        } else {
            if (valveState.toUpperCase().equals(HtGetMessage.VALVE_OPEN)) {
                return "阀开";
            } else if (valveState.toUpperCase().equals(HtGetMessage.VALVE_CLOSE)) {
                return "阀关";
            } else {
                return "未知";
            }
        }
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getCommandType() {

        if (commandType.equals(COMMAND_TYPE_DOOR_STATE)) {
            return "查看阀门状态";
        } else if (commandType.equals(COMMAND_TYPE_OPEN_DOOR)) {
            return "打开阀门";
        } else if (commandType.equals(COMMAND_TYPE_CLOSE_DOOR)) {
            return "关闭阀门";
        } else if (commandType.equals(COMMAND_TYPE_COPY_NORMAL)) {
            return "普通抄表";
        } else if (commandType.equals(COMMAND_TYPE_COPY_FROZEN)) {
            return "抄取冻结量";
        } else if (commandType.toLowerCase().equals(COMMAND_TYPE_QUERY_PARAMETER)) {
            return "查询参数";
        } else if (commandType.equals(COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)) {
            return "修改表号或累计量";
        } else if (commandType.equals(COMMAND_TYPE_SET_PARAMETER)) {
            return "设置表具参数";
        } else if (commandType.toLowerCase().equals(COMMAND_TYPE_SET_KEY)) {
            return "修改密钥";
        } else {
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
        if (voltage != null) {
            //16进制转10进制
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
            return "杭天"
                    + "\n\n解析结果:\n"
                    + " 命令类型:" + this.getCommandType()
                    + " 表号:" + this.getBookNo()
                    + " \n阀门状态:" + this.getValveState()
                    + " 电池电压:" + this.getVoltage()
                    + " 信号强度:" + this.getSignal()
                    + " 冻结日期:" + this.getFrozenTime()
                    + " 抄表值:" + this.getCopyValue();
        } else {
            return "\n\n杭天"
                    + "解析结果:\n"
                    + " 命令类型:" + this.getCommandType()
                    + " 表号:" + this.getBookNo()
                    + " \n阀门状态:" + this.getValveState()
                    + " 电池电压:" + this.getVoltage();
//                    + " 信号强度:" + this.getSignal();
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
