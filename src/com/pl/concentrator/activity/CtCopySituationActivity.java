package com.pl.concentrator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 抄表详情界面
 */

public class CtCopySituationActivity extends CtBaseTitleActivity {

    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.ivCopy)
    ImageView ivCopy;
    @BindView(R.id.tvNoCopyNum)
    TextView tvNoCopyNum;
    @BindView(R.id.ivNoCopy)
    ImageView ivNoCopy;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;
    @BindView(R.id.btCopyAllBook)
    Button btCopyAllBook;
    @BindView(R.id.btShowAllBook)
    Button btShowAllBook;
    @BindView(R.id.layoutNetworking)
    LinearLayout layoutNetworking;
    private ProgressDialog progressDialog;
    private Intent mIntent;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_situation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("XX的抄表详情");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("载入中...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        btBeginCopy.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);
    }


    @OnClick({R.id.btBeginCopy, R.id.btCopyAllBook, R.id.btShowAllBook, R.id.layoutNetworking})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBeginCopy:
                mIntent = new Intent(getContext(), CtCopyingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btCopyAllBook:
                mIntent = new Intent(getContext(), CtCopyingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btShowAllBook:

                mIntent = new Intent(getContext(), CtShowBookListActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutNetworking:
                //实抄组网
                mIntent = new Intent(getContext(), CtNetworkingActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}




