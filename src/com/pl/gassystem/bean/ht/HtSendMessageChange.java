package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * �޸ı�ŵ�����
 */

public class HtSendMessageChange extends HtSendMessage {

    public static final String CHANGE_TYPE_BOOK_NO = "01";//ֻ�޸ı��
    public static final String CHANGE_TYPE_CUMULANT = "02";//ֻ�޸��ۼ���
    public static final String CHANGE_TYPE_ALL = "03";//�޸�ȫ��

    private String newBookNo = "00000000";//���
    private String cumulant = "00000000";//�ۼ���
    private String changeType = "";//�޸�����

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getNewBookNo() {
        return newBookNo;
    }

    public void setNewBookNo(String newBookNo) {
        this.newBookNo = newBookNo;
    }

    public String getCumulant() {
        return cumulant;
    }

    public void setCumulant(String cumulant) {
        this.cumulant = cumulant;
    }
}
