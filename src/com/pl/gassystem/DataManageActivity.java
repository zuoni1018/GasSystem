package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bll.BookInfoBiz;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.utils.GlobalConsts;

public class DataManageActivity extends Activity {

    private TextView tvTitlebar_name;
    private ImageButton btnOnlybackQuit;
    private LinearLayout linDataDownload, linDataUpdate, linDataDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_data_manage);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("数据管理");

        setupView();
        addListener();
    }

    private void setupView() {
        // TODO 自动生成的方法存根
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        linDataDownload = (LinearLayout) findViewById(R.id.linDataDownload);
        linDataUpdate = (LinearLayout) findViewById(R.id.linDataUpdate);
        linDataDelete = (LinearLayout) findViewById(R.id.linDataDelete);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linDataDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 数据下载
                Intent intent = new Intent(DataManageActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                startActivity(intent);
            }
        });

        linDataUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 数据上传
                Intent intent = new Intent(DataManageActivity.this, BookInfoActivity.class);
                intent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_UPLOAD);
                startActivity(intent);
            }
        });

        linDataDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 数据删除
                new AlertDialog.Builder(DataManageActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog)
                        .setTitle("确认清除")
                        .setMessage("本操作会清除本机所有账册信息和抄表记录，是否确认执行？")
                        .setCancelable(false)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        BookInfoBiz bookInfoBiz = new BookInfoBiz(DataManageActivity.this);
                                        GroupInfoBiz groupInfoBiz = new GroupInfoBiz(DataManageActivity.this);
                                        GroupBindBiz groupBindBiz = new GroupBindBiz(DataManageActivity.this);
                                        CopyBiz copyBiz = new CopyBiz(DataManageActivity.this);
                                        copyBiz.removeCopyDataAll();
                                        copyBiz.removeCopyDataICRFAll();
                                        copyBiz.removeCopyDataPhotoAll();
                                        groupBindBiz.removeGroupBindAll();
                                        groupInfoBiz.removeGroupInfoAll();
                                        bookInfoBiz.removeBookInfoAll();
                                        new AlertDialog.Builder(
                                                DataManageActivity.this,
                                                android.R.style.Theme_DeviceDefault_Light_Dialog)
                                                .setTitle("成功")
                                                .setMessage("清理完成！")
                                                .setCancelable(false)
                                                .setPositiveButton(
                                                        "确定",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {
                                                            }
                                                        }).show();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
            }
        });
    }

}
