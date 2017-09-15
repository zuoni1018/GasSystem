package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ct.Concentrator;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/6
 */

public class GetCollectorInfoByCollectorNo {
    private List<Concentrator> CollectorInfoByCollectorNo;
    public List<Concentrator> getCollectorInfoByCollectorNo() {
        return CollectorInfoByCollectorNo;
    }
    public void setCollectorInfoByCollectorNo(List<Concentrator> CollectorInfoByCollectorNo) {
        this.CollectorInfoByCollectorNo = CollectorInfoByCollectorNo;
    }
}
