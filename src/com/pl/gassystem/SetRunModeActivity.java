package com.pl.gassystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bll.SetBiz;
import com.pl.utils.GlobalConsts;

public class SetRunModeActivity extends Activity {

	private TextView tvTitlebar_name, tvRunModeTipStandard,
			tvRunModeTipHuiZhou, tvRunModeTipLORA, tvRunModeTipFSK,
			tvRunModeTipShangHai, tvRunModeTipPhoto,tvRunModeTipZHGT;
	private ImageButton btnOnlybackQuit;
	private LinearLayout linRunModeStandard, linRunModeHuiZhou, linRunModeLORA,
			linRunModeFSK, linRunModeShangHai, linRunModePhoto,linRunModeZHGT;
	private SetBiz setBiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_set_run_mode);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_onlyback);

		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText("抄表运行模式");
		setBiz = new SetBiz(this);

		setupView();
		addListener();

		loadMode();
	}

	private void loadMode() {
		String runMode = setBiz.getRunMode();
		if (runMode.equals(GlobalConsts.RUNMODE_STANDARD)) {
			tvRunModeTipStandard.setVisibility(View.VISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.VISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_LORA)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.VISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_FSK)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.VISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_SHANGHAI)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.VISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_PHOTO)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.VISIBLE);
			tvRunModeTipZHGT.setVisibility(View.INVISIBLE);
		} else if (runMode.equals(GlobalConsts.RUNMODE_ZHGT)) {
			tvRunModeTipStandard.setVisibility(View.INVISIBLE);
			tvRunModeTipHuiZhou.setVisibility(View.INVISIBLE);
			tvRunModeTipFSK.setVisibility(View.INVISIBLE);
			tvRunModeTipLORA.setVisibility(View.INVISIBLE);
			tvRunModeTipShangHai.setVisibility(View.INVISIBLE);
			tvRunModeTipPhoto.setVisibility(View.INVISIBLE);
			tvRunModeTipZHGT.setVisibility(View.VISIBLE);
		}
	}

	private void setupView() {
		tvRunModeTipHuiZhou = (TextView) findViewById(R.id.tvRunModeTipHuiZhou);
		tvRunModeTipStandard = (TextView) findViewById(R.id.tvRunModeTipStandard);
		tvRunModeTipFSK = (TextView) findViewById(R.id.tvRunModeTipFSK);
		tvRunModeTipLORA = (TextView) findViewById(R.id.tvRunModeTipLORA);
		tvRunModeTipShangHai = (TextView) findViewById(R.id.tvRunModeTipShangHai);
		tvRunModeTipPhoto = (TextView) findViewById(R.id.tvRunModeTipPhoto);
		tvRunModeTipZHGT = (TextView) findViewById(R.id.tvRunModeTipZHGT);
		btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		linRunModeStandard = (LinearLayout) findViewById(R.id.linRunModeStandard);
		linRunModeHuiZhou = (LinearLayout) findViewById(R.id.linRunModeHuiZhou);
		linRunModeFSK = (LinearLayout) findViewById(R.id.linRunModeFSK);
		linRunModeLORA = (LinearLayout) findViewById(R.id.linRunModeLORA);
		linRunModeShangHai = (LinearLayout) findViewById(R.id.linRunModeShangHai);
		linRunModePhoto = (LinearLayout) findViewById(R.id.linRunModePhoto);
		linRunModeZHGT = (LinearLayout) findViewById(R.id.linRunModeZHGT);
	}

	private void addListener() {
		btnOnlybackQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		linRunModeStandard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("1");
				loadMode();
			}
		});

		linRunModeLORA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("2");
				loadMode();
			}
		});

		linRunModeFSK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("3");
				loadMode();
			}
		});

		linRunModeHuiZhou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("4");
				loadMode();
			}
		});

		linRunModeShangHai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("5");
				loadMode();
			}
		});

		linRunModePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("6");
				loadMode();
			}
		});
		
		linRunModeZHGT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				setBiz.updateRunMode("7");
				loadMode();
			}
		});

	}

}
