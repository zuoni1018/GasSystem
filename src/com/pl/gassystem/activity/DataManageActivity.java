package com.pl.gassystem.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.pl.bll.BookInfoBiz;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.gassystem.BookInfoActivity;
import com.pl.gassystem.ImportExcelActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.userinfo.XiNingUserInfoImportActivity;
import com.pl.utils.GlobalConsts;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 数据传输界面
 */
public class DataManageActivity extends BaseTitleActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("数据管理");

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_data_manage;
    }


    @OnClick({R.id.linDataDownload, R.id.linDataUpdate, R.id.linDataDelete,
            R.id.layoutExcelBooksImport, R.id.layoutExcelXiNingUserInfoImport})
    public void onViewClicked(View view) {
        Intent mIntent;
        switch (view.getId()) {
            case R.id.linDataDownload:
                mIntent = new Intent(DataManageActivity.this, BookInfoActivity.class);
                mIntent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_DOWNLOAD);
                startActivity(mIntent);
                break;
            case R.id.linDataUpdate:
                mIntent = new Intent(DataManageActivity.this, BookInfoActivity.class);
                mIntent.putExtra("bookInfoType", GlobalConsts.BOOKINFO_TYPE_UPLOAD);
                startActivity(mIntent);
                break;
            case R.id.linDataDelete:
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
                break;
            case R.id.layoutExcelBooksImport:
                go2Activity(ImportExcelActivity.class);
                break;
            case R.id.layoutExcelXiNingUserInfoImport:
                go2Activity( XiNingUserInfoImportActivity.class);
                break;
        }
    }
}
