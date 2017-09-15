package com.pl.gassystem.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于界面
 */
public class AboutActivity extends BaseTitleActivity {
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("关于本软件");
        tvVersion.setText("内部测试版：" + getVersion());
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

    @Override
    protected int setLayout() {
        return R.layout.activity_about;
    }

    @OnClick(R.id.tvLog)
    public void onViewClicked() {
        go2Activity(LogActivity.class);
    }
}
