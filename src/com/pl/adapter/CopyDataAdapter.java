package com.pl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.pl.entity.CopyData;
import com.pl.gassystem.R;
import com.pl.utils.MeterType;

import java.util.ArrayList;
import java.util.List;

public class CopyDataAdapter extends BaseAdapter {

	private ArrayList<CopyData> copyDatas;
	private LayoutInflater inflater;
	private Context context;

	public void setCopyDatas(ArrayList<CopyData> copyDatas) {
		if (copyDatas != null) {
			this.copyDatas = copyDatas;
		} else {
			this.copyDatas = new ArrayList<CopyData>();
		}
	}

	public List<CopyData> getCopyDatas(){
		return copyDatas;
	}
	public CopyDataAdapter(Context context, ArrayList<CopyData> copyDatas) {
		this.setCopyDatas(copyDatas);
		inflater = LayoutInflater.from(context);
		this.context=context;
	}

	// 更新ListView数据
	public void changeData(ArrayList<CopyData> copyDatas) {
		this.setCopyDatas(copyDatas);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return copyDatas.size();
	}

	@Override
	public CopyData getItem(int position) {
		return copyDatas.get(position);
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
			convertView = inflater.inflate(R.layout.item_copydata_info, null);
			holder = new ViewHolder();
			holder.tvCopyDataMeterNo = (TextView) convertView
					.findViewById(R.id.tvCopyDataMeterNo);
			holder.tvCopyDataMeterName = (TextView) convertView
					.findViewById(R.id.tvCopyDataMeterName);
			holder.tvCopyDataCurrentShow = (TextView) convertView
					.findViewById(R.id.tvCopyDataCurrentShow);
			holder.tvCopyDataCurrentDosage = (TextView) convertView
					.findViewById(R.id.tvCopyDataCurrentDosage);
			holder.tvCopyDataCopyTime = (TextView) convertView
					.findViewById(R.id.tvCopyDataCopyTime);
			holder.tvCopyDataCopyState = (TextView) convertView
					.findViewById(R.id.tvCopyDataCopyState);
			holder.checkBox = (ImageView) convertView.findViewById(R.id.checkBox);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CopyData copyData = getItem(position);
		holder.tvCopyDataMeterNo.setText(copyData.getMeterNo());
		holder.tvCopyDataMeterName.setText(copyData.getMeterName());

		String[]  a1=copyData.getCurrentShow().split("\\.");
		if(a1.length==2){
			if(a1[1].length()>2){
				holder.tvCopyDataCurrentShow.setText(a1[0]+"."+a1[1].substring(0,2));//本次读数
			}else {
				holder.tvCopyDataCurrentShow.setText(a1[0]+"."+a1[1]);//本次读数
			}
		}else {
			holder.tvCopyDataCurrentShow.setText(a1[0]);//本次读数
		}


		String[]  a2=copyData.getCurrentDosage().split("\\.");
		if(a2.length==2){
			if(a2[1].length()>2){
				holder.tvCopyDataCurrentDosage.setText(a2[0]+"."+a2[1].substring(0,2));//本次读数
			}else {
				holder.tvCopyDataCurrentDosage.setText(a2[0]+"."+a2[1]);//本次读数
			}
		}else {
			holder.tvCopyDataCurrentDosage.setText(a2[0]);//本次读数
		}

//		holder.tvCopyDataCurrentShow.setText(copyData.getCurrentShow());//本次读数
//		holder.tvCopyDataCurrentDosage.setText(copyData.getCurrentDosage());//本次用量
		holder.tvCopyDataCopyTime.setText(copyData.getCopyTime());
		holder.tvCopyDataCopyState.setText(MeterType.GetCopyState(copyData
				.getCopyState()));

		final ViewHolder finalHolder = holder;
		if(copyDatas.get(position).isChoose()){
			finalHolder.checkBox.setImageResource(R.mipmap.choose_01);
		}else {
			finalHolder.checkBox.setImageResource(R.mipmap.choose_02);
		}

		holder.checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(copyDatas.get(position).isChoose()){
					copyDatas.get(position).setChoose(false);
					finalHolder.checkBox.setImageResource(R.mipmap.choose_02);
				}else {
					copyDatas.get(position).setChoose(true);
					finalHolder.checkBox.setImageResource(R.mipmap.choose_01);
				}
			}
		});
//		holder.btGoCopy.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//表编号列表
//				ArrayList<String> meterNos = new ArrayList<>();
//				meterNos.add(copyData.getMeterNo());
//				//04
//				Intent intent = new Intent(context, CopyingActivity.class);
//				intent.putExtra("meterNos", meterNos);
//				intent.putExtra("meterTypeNo", "04");
//				intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
//				intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
//				context.startActivity(intent);
//			}
//		});

		if (copyData.getMeterState() != 0000) {
			BadgeView badgeView = new BadgeView(inflater.getContext());
			badgeView.setTargetView(holder.tvCopyDataMeterName);
			badgeView.setText("!");
		}


//		05
		return convertView;
	}

	class ViewHolder {
		private TextView tvCopyDataMeterNo;
		private TextView tvCopyDataMeterName;
		private TextView tvCopyDataCurrentShow;
		private TextView tvCopyDataCurrentDosage;
		private TextView tvCopyDataCopyTime;
		private TextView tvCopyDataCopyState;
		private ImageView checkBox;
	}

}
