package com.pl.gassystem;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.SPUtils;
import com.pl.bll.BookInfoBiz;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.BookInfo;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupBind;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.base.BaseTitleActivity;
import com.pl.utils.StringFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

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
    private boolean isRun = false;
    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;

    private RadioGroup mRadioGroup;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ImportExcelActivity.this);
            mProgressDialog.dismiss();
            switch (msg.what) {

                case EXCEL_ERROR:

                    dialog.setTitle("EXCEL 导出失败，请检查Excel格式是否正确或重新再试");
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
    private BookInfoBiz bookInfoBiz;
    private String bookNo;//账册编号
    private GroupInfoBiz groupInfoBiz;

    private GroupBindBiz groupBindBiz;
    private CopyBiz copyBiz;

    private String groupNo;

    private int lastColumn;

    private boolean isIC = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isRun = true;
        isIC = true;

    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();
    }

    @Override
    protected void initData() {
        bookInfoBiz = new BookInfoBiz(this);
        groupInfoBiz = new GroupInfoBiz(this);
        groupBindBiz = new GroupBindBiz(this);
        copyBiz = new CopyBiz(this);
    }

    @Override
    protected void initOnClickListener() {
        btChoose.setOnClickListener(this);
        btImport.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb01) {
                    isIC = true;
                } else if (checkedId == R.id.rb02) {
                    isIC = false;
                }

            }
        });
    }

    @Override
    protected void initUI() {
        initDialog();
        btChoose = (Button) findViewById(R.id.btChoose);
        btImport = (Button) findViewById(R.id.btImport);
        tvFilePath = (TextView) findViewById(R.id.tvFilePath);
        tvFilePath.setText((String) SPUtils.get(ImportExcelActivity.this, "filePath", ""));
        filePath = (String) SPUtils.get(ImportExcelActivity.this, "filePath", "");
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
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
                SPUtils.put(ImportExcelActivity.this, "filePath", filePath);
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

    private String meterType = "04";

    public void readExcel() {
        try {


            InputStream is = new FileInputStream(filePath);//从文件获得输入流
            File file = new File(filePath);
            String fileName = file.getName().substring(0, file.getName().length() - 4);
            Workbook book = Workbook.getWorkbook(file);//获得excel
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();//获得总行数
            int Cols = sheet.getColumns();//获得总列数
            //获得meterType
//            Cell cell1 = sheet.getCell(0, 0);
//            String result = cell1.getContents();
//            if (result.contains("4")) {
//                meterType = "04";
//                lastColumn = 20;
//            } else if (result.contains("5")) {
//                meterType = "05";
//                lastColumn = 13;
//            } else {
////                Toast.makeText(this, "导入的Excel格式不对，请检查Excel格式", Toast.LENGTH_SHORT).show();
//                Message message = Message.obtain();
//                message.what = EXCEL_OK;
//                mHandler.sendMessage(message);
//                return;
//            }

            if (isIC) {
                meterType = "04";
                lastColumn = 20;
            } else {
                meterType = "05";
                lastColumn = 13;
            }

            //向数据库中添加账单添加账单

            BookInfo bkInfo = new BookInfo();
            bkInfo.setBookName(fileName);
            bkInfo.setEstateNo("");
            bkInfo.setRemark("");
            bkInfo.setStaffNo("");
            bkInfo.setMeterTypeNo(meterType);
            bkInfo.setBookNo(StringFormatter.getAddStringNo(bookInfoBiz.getLastBookNo()));
            bookNo = bkInfo.getBookNo();
            bookInfoBiz.addBookInfo(bkInfo);
            //插入分组
            String groupInfoName = "";
            //得到第一个分组名称
//            if(meterType.equals("05")&&getCellDate(sheet,lastColumn,2).equals("")){
////                Toast.makeText(this, "导入的Excel格式不对，请检查Excel格式", Toast.LENGTH_SHORT).show();
//                Message message = Message.obtain();
//                message.what = EXCEL_OK;
//                mHandler.sendMessage(message);
//                return;
//            }
//            if(meterType.equals("04")&&getCellDate(sheet,lastColumn,2).equals("")){
////                Toast.makeText(this, "导入的Excel格式不对，请检查Excel格式", Toast.LENGTH_SHORT).show();
//                Message message = Message.obtain();
//                message.what = EXCEL_OK;
//                mHandler.sendMessage(message);
//                return;
//            }

            if (meterType.equals("05")) {
                //如果是"05"则每次去判断第21例是否为空
                //从第三行开始拿数据
                for (int num = 1; num < Rows && isRun; num++) {
                    //得到当前行第21例的数据
                    if (!getCellDate(sheet, lastColumn, num).trim().equals("")) {
                        //插入新的分组
                        groupInfoName = getCellDate(sheet, lastColumn, num).trim();//得到当前的分组名称
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.setGroupName(groupInfoName);
                        groupInfo.setEstateNo("");
                        groupInfo.setRemark("");
                        groupInfo.setMeterTypeNo(meterType);
                        groupInfo.setBookNo(bookNo);
                        String beginGroupNo = bookNo.substring(5);
                        String endGroupNo;
                        ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);
                        if (groupInfos != null && groupInfos.size() > 0) {
                            endGroupNo = StringFormatter.getAddStringGroupNo(groupInfos.get(0).getGroupNo().substring(5));
                        } else {
                            endGroupNo = "00001";
                        }
                        groupNo = beginGroupNo + endGroupNo;
                        groupInfo.setGroupNo(groupNo);
                        groupInfoBiz.addGroupInfo(groupInfo);//添加分组

                    }
                    //插入表数据
                    //向分组内插入表的信息

                    CopyData copyData = new CopyData();

                    copyData.setMeterNo(getCellDate(sheet, 0, num).trim());
                    copyData.setLastShow(getCellDate(sheet, 1, num).trim());
                    copyData.setLastDosage(getCellDate(sheet, 2, num).trim());
                    copyData.setCurrentShow(getCellDate(sheet, 3, num).trim());
                    copyData.setCurrentDosage(getCellDate(sheet, 4, num).trim());
                    copyData.setMeterState(getInt(getCellDate(sheet, 5, num).trim()));
                    copyData.setCopyState(getInt(getCellDate(sheet, 6, num).trim()));
                    copyData.setCopyTime(getCellDate(sheet, 7, num).trim());
                    copyData.setCopyMan(getCellDate(sheet, 8, num).trim());
                    copyData.setMeterName(getCellDate(sheet, 9, num).trim());
                    copyData.setdBm(getCellDate(sheet, 10, num).trim());
                    copyData.setElec(getCellDate(sheet, 11, num).trim());

                    copyBiz.addCopyData(copyData);
                    // 绑定数据
                    GroupBind groupBind = new GroupBind();
                    groupBind.setMeterName(copyData.getMeterName() + "");
                    groupBind.setGroupNo(groupNo);
                    groupBind.setMeterNo(copyData.getMeterNo());
                    groupBind.setMeterType("05");
                    groupBindBiz.addGroupBind(groupBind);

//                    if (meterType.equals("04")) {// IC卡无线
//                        CopyDataICRF copyDataICRF = new CopyDataICRF();
//                        copyDataICRF.setMeterNo("");//设置抄表名称
//                        copyDataICRF.setCumulant("0.00");
//                        copyDataICRF.setSurplusMoney("0");
//                        copyDataICRF.setOverZeroMoney("0");
//                        copyDataICRF.setBuyTimes(0);
//                        copyDataICRF.setOverFlowTimes(0);
//                        copyDataICRF.setMagAttTimes(0);
//                        copyDataICRF.setCardAttTimes(0);
//                        copyDataICRF.setCurrMonthTotal("0.00");
//                        copyDataICRF.setLast1MonthTotal("0.00");
//                        copyDataICRF.setLast2MonthTotal("0.00");
//                        copyDataICRF.setLast3MonthTotal("0.00");
//                        copyDataICRF.setMeterName("");
//                        copyBiz.addCopyDataICRF(copyDataICRF);
//                    } else if (meterType.equals("05")) {// 纯无线
//                        CopyData copyData = new CopyData();
//                        copyData.setMeterNo("");
//                        copyData.setLastShow("0.00");
//                        copyData.setLastDosage("0");
//                        copyData.setCurrentShow("0.00");
//                        copyData.setCurrentDosage("0");
//                        copyData.setUnitPrice("2.5");
//                        copyData.setPrintFlag(0);
//                        copyData.setMeterName("");
//                        copyBiz.addCopyData(copyData);
//                    }
//                    num++;
                }


            } else if (meterType.equals("04")) {
                Toast.makeText(this, "IC无线导入功能正在开发中。。。", Toast.LENGTH_SHORT).show();
            }

            // 得到第一列第一行的单元格
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

    private int getInt(String s) {

        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }

    }


    private CopyDataICRF getCopyDataICRF() {
        return null;
    }

    private CopyData getCopyDate() {


        return null;
    }

    private String getCellDate(Sheet sheet, int i, int j) {
        Cell cell1 = sheet.getCell(i, j);
        String result = cell1.getContents();
        return result;
    }


}
