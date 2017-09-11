package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pl.bll.SetBiz;
import com.pl.bll.XmlParser;
import com.pl.common.CustomProgressDialog;
import com.pl.entity.HrCustomerInfo;
import com.pl.utils.GlobalConsts;
import com.zxing.activity.CaptureActivity;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputPhotoComNumActivity extends Activity {

	private EditText etInputPhotoNum, etInputPhotoYHTM, etInputPhotoXBDS,
			etInputPhotoMQBBH, etInputPhotoHUNANME, etInputPhotoOTEL,
			etInputPhotoADDR;
	private Button btnInputPhotoSubmit, btnScanPhotoNum, btnScanPhotoSeach;
	private ImageButton btnquit;
	private TextView tvTitlebar_name;
	private RadioGroup rgInputPhotoMeterType, rgInputPhotoBaseType;
	private static CustomProgressDialog cpDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_input_photo_com_num);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_onlyback);

		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText("摄像抄表参数设置");

		// 禁止自动弹出输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		etInputPhotoNum = (EditText) findViewById(R.id.etInputPhotoNum);
		etInputPhotoYHTM = (EditText) findViewById(R.id.etInputPhotoYHTM);
		etInputPhotoXBDS = (EditText) findViewById(R.id.etInputPhotoXBDS);
		etInputPhotoMQBBH = (EditText) findViewById(R.id.etInputPhotoMQBBH);
		etInputPhotoHUNANME = (EditText) findViewById(R.id.etInputPhotoHUNANME);
		etInputPhotoOTEL = (EditText) findViewById(R.id.etInputPhotoOTEL);
		etInputPhotoADDR = (EditText) findViewById(R.id.etInputPhotoADDR);
		btnInputPhotoSubmit = (Button) findViewById(R.id.btnInputPhotoSubmit);
		btnquit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
		rgInputPhotoMeterType = (RadioGroup) findViewById(R.id.rgInputPhotoMeterType);
		rgInputPhotoBaseType = (RadioGroup) findViewById(R.id.rgInputPhotoBaseType);
		btnScanPhotoNum = (Button) findViewById(R.id.btnScanPhotoNum);
		btnScanPhotoSeach = (Button) findViewById(R.id.btnScanPhotoSeach);

		addListener();
	}

	private void addListener() {
		btnquit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnInputPhotoSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String meterTypeNo = "7";
				if (rgInputPhotoMeterType.getCheckedRadioButtonId() == R.id.rdoSeven) {
					meterTypeNo = "7";
				} else if (rgInputPhotoMeterType.getCheckedRadioButtonId() == R.id.rdoEight) {
					meterTypeNo = "8";
				}else {
					meterTypeNo = "9";
				}
				String baseType = "1";
				if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType1) {
					baseType = "1";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType2) {
					baseType = "2";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType3) {
					baseType = "3";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType4) {
					baseType = "4";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType5) {
					baseType = "5";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType6) {
					baseType = "6";
				} else if (rgInputPhotoBaseType.getCheckedRadioButtonId() == R.id.rdoBaseType7) {
					baseType = "7";
				}
				Intent intent = new Intent(InputPhotoComNumActivity.this,
						CopyPhotoActivity.class);
				String meterNo = etInputPhotoNum.getText().toString();
				if (meterNo.length() >= 10) {
					if (meterNo.length() > 10) {
						meterNo = meterNo.substring(meterNo.length() - 10);
					}
					intent.putExtra("meterNo", meterNo);
					intent.putExtra("meterTypeNo", meterTypeNo);
					intent.putExtra("baseType", baseType);
					intent.putExtra("YHTM", etInputPhotoYHTM.getText()
							.toString());
					intent.putExtra("XBDS", etInputPhotoXBDS.getText()
							.toString());
					intent.putExtra("MQBBH", etInputPhotoMQBBH.getText()
							.toString());
					intent.putExtra("HUNANME", etInputPhotoHUNANME.getText()
							.toString());
					intent.putExtra("OTEL", etInputPhotoOTEL.getText()
							.toString());
					intent.putExtra("ADDR", etInputPhotoADDR.getText()
							.toString());
					intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
					intent.putExtra("operationType",
							GlobalConsts.COPY_OPERATION_COPY);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "通讯编号必须大于10位!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		btnScanPhotoNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(
						InputPhotoComNumActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});

		btnScanPhotoSeach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cpDialog == null) {
					cpDialog = CustomProgressDialog
							.createDialog(InputPhotoComNumActivity.this);
				}
				if (!cpDialog.isShowing()) {
					cpDialog.show();
				}
				etInputPhotoHUNANME.setText("");
				etInputPhotoOTEL.setText("");
				etInputPhotoADDR.setText("");
				SetBiz setBiz = new SetBiz(InputPhotoComNumActivity.this);
				String url = setBiz.getBookInfoUrl()
						+ "WebMain.asmx/HUcustomerInfo";
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("HUCODE", etInputPhotoMQBBH.getText().toString());
				client.post(url, params, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO 自动生成的方法存根
						Toast.makeText(InputPhotoComNumActivity.this,
								"查询不到此用户！", Toast.LENGTH_SHORT).show();
						if (cpDialog != null && cpDialog.isShowing()) {
							cpDialog.dismiss();
							cpDialog = null;
						}
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO 自动生成的方法存根
						InputStream in = new ByteArrayInputStream(arg2);
						XmlParser parser = new XmlParser();
						try {
							HrCustomerInfo hrCustomerInfo = parser
									.parseHrCustomerInfo(in);
							if (hrCustomerInfo != null) {
								etInputPhotoHUNANME.setText(hrCustomerInfo
										.getHUNAME());
								etInputPhotoOTEL.setText(hrCustomerInfo
										.getOTEL());
								etInputPhotoADDR.setText(hrCustomerInfo
										.getADDR());
							} else {
								Toast.makeText(InputPhotoComNumActivity.this,
										"查询不到此用户！", Toast.LENGTH_SHORT).show();
							}
							if (cpDialog != null && cpDialog.isShowing()) {
								cpDialog.dismiss();
								cpDialog = null;
							}
						} catch (XmlPullParserException | IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}
				});
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etInputPhotoNum.setText(scanResult);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
