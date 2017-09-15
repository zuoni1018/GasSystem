package com.pl.gassystem.bean.ct;

/**
 * Created by zangyi_shuai_ge on 2017/9/4
 */

public class Concentrator {

    /**
     * Id : 1
     * CollectorNo : 0571150001
     * LoginTime : 2015/7/8 11:31:13
     * LogoutTime : 2016/5/6 15:26:18
     * LoginState : 0
     * LoginStateMsg : ¿Îœﬂ
     * ReadNum : 88
     * NotReadNum :
     * AllNum : 88
     */

    private String Id;
    private String CollectorNo;
    private String LoginTime;
    private String LogoutTime;
    private String LoginState;
    private String LoginStateMsg;
    private String ReadNum;
    private String NotReadNum;
    private String AllNum;
    private String CollectorName;

    private int trueAllNum;
    private int trueReadNum;
    private int trueNotReadNum;

    private boolean isChoose;

    private int getTrueNum(String sNum) {
        if (sNum != null) {
            if (sNum.trim().equals("")) {
                return 0;
            } else {
                try {
                    return Integer.parseInt(sNum.trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    public int getTrueAllNum() {


        return getTrueNum(AllNum);
    }

    public int getTrueReadNum() {
        return getTrueNum(ReadNum);
    }

    public int getTrueNotReadNum() {
        return getTrueNum(NotReadNum);
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCollectorNo() {
        return CollectorNo;
    }

    public void setCollectorNo(String CollectorNo) {
        this.CollectorNo = CollectorNo;
    }

    public String getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(String LoginTime) {
        this.LoginTime = LoginTime;
    }

    public String getLogoutTime() {
        return LogoutTime;
    }

    public void setLogoutTime(String LogoutTime) {
        this.LogoutTime = LogoutTime;
    }

    public String getLoginState() {
        return LoginState;
    }

    public void setLoginState(String LoginState) {
        this.LoginState = LoginState;
    }

    public String getLoginStateMsg() {
        return LoginStateMsg;
    }

    public void setLoginStateMsg(String LoginStateMsg) {
        this.LoginStateMsg = LoginStateMsg;
    }

    public String getReadNum() {
        return ReadNum;
    }

    public void setReadNum(String ReadNum) {
        this.ReadNum = ReadNum;
    }

    public String getNotReadNum() {
        return NotReadNum;
    }

    public void setNotReadNum(String NotReadNum) {
        this.NotReadNum = NotReadNum;
    }

    public String getAllNum() {
        return AllNum;
    }

    public void setAllNum(String AllNum) {
        this.AllNum = AllNum;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getCollectorName() {
        return CollectorName;
    }

    public void setCollectorName(String collectorName) {
        CollectorName = collectorName;
    }
}
