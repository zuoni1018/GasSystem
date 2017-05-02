package com.pl.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.pl.gassystem.R;

public class FullScreen extends Activity {
	private EditText editTextFullScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreen);
		/* ��ȡIntent�е�Bundle���� */
		editTextFullScreen = (EditText) findViewById(R.id.editText_full);
		Bundle newBundle = getIntent().getExtras();
		if (newBundle != null) {
			String returnString = (String) newBundle
					.get(BluetoothChat.BluetoothData);
			if (returnString != null) {
				editTextFullScreen.setText(returnString);
			}
		}
	}

	public void onMyButtonClick(View view) {

		if (view.getId() == R.id.button_return) {
			finish();
		}
	}
}
