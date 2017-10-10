package com.pl.gassystem.bean.ht;

import java.io.Serializable;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGroupInfoBean implements Serializable {
    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    /**
     * KPXD : 1
     * KCQZSJ : 1122
     * DJR : 1122
     * KEYCODE : 0102030405060708
     * BookNo : 1
     * MeterTypeNo : 1
     * AreaNo : 0000000001
     * KPYZ : 1
     * Remark :
     * GroupName : È¼Íø1
     * GroupNo : 1
     * KEYVER : 1
     */

    private boolean isChoose=false;

    private String KPXD;
    private String KCQZSJ;
    private String DJR;
    private String KEYCODE;
    private String BookNo;
    private String MeterTypeNo;
    private String AreaNo;
    private String KPYZ;
    private String Remark;
    private String GroupName;
    private String GroupNo;
    private String KEYVER;
    public String getMeterType(){
        switch (MeterTypeNo) {
            case "0":
                return "ÉãÏñ±í";
            case "1":
                return "´¿ÎÞÏß";
            case "2":
                return "À©Æµ±í";
            default:
                return "Î´Öª";
        }
    }
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

    public String getBookNo() {
        return BookNo;
    }

    public void setBookNo(String BookNo) {
        this.BookNo = BookNo;
    }

    public String getMeterTypeNo() {
        return MeterTypeNo;
    }

    public void setMeterTypeNo(String MeterTypeNo) {
        this.MeterTypeNo = MeterTypeNo;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String AreaNo) {
        this.AreaNo = AreaNo;
    }

    public String getKPYZ() {
        return KPYZ;
    }

    public void setKPYZ(String KPYZ) {
        this.KPYZ = KPYZ;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getGroupNo() {
        return GroupNo;
    }

    public void setGroupNo(String GroupNo) {
        this.GroupNo = GroupNo;
    }

    public String getKEYVER() {
        return KEYVER;
    }

    public void setKEYVER(String KEYVER) {
        this.KEYVER = KEYVER;
    }
}
