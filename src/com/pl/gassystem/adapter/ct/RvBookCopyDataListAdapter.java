package com.pl.gassystem.adapter.ct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ct.ColletorMeterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvBookCopyDataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ColletorMeterBean> mList;
    private LayoutInflater mInflater;

    public RvBookCopyDataListAdapter(Context mContext, List<ColletorMeterBean> mList) {
        this.mContext = mContext;
        if (mList != null) {
            this.mList = mList;
        } else {
            this.mList = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(mContext);
    }

    //获取ViewHolder类型
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = null;

        switch (viewType) {
            case 1:
                mView = mInflater.inflate(R.layout.ct_rv_book_copydata_item, parent, false);
                return new MyViewHolder(mView);
            case 2:
                break;
        }
        return new MyViewHolder(mView);
    }

    /**
     * 创建
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder pHolder, final int position) {

        switch (getItemViewType(position)) {
            case 1:
                final MyViewHolder holder = (MyViewHolder) pHolder;

                if (!mList.get(position).isShowMore()) {
                    holder.layoutOther.setVisibility(View.GONE);
                    holder.tvMore.setText("展开详情");
                } else {
                    holder.layoutOther.setVisibility(View.VISIBLE);
                    holder.tvMore.setText("关闭详情");
                }
                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isChoose = mList.get(position).isShowMore();
                        isChoose = !isChoose;
                        if (isChoose) {
                            holder.layoutOther.setVisibility(View.VISIBLE);
                            holder.tvMore.setText("关闭详情");
                        } else {
                            holder.layoutOther.setVisibility(View.GONE);
                            holder.tvMore.setText("展开详情");
                        }
                        mList.get(position).setShowMore(isChoose);

                    }
                });

                //表具类型
                if (mList.get(position).getMeterTypeNo().equals("04")) {
                    holder.tvMeterTypeNo.setText("IC无线");
                } else if (mList.get(position).getMeterTypeNo().equals("10")) {
                    holder.tvMeterTypeNo.setText("摄像表");
                } else {
                    holder.tvMeterTypeNo.setText("纯无线");
                }

                String copyState = mList.get(position).getReadState();
                switch (copyState) {
                    case "0":
                        holder.tvReadState.setText("未抄");
                        break;
                    case "1":
                        holder.tvReadState.setText("已抄");
                        break;
                    default:
                        holder.tvReadState.setText("正在抄表中...");
                        break;
                }

                holder.tvCommunicateNo.setText(mList.get(position).getCommunicateNo());
                holder.tvAddress.setText(mList.get(position).getAddress());
                holder.tvOcrRead.setText(mList.get(position).getOcrRead());
                holder.tvReadTime.setText(mList.get(position).getReadTime());
                holder.tvDevPower.setText(mList.get(position).getDevPower());
                holder.tvDevState.setText(mList.get(position).getDevState());
                holder.tvMeterState.setText(mList.get(position).getMeterState());
                holder.tvRemark.setText(mList.get(position).getRemark());
                break;
        }

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutOther;
        TextView tvMeterTypeNo, tvCommunicateNo, tvAddress, tvOcrRead, tvReadTime,
                tvDevPower, tvDevState, tvMeterState, tvRemark, tvReadState;
        TextView tvMore;

        MyViewHolder(View itemView) {
            super(itemView);
            tvMeterTypeNo = (TextView) itemView.findViewById(R.id.tvMeterTypeNo);
            tvCommunicateNo = (TextView) itemView.findViewById(R.id.tvCommunicateNo);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvOcrRead = (TextView) itemView.findViewById(R.id.tvOcrRead);
            tvReadTime = (TextView) itemView.findViewById(R.id.tvReadTime);
            tvDevPower = (TextView) itemView.findViewById(R.id.tvDevPower);
            tvDevState = (TextView) itemView.findViewById(R.id.tvDevState);
            tvMeterState = (TextView) itemView.findViewById(R.id.tvMeterState);
            tvRemark = (TextView) itemView.findViewById(R.id.tvRemark);
            tvReadState = (TextView) itemView.findViewById(R.id.tvReadState);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);
            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
        }
    }
}
