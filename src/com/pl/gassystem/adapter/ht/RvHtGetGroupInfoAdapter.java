package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.ht.HtGroupActivity;
import com.pl.gassystem.bean.ht.HtGroupInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetGroupInfoAdapter extends RecyclerView.Adapter<RvHtGetGroupInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtGroupInfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetGroupInfoAdapter(Context mContext, List<HtGroupInfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_get_group_info_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, HtGroupActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("HtGroupInfoBean", mList.get(position));
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });
        holder.tvBookNo.setText(mList.get(position).getBookNo());
        holder.tvAreaNo.setText(mList.get(position).getAreaNo());
        holder.tvMeterTypeNo.setText(mList.get(position).getMeterType());
        holder.tvRemark.setText(mList.get(position).getRemark());

        holder.tvKPXD.setText(mList.get(position).getKPXD());
        holder.tvKPYZ.setText(mList.get(position).getKPYZ());
        holder.tvDJR.setText(mList.get(position).getDJR());

        holder.tvKCQZSJ.setText(mList.get(position).getKCQZSJ());
        holder.tvKEYVER.setText(mList.get(position).getKEYVER());
        holder.tvKEY.setText(mList.get(position).getKEYCODE());

        if (!mList.get(position).isChoose()) {
            holder.layoutOther.setVisibility(View.GONE);
            holder.tvMore.setText("展开详情");
        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.tvMore.setText("关闭详情");
        }

        holder.tvGroupName.setText(mList.get(position).getGroupName());
        holder.tvGroupNo.setText(mList.get(position).getGroupNo());

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mList.get(position).setChoose(!mList.get(position).isChoose());

                if (!mList.get(position).isChoose()) {
                    holder.layoutOther.setVisibility(View.GONE);
                    holder.tvMore.setText("展开详情");
                } else {
                    holder.layoutOther.setVisibility(View.VISIBLE);
                    holder.tvMore.setText("关闭详情");
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain, layoutOther;
        TextView tvMore, tvBookNo, tvAreaNo, tvMeterTypeNo,
                tvGroupName, tvGroupNo,

        tvRemark, tvKPXD, tvKPYZ, tvDJR, tvKCQZSJ, tvKEYVER, tvKEY;


        MyViewHolder(View itemView) {
            super(itemView);
            tvBookNo = (TextView) itemView.findViewById(R.id.tvBookNo);
            tvAreaNo = (TextView) itemView.findViewById(R.id.tvAreaNo);
//            tvMeterTypeNo= (TextView) itemView.findViewById(R.id.tvMeterTypeNo);
            tvRemark = (TextView) itemView.findViewById(R.id.tvRemark);
            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);
            tvKPXD = (TextView) itemView.findViewById(R.id.tvKPXD);
            tvKPYZ = (TextView) itemView.findViewById(R.id.tvKPYZ);
            tvDJR = (TextView) itemView.findViewById(R.id.tvDJR);
            tvKCQZSJ = (TextView) itemView.findViewById(R.id.tvKCQZSJ);
            tvKEYVER = (TextView) itemView.findViewById(R.id.tvKEYVER);
            tvKEY = (TextView) itemView.findViewById(R.id.tvKEY);
            tvGroupName = (TextView) itemView.findViewById(R.id.tvGroupName);
            tvGroupNo = (TextView) itemView.findViewById(R.id.tvGroupNo);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);

            tvMeterTypeNo = (TextView) itemView.findViewById(R.id.tvMeterTypeNo);
        }
    }
}
