package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.ht.HtAreaActivity;
import com.pl.gassystem.bean.ht.HtAreaInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetAreaInfoAdapter extends RecyclerView.Adapter<RvHtGetAreaInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtAreaInfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetAreaInfoAdapter(Context mContext, List<HtAreaInfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_get_area_info_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(mContext, HtAreaActivity.class);
                mIntent.putExtra("AreaNo",mList.get(position).getAreaNo());
                mContext.startActivity(mIntent);

            }
        });
        holder.tvAreaName.setText(mList.get(position).getAreaName());
        holder.tvPayType.setText(mList.get(position).getPayType());
        holder.tvAreaNo.setText(mList.get(position).getAreaNo());
        holder.tvStartReadDate.setText(mList.get(position).getStartReadDate());
        holder.tvEndReadDate.setText(mList.get(position).getEndReadDate());
        holder.tvRemark.setText(mList.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView tvAreaNo,tvAreaName,tvPayType,tvStartReadDate,tvEndReadDate,tvRemark;

        MyViewHolder(View itemView) {
            super(itemView);
            tvAreaName= (TextView) itemView.findViewById(R.id.tvAreaName);
            tvPayType= (TextView) itemView.findViewById(R.id.tvPayType);
            tvAreaNo= (TextView) itemView.findViewById(R.id.tvAreaNo);
            tvStartReadDate= (TextView) itemView.findViewById(R.id.tvStartReadDate);
            tvRemark= (TextView) itemView.findViewById(R.id.tvRemark);
            tvEndReadDate= (TextView) itemView.findViewById(R.id.tvEndReadDate);
            layoutMain= (LinearLayout) itemView.findViewById(R.id.layoutMain);
        }
    }
}
