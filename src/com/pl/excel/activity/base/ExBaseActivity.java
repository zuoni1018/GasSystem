package com.pl.excel.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.common.utils.ToastUtils;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * Excel����Activity
 */

public class ExBaseActivity extends Activity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        ExActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ExActivityCollector.removeActivity(this);
        super.onDestroy();
    }
    public Context getContext(){
        return context;
    }
    public void  showToast(String message){
        ToastUtils.showToast(context.getApplicationContext(),message);
//        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
