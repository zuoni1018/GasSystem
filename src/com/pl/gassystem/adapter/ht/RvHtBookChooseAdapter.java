package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 杭天抄表 表选择
 */

public class RvHtBookChooseAdapter extends RecyclerView.Adapter<RvHtBookChooseAdapter.MyViewHolder> {

    private Context mContext;
    private List<HtBook> mList;
    private LayoutInflater mInflater;

    public RvHtBookChooseAdapter(Context mContext, List<HtBook> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_book_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tvBookNo.setText(mList.get(position).getBookNum());
        holder.tvBookName.setText(mList.get(position).getBookName());
        if(mList.get(position).isChoose()){
            holder.ivChoose.setImageResource(R.mipmap.choose_01);
        }else {
            holder.ivChoose.setImageResource(R.mipmap.choose_02);
        }

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nowChoose=mList.get(position).isChoose();
                mList.get(position).setChoose(!nowChoose);
                if(nowChoose){
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
        TextView tvBookNo,tvBookName;
        ImageView ivChoose;

        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.layoutMain);
            tvBookNo= (TextView) itemView.findViewById(R.id.tvBookNo);
            tvBookName= (TextView) itemView.findViewById(R.id.tvBookName);
            ivChoose= (ImageView) itemView.findViewById(R.id.ivChoose);
        }
    }
}
