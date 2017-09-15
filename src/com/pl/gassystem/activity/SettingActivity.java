package com.pl.gassystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pl.bll.PreferenceBiz;
import com.pl.common.MyApplication;
import com.pl.gassystem.CopySetMjcActivity;
import com.pl.gassystem.LoginActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.SetCopyTimeActivity;
import com.pl.gassystem.SetRunModeActivity;
import com.pl.gassystem.SetUpdateActivity;
import com.pl.gassystem.activity.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 系统设置按钮
 */
public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.tvSettingUserName)
    TextView tvSettingUserName;

    private Intent mIntent;

    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //设置用户名
        PreferenceBiz  preferenceBiz = new PreferenceBiz(this);
        tvSettingUserName.setText("当前用户:" + preferenceBiz.getUserName());
        setTitle("系统设置");

    }


    @OnClick({R.id.linSettingBookInfoUrl, R.id.linSettingCopyPhotoUrl, R.id.linSettingRunMode
            , R.id.linSettingCopyTimeSet, R.id.linSettingMJCSet, R.id.linSettingLogOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.linSettingBookInfoUrl:
                mIntent = new Intent(SettingActivity.this, SetUpdateActivity.class);
                mIntent.putExtra("SetType", 1);
                startActivity(mIntent);
                break;

            case R.id.linSettingCopyPhotoUrl:
                mIntent = new Intent(SettingActivity.this, SetUpdateActivity.class);
                mIntent.putExtra("SetType", 2);
                startActivity(mIntent);
                break;

            case R.id.linSettingRunMode:
                go2Activity(SetRunModeActivity.class);
                break;

            case R.id.linSettingCopyTimeSet:
                go2Activity(SetCopyTimeActivity.class);
                break;

            case R.id.linSettingMJCSet:
                go2Activity(CopySetMjcActivity.class);
                break;

            case R.id.linSettingLogOut:
                PreferenceBiz preferenceBiz = new PreferenceBiz(SettingActivity.this);
                preferenceBiz.remove();
                mIntent = new Intent();
                mIntent.setClass(SettingActivity.this, LoginActivity.class);
                startActivity(mIntent);
                MyApplication.getInstance().exit();
                break;
        }
    }
}
