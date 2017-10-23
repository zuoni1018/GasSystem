package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.widget.TextView;

import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/10/19
 */

public class HtMessageActivity extends HtBaseTitleActivity {
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @Override
    protected int setLayout() {
        return R.layout.test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String message=getIntent().getStringExtra("message");
        tvMessage.setText(message);
    }
}
