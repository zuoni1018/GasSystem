package com.pl.concentrator.bean.model;


import com.pl.entity.CopyData;

/**
 * ����������
 * ��������
 */
public class CtCopyData extends CopyData {

    private String CommunicateNo = "";//��̨��Ӧ�ı��
    private String CollectorNo = "";//���ڼ��������
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
