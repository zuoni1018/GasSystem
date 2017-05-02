package com.pl.bean;

import com.pl.entity.GroupInfo;

import java.util.ArrayList;

/**
 * Created by zangyi_shuai_ge on 2017/4/20
 */

public class GroupInfoStatistic {

    private String point;//抄表点
    private int allNum;//总数
    private int noNum;//未抄
    private int copyNum;//已抄

    public GroupInfo getmGroupInfo() {
        return mGroupInfo;
    }

    public void setmGroupInfo(GroupInfo mGroupInfo) {
        this.mGroupInfo = mGroupInfo;
    }

    private GroupInfo mGroupInfo;

    public ArrayList<String> getMeterNos() {
        return meterNos;
    }

    public void setMeterNos(ArrayList<String> meterNos) {
        this.meterNos = meterNos;
    }

    private ArrayList<String> meterNos;
    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getNoNum() {
        return noNum;
    }

    public void setNoNum(int noNum) {
        this.noNum = noNum;
    }

    public int getCopyNum() {
        return copyNum;
    }

    public void setCopyNum(int copyNum) {
        this.copyNum = copyNum;
    }
}
