package com.pl.gassystem.adapter.main;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pl.entity.GroupBind;
import com.pl.gassystem.R;

import java.util.ArrayList;

public class GroupBindAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<GroupBind> groupBinds;
    private Handler handler;

    public void setGroupBinds(ArrayList<GroupBind> groupBinds) {
        if (groupBinds != null) {
            this.groupBinds = groupBinds;
        } else {
            this.groupBinds = new ArrayList<GroupBind>();
        }
    }

    public GroupBindAdapter(Context context, ArrayList<GroupBind> groupBinds,
                            Handler handler) {
        this.setGroupBinds(groupBinds);
        this.handler = handler;
        inflater = LayoutInflater.from(context);
    }

    // 更新ListView数据
    public void changeData(ArrayList<GroupBind> groupBinds) {
        this.setGroupBinds(groupBinds);
        this.notifyDataSetChanged();
    }

    // 移除数据并更新的方法
    public void removeItem(int position) {
        if (position >= 0 && position < groupBinds.size()) {
            this.groupBinds.remove(position);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return groupBinds.size();
    }

    @Override
    public GroupBind getItem(int position) {
        return groupBinds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return 0;
    }

    public ArrayList<GroupBind> getObjs() {
        return groupBinds;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_groupbind_info, null);
            holder = new ViewHolder();
            holder.tvMeterNo = (TextView) convertView
                    .findViewById(R.id.tvMeterNo);
            holder.tvMeterName = (TextView) convertView
                    .findViewById(R.id.tvMeterName);
            holder.tvMeterType = (TextView) convertView
                    .findViewById(R.id.tvMeterType);
            holder.check = (TextView) convertView.findViewById(R.id.chek);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupBind groupBind = getItem(position);
        if (groupBind.isCheck()) {
            holder.check.setBackgroundResource(R.drawable.ck_true);
        } else {
            holder.check.setBackgroundResource(R.drawable.ck_false);
        }
        holder.tvMeterNo.setText(groupBind.getMeterNo());
        holder.tvMeterName.setText(groupBind.getMeterName());
        if (groupBind.getMeterType() != null) {
            switch (groupBind.getMeterType()) {
                case "1":
                    holder.tvMeterType.setText("FSK");
                    break;
                case "2":
                    holder.tvMeterType.setText("LORA");
                    break;
            }
        } else {
            holder.tvMeterType.setText("");
        }
        holder.check.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (groupBinds.get(position).isCheck()) {
                    groupBinds.get(position).setCheck(false);

                } else {
                    groupBinds.get(position).setCheck(true);
                }
                notifyDataSetChanged();
                if (handler != null) {
                    handler.sendEmptyMessage(1);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tvMeterNo;
        private TextView tvMeterName;
        private TextView tvMeterType;
        private TextView check;
    }

}
