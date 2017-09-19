package com.pl.gassystem.activity.ht;

import android.content.Intent;
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
 * 在当前界面选择命令类型
 */

public class HtMaintenanceActivity extends HtBaseTitleActivity {
    @BindView(R.id.layoutValveMaintain)
    LinearLayout layoutValveMaintain;
    @BindView(R.id.layoutQueryParameter)
    LinearLayout layoutQueryParameter;
    @BindView(R.id.layoutSetParameter)
    LinearLayout layoutSetParameter;
    @BindView(R.id.layoutChangeBookNoOrCumulant)
    LinearLayout layoutChangeBookNoOrCumulant;
    private String commandType = "";//命令类型

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_maintenance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("表具维护");


    }


    @OnClick({R.id.layoutValveMaintain, R.id.layoutQueryParameter, R.id.layoutChangeBookNoOrCumulant, R.id.layoutSetParameter})
    public void onViewClicked(View view) {
        Intent mIntent;
        switch (view.getId()) {
            case R.id.layoutValveMaintain:
                mIntent = new Intent(getContext(), HtValveMaintainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutQueryParameter:
                mIntent = new Intent(getContext(), HtQueryParameterActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutSetParameter:
                break;

            case R.id.layoutChangeBookNoOrCumulant:
                mIntent = new Intent(getContext(), HtChangeBookNoOrCumulantActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
