package com.pl.bean;



/**
 * Created by zangyi_shuai_ge on 2017/8/24
 */

public class UserInfo {
    //通过表具编号去查询用户信息
    //一条表具编号只对应一条用户信息
    //每次去存的时候没有则插入 有则更新

    //通用
    private String tableNumber="";//表具编号
    private String userName="";//用户名
    private String address="";//地址
    private String userNum="";//用户编号
    private String userPhone="";//用户手机号码
    private String tableName="";//表具名称
    //西宁
    private String xiNingTableNumber="";//西宁表具编号（西宁的表具钢号为通用版的表具编号）
    private String xiNingUnitPrice="";//西宁当前的单价
    public String getXiNingUnitPrice() {
        return xiNingUnitPrice;
    }

    public void setXiNingUnitPrice(String xiNingUnitPrice) {
        this.xiNingUnitPrice = xiNingUnitPrice;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getXiNingTableNumber() {
        return xiNingTableNumber;
    }

    public void setXiNingTableNumber(String xiNingTableNumber) {
        this.xiNingTableNumber = xiNingTableNumber;
    }
}
