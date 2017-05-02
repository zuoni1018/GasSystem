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

public class SetUpdateActivity extends Activity {

    private TextView tvTitlebar_name;
    private EditText etSetUpdate1;
    private ImageButton btnOnlybackQuit;
    private Button btnSetUpdateSubmit, btnSetUpdateCancel;
    private SetBiz setBiz;
    private static int SetType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_set_update);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        SetType = getIntent().getIntExtra("SetType", 1);
        setupView();
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        setBiz = new SetBiz(this);
        if (SetType == 1) {
            tvTitlebar_name.setText("数据上传下载地址");
            etSetUpdate1.setText(setBiz.getBookInfoUrl());
        } else if (SetType == 2) {
            tvTitlebar_name.setText("摄像图片识别地址");
            etSetUpdate1.setText(setBiz.getCopyPhotoUrl());
        }

        addListener();
    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        etSetUpdate1 = (EditText) findViewById(R.id.etSetUpdate1);
        btnSetUpdateSubmit = (Button) findViewById(R.id.btnSetUpdateSubmit);
        btnSetUpdateCancel = (Button) findViewById(R.id.btnSetUpdateCancel);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetUpdateCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSetUpdateSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SetType == 1) {
                    setBiz.updateBookInfoUrl(etSetUpdate1.getText().toString());
                } else if (SetType == 2) {
                    setBiz.updateCopyPhotoUrl(etSetUpdate1.getText().toString());
                }
                new AlertDialog.Builder(SetUpdateActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog)
                        .setTitle("成功")
                        .setMessage("修改成功")
                        .setCancelable(false)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                }).show();
            }
        });
    }

}
