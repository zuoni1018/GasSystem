package com.pl.concentrator.bean.model;

import com.pl.entity.CopyDataICRF;

/**
 * ����������
 * IC��������
 */
public class CtCopyDataICRF extends CopyDataICRF{


    private  String CommunicateNo="";//���������
    private String CollectorNo="";//���������
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
