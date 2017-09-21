package com.zuoni.zuoni_common.dialog.picker.callback;

/**
 * Created by zangyi_shuai_ge on 2017/7/1
 * 双列滚轮监听
 */

public interface OnDoubleDataSelectedListener {
    void onDataSelectedLeft(String itemValue);

    void onDataSelectedRight(String itemValue);
}