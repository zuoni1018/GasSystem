package com.pl.concentrator.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pl.concentrator.activity.CtMoveBookActivity;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvNetworkingListAdapter extends RecyclerView.Adapter<RvNetworkingListAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater mInflater;

    public RvNetworkingListAdapter(Context mContext, List<String> mList) {
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
        View mView = mInflater.inflate(R.layout.ct_rv_networking_list_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent( mContext, CtMoveBookActivity.class);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutMain;
        MyViewHolder(View itemView) {
            super(itemView);
            layoutMain = (RelativeLayout) itemView.findViewById(R.id.layoutMain);
        }
    }
}
