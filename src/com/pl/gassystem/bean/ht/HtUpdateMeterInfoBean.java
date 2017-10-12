package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtUpdateMeterInfoBean {


    /**
     * MeterTypeNo : 2
     * KEYVER2 : 01
     * KCQZSJ1 : 1123
     * KPYZ2 : 11
     * KPXD2 : 9
     * MeterNo : 05170016
     * DJR1 : 1122
     * KPYZ1 : 9
     * KEYCODE2 : 0102030405060708
     * KPXD1 : 9
     * KEYCODE1 : 0102030405060708
     * AreaNo : 0000000001
     * KEYVER1 : 01
     * DJR2 : 1122
     * KCQZSJ2 : 1123
     */

    private String MeterTypeNo;
    private String KEYVER2;
    private String KCQZSJ1;
    private String KPYZ2;
    private String KPXD2;
    private String MeterNo;
    private String DJR1;
    private String KPYZ1;
    private String KEYCODE2;
    private String KPXD1;
    private String KEYCODE1;
    private String AreaNo;
    private String KEYVER1;
    private String DJR2;
    private String KCQZSJ2;

    private boolean isChoose=false;

    public boolean isChoose() {
        return isChoose;
    }
    public String getMeterType(){
        switch (MeterTypeNo) {
            case "0":
                return "摄像表";
            case "1":
                return "纯无线";
            case "2":
                return "扩频表";
            default:
                return "未知";
        }
    }
    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getMeterTypeNo() {
        return MeterTypeNo;
    }

    public void setMeterTypeNo(String MeterTypeNo) {
        this.MeterTypeNo = MeterTypeNo;
    }

    public String getKEYVER2() {
        return KEYVER2;
    }

    public void setKEYVER2(String KEYVER2) {
        this.KEYVER2 = KEYVER2;
    }

    public String getKCQZSJ1() {
        return KCQZSJ1;
    }

    public void setKCQZSJ1(String KCQZSJ1) {
        this.KCQZSJ1 = KCQZSJ1;
    }

    public String getKPYZ2() {
        return KPYZ2;
    }

    public void setKPYZ2(String KPYZ2) {
        this.KPYZ2 = KPYZ2;
    }

    public String getKPXD2() {
        return KPXD2;
    }

    public void setKPXD2(String KPXD2) {
        this.KPXD2 = KPXD2;
    }

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String MeterNo) {
        this.MeterNo = MeterNo;
    }

    public String getDJR1() {
        return DJR1;
    }

    public void setDJR1(String DJR1) {
        this.DJR1 = DJR1;
    }

    public String getKPYZ1() {
        return KPYZ1;
    }

    public void setKPYZ1(String KPYZ1) {
        this.KPYZ1 = KPYZ1;
    }

    public String getKEYCODE2() {
        return KEYCODE2;
    }

    public void setKEYCODE2(String KEYCODE2) {
        this.KEYCODE2 = KEYCODE2;
    }

    public String getKPXD1() {
        return KPXD1;
    }

    public void setKPXD1(String KPXD1) {
        this.KPXD1 = KPXD1;
    }

    public String getKEYCODE1() {
        return KEYCODE1;
    }

    public void setKEYCODE1(String KEYCODE1) {
        this.KEYCODE1 = KEYCODE1;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String AreaNo) {
        this.AreaNo = AreaNo;
    }

    public String getKEYVER1() {
        return KEYVER1;
    }

    public void setKEYVER1(String KEYVER1) {
        this.KEYVER1 = KEYVER1;
    }

    public String getDJR2() {
        return DJR2;
    }

    public void setDJR2(String DJR2) {
        this.DJR2 = DJR2;
    }

    public String getKCQZSJ2() {
        return KCQZSJ2;
    }

    public void setKCQZSJ2(String KCQZSJ2) {
        this.KCQZSJ2 = KCQZSJ2;
    }
}
