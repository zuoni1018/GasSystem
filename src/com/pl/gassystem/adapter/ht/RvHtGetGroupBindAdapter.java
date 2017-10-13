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
import com.pl.gassystem.bean.ht.HtCustomerInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetGroupBindAdapter extends RecyclerView.Adapter<RvHtGetGroupBindAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtCustomerInfoBean> mList;
    private LayoutInflater mInflater;
    private boolean isRadio = false;

    public RvHtGetGroupBindAdapter(Context mContext, List<HtCustomerInfoBean> mList, boolean isRadio) {
        this.mContext = mContext;
        if (mList != null) {
            this.mList = mList;
        } else {
            this.mList = new ArrayList<>();
        }
        this.isRadio = isRadio;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mInflater.inflate(R.layout.ht_rv_get_group_bind_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRadio) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setChoose(false);
                    }
                    mList.get(position).setChoose(true);
                    notifyDataSetChanged();
                } else {
                    mList.get(position).setChoose(!mList.get(position).isChoose());
                    if (mList.get(position).isChoose()) {
                        holder.ivChoose.setImageResource(R.mipmap.choose_01);
                    } else {
                        holder.ivChoose.setImageResource(R.mipmap.choose_02);
                    }
                }

            }
        });

        if (mList.get(position).isChoose()) {
            holder.ivChoose.setImageResource(R.mipmap.choose_01);
        } else {
            holder.ivChoose.setImageResource(R.mipmap.choose_02);
        }

        holder.KPXD.setText(mList.get(position).getKPXD());
        holder.KCQZSJ.setText(mList.get(position).getKCQZSJ());
        holder.DJR.setText(mList.get(position).getDJR());

        holder.KEYCODE.setText(mList.get(position).getKEYCODE());
        holder.MeterFacNo.setText(mList.get(position).getMeterFacNo());
        holder.KPYZ.setText(mList.get(position).getKPYZ());

        holder.ADDR.setText(mList.get(position).getADDR());
        holder.CommunicateNo.setText(mList.get(position).getCommunicateNo());
        holder.KEYVER.setText(mList.get(position).getKEYVER());
        holder.MeterType.setText(mList.get(position).getMeterType());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView KPXD, KCQZSJ, DJR,KEYCODE,MeterFacNo,KPYZ,ADDR,CommunicateNo,KEYVER,MeterType;
        ImageView ivChoose;


        MyViewHolder(View itemView) {
            super(itemView);
            KPXD = (TextView) itemView.findViewById(R.id.KPXD);
            KCQZSJ = (TextView) itemView.findViewById(R.id.KCQZSJ);
            DJR = (TextView) itemView.findViewById(R.id.DJR);

            KEYCODE = (TextView) itemView.findViewById(R.id.KEYCODE);
            KPYZ = (TextView) itemView.findViewById(R.id.KPYZ);
            MeterFacNo = (TextView) itemView.findViewById(R.id.MeterFacNo);

            ADDR = (TextView) itemView.findViewById(R.id.ADDR);
            CommunicateNo = (TextView) itemView.findViewById(R.id.CommunicateNo);
            KEYVER = (TextView) itemView.findViewById(R.id.KEYVER);
            MeterType= (TextView) itemView.findViewById(R.id.MeterType);

            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);
            ivChoose = (ImageView) itemView.findViewById(R.id.ivChoose);
        }
    }
}
