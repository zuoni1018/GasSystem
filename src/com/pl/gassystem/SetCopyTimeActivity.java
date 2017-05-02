package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pl.bll.SetBiz;

/**
 * 抄表参数设置
 */
public class SetCopyTimeActivity extends Activity {

    private TextView tvTitlebar_name;
    private ImageButton btnOnlybackQuit;
    private EditText etSetCopyTimeWakeupTime, etSetCopyTimeCopyWait, etSetCopyTimeIntervalTime, etSetCopyTimeRepeatCount;
    private Button btnSetCopyTimeSubmit, btnSetCopyTimeCancel;
    private SetBiz setBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_set_copy_time);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("系统设置");
        setupView();
        setBiz = new SetBiz(this);
        etSetCopyTimeIntervalTime.setText(setBiz.getIntervalTime() + "");
        etSetCopyTimeWakeupTime.setText(setBiz.getWakeupTime());
        etSetCopyTimeCopyWait.setText(setBiz.getCopyWait() + "");
        etSetCopyTimeRepeatCount.setText(setBiz.getRepeatCount() + "");
        addListener();
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetCopyTimeCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetCopyTimeSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setBiz.updateCopyTimeSet(Integer.parseInt(etSetCopyTimeIntervalTime.getText().toString()), etSetCopyTimeWakeupTime.getText().toString(), Integer.parseInt(etSetCopyTimeCopyWait.getText().toString()), Integer.parseInt(etSetCopyTimeRepeatCount.getText().toString()));
                new AlertDialog.Builder(SetCopyTimeActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog)
                        .setTitle("成功")
                        .setMessage("修改成功")
                        .setCancelable(false)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        finish();
                                    }
                                }).show();
            }
        });
    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        etSetCopyTimeWakeupTime = (EditText) findViewById(R.id.etSetCopyTimeWakeupTime);
        etSetCopyTimeCopyWait = (EditText) findViewById(R.id.etSetCopyTimeCopyWait);
        etSetCopyTimeIntervalTime = (EditText) findViewById(R.id.etSetCopyTimeIntervalTime);
        btnSetCopyTimeSubmit = (Button) findViewById(R.id.btnSetCopyTimeSubmit);
        btnSetCopyTimeCancel = (Button) findViewById(R.id.btnSetCopyTimeCancel);
        etSetCopyTimeRepeatCount = (EditText) findViewById(R.id.etSetCopyTimeRepeatCount);
    }

}
