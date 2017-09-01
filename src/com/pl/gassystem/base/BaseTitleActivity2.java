package com.pl.gassystem.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.gassystem.R;

/**
 * Created by zangyi_shuai_ge on 2017/8/29
 */

public abstract class BaseTitleActivity2 extends Activity {


    private TextView tvTitle;//����
    private LinearLayout layoutBack;//���ذ�ť
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        mContext = this;
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton(v);
            }
        });
    }

    //���ñ���
    protected void setTitle(String title) {
        tvTitle.setText(title);
    }

    //���ذ�ť�ĵ���¼� ����������д
    protected void backButton(View v) {
        finish();
    }

    protected abstract int setLayout();

    public Context getContext() {
        return mContext;
    }
}
