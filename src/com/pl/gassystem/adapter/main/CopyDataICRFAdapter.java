package com.pl.gassystem.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.pl.entity.CopyDataICRF;
import com.pl.gassystem.R;
import com.pl.gassystem.utils.LogUtil;
import com.pl.utils.MeterType;

import java.util.ArrayList;
import java.util.List;

public class CopyDataICRFAdapter extends BaseAdapter {

    private ArrayList<CopyDataICRF> copyDataICRFs;
    private LayoutInflater inflater;
    private Context context;

    public void setCopyDataICRFs(ArrayList<CopyDataICRF> copyDataICRFs) {
        if (copyDataICRFs != null) {
            this.copyDataICRFs = copyDataICRFs;
        } else {
            this.copyDataICRFs = new ArrayList<>();
        }
    }

    public CopyDataICRFAdapter(Context context, ArrayList<CopyDataICRF> copyDataICRFs) {
        this.setCopyDataICRFs(copyDataICRFs);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    // 更新ListView数据
    public void changeData(ArrayList<CopyDataICRF> copyDataICRFs) {
        this.setCopyDataICRFs(copyDataICRFs);
        this.notifyDataSetChanged();
    }

    public List<CopyDataICRF> getCopyDataICRFs(){
        return copyDataICRFs;
    }

    @Override
    public int getCount() {
        return copyDataICRFs.size();
    }

    @Override
    public CopyDataICRF getItem(int position) {
        return copyDataICRFs.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_copydataicrf_info, null);
            holder = new ViewHolder();
            holder.tvCopyDataICRFMeterNo = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFMeterNo);
            holder.tvCopyDataICRFMeterName = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFMeterName);
            holder.tvCopyDataICRFCumulant = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFCumulant);
            holder.tvCopyDataICRFSurplusMoney = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFSurplusMoney);
            holder.tvCopyDataICRFCopyTime = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFCopyTime);
            holder.tvCopyDataICRFCopyState = (TextView) convertView
                    .findViewById(R.id.tvCopyDataICRFCopyState);

//            holder.btGoCopy = (Button) convertView.findViewById(R.id.btGoCopy);
            holder.checkBox= (ImageView) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CopyDataICRF copyDataICRF = getItem(position);
        holder.tvCopyDataICRFMeterNo.setText(copyDataICRF.getMeterNo());
        holder.tvCopyDataICRFMeterName.setText(copyDataICRF.getMeterName());
        holder.tvCopyDataICRFCumulant.setText(copyDataICRF.getCumulant());
        holder.tvCopyDataICRFSurplusMoney.setText(copyDataICRF
                .getSurplusMoney());
        holder.tvCopyDataICRFCopyTime.setText(copyDataICRF.getCopyTime());
        holder.tvCopyDataICRFCopyState.setText(MeterType
                .GetCopyState(copyDataICRF.getCopyState()));

        LogUtil.i("zzzz222",copyDataICRF.getName()+"");

        final ViewHolder finalHolder = holder;
        if(copyDataICRFs.get(position).isChoose()){
            finalHolder.checkBox.setImageResource(R.mipmap.choose_01);
        }else {
            finalHolder.checkBox.setImageResource(R.mipmap.choose_02);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(copyDataICRFs.get(position).isChoose()){
                    copyDataICRFs.get(position).setChoose(false);
                    finalHolder.checkBox.setImageResource(R.mipmap.choose_02);
                }else {
                    copyDataICRFs.get(position).setChoose(true);
                    finalHolder.checkBox.setImageResource(R.mipmap.choose_01);
                }
            }
        });

//        holder.checkBox.setChecked( copyDataICRFs.get(position).isChoose());
//
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                copyDataICRFs.get(position).setChoose(isChecked);
//                LogUtil.i("zzzz22",copyDataICRFs.get(position).isChoose()+"");
//            }
//        });

//        holder.btGoCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //表编号列表
//                ArrayList<String> meterNos = new ArrayList<String>();
//                meterNos.add(copyDataICRF.getMeterNo());
//                //04
//
//                Intent intent = new Intent(context, CopyingActivity.class);
//                intent.putExtra("meterNos", meterNos);
//                intent.putExtra("meterTypeNo", "04");
//                intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
//                intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
//                context.startActivity(intent);
//
//
//            }
//        });

        if (copyDataICRF.getMeterState() != 0000) {
            BadgeView badgeView = new BadgeView(inflater.getContext());
            badgeView.setTargetView(holder.tvCopyDataICRFMeterName);
            badgeView.setText("!");
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tvCopyDataICRFMeterNo;
        private TextView tvCopyDataICRFMeterName;
        private TextView tvCopyDataICRFCumulant;
        private TextView tvCopyDataICRFSurplusMoney;
        private TextView tvCopyDataICRFCopyTime;
        private TextView tvCopyDataICRFCopyState;
        private Button btGoCopy;
        private ImageView checkBox;
    }

}
