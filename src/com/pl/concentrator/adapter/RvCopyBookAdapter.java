package com.pl.concentrator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pl.concentrator.bean.model.CtBookInfo;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvCopyBookAdapter extends RecyclerView.Adapter<RvCopyBookAdapter.MyViewHolder> {

    private Context mContext;
    private List<CtBookInfo> mList;
    private LayoutInflater mInflater;

    public RvCopyBookAdapter(Context mContext, List<CtBookInfo> mList) {
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
        View mView = mInflater.inflate(R.layout.ct_rv_copy_book_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent=new Intent( mContext, CtMoveBookActivity.class);
//                mIntent.putExtra("CommunicateNo",mList.get(position).getCommunicateNo());
//                mContext.startActivity(mIntent);
//            }
//        });
        holder.tvAddress.setText(mList.get(position).getAddress());
        holder.tvCommunicateNo.setText(mList.get(position).getCommunicateNo());
        String state=mList.get(position).getReadState();
//        if(state.equals("0")){
//            holder.tvReadState.setText("≥≠±Ì÷–");
//        }else if(state.equals("1")){
//            holder.tvReadState.setText("“—≥≠");
//        }else {
//            holder.tvReadState.setText("Œ¥≥≠");
//        }
//
//        holder.tvDevState.setText(mList.get(position).getDevState());

        if(mList.get(position).isChoose()){
            holder.ivChoose.setImageResource(R.mipmap.choose_01);
        }else {
            holder.ivChoose.setImageResource(R.mipmap.choose_02);
        }

        holder.ivChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean myChoose = mList.get(position).isChoose();
                mList.get(position).setChoose(!myChoose);
                if(myChoose){
                    holder.ivChoose.setImageResource(R.mipmap.choose_02);
                }else {
                    holder.ivChoose.setImageResource(R.mipmap.choose_01);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutMain;
        TextView tvCommunicateNo, tvAddress;
        ImageView ivChoose;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.layoutMain);
            tvCommunicateNo = (TextView) itemView.findViewById(R.id.tvCommunicateNo);

            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
//            tvReadState = (TextView) itemView.findViewById(R.id.tvReadState);
//            tvDevState = (TextView) itemView.findViewById(R.id.tvDevState);
            ivChoose = (ImageView) itemView.findViewById(R.id.ivChoose);
        }
    }
}
