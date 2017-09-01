package com.pl.concentrator.activity;

import android.os.Bundle;

import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.gassystem.R;

import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class CtCopyDataBookDetailActivity extends CtBaseTitleActivity {
    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copydata_book_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("表具详情信息");

    }
}
