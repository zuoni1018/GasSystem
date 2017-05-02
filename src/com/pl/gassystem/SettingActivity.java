package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bll.PreferenceBiz;
import com.pl.common.MyApplication;

/**
 * 系统设置按钮
 */
public class SettingActivity extends Activity {

    private TextView tvTitlebar_name, tvSettingUserName;
    private ImageButton btnOnlybackQuit;
    private PreferenceBiz preferenceBiz;
    private LinearLayout linSettingUser, linSettingBookInfoUrl, linSettingCopyPhotoUrl, linSettingRunMode, linSettingLogOut, linSettingCopyTimeSet, linSettingMJCSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_setting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("系统设置");

        setupView();
        addListener();

    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        tvSettingUserName = (TextView) findViewById(R.id.tvSettingUserName);
        preferenceBiz = new PreferenceBiz(this);
        tvSettingUserName.setText("当前用户：" + preferenceBiz.getUserName());//设置用户名
        linSettingUser = (LinearLayout) findViewById(R.id.linSettingUser);
        linSettingBookInfoUrl = (LinearLayout) findViewById(R.id.linSettingBookInfoUrl);
        linSettingCopyPhotoUrl = (LinearLayout) findViewById(R.id.linSettingCopyPhotoUrl);
        linSettingRunMode = (LinearLayout) findViewById(R.id.linSettingRunMode);
        linSettingLogOut = (LinearLayout) findViewById(R.id.linSettingLogOut);
        linSettingCopyTimeSet = (LinearLayout) findViewById(R.id.linSettingCopyTimeSet);
        linSettingMJCSet = (LinearLayout) findViewById(R.id.linSettingMJCSet);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linSettingUser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 用户名

            }
        });

        linSettingBookInfoUrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SetUpdateActivity.class);
                intent.putExtra("SetType", 1);
                startActivity(intent);
            }
        });

        linSettingCopyPhotoUrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SetUpdateActivity.class);
                intent.putExtra("SetType", 2);
                startActivity(intent);
            }
        });

        linSettingRunMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SetRunModeActivity.class);
                startActivity(intent);
            }
        });

        linSettingCopyTimeSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, SetCopyTimeActivity.class);
                startActivity(intent);
            }
        });

        linSettingMJCSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CopySetMjcActivity.class);
                startActivity(intent);
            }
        });

        linSettingLogOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PreferenceBiz preferenceBiz = new PreferenceBiz(SettingActivity.this);
                preferenceBiz.remove();
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, LoginActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                startActivity(intent);
                MyApplication.getInstance().exit();
            }
        });

    }

}
