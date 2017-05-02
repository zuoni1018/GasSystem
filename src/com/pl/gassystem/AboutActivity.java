package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * 关于界面
 */
public class AboutActivity extends Activity {

    private TextView tvTitlebar_name, tvVersion, tvLog;
    private ImageButton btnOnlybackQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_about);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("关于本软件");
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText("内部测试版：" + getVersion());
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        tvLog = (TextView) findViewById(R.id.tvLog);

        btnOnlybackQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvLog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
