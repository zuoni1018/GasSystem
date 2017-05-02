package com.pl.gassystem.base;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseActivity;
import com.pl.gassystem.R;

/**
 * Created by zangyi_shuai_ge on 2017/4/25
 * 带有标题和返回按钮的界面
 */

public abstract class BaseTitleActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvTitle;//标题
    private LinearLayout layoutBack;//返回按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        //设置标题和返回键
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton(v);

            }
        });
        initUI();//初始化界面
        initData();//初始化数据
        initOnClickListener();//设置监听事件

    }

    //初始化数据
    protected abstract void initData();

    //返回按钮的点击事件 方法可以重写
    protected void backButton(View v) {
        finish();
    }

    //设置标题
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    protected abstract void initOnClickListener();

    protected abstract void initUI();

    protected abstract int setLayout();
}

