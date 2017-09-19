package com.pl.gassystem.userinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.gassystem.utils.SPUtils;
import com.pl.bean.UserInfo;
import com.pl.dal.userinfo.UserInfoDao;
import com.pl.gassystem.R;
import com.pl.gassystem.base.BaseTitleActivity2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import static com.pl.gassystem.ImportExcelActivity.getRealFilePath;

/**
 * Created by zangyi_shuai_ge on 2017/8/29
 * 西宁个人信息导入
 */

public class XiNingUserInfoImportActivity extends BaseTitleActivity2 {

    //导入账册的时候同时需要导入用户信息
    private Button btChoose;
    private Button btImport;
    private TextView tvFilePath;
    private String filePath = "";

    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private final int EXCEL_FALSE = 4;
    private final int EXCEL_START = 5;
    private final int EXCEL_IMPORT = 6;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXCEL_ERROR:
                    progressDialog.dismiss();
                    alertDialog.setMessage("EXCEL 导出失败，请检查Excel格式是否正确或重新再试");
                    alertDialog.show();
                    break;
                case EXCEL_OK:
                    progressDialog.dismiss();
                    alertDialog.setMessage("EXCEL 导出完成");
                    alertDialog.show();
                    break;
                case EXCEL_PROGRESS:
                    break;
                case EXCEL_FALSE:
                    progressDialog.dismiss();
                    alertDialog.setMessage("选取文件不存在，请重新选择");
                    alertDialog.show();
                    break;
                case EXCEL_START:
                    progressDialog.setMessage("正在导入Excel,请勿退出...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("西宁用户信息导入");
        progressDialog = new ProgressDialog(XiNingUserInfoImportActivity.this);
        btChoose = (Button) findViewById(R.id.btChoose);
        btImport = (Button) findViewById(R.id.btImport);
        tvFilePath = (TextView) findViewById(R.id.tvFilePath);
        tvFilePath.setText((String) SPUtils.get(XiNingUserInfoImportActivity.this, "filePath", ""));
        filePath = (String) SPUtils.get(XiNingUserInfoImportActivity.this, "filePath", "");
        //选择文件
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        //Excel导入数据库
        btImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath.trim().equals("")) {
                    Toast.makeText(XiNingUserInfoImportActivity.this, "请先选择文件路径", Toast.LENGTH_SHORT).show();
                } else {
                    String mFilePath = filePath.substring(filePath.length() - 4, filePath.length());
                    if (mFilePath.equals(".xls")) {
                        //开启一个线程开始导入Excel
                        ImportExcelThread importExcelThread = new ImportExcelThread();
                        importExcelThread.start();
                    } else {
                        Toast.makeText(XiNingUserInfoImportActivity.this, "您选择的不是 .xls 文件请重新选择", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(XiNingUserInfoImportActivity.this);
        builder.setTitle("提示");
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

    }


    private class ImportExcelThread extends Thread {
        @Override
        public void run() {
            File file = new File(filePath);
            if (file.exists()) {
                readExcel();
            } else {
                Message message = Message.obtain();
                message.what = EXCEL_FALSE;
                mHandler.sendMessage(message);
            }
        }
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_xi_ning_user_info_import;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = uri.getPath();
                filePath = getRealFilePath(XiNingUserInfoImportActivity.this, uri);
//                filePath=filePath.replaceAll(":","/");
                tvFilePath.setText(filePath);
                SPUtils.put(XiNingUserInfoImportActivity.this, "filePath", filePath);
            }
        }
    }

    public void readExcel() {
        //开始
        Message message = Message.obtain();
        message.what = EXCEL_START;
        mHandler.sendMessage(message);

        try {
            InputStream is = new FileInputStream(filePath);//从文件获得输入流
            File file = new File(filePath);
            Workbook book = Workbook.getWorkbook(file);//获得excel
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();//获得总行数
            int Cols = sheet.getColumns();//获得总列数
            UserInfoDao userInfoDao=new UserInfoDao(XiNingUserInfoImportActivity.this);
            for (int i = 1; i < Rows; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setTableNumber(getCellDate(sheet, 2, i).trim() + "");//表具编号
                userInfo.setUserNum(getCellDate(sheet, 0, i).trim());//用户编号
                userInfo.setXiNingTableNumber(getCellDate(sheet, 1, i).trim());//西宁表具编号
                userInfo.setUserName(getCellDate(sheet, 6, i).trim());//用户名称
                userInfo.setAddress(getCellDate(sheet, 5, i).trim());//房间地址
                userInfo.setXiNingUnitPrice(getCellDate(sheet,10,i).trim());//当前单价
                userInfoDao.putUserInfo(userInfo);
            }
            //抄表结束
            message=Message.obtain();
            message.what=EXCEL_OK;
            mHandler.sendMessage(message);

        } catch (Exception e) {
            message = Message.obtain();
            message.what = EXCEL_ERROR;
            mHandler.sendMessage(message);
        }
    }

    private String getCellDate(Sheet sheet, int i, int j) {
        Cell cell1 = sheet.getCell(i, j);
        return cell1.getContents();
    }

}
