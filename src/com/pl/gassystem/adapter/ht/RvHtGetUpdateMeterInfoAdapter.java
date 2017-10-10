package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtUpdateMeterInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetUpdateMeterInfoAdapter extends RecyclerView.Adapter<RvHtGetUpdateMeterInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtUpdateMeterInfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetUpdateMeterInfoAdapter(Context mContext, List<HtUpdateMeterInfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_get_update_meter_info_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent mIntent = new Intent(mContext, HtGetGroupBindActivity.class);
//                mIntent.putExtra("GroupNo", mList.get(position).getGroupNo());
//                mIntent.putExtra("XinDao", mList.get(position).getKPXD());
//                mIntent.putExtra("YinZi", mList.get(position).getKPYZ());

//                mContext.startActivity(mIntent);
            }
        });


        holder.tvKPXD.setText(mList.get(position).getKPXD1());
        holder.tvKPYZ.setText(mList.get(position).getKPYZ1());
        holder.tvDJR.setText(mList.get(position).getDJR1());
//
        holder.tvKCQZSJ.setText(mList.get(position).getKCQZSJ1());
        holder.tvKEYVER.setText(mList.get(position).getKEYVER1());
        holder.tvKEY.setText(mList.get(position).getKEYCODE1());


        holder.tvKPXD2.setText(mList.get(position).getKPXD2());
        holder.tvKPYZ2.setText(mList.get(position).getKPYZ2());
        holder.tvDJR2.setText(mList.get(position).getDJR2());
//
        holder.tvKCQZSJ2.setText(mList.get(position).getKCQZSJ2());
        holder.tvKEYVER2.setText(mList.get(position).getKEYVER2());
        holder.tvKEY2.setText(mList.get(position).getKEYCODE2());

        if (!mList.get(position).isChoose()) {
            holder.layoutOther.setVisibility(View.GONE);
            holder.tvMore.setText("�鿴�²���");
        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.tvMore.setText("�ر��²���");
        }

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mList.get(position).setChoose(!mList.get(position).isChoose());

                if (!mList.get(position).isChoose()) {
                    holder.layoutOther.setVisibility(View.GONE);
                    holder.tvMore.setText("�鿴�²���");
                } else {
                    holder.layoutOther.setVisibility(View.VISIBLE);
                    holder.tvMore.setText("�ر��²���");
                }

            }
        });

        holder.tvMeterNo.setText(mList.get(position).getMeterNo());
        holder.tvMeterTypeNo.setText(mList.get(position).getMeterType());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain, layoutOther;
        TextView tvMore, tvMeterNo, tvMeterTypeNo,


        tvKPXD, tvKPYZ, tvDJR, tvKCQZSJ, tvKEYVER, tvKEY,

        tvKPXD2, tvKPYZ2, tvDJR2, tvKCQZSJ2, tvKEYVER2, tvKEY2;
        MyViewHolder(View itemView) {
            super(itemView);
            tvMeterNo = (TextView) itemView.findViewById(R.id.tvMeterNo);
            tvMeterTypeNo= (TextView) itemView.findViewById(R.id.tvMeterTypeNo);

            layoutOther = (LinearLayout) itemView.findViewById(R.id.layoutOther);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);

            tvKPXD = (TextView) itemView.findViewById(R.id.tvKPXD1);
            tvKPYZ = (TextView) itemView.findViewById(R.id.tvKPYZ1);
            tvDJR = (TextView) itemView.findViewById(R.id.tvDJR1);
            tvKCQZSJ = (TextView) itemView.findViewById(R.id.tvKCQZSJ1);
            tvKEYVER = (TextView) itemView.findViewById(R.id.tvKEYVER1);
            tvKEY = (TextView) itemView.findViewById(R.id.tvKEY1);


            tvKPXD2 = (TextView) itemView.findViewById(R.id.tvKPXD2);
            tvKPYZ2 = (TextView) itemView.findViewById(R.id.tvKPYZ2);
            tvDJR2 = (TextView) itemView.findViewById(R.id.tvDJR2);
            tvKCQZSJ2 = (TextView) itemView.findViewById(R.id.tvKCQZSJ2);
            tvKEYVER2 = (TextView) itemView.findViewById(R.id.tvKEYVER2);
            tvKEY2 = (TextView) itemView.findViewById(R.id.tvKEY2);


            tvMore = (TextView) itemView.findViewById(R.id.tvMore);


        }
    }
}
