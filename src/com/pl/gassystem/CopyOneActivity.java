package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.entity.GroupInfo;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;

public class CopyOneActivity extends Activity {

	private TextView tvTitlebar_name;
	private String groupName;
	private ImageButton btnOnlybackQuit;
	private LinearLayout linCopyoneUnRead, linCopyoneRead, linCopyoneAll,
			linCopyonePutNumber, linCopyonePrint;
	private ImageView ivToCopyGroup, ivToCopyOneOther;
	private GroupInfo gpInfo;
	private ArrayList<String> meterNos;
	private CopyBiz copyBiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_copy_one);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_onlyback);

		gpInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");
		if (gpInfo != null) {
			groupName = gpInfo.getGroupName();
		} else {
			groupName = "未知分组";
		}
		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText(groupName + "单抄");

		copyBiz = new CopyBiz(this);

		setupView();
		addListener();
	}

	private void setupView() {
		btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		linCopyoneUnRead = (LinearLayout) findViewById(R.id.linCopyoneUnRead);
		linCopyoneRead = (LinearLayout) findViewById(R.id.linCopyoneRead);
		linCopyoneAll = (LinearLayout) findViewById(R.id.linCopyoneAll);
		linCopyonePutNumber = (LinearLayout) findViewById(R.id.linCopyonePutNumber);
		linCopyonePrint = (LinearLayout) findViewById(R.id.linCopyonePrint);
		ivToCopyGroup = (ImageView) findViewById(R.id.ivToCopyGroup);
		ivToCopyOneOther = (ImageView) findViewById(R.id.ivToCopyOneOther);
	}

	private void addListener() {
		btnOnlybackQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		linCopyoneUnRead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 选取未抄
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyOneActivity.this, CopyResultActivity.class);
						intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SELECTUNCOPY);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
						startActivity(intent);
					} else {
						Toast.makeText(CopyOneActivity.this, "该分组最近抄表结果中无未抄取表", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyOneActivity.this, "未知分组", Toast.LENGTH_SHORT).show();
				}
			}
		});

		linCopyoneRead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 选取已抄
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyReadMeterNo(gpInfo.getGroupNo(),
							gpInfo.getMeterTypeNo());
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyOneActivity.this, CopyResultActivity.class);
						intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SELECTCOPY);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
						startActivity(intent);
					} else {
						Toast.makeText(CopyOneActivity.this, "该分组最近抄表结果中无已抄取表", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyOneActivity.this, "未知分组", Toast.LENGTH_SHORT).show();
				}
			}
		});

		linCopyoneAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 显示全部
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyOneActivity.this, CopyResultActivity.class);
						intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SELECTALL);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
						startActivity(intent);
					} else {
						Toast.makeText(CopyOneActivity.this, "该分组内无表具", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyOneActivity.this, "未知分组", Toast.LENGTH_SHORT).show();
				}
			}
		});

		linCopyonePutNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 输入通讯号
				Intent intent = new Intent(CopyOneActivity.this, InputComNumActivity.class);
				intent.putExtra("operationType", 1);
				intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
				startActivity(intent);
			}
		});

		linCopyonePrint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 表具维护
				Intent intent = new Intent(CopyOneActivity.this, MaintenanceActivity.class);
				startActivity(intent);
			}
		});

		ivToCopyGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 跳转之分组抄表
				Intent intent = new Intent(CopyOneActivity.this, CopyGroupActivity.class);
				intent.putExtra("GroupInfo", gpInfo);
				startActivity(intent);
			}
		});

		ivToCopyOneOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// 其他
				Toast.makeText(CopyOneActivity.this, "暂未开放", Toast.LENGTH_SHORT).show();
			}
		});

	}

}
