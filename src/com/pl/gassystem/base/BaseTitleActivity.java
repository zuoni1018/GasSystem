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
 * ���б���ͷ��ذ�ť�Ľ���
 */

public abstract class BaseTitleActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvTitle;//����
    private LinearLayout layoutBack;//���ذ�ť

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        //���ñ���ͷ��ؼ�
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton(v);

            }
        });
        initUI();//��ʼ������
        initData();//��ʼ������
        initOnClickListener();//���ü����¼�

    }

    //��ʼ������
    protected abstract void initData();

    //���ذ�ť�ĵ���¼� ����������д
    protected void backButton(View v) {
        finish();
    }

    //���ñ���
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    protected abstract void initOnClickListener();

    protected abstract void initUI();

    protected abstract int setLayout();
}

