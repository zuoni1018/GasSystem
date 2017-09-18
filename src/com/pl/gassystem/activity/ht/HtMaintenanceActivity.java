package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 杭天表具维护
 */

public class HtMaintenanceActivity extends HtBaseTitleActivity {
    @BindView(R.id.layoutOpenValve)
    LinearLayout layoutOpenValve;
    @BindView(R.id.layoutCloseValve)
    LinearLayout layoutCloseValve;
    @BindView(R.id.layoutSeeValveState)
    LinearLayout layoutSeeValveState;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_maintenance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layoutOpenValve, R.id.layoutCloseValve, R.id.layoutSeeValveState})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutOpenValve:
                break;
            case R.id.layoutCloseValve:
                break;
            case R.id.layoutSeeValveState:
                break;
        }
    }
}
