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
 * ����������Ϣ����
 */

public class XiNingUserInfoImportActivity extends BaseTitleActivity2 {

    //�����˲��ʱ��ͬʱ��Ҫ�����û���Ϣ
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
                    alertDialog.setMessage("EXCEL ����ʧ�ܣ�����Excel��ʽ�Ƿ���ȷ����������");
                    alertDialog.show();
                    break;
                case EXCEL_OK:
                    progressDialog.dismiss();
                    alertDialog.setMessage("EXCEL �������");
                    alertDialog.show();
                    break;
                case EXCEL_PROGRESS:
                    break;
                case EXCEL_FALSE:
                    progressDialog.dismiss();
                    alertDialog.setMessage("ѡȡ�ļ������ڣ�������ѡ��");
                    alertDialog.show();
                    break;
                case EXCEL_START:
                    progressDialog.setMessage("���ڵ���Excel,�����˳�...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("�����û���Ϣ����");
        progressDialog = new ProgressDialog(XiNingUserInfoImportActivity.this);
        btChoose = (Button) findViewById(R.id.btChoose);
        btImport = (Button) findViewById(R.id.btImport);
        tvFilePath = (TextView) findViewById(R.id.tvFilePath);
        tvFilePath.setText((String) SPUtils.get(XiNingUserInfoImportActivity.this, "filePath", ""));
        filePath = (String) SPUtils.get(XiNingUserInfoImportActivity.this, "filePath", "");
        //ѡ���ļ�
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//�������ͣ����������������ͣ������׺�Ŀ�������д��
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        //Excel�������ݿ�
        btImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath.trim().equals("")) {
                    Toast.makeText(XiNingUserInfoImportActivity.this, "����ѡ���ļ�·��", Toast.LENGTH_SHORT).show();
                } else {
                    String mFilePath = filePath.substring(filePath.length() - 4, filePath.length());
                    if (mFilePath.equals(".xls")) {
                        //����һ���߳̿�ʼ����Excel
                        ImportExcelThread importExcelThread = new ImportExcelThread();
                        importExcelThread.start();
                    } else {
                        Toast.makeText(XiNingUserInfoImportActivity.this, "��ѡ��Ĳ��� .xls �ļ�������ѡ��", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(XiNingUserInfoImportActivity.this);
        builder.setTitle("��ʾ");
        builder.setPositiveButton("֪����", new DialogInterface.OnClickListener() {
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
        //��ʼ
        Message message = Message.obtain();
        message.what = EXCEL_START;
        mHandler.sendMessage(message);

        try {
            InputStream is = new FileInputStream(filePath);//���ļ����������
            File file = new File(filePath);
            Workbook book = Workbook.getWorkbook(file);//���excel
            book.getNumberOfSheets();
            // ��õ�һ�����������
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();//���������
            int Cols = sheet.getColumns();//���������
            UserInfoDao userInfoDao=new UserInfoDao(XiNingUserInfoImportActivity.this);
            for (int i = 1; i < Rows; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setTableNumber(getCellDate(sheet, 2, i).trim() + "");//��߱��
                userInfo.setUserNum(getCellDate(sheet, 0, i).trim());//�û����
                userInfo.setXiNingTableNumber(getCellDate(sheet, 1, i).trim());//������߱��
                userInfo.setUserName(getCellDate(sheet, 6, i).trim());//�û�����
                userInfo.setAddress(getCellDate(sheet, 5, i).trim());//�����ַ
                userInfo.setXiNingUnitPrice(getCellDate(sheet,10,i).trim());//��ǰ����
                userInfoDao.putUserInfo(userInfo);
            }
            //�������
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
