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
import com.pl.gassystem.bean.gson.HtGetReadMeterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetReadMeterInfoAdapter extends RecyclerView.Adapter<RvHtGetReadMeterInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtGetReadMeterInfo.ArrayOfModCustomerinfoBean.ModCustomerinfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetReadMeterInfoAdapter(Context mContext, List<HtGetReadMeterInfo.ArrayOfModCustomerinfoBean.ModCustomerinfoBean> mList) {
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


        holder.AdrCode.setText(mList.get(position).getAdrCode());
        holder.CommunicateNo.setText(mList.get(position).getCommunicateNo());
        holder.MeterFacNo.setText(mList.get(position).getMeterFacNo());
        holder.HUNAME.setText(mList.get(position).getHUNAME());
        holder.ADDR.setText(mList.get(position).getADDR());

        holder.MQBBH.setText(mList.get(position).getMQBBH());
        holder.OTEL.setText(mList.get(position).getOTEL());
        holder.HTEL.setText(mList.get(position).getHTEL());
        holder.HUCODE.setText(mList.get(position).getHUCODE());
        holder.XBDS.setText(mList.get(position).getXBDS());

        holder.YICODE.setText(mList.get(position).getYICODE());
        holder.KPXD.setText(mList.get(position).getKPXD());
        holder.KPYZ.setText(mList.get(position).getKPYZ());
        holder.DJR.setText(mList.get(position).getDJR());
        holder.KCQZSJ.setText(mList.get(position).getKCQZSJ());

        holder.KEYVER.setText(mList.get(position).getKEYVER());
        holder.KEYCODE.setText(mList.get(position).getKEYCODE());
        holder.AreaNo.setText(mList.get(position).getAreaNo());


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

        holder.MeterType.setText(mList.get(position).getMeterType());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain, layoutOther;
        TextView tvMore;

        TextView AdrCode, CommunicateNo, MeterFacNo, HUNAME, ADDR,
                MQBBH, OTEL, HTEL, HUCODE, XBDS,
                YICODE, KPXD, KPYZ, DJR, KCQZSJ,
                KEYVER, KEYCODE, AreaNo,MeterType;

        ImageView ivCheck;


        MyViewHolder(View itemView) {
            super(itemView);
            tvMore = (TextView) itemView.findViewById(R.id.tvMore);

            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);
            ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);

            AdrCode = (TextView) itemView.findViewById(R.id.AdrCode);
            CommunicateNo = (TextView) itemView.findViewById(R.id.CommunicateNo);
            MeterFacNo = (TextView) itemView.findViewById(R.id.MeterFacNo);
            HUNAME = (TextView) itemView.findViewById(R.id.HUNAME);
            ADDR = (TextView) itemView.findViewById(R.id.ADDR);

            MQBBH = (TextView) itemView.findViewById(R.id.MQBBH);
            OTEL = (TextView) itemView.findViewById(R.id.OTEL);
            HTEL = (TextView) itemView.findViewById(R.id.HTEL);
            HUCODE = (TextView) itemView.findViewById(R.id.HUCODE);
            XBDS = (TextView) itemView.findViewById(R.id.XBDS);

            YICODE = (TextView) itemView.findViewById(R.id.YICODE);
            KPXD = (TextView) itemView.findViewById(R.id.KPXD);
            KPYZ = (TextView) itemView.findViewById(R.id.KPYZ);
            DJR = (TextView) itemView.findViewById(R.id.DJR);
            KCQZSJ = (TextView) itemView.findViewById(R.id.KCQZSJ);

            KEYVER = (TextView) itemView.findViewById(R.id.KEYVER);
            KEYCODE = (TextView) itemView.findViewById(R.id.KEYCODE);
            AreaNo = (TextView) itemView.findViewById(R.id.AreaNo);

            MeterType= (TextView) itemView.findViewById(R.id.MeterType);
        }
    }
}
