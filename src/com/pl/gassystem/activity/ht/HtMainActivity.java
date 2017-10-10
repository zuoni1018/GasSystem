package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtMainActivity extends HtBaseTitleActivity {
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;

    private Intent mIntent;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("º¼È¼Ö÷½çÃæ");
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                mIntent=new Intent(getContext(),HtGetBookInfoActivity.class);
                startActivity(mIntent);
                break;
            case R.id.bt2:
                mIntent=new Intent(getContext(),HtGetAreaInfoActivity.class);
                startActivity(mIntent);
                break;
            case R.id.bt3:
                break;
        }
    }
}
