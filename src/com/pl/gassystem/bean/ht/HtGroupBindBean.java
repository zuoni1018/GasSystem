package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGroupBindBean {
    /**
     * MeterTypeNo : 1
     * MeterNo : 12345678
     * GroupNo : 1
     */
    public String getMeterType(){
        switch (MeterTypeNo) {
            case "0":
                return "�����";
            case "1":
                return "������";
            case "2":
                return "��Ƶ��";
            default:
                return "δ֪";
        }
    }
    private String MeterTypeNo;
    private String MeterNo;
    private String GroupNo;

    public String getMeterTypeNo() {
        return MeterTypeNo;
    }

    public void setMeterTypeNo(String MeterTypeNo) {
        this.MeterTypeNo = MeterTypeNo;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String MeterNo) {
        this.MeterNo = MeterNo;
    }

    public String getGroupNo() {
        return GroupNo;
    }

    public void setGroupNo(String GroupNo) {
        this.GroupNo = GroupNo;
    }

    private boolean isChoose=false;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
