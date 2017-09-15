package com.pl.gassystem.bean.ct;

/**
 * Created by zangyi_shuai_ge on 2017/9/5
 */

public class CtBookInfo {
    /**
     * CommunicateNo : 1505040233
     * address : Ä¬ÈÏÐ¡Çø150´±
     * ReadState : 1
     * DevState : 0
     */

    private String CommunicateNo;
    private String address;
    private String ReadState;
    private String DevState;
    private boolean isChoose;
    private String  meterTypeNo;
    private  String MeterNo;
    private String CollectorNo;

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String CommunicateNo) {
        this.CommunicateNo = CommunicateNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReadState() {
        return ReadState;
    }

    public void setReadState(String ReadState) {
        this.ReadState = ReadState;
    }

    public String getDevState() {
        return DevState;
    }

    public void setDevState(String DevState) {
        this.DevState = DevState;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getMeterTypeNo() {
        return meterTypeNo;
    }

    public void setMeterTypeNo(String meterTypeNo) {
        this.meterTypeNo = meterTypeNo;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String meterNo) {
        MeterNo = meterNo;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String collectorNo) {
        CollectorNo = collectorNo;
    }
}
