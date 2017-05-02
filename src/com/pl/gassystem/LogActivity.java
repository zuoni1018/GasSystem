package com.pl.gassystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class LogActivity extends Activity {

	private TextView tvTitlebar_name, logInfo;
	private ImageButton btnOnlybackQuit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_log);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_onlyback);

		tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
		tvTitlebar_name.setText("������־");
		logInfo = (TextView) findViewById(R.id.logInfo);
		btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);

		btnOnlybackQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				finish();
			}
		});
		String log =  "2017-04-28 V1.4���£�\n1�����������Զ�����δ������2�εĻ���\n2���Ż�����ͳ�ƺ���ʾ������\n\n" +
				"2016-12-16 V1.3 ���£�\n1�����Ӷ�����͸��ģ��Ƶ��Ȳ���������\n2�����񳭱��������ñ��Ƶ�㹦��\n3�����Ӷ��¼ӳ�֡���񳭱�ģʽ��֧��\n4���޸��ֻ�ʶ��ͼƬ��ʾ���⡢�Ż���֡����\n\n"
				+ "2016-07-24 V1.2 ���£�\n1���ɳ������߱������������\n2���Ϻ�ģʽ�����������Э������\n\n"
				+ "2015-10-30 V1.1 ���£�\n1������ģʽѡ�񡢳�������Զ�������\n2������������˻���¼���ܣ���������\n3��������ֳ���װ��Ϣ�ϴ�����\n\n"
				+ "2015-08-15 V1.0 �������ܣ� \n"
				+ "1��������Ⱥ�������鿴\n2���������ط�����\n3��������Խ��˲ἰ��߰�\n4�����ط������˲ἰ����Ϣ����������\n"
				+ "5���ϴ���������������������������\n6������������ϴ�ʶ���ܣ���������\n7�������ͼƬλ�����µ���";
		logInfo.setText(log);
	}

}
