package com.pl.concentrator.bean;

/**
 * ����������
 * ���񳭱�
 */
public class CtCopyDataPhoto {
    private int Id;// ��ˮ��
    private String CommunicateNo;// ��Ʊ��
    private int ReadState;// ����״̬��0-δ����1-�ѳ���
    private String ImageName;// ͼƬ·��+����
    private String CollectorNo;// ���������
    private String Operater;// ����Ա
    private String ReadTime;// ��ʱ����ʱ��
    private String DevState;// �ɼ����ź�ǿ��
    private String DevPower;// �ɼ�����ѹ
    private String OcrRead;// ʶ�����
    private int OcrState;// ʶ��״̬��0-δʶ��1-��ʶ��2-���ɣ�
    private String ThisRead;// �˹�ȷ�϶���
    private String OcrResult;// ʶ��Դ�0-��ȷ��1-����
    private String OcrTime;// ʶ��ʱ��
    private String CreateTime;// ��������ʱ��
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
