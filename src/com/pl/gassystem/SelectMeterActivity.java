package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pl.adapter.GroupBindAdapter;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.GroupBind;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;
/**
 * ���� �رշ��� �޸����߱�ͨѶ�� ����
 *
 * */
public class SelectMeterActivity extends Activity {

	private Button btnSelectMeterInputCom;
	private TextView tvTitlebar_name;
	private GroupBindBiz groupBindBiz;
	private GroupBindAdapter adapter;
	private GroupInfoBiz groupInfoBiz;
	private ListView lvSelectMeter;
	private ImageButton btnquit;
	private ArrayList<String> meterNos;
	// ִ��ģʽ
	private static int operationType;
	private static boolean isParam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_select_meter);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);

		operationType = getIntent().getIntExtra("operationType", 1);
		if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
			tvTitlebar_name.setText("ѡ��Ҫ�����ı��");
			isParam = false;
		} else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
			tvTitlebar_name.setText("ѡ��Ҫ�ط��ı��");
			isParam = false;
		} else if (operationType == GlobalConsts.COPYSH_OPERATION_RESET
				|| operationType == GlobalConsts.COPYSH_OPERATION_GETCOPYDAY
				|| operationType == GlobalConsts.COPY_OPERATION_COPYFROZEN) {
			tvTitlebar_name.setText("��ѡ��Ҫ�����ı��");
			isParam = false;
		} else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
			tvTitlebar_name.setText("ѡ��Ҫ�޸�ͨѶ�ŵı��");
			isParam = true;
		} else {
			tvTitlebar_name.setText("��ѡ��Ҫ�����ı��");
			isParam = true;
		}

		groupBindBiz = new GroupBindBiz(this);
		groupInfoBiz = new GroupInfoBiz(this);

		setupView();
		addListener();

	}

	private void addListener() {
		btnquit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnSelectMeterInputCom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// ����ͨѶ���
				Intent intent = new Intent(SelectMeterActivity.this, InputComNumActivity.class);
				intent.putExtra("operationType", operationType);
				startActivity(intent);
			}
		});

		lvSelectMeter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GroupBind groupBind = adapter.getItem(position);
				if (!isParam) { // ������������
					Intent intent = new Intent(SelectMeterActivity.this, CopyingActivity.class);
					meterNos = new ArrayList<String>();
					meterNos.add(groupBind.getMeterNo());
					intent.putExtra("meterNos", meterNos);
					intent.putExtra("meterTypeNo", groupInfoBiz.getMeterTypeNoByGroupNo(groupBind.getGroupNo()));
					intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
					intent.putExtra("operationType", operationType);
					startActivity(intent);
				} else {
					Intent intent = new Intent(SelectMeterActivity.this, InputComNumActivity.class);
					String aaaString = groupInfoBiz.getMeterTypeNoByGroupNo(groupBind.getGroupNo());
					intent.putExtra("meterTNo", aaaString);
					intent.putExtra("operationType", operationType);
					intent.putExtra("meterNo", groupBind.getMeterNo());
					intent.putExtra("meterTypeNo", groupBind.getMeterType());
					startActivity(intent);
				}
			}
		});

	}

	private void setupView() {
		lvSelectMeter = (ListView) findViewById(R.id.lvSelectMeter);
		ArrayList<GroupBind> groupBinds = groupBindBiz.getGroupBindAll();
		adapter = new GroupBindAdapter(this, groupBinds, null);
		lvSelectMeter.setAdapter(adapter);
		btnquit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		btnSelectMeterInputCom = (Button) findViewById(R.id.btnSelectMeterInputCom);
	}

}
