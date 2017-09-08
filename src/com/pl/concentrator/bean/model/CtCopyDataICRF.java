package com.pl.concentrator.bean.model;

import com.pl.entity.CopyDataICRF;

/**
 * 集中器抄表
 * IC纯无线类
 */
public class CtCopyDataICRF extends CopyDataICRF{


    private  String CommunicateNo="";//集中器编号
    private String CollectorNo="";//集中器编号
    private String MeterTypeNo = "04";
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
        MeterTypeNo = "04";
    }





}
