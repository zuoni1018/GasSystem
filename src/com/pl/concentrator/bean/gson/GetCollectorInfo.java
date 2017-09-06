package com.pl.concentrator.bean.gson;

import com.pl.concentrator.bean.model.Concentrator;

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
