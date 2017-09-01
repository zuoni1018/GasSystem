package com.pl.concentrator.bean;

/**
 * 集中器抄表
 * 摄像抄表
 */
public class CtCopyDataPhoto {
    private int Id;// 流水号
    private String CommunicateNo;// 表计编号
    private int ReadState;// 抄表状态（0-未抄，1-已抄）
    private String ImageName;// 图片路径+名称
    private String CollectorNo;// 集中器编号
    private String Operater;// 操作员
    private String ReadTime;// 定时抄表时间
    private String DevState;// 采集器信号强度
    private String DevPower;// 采集器电压
    private String OcrRead;// 识别读数
    private int OcrState;// 识别状态（0-未识别，1-已识别，2-置疑）
    private String ThisRead;// 人工确认读数
    private String OcrResult;// 识别对错（0-正确，1-错误）
    private String OcrTime;// 识别时间
    private String CreateTime;// 命令生成时间
    private String meterName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCommunicateNo() {
        return CommunicateNo;
    }

    public void setCommunicateNo(String communicateNo) {
        CommunicateNo = communicateNo;
    }

    public int getReadState() {
        return ReadState;
    }

    public void setReadState(int readState) {
        ReadState = readState;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String collectorNo) {
        CollectorNo = collectorNo;
    }

    public String getOperater() {
        return Operater;
    }

    public void setOperater(String operater) {
        Operater = operater;
    }

    public String getReadTime() {
        return ReadTime;
    }

    public void setReadTime(String readTime) {
        ReadTime = readTime;
    }

    public String getDevState() {
        return DevState;
    }

    public void setDevState(String devState) {
        DevState = devState;
    }

    public String getDevPower() {
        return DevPower;
    }

    public void setDevPower(String devPower) {
        DevPower = devPower;
    }

    public String getOcrRead() {
        return OcrRead;
    }

    public void setOcrRead(String ocrRead) {
        OcrRead = ocrRead;
    }

    public int getOcrState() {
        return OcrState;
    }

    public void setOcrState(int ocrState) {
        OcrState = ocrState;
    }

    public String getThisRead() {
        return ThisRead;
    }

    public void setThisRead(String thisRead) {
        ThisRead = thisRead;
    }

    public String getOcrResult() {
        return OcrResult;
    }

    public void setOcrResult(String ocrResult) {
        OcrResult = ocrResult;
    }

    public String getOcrTime() {
        return OcrTime;
    }

    public void setOcrTime(String ocrTime) {
        OcrTime = ocrTime;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }


    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

}
