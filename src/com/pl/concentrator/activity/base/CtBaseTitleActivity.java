package com.pl.concentrator.activity.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bll.SetBiz;
import com.pl.gassystem.R;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * Activity ����
 */

public abstract class CtBaseTitleActivity extends CtBaseActivity {

    private TextView tvTitle;//����
    private LinearLayout layoutBack;//���ذ�ť


    private  Animation anim_btn_begin;
    private  Animation anim_btn_end;

    public SetBiz setBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(setLayout());
        setBiz=new SetBiz(getContext());
        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        setViewAnimation(layoutBack);
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

    public void setViewAnimation( View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.startAnimation(anim_btn_end);
                }
                return false;
            }
        });
    }
    public void  setTitleOnClickListener(View.OnClickListener onClickListener){
        tvTitle.setOnClickListener(onClickListener);
    }

}
