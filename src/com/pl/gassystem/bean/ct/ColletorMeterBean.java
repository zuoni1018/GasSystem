package com.pl.gassystem.bean.ct;

/**
 * Created by zangyi_shuai_ge on 2017/9/20
 */

public  class ColletorMeterBean {
    /**
     * CommunicateNo : 2017090604
     * address : 222
     * OcrRead : 16324.700
     * ReadState : 0
     * DevPower : 60
     * DevState : -17
     * ReadTime : 2017/9/7 16:07:01
     * remark :
     * MeterState : 32768
     * meterTypeNo : 05
     */

    private String CommunicateNo;
    private String address;
    private String OcrRead;
    private String ReadState;

    private String DevPower;//剩余电量
    private String DevState;//信号强度
    private String ReadTime;

    private String remark;
    private String MeterState;
    private String meterTypeNo;

    private boolean isShowMore=false;//是否显示更多

    public boolean isShowMore() {
        return isShowMore;
    }

    public void setShowMore(boolean showMore) {
        isShowMore = showMore;
    }

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

    public String getOcrRead() {
        return OcrRead;
    }

    public void setOcrRead(String OcrRead) {
        this.OcrRead = OcrRead;
    }

    public String getReadState() {

        //定抄 摄像表抄表反了
        if(meterTypeNo.equals("10")){
            switch (ReadState) {
                case "0":
                    return "1";
                case "1":
                    return "0";
                default:
                    return ReadState;
            }
        }
        return ReadState;
    }

    public void setReadState(String ReadState) {
        this.ReadState = ReadState;
    }

    public String getDevPower() {
        return DevPower;
    }

    public void setDevPower(String DevPower) {
        this.DevPower = DevPower;
    }

    public String getDevState() {
        return DevState;
    }

    public void setDevState(String DevState) {
        this.DevState = DevState;
    }

    public String getReadTime() {
        return ReadTime;
    }

    public void setReadTime(String ReadTime) {
        this.ReadTime = ReadTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMeterState() {
        return MeterState;
    }

    public void setMeterState(String MeterState) {
        this.MeterState = MeterState;
    }

    public String getMeterTypeNo() {
        return meterTypeNo;
    }

    public void setMeterTypeNo(String meterTypeNo) {
        this.meterTypeNo = meterTypeNo;
    }
}