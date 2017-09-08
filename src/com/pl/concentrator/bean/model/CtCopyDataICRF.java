package com.pl.concentrator.bean.model;

/**
 * 集中器抄表
 * IC纯无线类
 */
public class CtCopyDataICRF {
    private String name;
    private int Id = 0;// 流水号
    private String meterNo = "";// 表计编号
    private String meterName;// 表具名称
    private String Cumulant = "";// 累计量
    private String SurplusMoney = "";// 剩余金额
    private String OverZeroMoney = "";// 过零金额
    private int BuyTimes = 0;// 购买次数
    private int OverFlowTimes = 0;// 过流次数
    private int MagAttTimes = 0;// 磁攻击次数
    private int CardAttTimes = 0;// 卡攻击次数
    private int MeterState;// 表状态
    private String StateMessage;// 表状态解析信息
    private String CurrMonthTotal;// 当月累计量
    private String Last1MonthTotal;// 上月累计量
    private String Last2MonthTotal;// 上上月累计量
    private String Last3MonthTotal;// 上上上月累计量
    private String copyWay;// 抄表方式(S无线抄表 M人工输入抄表 G估抄Y远程抄表)
    private String copyTime;// 抄表时间
    private String copyMan;// 抄表员姓名
    private int copyState;// 抄表状态(0未抄 1已抄)
    private String unitPrice;// 当前单价
    private String accMoney; // 累计用气金额
    private String accBuyMoney; // 累计充值金额
    private String currentShow; // 本周期使用量
    private String dBm;// 信号强度
    private String elec;// 电量
    private boolean isChoose = false;//默认为不选中

    private  String CommunicateNo="";//集中器编号
    private String CollectorNo="";//集中器编号

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
