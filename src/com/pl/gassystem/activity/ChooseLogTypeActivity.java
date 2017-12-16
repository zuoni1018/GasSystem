package com.pl.gassystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pl.gassystem.InputComNumActivity;
import com.pl.gassystem.InputPhotoComNumActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.activity.ht.HtSingleCopyTestActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/11/6
 * 单抄测试界面
 */

public class ChooseLogTypeActivity extends BaseTitleActivity {
    @BindView(R.id.bt01)
    Button bt01;
    @BindView(R.id.bt02)
    Button bt02;
    @BindView(R.id.bt03)
    Button bt03;

    private int tag = 0;
    public static final int TAG_SEE_LOG = 1;
    public static final int TAG_COPY_TEST = 2;


    @Override
    protected int setLayout() {
        return R.layout.ht_activity_choose_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tag = getIntent().getIntExtra("tag", 0);
        if (tag == 1) {
            setTitle("日志查看");
        } else if (tag == 2) {
            setTitle("单抄测试");
        }


    }

    private Intent mIntent;
    @OnClick({R.id.bt01, R.id.bt02, R.id.bt03})
    public void onViewClicked(View view) {
        switch (tag){
            case TAG_SEE_LOG:
                //日志查看
                switch (view.getId()) {
                    case R.id.bt01:
                        //无线表
                        mIntent = new Intent(getContext(), LogsActivity.class);
                        mIntent.putExtra("logType", "WX");
                        startActivity(mIntent);
                        break;
                    case R.id.bt02:
                        //扩频表
                        mIntent = new Intent(getContext(), LogsActivity.class);
                        mIntent.putExtra("logType", "KP");
                        startActivity(mIntent);
                        break;
                    case R.id.bt03:
                        mIntent = new Intent(getContext(), LogsActivity.class);
                        mIntent.putExtra("logType", "SX");
                        startActivity(mIntent);
                        break;
                }
                break;
            case TAG_COPY_TEST:
                //单抄测试
                switch (view.getId()) {
                    case R.id.bt01:
                        //无线表
                        mIntent = new Intent(getContext(), InputComNumActivity.class);
                        mIntent.putExtra("operationType", 1);
                        startActivity(mIntent);
                        break;
                    case R.id.bt02:
                        //扩频表
                        mIntent = new Intent(getContext(), HtSingleCopyTestActivity.class);
                        startActivity(mIntent);
                        break;
                    case R.id.bt03:
                        //摄像表
                        mIntent = new Intent(getContext(), InputPhotoComNumActivity.class);
                        startActivity(mIntent);
                        break;
                }
                break;
        }
    }
}
