package com.pl.gassystem.adapter.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.dao.LogMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 杭天抄表 表选择
 */

public class RvLogsAdapter extends RecyclerView.Adapter<RvLogsAdapter.MyViewHolder> {

    private Context mContext;
    private List<LogMessage> mList;
    private LayoutInflater mInflater;

    public RvLogsAdapter(Context mContext, List<LogMessage> mList) {
        this.mContext = mContext;
        if (mList != null) {
            this.mList = mList;
        } else {
            this.mList = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mInflater.inflate(R.layout.ht_rv_log, parent, false);
        return new MyViewHolder(mView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv01.setText(position+"");
        holder.tv02.setText(getDateToString(mList.get(position).getTime(),"yyyy-MM-dd HH:mm:ss:SSS"));

        if(mList.get(position).getType().equals("0")){
            holder.tv03.setText("发送");
            holder.tv04.setTextColor(mContext.getResources().getColor(R.color.color_red));
        }else {
            holder.tv03.setText("接收");
            holder.tv04.setTextColor(mContext.getResources().getColor(R.color.color_blue));
        }
        holder.tv04.setText(mList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv01,tv02,tv03,tv04;

        MyViewHolder(View itemView) {
            super(itemView);
            tv01= (TextView) itemView.findViewById(R.id.tv01);
            tv02= (TextView) itemView.findViewById(R.id.tv02);
            tv03= (TextView) itemView.findViewById(R.id.tv03);
            tv04= (TextView) itemView.findViewById(R.id.tv04);
        }
    }

    public static String getDateToString(String  time, String pattern) {
        long milSecond=Long.valueOf(time);
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
