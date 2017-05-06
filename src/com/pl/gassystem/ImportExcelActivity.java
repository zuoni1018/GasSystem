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

                    dialog.setTitle("EXCEL ����ʧ��");
                    dialog.show();
                    break;
                case EXCEL_OK:

                    dialog.setTitle("EXCEL �������");
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
                intent.setType("xls/*");//�������ͣ����������������ͣ������׺�Ŀ�������д��
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;

            case R.id.btImport:
                String mFilePath = filePath.substring(filePath.length() - 4, filePath.length());
                if (mFilePath.equals(".xls")) {
                    //�����߳̽���
                    mProgressDialog.setMessage("���ڴ�Excel�������ݿ��벻Ҫ�˳�App������������");
                    mProgressDialog.show();
                    ImportExcelThread t = new ImportExcelThread();
                    t.start();
                } else {
                    Toast.makeText(this, "��ѡ��Ĳ��� .xls �ļ�������ѡ��", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ImportExcelActivity.this, "ѡȡ�ļ������ڣ�������ѡ��", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private ProgressDialog mProgressDialog;

    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("���������Ժ�...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    public void readExcel() {
        try {

            InputStream is = new FileInputStream(filePath);//���ļ����������
            Workbook book = Workbook.getWorkbook(new File(filePath));//���excel
            book.getNumberOfSheets();
            // ��õ�һ�����������
            Sheet sheet = book.getSheet(0);

           //��ù��������������
            int Rows = sheet.getRows();//���������
            int Cols = sheet.getColumns();//���������


            // �õ���һ�е�һ�еĵ�Ԫ��
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
