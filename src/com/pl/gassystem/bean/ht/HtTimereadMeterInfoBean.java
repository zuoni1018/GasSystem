package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtTimereadMeterInfoBean {
    /**
     * JSQD : 28
     * OcrRead :
     * Deafault :
     * OcrResult :
     * KPYZ :
     * Operater :
     * ReadType : 2
     * Id : 3739
     * ReadTime : 2017-10-10 14:00:27
     * OcrState :
     * DevState : 63
     * DevPower : 3.6
     * KPXD :
     * PCH : 17100901
     * ReadState : 1
     * DJRQ : 0000
     * OcrTime :
     * CollectorNo : 04170003
     * ISC : 0
     * CreateTime : 2017-10-10 14:00:27
     * ThisRead : 0
     * FSQD :
     * CommunicateNo : 04160101
     * ImageName : FFFFFFFFFFFFFFFFFF
     */

    private String JSQD;
    private String OcrRead;
    private String Deafault;
    private String OcrResult;
    private String KPYZ;
    private String Operater;
    private String ReadType;
    private String Id;
    private String ReadTime;
    private String OcrState;
    private String DevState;
    private String DevPower;
    private String KPXD;
    private String PCH;
    private String ReadState;
    private String DJRQ;
    private String OcrTime;
    private String CollectorNo;
    private String ISC;
    private String CreateTime;
    private String ThisRead;
    private String FSQD;
    private String CommunicateNo;
    private String ImageName;

    private boolean isChoose=false;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getJSQD() {
        return JSQD;
    }

    public void setJSQD(String JSQD) {
        this.JSQD = JSQD;
    }

    public String getOcrRead() {
        return OcrRead;
    }

    public void setOcrRead(String OcrRead) {
        this.OcrRead = OcrRead;
    }

    public String getDeafault() {
        return Deafault;
    }

    public void setDeafault(String Deafault) {
        this.Deafault = Deafault;
    }

    public String getOcrResult() {
        return OcrResult;
    }

    public void setOcrResult(String OcrResult) {
        this.OcrResult = OcrResult;
    }

    public String getKPYZ() {
        return KPYZ;
    }

    public void setKPYZ(String KPYZ) {
        this.KPYZ = KPYZ;
    }

    public String getOperater() {
        return Operater;
    }

    public void setOperater(String Operater) {
        this.Operater = Operater;
    }

    public String getReadType() {
        return ReadType;
    }

    public void setReadType(String ReadType) {
        this.ReadType = ReadType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getReadTime() {
        return ReadTime;
    }

    public void setReadTime(String ReadTime) {
        this.ReadTime = ReadTime;
    }

    public String getOcrState() {
        return OcrState;
    }

    public void setOcrState(String OcrState) {
        this.OcrState = OcrState;
    }

    public String getDevState() {
        return DevState;
    }

    public void setDevState(String DevState) {
        this.DevState = DevState;
    }

    public String getDevPower() {
        return DevPower;
    }

    public void setDevPower(String DevPower) {
        this.DevPower = DevPower;
    }

    public String getKPXD() {
        return KPXD;
    }

    public void setKPXD(String KPXD) {
        this.KPXD = KPXD;
    }

    public String getPCH() {
        return PCH;
    }

    public void setPCH(String PCH) {
        this.PCH = PCH;
    }

    public String getReadState() {
        switch (ReadState) {
            case "1":
                return "Î´³­";
            case "0":
                return "ÒÑ³­";
            default:
                return "Î´Öª";
        }
    }

    public void setReadState(String ReadState) {
        this.ReadState = ReadState;
    }

    public String getDJRQ() {
        return DJRQ;
    }

    public void setDJRQ(String DJRQ) {
        this.DJRQ = DJRQ;
    }

    public String getOcrTime() {
        return OcrTime;
    }

    public void setOcrTime(String OcrTime) {
        this.OcrTime = OcrTime;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String CollectorNo) {
        this.CollectorNo = CollectorNo;
    }

    public String getISC() {
        return ISC;
    }

    public void setISC(String ISC) {
        this.ISC = ISC;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getThisRead() {
        return ThisRead;
    }

    public void setThisRead(String ThisRead) {
        this.ThisRead = ThisRead;
    }

    public String getFSQD() {
        return FSQD;
    }

    public void setFSQD(String FSQD) {
        this.FSQD = FSQD;
    }

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String CommunicateNo) {
        this.CommunicateNo = CommunicateNo;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String ImageName) {
        this.ImageName = ImageName;
    }
}
