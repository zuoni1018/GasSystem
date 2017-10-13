package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtCopyResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtResultCopyAdapter extends RecyclerView.Adapter<RvHtResultCopyAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtCopyResult> mList;
    private LayoutInflater mInflater;

    public RvHtResultCopyAdapter(Context mContext, List<HtCopyResult> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_copy_result_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.CommunicateNo.setText(mList.get(position).getCommunicateNo());
        holder.ThisRead.setText(mList.get(position).getThisRead());
        holder.DevPower.setText(mList.get(position).getDevPower());
        holder.JSQD.setText(mList.get(position).getJSQD());
        holder.DJR.setText(mList.get(position).getDJR());
        holder.MeterFacNo.setText(mList.get(position).getMeterFacNo());
        holder.ReadType.setText(mList.get(position).getReadType());
        holder.State.setText(mList.get(position).getState());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

//        LinearLayout layoutMain;
        TextView CommunicateNo,ThisRead,DevPower,JSQD,DJR,MeterFacNo,ReadType,State;

        MyViewHolder(View itemView) {
            super(itemView);
            CommunicateNo= (TextView) itemView.findViewById(R.id.CommunicateNo);
            ThisRead= (TextView) itemView.findViewById(R.id.ThisRead);
            DevPower= (TextView) itemView.findViewById(R.id.DevPower);
            JSQD= (TextView) itemView.findViewById(R.id.JSQD);
            DJR= (TextView) itemView.findViewById(R.id.DJR);
            MeterFacNo= (TextView) itemView.findViewById(R.id.MeterFacNo);
            ReadType= (TextView) itemView.findViewById(R.id.ReadType);
            State= (TextView) itemView.findViewById(R.id.State);
        }
    }
}
