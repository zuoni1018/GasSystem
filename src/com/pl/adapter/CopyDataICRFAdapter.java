package com.pl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.pl.entity.CopyDataICRF;
import com.pl.gassystem.R;
import com.pl.utils.MeterType;

import java.util.ArrayList;

public class CopyDataICRFAdapter extends BaseAdapter {

	private ArrayList<CopyDataICRF> copyDataICRFs;
	private LayoutInflater inflater;

	public void setCopyDataICRFs(ArrayList<CopyDataICRF> copyDataICRFs) {
		if (copyDataICRFs != null) {
			this.copyDataICRFs = copyDataICRFs;
		} else {
			this.copyDataICRFs = new ArrayList<CopyDataICRF>();
		}
	}

	public CopyDataICRFAdapter(Context context,
			ArrayList<CopyDataICRF> copyDataICRFs) {
		this.setCopyDataICRFs(copyDataICRFs);
		inflater = LayoutInflater.from(context);
	}

	// 更新ListView数据
	public void changeData(ArrayList<CopyDataICRF> copyDataICRFs) {
		this.setCopyDataICRFs(copyDataICRFs);
		this.notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_copydataicrf_info,
					null);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CopyDataICRF copyDataICRF = getItem(position);
		holder.tvCopyDataICRFMeterNo.setText(copyDataICRF.getMeterNo());
		holder.tvCopyDataICRFMeterName.setText(copyDataICRF.getMeterName());
		holder.tvCopyDataICRFCumulant.setText(copyDataICRF.getCumulant());
		holder.tvCopyDataICRFSurplusMoney.setText(copyDataICRF
				.getSurplusMoney());
		holder.tvCopyDataICRFCopyTime.setText(copyDataICRF.getCopyTime());
		holder.tvCopyDataICRFCopyState.setText(MeterType
				.GetCopyState(copyDataICRF.getCopyState()));

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
	}

}
