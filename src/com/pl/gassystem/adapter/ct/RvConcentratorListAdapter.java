package com.pl.gassystem.adapter.ct;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pl.gassystem.bean.ct.Concentrator;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.ct.CtCopySituationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 集中器列表
 */

public class RvConcentratorListAdapter extends RecyclerView.Adapter<RvConcentratorListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Concentrator> mList;
    private LayoutInflater mInflater;

    public RvConcentratorListAdapter(Context mContext, List<Concentrator> mList) {
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
        View mView = mInflater.inflate(R.layout.ct_rv_concentrator_list_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CtCopySituationActivity.class);
                mIntent.putExtra("CollectorNo", mList.get(position).getCollectorNo());//集中器编号
                mContext.startActivity(mIntent);
            }
        });

        holder.tvCollectorNo.setText(mList.get(position).getCollectorNo());
        holder.tvAllNum.setText(mList.get(position).getTrueAllNum() + "");
        holder.tvNoReadNum.setText(mList.get(position).getTrueNotReadNum() + "");
        holder.tvReadNum.setText(mList.get(position).getTrueReadNum() + "");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutMain;
        TextView tvCollectorNo, tvNoReadNum, tvReadNum, tvAllNum;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.layoutMain);

            tvCollectorNo = (TextView) itemView.findViewById(R.id.tvCollectorNo);

            tvNoReadNum = (TextView) itemView.findViewById(R.id.tvNoReadNum);
            tvReadNum = (TextView) itemView.findViewById(R.id.tvReadNum);
            tvAllNum = (TextView) itemView.findViewById(R.id.tvAllNum);
        }
    }
}
