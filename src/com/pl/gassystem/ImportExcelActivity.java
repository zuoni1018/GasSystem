package com.pl.gassystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bean.UserInfo;
import com.pl.gassystem.utils.SPUtils;
import com.pl.bll.BookInfoBiz;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupBindBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.dal.userinfo.UserInfoDao;
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
 * 导入账册
 */
public class ImportExcelActivity extends BaseTitleActivity {
    //导入账册的时候同时需要导入用户信息
    private Button btChoose;
    private Button btImport;
    private TextView tvFilePath;
    private String filePath = "";
    private boolean isRun = false;
    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private final int EXCEL_FALSE = 4;


    private final int EXCEL_IMPORT_DELETE = 5;
    private final int EXCEL_IMPORT = 6;

    private RadioGroup mRadioGroup;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ImportExcelActivity.this);

            switch (msg.what) {

                case EXCEL_ERROR:
                    mProgressDialog.dismiss();
                    dialog.setTitle("EXCEL 导出失败，请检查Excel格式是否正确或重新再试");
                    dialog.show();
                    break;
                case EXCEL_OK:
                    mProgressDialog.dismiss();
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
                case EXCEL_FALSE:
                    mProgressDialog.dismiss();
                    Toast.makeText(ImportExcelActivity.this, "选取文件不存在，请重新选择", Toast.LENGTH_SHORT).show();
                    break;


                case EXCEL_IMPORT_DELETE://需要删除再进行导入
                    AlertDialog.Builder bulider = new AlertDialog.Builder(ImportExcelActivity.this);
                    bulider.setTitle("账册已存在");
                    bulider.setMessage("该账册已经存在，是否需要覆盖原来的账册");
                    bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除数据
                            GroupInfoBiz groupInfoBiz = new GroupInfoBiz(ImportExcelActivity.this);
                            GroupBindBiz groupBindBiz = new GroupBindBiz(ImportExcelActivity.this);
                            //需要开启线程吗
                            ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(deleteBookNo);
                            for (int i = 0; i < groupInfos.size(); i++) {
                                groupBindBiz.removeGroupBindByGroupNo(groupInfos.get(i).getGroupNo()); // 删除绑定数据
                            }
                            groupInfoBiz.removeGroupInfoByBookNo(deleteBookNo);// 删除分组数据
                            bookInfoBiz.removeBookInfo(deleteBookNo);// 删除账册数据
                            //删除完成之后,导入账册
                            Message message = Message.obtain();
                            message.what = EXCEL_IMPORT;
                            mHandler.sendMessage(message);
                        }
                    });
                    bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.dismiss();
                        }
                    });
                    bulider.setCancelable(false);
                    AlertDialog mAlertDialog = bulider.create();
                    mAlertDialog.show();
                    break;
                case EXCEL_IMPORT://直接导入
                    mProgressDialog.setMessage("正在从Excel导入数据库请不要退出App、或其他操作");
                    mProgressDialog.show();
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            importExcel();
                        }
                    };
                    t.start();

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
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("账册导入");
        isRun = true;
        isIC = true;
        type = 1;

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
                    type = 1;
                } else if (checkedId == R.id.rb02) {
                    isIC = false;
                    type = 2;
                } else {
                    isIC = true;
                    type = 3;
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
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;

            case R.id.btImport:
//                UserInfo userInfo = new UserInfo();
//                userInfo.setTableNumber("2");//表具编号
//                userInfo.setUserNum("3");//用户编号
//                userInfo.setXiNingTableNumber(getCellDate(sheet, 1, num).trim());//西宁表具编号
//                userInfo.setUserName(getCellDate(sheet, 6, num).trim());//用户名称
//                userInfo.setAddress(getCellDate(sheet, 5, num).trim());//房间地址
//                    userInfo.saveOrUpdate("tableNumber=?",110+"");//去保存
//                userInfo.save();
//                LogUtil.i("存数据888", userInfo.save() + "");
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
                filePath = getRealFilePath(ImportExcelActivity.this, uri);
//                filePath=filePath.replaceAll(":","/");
                tvFilePath.setText(filePath);
                SPUtils.put(ImportExcelActivity.this, "filePath", filePath);
            }
        }
    }


    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private class ImportExcelThread extends Thread {
        @Override
        public void run() {
//            super.run();
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

    private ProgressDialog mProgressDialog;
    private boolean needDelete = false;

    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("还车中请稍后...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    private String meterType = "04";
    private String deleteBookNo;


    private String fileName = "";//需要导入的账册名称
    private Workbook book;//获得Excel文件
    private Sheet sheet;//获得Excel工作表
    int Rows;


    public void readExcel() {
        try {
            InputStream is = new FileInputStream(filePath);//从文件获得输入流
            File file = new File(filePath);
            fileName = file.getName().substring(0, file.getName().length() - 4);
            book = Workbook.getWorkbook(file);//获得excel
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            sheet = book.getSheet(0);
            Rows = sheet.getRows();//获得总行数
            int Cols = sheet.getColumns();//获得总列数
            if (type == 1) {
                meterType = "04";
                lastColumn = 25;
            } else if (type == 2) {
                meterType = "05";
                lastColumn = 13;
            } else {
                meterType = "04";
                lastColumn = 13;
            }
            //查询覆盖
            ArrayList<BookInfo> bookInfos = bookInfoBiz.getBookInfos();
            for (int i = 0; i < bookInfos.size(); i++) {
                if (fileName.equals(bookInfos.get(i).getBookName())) {
                    needDelete = true;
                    //获得需要删除的bookNo
                    deleteBookNo = bookInfos.get(i).getBookNo();
                    break;
                } else {
                    needDelete = false;
                }
            }
            if (needDelete) {
                //发送进行导入
                Message message = Message.obtain();
                message.what = EXCEL_IMPORT_DELETE;
                mHandler.sendMessage(message);
            } else {
                Message message = Message.obtain();
                message.what = EXCEL_IMPORT;
                mHandler.sendMessage(message);
            }

        } catch (Exception e) {
            Message message = Message.obtain();
            message.what = EXCEL_ERROR;
            mHandler.sendMessage(message);
        }
    }


    //从Excel里向App内导入导入数据
    private void importExcel() {

        //向数据库中添加账单添加账单  新建账册
        BookInfo bkInfo = new BookInfo();
        bkInfo.setBookName(fileName);
        bkInfo.setEstateNo("");
        bkInfo.setRemark("");
        bkInfo.setStaffNo("");
        bkInfo.setMeterTypeNo(meterType);
        //
        if (needDelete) {
            bkInfo.setBookNo(deleteBookNo);
            bookNo = bkInfo.getBookNo();
        } else {
            bkInfo.setBookNo(StringFormatter.getAddStringNo(bookInfoBiz.getLastBookNo()));
            bookNo = bkInfo.getBookNo();
        }

        bookInfoBiz.addBookInfo(bkInfo);
        String groupInfoName = "";

        //添加分组
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


            }
        } else if (meterType.equals("04")) {
            //普通IC无线
            if (type == 1) {

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
                    CopyDataICRF copyDataICRF = new CopyDataICRF();
                    copyDataICRF.setElec(getCellDate(sheet, 0, num).trim());
                    copyDataICRF.setMeterNo(getCellDate(sheet, 1, num).trim());
                    copyDataICRF.setMeterName(getCellDate(sheet, 2, num).trim());
                    copyDataICRF.setCumulant(getCellDate(sheet, 3, num).trim());
                    copyDataICRF.setSurplusMoney(getCellDate(sheet, 4, num).trim());


                    copyDataICRF.setOverZeroMoney(getCellDate(sheet, 5, num).trim());
                    copyDataICRF.setBuyTimes(getInt(getCellDate(sheet, 6, num).trim()));
                    copyDataICRF.setOverFlowTimes(getInt(getCellDate(sheet, 7, num).trim()));
                    copyDataICRF.setMagAttTimes(getInt(getCellDate(sheet, 8, num).trim()));
                    copyDataICRF.setCardAttTimes(getInt(getCellDate(sheet, 9, num).trim()));

                    copyDataICRF.setMeterState(getInt(getCellDate(sheet, 10, num).trim()));
                    copyDataICRF.setStateMessage(getCellDate(sheet, 11, num).trim());
                    copyDataICRF.setCurrMonthTotal(getCellDate(sheet, 12, num).trim());
                    copyDataICRF.setLast1MonthTotal(getCellDate(sheet, 13, num).trim());
                    copyDataICRF.setLast2MonthTotal(getCellDate(sheet, 14, num).trim());

                    copyDataICRF.setLast3MonthTotal(getCellDate(sheet, 15, num).trim());
                    copyDataICRF.setCopyWay(getCellDate(sheet, 16, num).trim());
                    copyDataICRF.setCopyTime(getCellDate(sheet, 17, num).trim());
                    copyDataICRF.setCopyTime(getCellDate(sheet, 18, num).trim());
                    copyDataICRF.setCopyState(getInt(getCellDate(sheet, 19, num).trim()));


                    copyDataICRF.setLast3MonthTotal(getCellDate(sheet, 20, num).trim());
                    copyDataICRF.setAccBuyMoney(getCellDate(sheet, 21, num).trim());
                    copyDataICRF.setCurrentShow(getCellDate(sheet, 22, num).trim());
                    copyDataICRF.setdBm(getCellDate(sheet, 23, num).trim());
                    copyBiz.addCopyDataICRF(copyDataICRF);
                    // 绑定数据
                    GroupBind groupBind = new GroupBind();
                    groupBind.setMeterName(copyDataICRF.getMeterName() + "");
                    groupBind.setGroupNo(groupNo);
                    groupBind.setMeterNo(copyDataICRF.getMeterNo());
                    groupBind.setMeterType("05");
                    groupBindBiz.addGroupBind(groupBind);

                }


            } else if (type == 3) {
                //西宁无线
                for (int num = 1; num < Rows && isRun; num++) {

                    String address = getCellDate(sheet, 4, num).trim();//这是分组名

                    if (!address.equals(groupInfoName) && !address.equals("")) {
                        //新建一个分组

                        groupInfoName = address;
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

                    CopyDataICRF copyDataICRF = new CopyDataICRF();
                    copyDataICRF.setNo01(getCellDate(sheet, 0, num).trim());
                    copyDataICRF.setNo02(getCellDate(sheet, 1, num).trim());
                    copyDataICRF.setMeterNo(getCellDate(sheet, 2, num).trim());//设置表号码
                    copyDataICRF.setMeterName(getCellDate(sheet, 5, num).trim() + getCellDate(sheet, 6, num).trim());//用户名
                    copyDataICRF.setName(getCellDate(sheet, 6, num).trim());
                    copyDataICRF.setUnitPrice(getCellDate(sheet, 10, num).trim());//设置单价
                    copyDataICRF.setCopyState(0);

                    //新建一张用户信息表
                    UserInfo userInfo = new UserInfo();
                    userInfo.setTableNumber(getCellDate(sheet, 2, num).trim() + "");//表具编号
                    userInfo.setUserNum(getCellDate(sheet, 0, num).trim());//用户编号
                    userInfo.setXiNingTableNumber(getCellDate(sheet, 1, num).trim());//西宁表具编号
                    userInfo.setUserName(getCellDate(sheet, 6, num).trim());//用户名称
                    userInfo.setAddress(getCellDate(sheet, 5, num).trim());//房间地址
                    userInfo.setXiNingUnitPrice(getCellDate(sheet,10,num).trim());
//                    userInfo.saveOrUpdate("tableNumber=?",110+"");//去保存
//                    userInfo.save();

                    UserInfoDao userInfoDao=new UserInfoDao(ImportExcelActivity.this);
                    userInfoDao.putUserInfo(userInfo);


                    if (!copyDataICRF.getMeterNo().equals("")) {
                        copyBiz.addCopyDataICRF(copyDataICRF);
                        // 绑定数据
                        GroupBind groupBind = new GroupBind();
                        groupBind.setMeterName(copyDataICRF.getMeterName() + "");
                        groupBind.setGroupNo(groupNo);
                        groupBind.setMeterNo(copyDataICRF.getMeterNo());
                        groupBind.setMeterType("05");
                        groupBindBiz.addGroupBind(groupBind);
                    }
                }

            }


        }

        //导入成功
        Message message = Message.obtain();
        message.what = EXCEL_OK;
        mHandler.sendMessage(message);
        //关闭
        book.close();
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
