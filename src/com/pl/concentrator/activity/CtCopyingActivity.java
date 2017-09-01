package com.pl.concentrator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.gassystem.R;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 */

public class CtCopyingActivity extends CtBaseTitleActivity {
    private AlertDialog alertDialog;
    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copying;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("蓝牙抄表");

    }

    @Override
    protected void finishActivity() {
        if(alertDialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("是否退出蓝牙操作界面?");
            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog=builder.create();
        }
        alertDialog.show();
//        super.finishActivity();
    }
}
