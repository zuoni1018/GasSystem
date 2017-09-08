package com.pl.concentrator.bean.model;


import com.pl.entity.CopyData;

/**
 * 集中器抄表
 * 纯无线类
 */
public class CtCopyData extends CopyData {

    private String CommunicateNo = "";//后台对应的表号
    private String CollectorNo = "";//所在集中器编号
    private String MeterTypeNo = "05";

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String communicateNo) {
        CommunicateNo = communicateNo;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String collectorNo) {
        CollectorNo = collectorNo;
    }

    public String getMeterTypeNo() {
        return MeterTypeNo;
    }

    public void setMeterTypeNo(String meterTypeNo) {
        MeterTypeNo = meterTypeNo;
    }
}
