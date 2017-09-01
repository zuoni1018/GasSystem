package com.pl.concentrator.activity.base;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * Activity ����
 */

public abstract class CtBaseTitleActivity extends CtBaseActivity {

    private TextView tvTitle;//����
    private LinearLayout layoutBack;//���ذ�ť


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }
    //���ñ���
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    //���ذ�ť�ĵ���¼� ����������д
    protected void finishActivity() {
        finish();
    }

    protected abstract int setLayout();

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishActivity();
    }
}
