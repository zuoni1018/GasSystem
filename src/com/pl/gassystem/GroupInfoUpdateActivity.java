package com.pl.gassystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pl.bll.GroupInfoBiz;
import com.pl.entity.GroupInfo;
import com.pl.utils.GlobalConsts;
import com.pl.utils.StringFormatter;

import java.util.ArrayList;

public class GroupInfoUpdateActivity extends Activity {

	private TextView tvBookNo;
	private EditText etBookName, etestateNo, etRemark;
	private RadioGroup rgMeterType;
	private Button btnSubmit, btnQuit;

	private GroupInfoBiz groupInfoBiz;
	private int opType;
	private String bookNo;
	private String meterTypeNo;

	private TextView tvTitleBarName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_group_info_update);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main);

		groupInfoBiz = new GroupInfoBiz(this);
		opType = getIntent().getIntExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
		bookNo = getIntent().getStringExtra("BookNo").toString();
		meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();

		setupview();
		addListener();
	}

	private void setupview() {
		tvBookNo = (TextView) findViewById(R.id.tvBookNo);
		etBookName = (EditText) findViewById(R.id.etBookName);
		etestateNo = (EditText) findViewById(R.id.etestateNo);
		etRemark = (EditText) findViewById(R.id.etRemark);
		rgMeterType = (RadioGroup) findViewById(R.id.rgMeterType);
		btnSubmit = (Button) findViewById(R.id.btnBkUpSubmit);
		btnQuit = (Button) findViewById(R.id.btnBkUpQuit);

		tvTitleBarName = (TextView) findViewById(R.id.tvMainTitleBarName);

		switch (opType) {
		case GlobalConsts.TYPE_ADD:
			btnSubmit.setText("提交");
			tvTitleBarName.setText("新建分组");
			if ("04".equals(meterTypeNo)) {
				rgMeterType.check(R.id.rdoIC);
			} else if ("05".equals(meterTypeNo)) {
				rgMeterType.check(R.id.rdoWX);
			}
			break;
		case GlobalConsts.TYPE_UPDATE:
			btnSubmit.setText("修改");
			tvTitleBarName.setText("修改分组");
			GroupInfo groupInfo = (GroupInfo) getIntent().getSerializableExtra(
					GlobalConsts.EXTRA_BOOKINFO_OP_DATA);
			tvBookNo.setText(groupInfo.getGroupNo());
			etBookName.setText(groupInfo.getGroupName());
			etestateNo.setText(groupInfo.getEstateNo());
			etRemark.setText(groupInfo.getRemark());
			if ("04".equals(groupInfo.getMeterTypeNo())) {
				rgMeterType.check(R.id.rdoIC);
			} else if ("05".equals(groupInfo.getMeterTypeNo())) {
				rgMeterType.check(R.id.rdoWX);
			}
			break;
		}
	}

	private void addListener() {
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取输入
				GroupInfo groupInfo = new GroupInfo();
				groupInfo.setGroupName(etBookName.getText().toString());
				groupInfo.setEstateNo(etestateNo.getText().toString());
				groupInfo.setRemark(etRemark.getText().toString());
				String meterType = "";
				if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoIC) {
					meterType = "04";
				} else if (rgMeterType.getCheckedRadioButtonId() == R.id.rdoWX) {
					meterType = "05";
				}
				groupInfo.setMeterTypeNo(meterType);
				groupInfo.setBookNo(bookNo);
				// 操作
				switch (opType) {
				case GlobalConsts.TYPE_ADD:
					String beginGroupNo = bookNo.substring(5);
					String endGroupNo;
					ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);
					if (groupInfos != null && groupInfos.size() > 0) {
						endGroupNo = StringFormatter.getAddStringGroupNo(groupInfos.get(0).getGroupNo().substring(5));
					} else {
						endGroupNo = "00001";
					}
					groupInfo.setGroupNo(beginGroupNo + endGroupNo);
					groupInfoBiz.addGroupInfo(groupInfo);
					break;
				case GlobalConsts.TYPE_UPDATE:
					groupInfo.setGroupNo(tvBookNo.getText().toString());
					groupInfoBiz.updateGroupInfo(groupInfo);
					break;
				}
				finish();
			}
		});

		btnQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
