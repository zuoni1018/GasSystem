package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtBookInfoBean {
    /**
     * StaffNo : 1
     * MeterTypeNo : 1
     * BookName : È¼Íø1
     * AreaNo : 0000000001
     * BookNo : 1
     * Remark :
     */

    private String StaffNo;
    private String MeterTypeNo;
    private String BookName;
    private String AreaNo;
    private String BookNo;
    private String Remark;

    public String getStaffNo() {
        return StaffNo;
    }

    public void setStaffNo(String StaffNo) {
        this.StaffNo = StaffNo;
    }

    public String getMeterTypeNo() {
        return MeterTypeNo;
    }

    public void setMeterTypeNo(String MeterTypeNo) {
        this.MeterTypeNo = MeterTypeNo;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String BookName) {
        this.BookName = BookName;
    }

    public String getAreaNo() {
        return AreaNo;
    }

    public void setAreaNo(String AreaNo) {
        this.AreaNo = AreaNo;
    }

    public String getMeterType() {
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

    public String getBookNo() {
        return BookNo;
    }

    public void setBookNo(String BookNo) {
        this.BookNo = BookNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
}
