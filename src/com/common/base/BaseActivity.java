package com.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * Activity ????
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass() + "", "====onCreate====");
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(this.getClass() + "", "====onRestart====");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(this.getClass() + "", "====onStart====");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(this.getClass() + "", "====onResume====");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(this.getClass() + "", "====onPause====");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(this.getClass() + "", "====onStop====");
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        Log.i(this.getClass() + "", "====onDestroy====");
        super.onDestroy();

    }

}
