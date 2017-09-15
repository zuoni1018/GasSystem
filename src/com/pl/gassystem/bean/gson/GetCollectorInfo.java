package com.pl.gassystem.bean.gson;

import com.pl.gassystem.bean.ct.Concentrator;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/4
 * 集中器列表
 */

public class GetCollectorInfo {

    private List<Concentrator> CollectorInfo;

    public List<Concentrator> getCollectorInfo() {
        return CollectorInfo;
    }
    public void setCollectorInfo(List<Concentrator> CollectorInfo) {
        this.CollectorInfo = CollectorInfo;
    }
}
