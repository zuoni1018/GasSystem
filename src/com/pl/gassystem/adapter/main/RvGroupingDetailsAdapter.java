package com.pl.gassystem.adapter.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bean.GroupInfoStatistic;
import com.pl.gassystem.R;
import com.pl.gassystem.callback.RvItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 杭天抄表 表选择
 */

public class RvGroupingDetailsAdapter extends RecyclerView.Adapter<RvGroupingDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private List<GroupInfoStatistic> mList;
    private LayoutInflater mInflater;
    private RvItemOnClickListener rvItemOnClickListener;

    public void setRvItemOnClickListener(RvItemOnClickListener rvItemOnClickListener) {
        this.rvItemOnClickListener = rvItemOnClickListener;
    }

    public RvGroupingDetailsAdapter(Context mContext, List<GroupInfoStatistic> mList) {
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
        View mView = mInflater.inflate(R.layout.rv_grouping_details_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvItemOnClickListener != null) {
                    rvItemOnClickListener.onClick(position);
                }
            }
        });

        holder.tvPoint.setText(mList.get(position).getPoint());
        holder.tvAllCopyNum.setText(mList.get(position).getAllNum() + "");
        holder.tvNoCopyNum.setText(mList.get(position).getNoNum() + "");
        holder.tvCopyNum.setText(mList.get(position).getCopyNum()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        TextView tvAllCopyNum, tvNoCopyNum, tvCopyNum, tvPoint;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);

            tvAllCopyNum = (TextView) itemView.findViewById(R.id.tvAllCopyNum);
            tvNoCopyNum = (TextView) itemView.findViewById(R.id.tvNoCopyNum);
            tvCopyNum = (TextView) itemView.findViewById(R.id.tvCopyNum);
            tvPoint = (TextView) itemView.findViewById(R.id.tvPoint);
        }
    }
}
