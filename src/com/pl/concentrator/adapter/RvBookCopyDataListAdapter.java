package com.pl.concentrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.concentrator.activity.CtCopyDataBookDetailActivity;
import com.pl.concentrator.bean.model.CtCopyData;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvBookCopyDataListAdapter extends RecyclerView.Adapter<RvBookCopyDataListAdapter.MyViewHolder> {

    private Context mContext;
    private List<CtCopyData> mList;
    private LayoutInflater mInflater;

    public RvBookCopyDataListAdapter(Context mContext, List<CtCopyData> mList) {
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
        View mView = mInflater.inflate(R.layout.ct_rv_book_copydata_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CtCopyDataBookDetailActivity.class);
                mIntent.putExtra("info", mList.get(position));
                mContext.startActivity(mIntent);
            }
        });
        holder.tvCopyDataMeterNo.setText(mList.get(position).getCommunicateNo());
        holder.tvCopyDataMeterName.setText(mList.get(position).getMeterName());
        holder.tvCopyDataCurrentShow.setText(mList.get(position).getCurrentShow());
        holder.tvCopyDataCurrentDosage.setText(mList.get(position).getCurrentDosage());
        int copyState = mList.get(position).getCopyState();
        if (copyState == 0) {
            holder.tvCopyDataCopyState.setText("Î´³­");
        } else {
            holder.tvCopyDataCopyState.setText("ÒÑ³­");
        }
        holder.tvCopyDataCopyTime.setText(mList.get(position).getCopyTime());

        if (mList.get(position).isChoose()) {
            holder.checkBox.setImageResource(R.mipmap.choose_01);
        } else {
            holder.checkBox.setImageResource(R.mipmap.choose_02);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChoose = mList.get(position).isChoose();
                isChoose = !isChoose;
                if (isChoose) {
                    holder.checkBox.setImageResource(R.mipmap.choose_01);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.choose_02);
                }
                mList.get(position).setChoose(isChoose);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        TextView tvCopyDataMeterNo, tvCopyDataMeterName, tvCopyDataCurrentShow, tvCopyDataCurrentDosage, tvCopyDataCopyState, tvCopyDataCopyTime;

        ImageView checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutMain);
            tvCopyDataMeterNo = (TextView) itemView.findViewById(R.id.tvCopyDataMeterNo);
            tvCopyDataMeterName = (TextView) itemView.findViewById(R.id.tvCopyDataMeterName);
            tvCopyDataCurrentShow = (TextView) itemView.findViewById(R.id.tvCopyDataCurrentShow);
            tvCopyDataCurrentDosage = (TextView) itemView.findViewById(R.id.tvCopyDataCurrentDosage);
            tvCopyDataCopyState = (TextView) itemView.findViewById(R.id.tvCopyDataCopyState);
            tvCopyDataCopyTime = (TextView) itemView.findViewById(R.id.tvCopyDataCopyTime);
            checkBox = (ImageView) itemView.findViewById(R.id.checkBox);
        }
    }
}
