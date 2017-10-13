package com.pl.gassystem.adapter.ht;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.ht.HtCopyingActivity;
import com.pl.gassystem.bean.ht.HtSendMessage;
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
//                单选
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("修改表" + mList.get(position).getMeterNo() + "的参数");
                builder.setPositiveButton("修改密钥", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent mIntent = new Intent(mContext, HtCopyingActivity.class);
                        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_SET_KEY);//命令类型
                        ArrayList<String> bookNos = new ArrayList<>();
                        bookNos.add(mList.get(position).getMeterNo());
                        mIntent.putStringArrayListExtra("bookNos", bookNos);//操作表

                        //旧参数
                        mIntent.putExtra("YinZi", mList.get(position).getKPYZ1());
                        mIntent.putExtra("XinDao", mList.get(position).getKPXD1());
                        mIntent.putExtra("nowKey", mList.get(position).getKEYCODE1());

                        //新参数
                        mIntent.putExtra("newKey", mList.get(position).getKEYVER2() + mList.get(position).getKEYCODE2());
                        mIntent.putExtra("KEYVER", mList.get(position).getKEYVER2());

                        mContext.startActivity(mIntent);

                    }
                });
                builder.setNegativeButton("修改参数", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent mIntent = new Intent(mContext, HtCopyingActivity.class);
                        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_SET_PARAMETER);//命令类型
                        ArrayList<String> bookNos = new ArrayList<>();
                        bookNos.add(mList.get(position).getMeterNo());
                        mIntent.putStringArrayListExtra("bookNos", bookNos);//操作表

                        //原来的参数
                        mIntent.putExtra("YinZi", mList.get(position).getKPYZ1());
                        mIntent.putExtra("XinDao", mList.get(position).getKPXD1());
                        mIntent.putExtra("nowKey", mList.get(position).getKEYCODE1());

                        //需要设置的参数
                        mIntent.putExtra("yinzi", mList.get(position).getKPYZ2());//扩频因子
                        mIntent.putExtra("xindao", mList.get(position).getKPXD2());//扩频信道
                        mIntent.putExtra("dongjieri", mList.get(position).getDJR2());//设置冻结日
                        mIntent.putExtra("kaichuangshijian", mList.get(position).getKCQZSJ2());//开窗起止时间
                        mIntent.putExtra("isSetYinZi", true);

                        mContext.startActivity(mIntent);

                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });


//        if(mList.get(position).isChoose()){
//            holder.ivCheck.setImageResource(R.mipmap.choose_01);
//        }else {
//            holder.ivCheck.setImageResource(R.mipmap.choose_02);
//        }


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
            holder.tvMore.setText("查看新参数");
        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.tvMore.setText("关闭新参数");
        }

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mList.get(position).setChoose(!mList.get(position).isChoose());

                if (!mList.get(position).isChoose()) {
                    holder.layoutOther.setVisibility(View.GONE);
                    holder.tvMore.setText("查看新参数");
                } else {
                    holder.layoutOther.setVisibility(View.VISIBLE);
                    holder.tvMore.setText("关闭新参数");
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

        ImageView ivCheck;

        MyViewHolder(View itemView) {
            super(itemView);
            tvMeterNo = (TextView) itemView.findViewById(R.id.tvMeterNo);
            tvMeterTypeNo = (TextView) itemView.findViewById(R.id.tvMeterTypeNo);

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

            ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
        }
    }
}
