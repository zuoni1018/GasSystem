package com.pl.gassystem.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.zuoni.zuoni_common.utils.common.ToastUtils;


/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * 抄表App 重构基类
 */

public class BaseActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    public Context getContext() {
        return context;
    }

    public void showToast(String message) {
        ToastUtils.showToast(context.getApplicationContext(), message);
    }
}
