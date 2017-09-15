package com.pl.gassystem.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pl.gassystem.R;

import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/5/4
 */

public class DateListViewAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public DateListViewAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.date_item, null);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDate.setText("" + mList.get(position));
        return convertView;
    }

    private class ViewHolder {

        private TextView tvDate;
    }
}

