package com.pl.gassystem.bean.ht;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 修改表号的命令
 */

public class HtSendMessageChange extends HtSendMessage {

    public static final String CHANGE_TYPE_BOOK_NO = "01";//只修改表号
    public static final String CHANGE_TYPE_CUMULANT = "02";//只修改累计量
    public static final String CHANGE_TYPE_ALL = "03";//修改全部

    private String newBookNo = "00000000";//表号
    private String cumulant = "00000000";//累计量
    private String changeType = "";//修改类型

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
