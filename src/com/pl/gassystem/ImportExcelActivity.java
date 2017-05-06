package com.pl.gassystem;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.gassystem.base.BaseTitleActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by zangyi_shuai_ge on 2017/5/6
 */


public class ImportExcelActivity extends BaseTitleActivity {
    private Button btChoose;
    private Button btImport;
    private TextView tvFilePath;
    private String filePath = "";

    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ImportExcelActivity.this);
            mProgressDialog.dismiss();
            switch (msg.what) {

                case EXCEL_ERROR:

                    dialog.setTitle("EXCEL 导出失败");
                    dialog.show();
                    break;
                case EXCEL_OK:

                    dialog.setTitle("EXCEL 导出完成");
                    dialog.show();


//                    Intent intent = new Intent("android.intent.action.VIEW");
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Uri uri = Uri.fromFile(new File(filePath));
//                    intent.setDataAndType(uri, "application/vnd.ms-excel");
//                    startActivity(intent);

                    break;
                case EXCEL_PROGRESS:

                    break;
            }
        }


    };

    @Override
    protected void initData() {

    }

    @Override
    protected void initOnClickListener() {
        btChoose.setOnClickListener(this);
        btImport.setOnClickListener(this);
    }

    @Override
    protected void initUI() {
        initDialog();
        btChoose = (Button) findViewById(R.id.btChoose);
        btImport = (Button) findViewById(R.id.btImport);
        tvFilePath = (TextView) findViewById(R.id.tvFilePath);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_import_excel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btChoose:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("xls/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;

            case R.id.btImport:
                String mFilePath = filePath.substring(filePath.length() - 4, filePath.length());
                if (mFilePath.equals(".xls")) {
                    //开启线程解析
                    mProgressDialog.setMessage("正在从Excel导入数据库请不要退出App、或其他操作");
                    mProgressDialog.show();
                    ImportExcelThread t = new ImportExcelThread();
                    t.start();
                } else {
                    Toast.makeText(this, "您选择的不是 .xls 文件请重新选择", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = uri.getPath();
                tvFilePath.setText(filePath);
            }
        }
    }

    private class ImportExcelThread extends Thread {
        @Override
        public void run() {
//            super.run();
            File file = new File(filePath);
            if (file.exists()) {
                readExcel();
            } else {
                Toast.makeText(ImportExcelActivity.this, "选取文件不存在，请重新选择", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private ProgressDialog mProgressDialog;

    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("还车中请稍后...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    public void readExcel() {
        try {

            InputStream is = new FileInputStream(filePath);//从文件获得输入流
            Workbook book = Workbook.getWorkbook(new File(filePath));//获得excel
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);

           //获得工作表里面的数据
            int Rows = sheet.getRows();//获得总行数
            int Cols = sheet.getColumns();//获得总列数


            // 得到第一列第一行的单元格
            Cell cell1 = sheet.getCell(0, 0);
            String result = cell1.getContents();
//////////////////////////////////////////////////////////////////////////////


            book.close();
            Message message = Message.obtain();
            message.what = EXCEL_OK;
            mHandler.sendMessage(message);
        } catch (Exception e) {
            Message message = Message.obtain();
            message.what = EXCEL_ERROR;
            mHandler.sendMessage(message);
        }
    }

}
