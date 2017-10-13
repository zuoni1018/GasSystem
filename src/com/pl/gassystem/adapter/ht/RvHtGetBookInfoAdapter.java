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
import com.pl.gassystem.activity.ht.HtGetGroupInfoActivity;
import com.pl.gassystem.bean.ht.HtBookInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtGetBookInfoAdapter extends RecyclerView.Adapter<RvHtGetBookInfoAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtBookInfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtGetBookInfoAdapter(Context mContext, List<HtBookInfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_get_book_info_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(mContext, HtGetGroupInfoActivity.class);
                mIntent.putExtra("BookNo",mList.get(position).getBookNo());
                mIntent.putExtra("AreaNo",mList.get(position).getAreaNo());
                mContext.startActivity(mIntent);

            }
        });
        holder.tvBookNo.setText(mList.get(position).getBookNo());
        holder.tvBookName.setText(mList.get(position).getBookName());
        holder.tvAreaNo.setText(mList.get(position).getAreaNo());
        holder.tvMeterTypeNo.setText(mList.get(position).getMeterType());
        holder.tvRemark.setText(mList.get(position).getRemark());
        holder.StaffNo.setText(mList.get(position).getStaffNo());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView tvBookNo,tvBookName,tvAreaNo,tvMeterTypeNo,tvRemark,StaffNo;

        MyViewHolder(View itemView) {
            super(itemView);
            tvBookNo= (TextView) itemView.findViewById(R.id.tvBookNo);
            tvBookName= (TextView) itemView.findViewById(R.id.tvBookName);
            tvAreaNo= (TextView) itemView.findViewById(R.id.tvAreaNo);
            tvMeterTypeNo= (TextView) itemView.findViewById(R.id.tvMeterTypeNo);
            tvRemark= (TextView) itemView.findViewById(R.id.tvRemark);
            layoutMain= (LinearLayout) itemView.findViewById(R.id.layoutMain);
            StaffNo= (TextView) itemView.findViewById(R.id.StaffNo);
        }
    }
}
