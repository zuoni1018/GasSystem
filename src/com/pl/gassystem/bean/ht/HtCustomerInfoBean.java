package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/10/11
 */

public class HtCustomerInfoBean {


    private  boolean isChoose=false;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    /**
     * KPXD : 14
     * KCQZSJ : 0023
     * DJR :
     * KEYCODE : 0102030405060708
     * MeterFacNo : 2
     * KPYZ : 9
     * Id : 1395
     * ADDR : 20170721
     * CommunicateNo : 05170016
     * KEYVER : 1
     */

    private String KPXD;
    private String KCQZSJ;
    private String DJR;
    private String KEYCODE;
    private String MeterFacNo;
    private String KPYZ;
    private String Id;
    private String ADDR;
    private String CommunicateNo;
    private String KEYVER;

    public String getKPXD() {
        return KPXD;
    }

    public void setKPXD(String KPXD) {
        this.KPXD = KPXD;
    }

    public String getKCQZSJ() {
        return KCQZSJ;
    }

    public void setKCQZSJ(String KCQZSJ) {
        this.KCQZSJ = KCQZSJ;
    }

    public String getDJR() {
        return DJR;
    }

    public void setDJR(String DJR) {
        this.DJR = DJR;
    }

    public String getKEYCODE() {
        return KEYCODE;
    }

    public void setKEYCODE(String KEYCODE) {
        this.KEYCODE = KEYCODE;
    }

    public String getMeterFacNo() {
        return MeterFacNo;
    }

    public void setMeterFacNo(String MeterFacNo) {
        this.MeterFacNo = MeterFacNo;
    }

    public String getKPYZ() {
        return KPYZ;
    }

    public void setKPYZ(String KPYZ) {
        this.KPYZ = KPYZ;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String CommunicateNo) {
        this.CommunicateNo = CommunicateNo;
    }

    public String getKEYVER() {
        return KEYVER;
    }

    public void setKEYVER(String KEYVER) {
        this.KEYVER = KEYVER;
    }
}
