package com.pl.gassystem.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pl.entity.GroupInfo;
import com.pl.gassystem.R;
import com.pl.utils.MeterType;

import java.util.ArrayList;

public class GroupInfoAdapter extends BaseAdapter {

	private ArrayList<GroupInfo> groupInfos;
	private LayoutInflater inflater;

	public void setGroupInfos(ArrayList<GroupInfo> groupInfos) {
		if (groupInfos != null) {
			this.groupInfos = groupInfos;
		} else {
			this.groupInfos = new ArrayList<GroupInfo>();
		}
	}

	public GroupInfoAdapter(Context context, ArrayList<GroupInfo> groupInfos) {
		this.setGroupInfos(groupInfos);
		inflater = LayoutInflater.from(context);
	}
	public void changeData(ArrayList<GroupInfo> groupInfos) {
		this.setGroupInfos(groupInfos);
		this.notifyDataSetChanged();
	}

	public void removeItem(int position) {
		if (position >= 0 && position < groupInfos.size()) {
			this.groupInfos.remove(position);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return groupInfos.size();
	}

	@Override
	public GroupInfo getItem(int position) {
		return groupInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_book_info, null);
			holder = new ViewHolder();
			holder.tvGroupInfoName = (TextView) convertView.findViewById(R.id.tvBookInfoName);
			holder.tvMeterType = (TextView) convertView.findViewById(R.id.tvMeterType);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GroupInfo groupInfo = getItem(position);
		holder.tvGroupInfoName.setText(groupInfo.getGroupName());
		holder.tvMeterType.setText("±íÀàÐÍ: "+MeterType.GetMeterTypeName(groupInfo.getMeterTypeNo())+"");

		return convertView;
	}

	class ViewHolder {
		private TextView tvGroupInfoName;
		private TextView tvMeterType;
	}

}
