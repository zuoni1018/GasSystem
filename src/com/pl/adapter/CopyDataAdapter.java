package com.pl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.pl.entity.CopyData;
import com.pl.gassystem.R;
import com.pl.utils.MeterType;

import java.util.ArrayList;

public class CopyDataAdapter extends BaseAdapter {

	private ArrayList<CopyData> copyDatas;
	private LayoutInflater inflater;

	public void setCopyDatas(ArrayList<CopyData> copyDatas) {
		if (copyDatas != null) {
			this.copyDatas = copyDatas;
		} else {
			this.copyDatas = new ArrayList<CopyData>();
		}
	}

	public CopyDataAdapter(Context context, ArrayList<CopyData> copyDatas) {
		this.setCopyDatas(copyDatas);
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CopyData copyData = getItem(position);
		holder.tvCopyDataMeterNo.setText(copyData.getMeterNo());
		holder.tvCopyDataMeterName.setText(copyData.getMeterName());
		holder.tvCopyDataCurrentShow.setText(copyData.getCurrentShow());
		holder.tvCopyDataCurrentDosage.setText(copyData.getCurrentDosage());
		holder.tvCopyDataCopyTime.setText(copyData.getCopyTime());
		holder.tvCopyDataCopyState.setText(MeterType.GetCopyState(copyData
				.getCopyState()));

		if (copyData.getMeterState() != 0000) {
			BadgeView badgeView = new BadgeView(inflater.getContext());
			badgeView.setTargetView(holder.tvCopyDataMeterName);
			badgeView.setText("!");
		}

		return convertView;
	}

	class ViewHolder {
		private TextView tvCopyDataMeterNo;
		private TextView tvCopyDataMeterName;
		private TextView tvCopyDataCurrentShow;
		private TextView tvCopyDataCurrentDosage;
		private TextView tvCopyDataCopyTime;
		private TextView tvCopyDataCopyState;
	}

}
