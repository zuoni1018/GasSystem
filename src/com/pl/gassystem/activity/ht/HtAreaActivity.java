package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pl.gassystem.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtAreaActivity extends HtBaseTitleActivity {

    private Intent mIntent;

    private String AreaNo="";

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_area;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("Ð¡Çø²Ù×÷");

        AreaNo=getIntent().getStringExtra("AreaNo");
    }
    @OnClick({ R.id.bt2, R.id.bt3,R.id.bt4,R.id.bt5})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt2:
                mIntent = new Intent(getContext(), HtGetUpdateMeterInfoActivity.class);
                mIntent.putExtra("AreaNo",AreaNo);
                startActivity(mIntent);
                break;
            case R.id.bt3:
                mIntent = new Intent(getContext(),  HtGetReadMeterInfoActivity.class);
                mIntent.putExtra("AreaNo", AreaNo);
                mIntent.putExtra("ReadState","1");
                startActivity(mIntent);
                break;
            case R.id.bt4:
                mIntent = new Intent(getContext(),  HtGetReadMeterInfoActivity.class);
                mIntent.putExtra("AreaNo", AreaNo);
                mIntent.putExtra("ReadState","0");
                startActivity(mIntent);
                break;
            case R.id.bt5:
                mIntent = new Intent(getContext(),  HtGetReadMeterInfoActivity.class);
                mIntent.putExtra("AreaNo", AreaNo);
                mIntent.putExtra("ReadState","");
                startActivity(mIntent);
                break;
        }
    }

}
