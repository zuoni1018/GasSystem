package com.pl.concentrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pl.concentrator.bean.model.Concentrator;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvMoveBookAdapter extends RecyclerView.Adapter<RvMoveBookAdapter.MyViewHolder> {

    private Context mContext;
    private List<Concentrator> mList;
    private LayoutInflater mInflater;

    public RvMoveBookAdapter(Context mContext, List<Concentrator> mList) {
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
        View mView = mInflater.inflate(R.layout.ct_rv_move_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (mList.get(position).isChoose()) {
            holder.ivChoose.setImageResource(R.mipmap.choose_01);
        } else {
            holder.ivChoose.setImageResource(R.mipmap.choose_02);
        }
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nowChoose = mList.get(position).isChoose();
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setChoose(false);
                    if (position == i) {
                        mList.get(i).setChoose(!nowChoose);
                    }
                }
                RvMoveBookAdapter.this.notifyDataSetChanged();
            }
        });

        holder.tvCollectorNo.setText(mList.get(position).getCollectorNo());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutMain;
        TextView tvCollectorNo;
        ImageView ivChoose;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.layoutMain);
            ivChoose = (ImageView) itemView.findViewById(R.id.ivChoose);
            tvCollectorNo = (TextView) itemView.findViewById(R.id.tvCollectorNo);
        }
    }
}
