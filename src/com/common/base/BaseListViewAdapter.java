package com.common.base;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * ListView ������Adapter�Ļ���
 */

abstract class BaseListViewAdapter<T> extends BaseAdapter {

    private List<T> mList;//����Դ

    public BaseListViewAdapter(List<T> pList) {
        mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
