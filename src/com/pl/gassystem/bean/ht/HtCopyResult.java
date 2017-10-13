package com.pl.gassystem.bean.ht;

import java.io.Serializable;

/**
 * Created by zangyi_shuai_ge on 2017/10/13
 */

public class HtCopyResult implements Serializable {

    private String CommunicateNo;
    private String ThisRead;
    private String DevPower;
    private String JSQD;
    private String DJR;
    private String State;
    private String MeterFacNo;
    private String ReadType;

    public String getReadType() {
        return ReadType;
    }

    public void setReadType(String readType) {
        ReadType = readType;
    }

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String communicateNo) {
        CommunicateNo = communicateNo;
    }

    public String getThisRead() {
        return ThisRead;
    }

    public void setThisRead(String thisRead) {
        ThisRead = thisRead;
    }

    public String getDevPower() {
        return DevPower;
    }

    public void setDevPower(String devPower) {
        DevPower = devPower;
    }

    public String getJSQD() {
        return JSQD;
    }

    public void setJSQD(String JSQD) {
        this.JSQD = JSQD;
    }

    public String getDJR() {
        return DJR;
    }

    public void setDJR(String DJR) {
        this.DJR = DJR;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getMeterFacNo() {
        return MeterFacNo;
    }

    public void setMeterFacNo(String meterFacNo) {
        MeterFacNo = meterFacNo;
    }
}
