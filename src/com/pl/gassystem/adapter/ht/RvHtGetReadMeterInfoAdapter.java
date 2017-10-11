package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtTimereadMeterInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetReadMeterInfoAdapter extends RecyclerView.Adapter<RvHtGetReadMeterInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtTimereadMeterInfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetReadMeterInfoAdapter(Context mContext, List<HtTimereadMeterInfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_get_read_meter_info_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.get(position).setCheck(!mList.get(position).isCheck());
                if (mList.get(position).isCheck()) {
                    holder.ivCheck.setImageResource(R.mipmap.choose_01);
                } else {
                    holder.ivCheck.setImageResource(R.mipmap.choose_02);
                }
            }
        });

        holder.tvCommunicateNo.setText(mList.get(position).getCommunicateNo());
        holder.tvReadState.setText(mList.get(position).getReadState());
        holder.tvImageName.setText(mList.get(position).getImageName());
        holder.tvCollectorNo.setText(mList.get(position).getCollectorNo());
        holder.tvOperater.setText(mList.get(position).getOperater());
        holder.tvReadTime.setText(mList.get(position).getReadTime());
        holder.tvDevState.setText(mList.get(position).getDevState());
        holder.tvDevPower.setText(mList.get(position).getDevPower());


        holder.tvOcrRead.setText(mList.get(position).getOcrRead());
        holder.tvOcrState.setText(mList.get(position).getOcrState());
        holder.tvThisRead.setText(mList.get(position).getThisRead());
        holder.tvOcrResult.setText(mList.get(position).getOcrResult());
        holder.tvOcrTime.setText(mList.get(position).getOcrTime());
        holder.tvcreateTime.setText(mList.get(position).getCreateTime());
        holder.tvReadType.setText(mList.get(position).getReadType());
        holder.tvKPXD.setText(mList.get(position).getKPXD());

        holder.tvKPYZ.setText(mList.get(position).getKPYZ());
        holder.tvFSQD.setText(mList.get(position).getFSQD());
        holder.tvJSQD.setText(mList.get(position).getJSQD());
        holder.tvDJRQ.setText(mList.get(position).getDJRQ());
        holder.tvPCH.setText(mList.get(position).getPCH());


        if (mList.get(position).isCheck()) {
            holder.ivCheck.setImageResource(R.mipmap.choose_01);
        } else {
            holder.ivCheck.setImageResource(R.mipmap.choose_02);
        }


        if (!mList.get(position).isChoose()) {
            holder.layoutOther.setVisibility(View.GONE);
            holder.tvMore.setText("展开详情");
        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.tvMore.setText("关闭详情");
        }

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
        TextView tvMore, tvCommunicateNo, tvReadState, tvImageName, tvCollectorNo, tvOperater, tvReadTime, tvDevState, tvDevPower,
                tvOcrRead, tvOcrState, tvThisRead, tvOcrResult, tvOcrTime, tvcreateTime, tvReadType, tvKPXD,
                tvKPYZ, tvFSQD, tvJSQD, tvDJRQ, tvPCH;

        ImageView ivCheck;


        MyViewHolder(View itemView) {
            super(itemView);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);

            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);

            tvCommunicateNo = (TextView) itemView.findViewById(R.id.tvCommunicateNo);
            tvReadState = (TextView) itemView.findViewById(R.id.tvReadState);
            tvImageName = (TextView) itemView.findViewById(R.id.tvImageName);
            tvCollectorNo = (TextView) itemView.findViewById(R.id.tvCollectorNo);
            tvOperater = (TextView) itemView.findViewById(R.id.tvOperater);
            tvDevState = (TextView) itemView.findViewById(R.id.tvDevState);
            tvReadTime = (TextView) itemView.findViewById(R.id.tvReadTime);
            tvDevPower = (TextView) itemView.findViewById(R.id.tvDevPower);
            tvOcrRead = (TextView) itemView.findViewById(R.id.tvOcrRead);
            tvOcrState = (TextView) itemView.findViewById(R.id.tvOcrState);

            tvThisRead = (TextView) itemView.findViewById(R.id.tvThisRead);
            tvOcrResult = (TextView) itemView.findViewById(R.id.tvOcrResult);
            tvOcrTime = (TextView) itemView.findViewById(R.id.tvOcrTime);
            tvcreateTime = (TextView) itemView.findViewById(R.id.tvcreateTime);
            tvReadType = (TextView) itemView.findViewById(R.id.tvReadType);
            tvKPYZ = (TextView) itemView.findViewById(R.id.tvKPYZ);
            tvFSQD = (TextView) itemView.findViewById(R.id.tvFSQD);
            tvJSQD = (TextView) itemView.findViewById(R.id.tvJSQD);
            tvDJRQ = (TextView) itemView.findViewById(R.id.tvDJRQ);

            tvKPXD = (TextView) itemView.findViewById(R.id.tvKPXD);
            tvPCH = (TextView) itemView.findViewById(R.id.tvPCH);

            ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
        }
    }
}
