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

public class CopyGroupActivity extends Activity {

	private TextView tvTitlebar_name;
	private String groupName;

	private ImageButton btnOnlybackQuit;
	private LinearLayout linCopyStart, linCopyUnRead, linReadUn, linReadAll, linCopyGo;
	private ImageView ivToCopyOne, ivToCopyOther;
	private GroupInfo gpInfo;
	private CopyBiz copyBiz;
	private ArrayList<String> meterNos;
	private String meterTypeNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_copy_group);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

		gpInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");
		if (gpInfo != null) {
			groupName = gpInfo.getGroupName();
		} else {
			groupName = "δ֪����";
		}
		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText(groupName + "Ⱥ��");

		copyBiz = new CopyBiz(this);

		setupView();
		addListener();

	}

	private void setupView() {
		btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		linCopyStart = (LinearLayout) findViewById(R.id.linCopyStart);
		linCopyUnRead = (LinearLayout) findViewById(R.id.linCopyUnRead);
		linReadUn = (LinearLayout) findViewById(R.id.linReadUn);
		linReadAll = (LinearLayout) findViewById(R.id.linReadAll);
		linCopyGo = (LinearLayout) findViewById(R.id.linCopyGo);//��ʼ����ť
		ivToCopyOne = (ImageView) findViewById(R.id.ivToCopyOne);
		ivToCopyOther = (ImageView) findViewById(R.id.ivToCopyOther);
	}

	private void addListener() {
		btnOnlybackQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		linCopyStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ��ȡȫ��

				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
					meterTypeNo = gpInfo.getMeterTypeNo();
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyGroupActivity.this, CopyingActivity.class);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", meterTypeNo);
						intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
						intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
						// intent.putExtra("isCopyUnRead", false);
						startActivity(intent);
					} else {
						Toast.makeText(CopyGroupActivity.this, "�÷������ޱ�ߣ��޷�����",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyGroupActivity.this, "δ֪�����޷�Ⱥ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		linCopyUnRead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ��ȡδ��
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
					meterTypeNo = gpInfo.getMeterTypeNo();
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyGroupActivity.this, CopyingActivity.class);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", meterTypeNo);
						intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
						intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
						startActivity(intent);
					} else {
						Toast.makeText(CopyGroupActivity.this, "�÷����������������δ��ȡ��", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyGroupActivity.this, "δ֪�����޷�Ⱥ��", Toast.LENGTH_SHORT).show();
				}
			}
		});

		linReadUn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ��ʾδ��
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyGroupActivity.this, CopyResultActivity.class);
						intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWUNCOPY);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
						startActivity(intent);
					} else {
						Toast.makeText(CopyGroupActivity.this, "�÷����������������δ��ȡ��", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyGroupActivity.this, "δ֪����",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		linReadAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ��ʾȫ��
				if (gpInfo != null) {
					meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
					if (meterNos != null && meterNos.size() > 0) {
						Intent intent = new Intent(CopyGroupActivity.this, CopyResultActivity.class);
						intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
						intent.putExtra("meterNos", meterNos);
						intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
						startActivity(intent);
					} else {
						Toast.makeText(CopyGroupActivity.this, "�÷������ޱ��", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(CopyGroupActivity.this, "δ֪����", Toast.LENGTH_SHORT).show();
				}
			}
		});

		linCopyGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ��ʼ����
				Intent intent = new Intent(CopyGroupActivity.this, GroupBindActivity.class);
				intent.putExtra("GroupInfo", gpInfo);
				startActivity(intent);
			}
		});

		ivToCopyOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {// ǰ������
				Intent intent = new Intent(CopyGroupActivity.this, CopyOneActivity.class);
				intent.putExtra("GroupInfo", gpInfo);
				startActivity(intent);
			}
		});

		ivToCopyOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// ǰ������
				Toast.makeText(CopyGroupActivity.this, "��δ����", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
