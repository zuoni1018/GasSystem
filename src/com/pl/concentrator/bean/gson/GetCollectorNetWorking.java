package com.pl.concentrator.bean.gson;

import com.pl.concentrator.bean.model.BookInfo;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/5
 */

public class GetCollectorNetWorking {

    /**
     * CollectorNetWorking : [{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"},{"CommunicateNo":"1505040233","address":"默认小区150幢","ReadState":"1","DevState":"0"}]
     * PageNo : 1
     * TotleNum : 88
     */

    private int PageNo;
    private int TotleNum;
    private List<BookInfo> CollectorNetWorking;

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int PageNo) {
        this.PageNo = PageNo;
    }

    public int getTotleNum() {
        return TotleNum;
    }

    public void setTotleNum(int TotleNum) {
        this.TotleNum = TotleNum;
    }

    public List<BookInfo> getCollectorNetWorking() {
        return CollectorNetWorking;
    }

    public void setCollectorNetWorking(List<BookInfo> CollectorNetWorking) {
        this.CollectorNetWorking = CollectorNetWorking;
    }

}
