package com.pl.bean;


/**
 * Created by zangyi_shuai_ge on 2017/8/24
 * 西宁用户信息表
 * 通过表具编号去查询用户个人信息
 */

public class XiNingUserInfo {

    //通用
    private String tableNumber = "";//表具编号
    private String userName = "";//用户名
    private String address = "";//地址
    private String userNum = "";//用户编号
    private String userPhone = "";//用户手机号码
    private String tableName = "";//表具名称
    //西宁
    private String xiNingTableNumber = "";//西宁表具编号（西宁的表具钢号为通用版的表具编号）
    private String xiNingUnitPrice = "";//西宁当前的单价

}
