package com.pl.concentrator.bean.model;

/**
 * ����������
 * IC��������
 */
public class CtCopyDataICRF {
    private String name;
    private int Id = 0;// ��ˮ��
    private String meterNo = "";// ��Ʊ��
    private String meterName;// �������
    private String Cumulant = "";// �ۼ���
    private String SurplusMoney = "";// ʣ����
    private String OverZeroMoney = "";// ������
    private int BuyTimes = 0;// �������
    private int OverFlowTimes = 0;// ��������
    private int MagAttTimes = 0;// �Ź�������
    private int CardAttTimes = 0;// ����������
    private int MeterState;// ��״̬
    private String StateMessage;// ��״̬������Ϣ
    private String CurrMonthTotal;// �����ۼ���
    private String Last1MonthTotal;// �����ۼ���
    private String Last2MonthTotal;// �������ۼ���
    private String Last3MonthTotal;// ���������ۼ���
    private String copyWay;// ����ʽ(S���߳��� M�˹����볭�� G����YԶ�̳���)
    private String copyTime;// ����ʱ��
    private String copyMan;// ����Ա����
    private int copyState;// ����״̬(0δ�� 1�ѳ�)
    private String unitPrice;// ��ǰ����
    private String accMoney; // �ۼ��������
    private String accBuyMoney; // �ۼƳ�ֵ���
    private String currentShow; // ������ʹ����
    private String dBm;// �ź�ǿ��
    private String elec;// ����
    private boolean isChoose = false;//Ĭ��Ϊ��ѡ��

    private  String CommunicateNo="";//���������
    private String CollectorNo="";//���������

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String communicateNo) {
        CommunicateNo = communicateNo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getCumulant() {
        return Cumulant;
    }

    public void setCumulant(String cumulant) {
        Cumulant = cumulant;
    }

    public String getSurplusMoney() {
        return SurplusMoney;
    }

    public void setSurplusMoney(String surplusMoney) {
        SurplusMoney = surplusMoney;
    }

    public String getOverZeroMoney() {
        return OverZeroMoney;
    }

    public void setOverZeroMoney(String overZeroMoney) {
        OverZeroMoney = overZeroMoney;
    }

    public int getBuyTimes() {
        return BuyTimes;
    }

    public void setBuyTimes(int buyTimes) {
        BuyTimes = buyTimes;
    }

    public int getOverFlowTimes() {
        return OverFlowTimes;
    }

    public void setOverFlowTimes(int overFlowTimes) {
        OverFlowTimes = overFlowTimes;
    }

    public int getMagAttTimes() {
        return MagAttTimes;
    }

    public void setMagAttTimes(int magAttTimes) {
        MagAttTimes = magAttTimes;
    }

    public int getCardAttTimes() {
        return CardAttTimes;
    }

    public void setCardAttTimes(int cardAttTimes) {
        CardAttTimes = cardAttTimes;
    }

    public int getMeterState() {
        return MeterState;
    }

    public void setMeterState(int meterState) {
        MeterState = meterState;
    }

    public String getStateMessage() {
        return StateMessage;
    }

    public void setStateMessage(String stateMessage) {
        StateMessage = stateMessage;
    }

    public String getCurrMonthTotal() {
        return CurrMonthTotal;
    }

    public void setCurrMonthTotal(String currMonthTotal) {
        CurrMonthTotal = currMonthTotal;
    }

    public String getLast1MonthTotal() {
        return Last1MonthTotal;
    }

    public void setLast1MonthTotal(String last1MonthTotal) {
        Last1MonthTotal = last1MonthTotal;
    }

    public String getLast2MonthTotal() {
        return Last2MonthTotal;
    }

    public void setLast2MonthTotal(String last2MonthTotal) {
        Last2MonthTotal = last2MonthTotal;
    }

    public String getLast3MonthTotal() {
        return Last3MonthTotal;
    }

    public void setLast3MonthTotal(String last3MonthTotal) {
        Last3MonthTotal = last3MonthTotal;
    }

    public String getCopyWay() {
        return copyWay;
    }

    public void setCopyWay(String copyWay) {
        this.copyWay = copyWay;
    }

    public String getCopyTime() {
        return copyTime;
    }

    public void setCopyTime(String copyTime) {
        this.copyTime = copyTime;
    }

    public String getCopyMan() {
        return copyMan;
    }

    public void setCopyMan(String copyMan) {
        this.copyMan = copyMan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCopyState() {
        return copyState;
    }

    public void setCopyState(int copyState) {
        this.copyState = copyState;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getdBm() {
        return dBm;
    }

    public void setdBm(String dBm) {
        this.dBm = dBm;
    }

    public String getElec() {
        return elec;
    }

    public void setElec(String elec) {
        this.elec = elec;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAccMoney() {
        return accMoney;
    }

    public void setAccMoney(String accMoney) {
        this.accMoney = accMoney;
    }

    public String getAccBuyMoney() {
        return accBuyMoney;
    }

    public void setAccBuyMoney(String accBuyMoney) {
        this.accBuyMoney = accBuyMoney;
    }

    public String getCurrentShow() {
        return currentShow;
    }

    public void setCurrentShow(String currentShow) {
        this.currentShow = currentShow;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String collectorNo) {
        CollectorNo = collectorNo;
    }
}
