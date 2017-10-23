package com.pl.gassystem.adapter.ht;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.gson.GetCopyDataPhoto;
import com.pl.gassystem.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class RvHtResultCopyPhotoAdapter extends RecyclerView.Adapter<RvHtResultCopyPhotoAdapter.MyViewHolder> {

    private Context mContext;
    private List<GetCopyDataPhoto.ModTimereadmeterinfoBean> mList;
    private LayoutInflater mInflater;

    public RvHtResultCopyPhotoAdapter(Context mContext, List<GetCopyDataPhoto.ModTimereadmeterinfoBean> mList) {
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
        View mView = mInflater.inflate(R.layout.ht_rv_copy_result_photo_item, parent, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.CommunicateNo.setText(mList.get(position).getCommunicateNo());
        holder.OcrRead.setText(mList.get(position).getOcrRead());
        holder.DevPower.setText(mList.get(position).getDevPower());
        holder.JSQD.setText(mList.get(position).getJSQD());
        holder.DevState.setText(mList.get(position).getDevState());
        LogUtil.i("图片",mList.get(position).getDeafault().replace("_","\\"));
//        Bitmap bitmap = getBitmapFromByte(hex2byte(mList.get(position).getImageName()));
//        if (bitmap != null) {
//            holder.imageView.setImageBitmap(bitmap);
//        } else {
//            holder.imageView.setImageResource(R.drawable.ic_launcher);
//        }
        Glide.with(mContext)
                .load("http://116.62.6.184:8089/"+mList.get(position).getDeafault().replace("_","\\"))
                .into(holder.imageView);




//
    }


    public Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 字符串转二进制
     *
     * @param str 要转换的字符串
     * @return 转换后的二进制数组
     */
    public static byte[] hex2byte(String str) { // 字符串转二进制
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] string2ByteArray(String str, String charset) {
        byte[] bytes = null;
        if (charset == null) {
            bytes = str.getBytes();
        } else {
            try {
                //如charset = "utf-8"
                bytes = str.getBytes(charset);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return bytes;
    }

    public Bitmap string2Bitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //        LinearLayout layoutMain;
        TextView CommunicateNo, OcrRead, DevPower, JSQD, DevState;

        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            CommunicateNo = (TextView) itemView.findViewById(R.id.CommunicateNo);
            OcrRead = (TextView) itemView.findViewById(R.id.OcrRead);
            DevPower = (TextView) itemView.findViewById(R.id.DevPower);
            JSQD = (TextView) itemView.findViewById(R.id.JSQD);
            DevState = (TextView) itemView.findViewById(R.id.DevState);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
