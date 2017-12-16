package com.pl.gassystem.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bean.UserInfo;
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
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.utils.SPUtils;
import com.pl.utils.StringFormatter;
import com.zuoni.zuoni_common.dialog.picker.DataPickerSingleDialog;
import com.zuoni.zuoni_common.dialog.picker.callback.OnSingleDataSelectedListener;
import com.zuoni.zuoni_common.utils.common.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by zangyi_shuai_ge on 2017/5/6
 * 导入账册
 */
public class ImportExcelActivity extends BaseTitleActivity {
    @BindView(R.id.tvFilePath)
    TextView tvFilePath;
    @BindView(R.id.layout01)
    LinearLayout layout01;
    @BindView(R.id.tv02)
    TextView tv02;
    @BindView(R.id.layout02)
    LinearLayout layout02;
    //导入账册的时候同时需要导入用户信息

    private String filePath = "";
    private boolean isRun = false;
    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private final int EXCEL_FALSE = 4;


    //当前已存在账册需要先执行是否删除账册判断
    private final int EXCEL_IMPORT_DELETE = 5;
    private final int EXCEL_IMPORT = 6;

    //导入的Excel格式不符合要求
    private final int EXCEL_FORMAT_ERROR = 7;

    private String bookNo;//账册编号

    private String groupNo;

    private boolean isIC = true;


    private GroupInfoBiz groupInfoBiz;
    private GroupBindBiz groupBindBiz;
    private BookInfoBiz bookInfoBiz;
    private CopyBiz copyBiz;


    private String meterType = "04";


    private int lastColumn;//Excel需要最少列数
    private String fileName = "";//需要导入的账册名称
    private Workbook book;//获得Excel文件
    private Sheet sheet;//获得Excel工作表
    private int Rows;//Excel总行数


    //IC无线
    private final int IMPORT_TYPE_IC_WU_XIAN = 1;
    //西宁IC无线
    private final int IMPORT_TYPE_XI_NING_IC_WU_XIAN = 3;
    //纯无线
    private final int IMPORT_TYPE_WU_XIAN = 2;
    //导入表具类型
    private int importType;


    //提示框
    private AlertDialog alertDialog;
    private ProgressDialog mProgressDialog;


    //需要导入的账册是否存在（若存在是否需要删除）
    private boolean needDelete = false;
    //若需要删除  删除的账册编号
    private String deleteBookNo;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ImportExcelActivity.this);
            switch (msg.what) {
                case EXCEL_ERROR:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("EXCEL 导出失败，请检查Excel格式是否正确或重新再试");
                    alertDialog.show();
                    break;
                case EXCEL_OK:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("EXCEL导入完成");
                    alertDialog.show();
                    break;
                case EXCEL_PROGRESS:
                    break;
                case EXCEL_FALSE:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("选取文件不存在，请重新选择");
                    alertDialog.show();
                    break;
                case EXCEL_IMPORT_DELETE:
                    //需要判断删除账册操作
                    mProgressDialog.dismiss();
                    AlertDialog.Builder bulider = new AlertDialog.Builder(ImportExcelActivity.this);
                    bulider.setTitle("账册已存在");
                    bulider.setMessage("该账册已经存在，是否需要覆盖原来的账册");
                    bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.setMessage("正在删除旧的账册信息");
                            mProgressDialog.show();
                            //获得该账册下的所有分组
                            ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(deleteBookNo);
                            for (int i = 0; i < groupInfos.size(); i++) {
                                groupBindBiz.removeGroupBindByGroupNo(groupInfos.get(i).getGroupNo()); // 删除分组绑定表里面的数据
                            }
                            groupInfoBiz.removeGroupInfoByBookNo(deleteBookNo);// 删除分组信息
                            bookInfoBiz.removeBookInfo(deleteBookNo);// 删除账册数据
                            //删除完成之后,导入账册
                            Message message = Message.obtain();
                            message.what = EXCEL_IMPORT;
                            mHandler.sendMessage(message);
                        }
                    });
                    bulider.setNegativeButton("取消", null);
                    bulider.setCancelable(false);
                    bulider.create().show();
                    break;
                case EXCEL_IMPORT:
                    //直接导入
                    mProgressDialog.dismiss();
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
                case EXCEL_FORMAT_ERROR:
                    //Excel格式出错
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("选择的Excel格式与所选表具类型不一致,请重新选择");
                    alertDialog.show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("账册导入");
        bookInfoBiz = new BookInfoBiz(this);
        groupInfoBiz = new GroupInfoBiz(this);
        groupBindBiz = new GroupBindBiz(this);
        copyBiz = new CopyBiz(this);

        initUI();
        isRun = true;
        isIC = true;
        importType = 0;
    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();
    }



    protected void initUI() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("还车中请稍后...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setPositiveButton("确定", null);
        alertDialog = builder.create();
        filePath = (String) SPUtils.get(ImportExcelActivity.this, "filePath", "");
        tvFilePath.setText(filePath);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_import_excel;
    }

    /**
     * 选择路径返回
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = uri.getPath();
                filePath = FileUtils.getRealFilePath(ImportExcelActivity.this, uri);//获取真实路径
                tvFilePath.setText(filePath);
                SPUtils.put(ImportExcelActivity.this, "filePath", filePath);
            }
        }
    }


    @OnClick({R.id.layout01, R.id.layout02, R.id.btImport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout01:
                //选择文件路径
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.layout02:
                //选择表具类型
                DataPickerSingleDialog.Builder builder = new DataPickerSingleDialog.Builder(getContext());
                List<String> mList = new ArrayList<>();
                mList.add("IC无线");
                mList.add("西宁IC无线");
                mList.add("纯无线");
                builder.setData(mList);
                builder.setOnDataSelectedListener(new OnSingleDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        tv02.setText(itemValue);
                        switch (itemValue) {
                            case "IC无线":
                                importType = IMPORT_TYPE_IC_WU_XIAN;
                                isIC = true;
                                break;
                            case "西宁IC无线":
                                isIC = true;
                                importType = IMPORT_TYPE_XI_NING_IC_WU_XIAN;
                                break;
                            case "纯无线":
                                isIC = false;
                                importType = IMPORT_TYPE_WU_XIAN;
                                break;
                        }

                    }
                });
                builder.create().show();
                break;
            case R.id.btImport:
                String mFilePath = filePath.substring(filePath.length() - 4, filePath.length());
                if (mFilePath.equals(".xls")) {
                    if (tv02.getText().toString().trim().equals("请选择表具类型")) {
                        showToast("请选择表具类型");
                        return;
                    }
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





    /**
     * 读取Excel里面的内容
     */
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


            //判断表具类型
            if (importType == IMPORT_TYPE_IC_WU_XIAN) {
                meterType = "04";
                lastColumn = 25;
            } else if (importType == IMPORT_TYPE_WU_XIAN) {
                meterType = "05";
                lastColumn = 13;
            } else if (importType == IMPORT_TYPE_XI_NING_IC_WU_XIAN) {
                meterType = "04";
                lastColumn = 13;
            }

            //判断列数是否符合要求
            if (Cols < lastColumn) {
                Message message = Message.obtain();
                message.what = EXCEL_FORMAT_ERROR;
                mHandler.sendMessage(message);
                return;//下面操作不执行
            }

            //判断账册是否已经存在

            //获取所有账册
            ArrayList<BookInfo> bookInfos = bookInfoBiz.getBookInfos();
            for (int i = 0; i < bookInfos.size(); i++) {
                if (fileName.equals(bookInfos.get(i).getBookName())) {
                    needDelete = true;
                    deleteBookNo = bookInfos.get(i).getBookNo();//获得需要删除的bookNo
                    break;
                } else {
                    needDelete = false;
                }
            }

            //是否需要执行删除操作
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


    /**
     * 从Excel里向App内导入导入数据
     */
    private void importExcel() {

        //向数据库中添加账单添加账单新建账册
        BookInfo bkInfo = new BookInfo();
        bkInfo.setBookName(fileName);
        bkInfo.setEstateNo("");//小区编号
        bkInfo.setRemark("Excel 账册导入");
        bkInfo.setStaffNo("");
        //设置表具类型
        bkInfo.setMeterTypeNo(meterType);//04 IC无线表  05纯无线表

        //设置账册编号
        bkInfo.setBookNo(StringFormatter.getAddStringNo(bookInfoBiz.getLastBookNo()));
        bookNo = bkInfo.getBookNo();
        //数据库插入账册
        bookInfoBiz.addBookInfo(bkInfo);

        String groupInfoName = "";//当前分组名称

        //添加分组
        if (meterType.equals("05")) {
            //纯无线格式
            //表计编号0 	上次读数1	上次用量2	本次读数3	本次用量4	表具状态5
            //抄表状态6 	抄表时间7	抄表员姓名8 	表具名称9	信号强度10	电量11	手机号码12	分组名称13

            //从 num=1 行开始取数据 num=0为tab
            for (int num = 1; num < Rows && isRun; num++) {
                //得到当前行第21例的数据不为空 说明为新的分组
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

                //向用户信息表内插入数据
                UserInfo userInfo = new UserInfo();
                userInfo.setTableNumber(getCellDate(sheet, 0, num).trim() + "");//表具编号
                userInfo.setAddress(groupInfoName);//分组名称
                userInfo.setUserPhone(getCellDate(sheet, 12, num).trim());//用户手机号码
                UserInfoDao userInfoDao = new UserInfoDao(getContext());
                userInfoDao.putUserInfo(userInfo);//插入用户信息

                //将数据和分组进行绑定
                GroupBind groupBind = new GroupBind();
                groupBind.setMeterName(copyData.getMeterName() + "");
                groupBind.setGroupNo(groupNo);
                groupBind.setMeterNo(copyData.getMeterNo());
                groupBind.setMeterType("05");//纯无线
                groupBindBiz.addGroupBind(groupBind);
            }
        } else if (meterType.equals("04")) {

            if (importType == 1) {
                //普通IC无线
                //电量0 	表计编号1	表具名称2	累计量3	 剩余金额4	 过零金额5	 购买次数6	 过流次数7
                //磁攻击次数8	卡攻击次数9 	表状态10	表状态解析信息11	当月累计量12	上月累计量13
                //上上月累计量14	上上上月累计量15	抄表方式16	抄表时间17	抄表状态18	当前单价19
                //累计用气金额20	累计充值金额21	本周期使用量22	信号强度23	手机号24	分组名称25

                for (int num = 1; num < Rows && isRun; num++) {
                    //得到当前行第25例的数据
                    if (!getCellDate(sheet, lastColumn, num).trim().equals("")) {
                        //插入新的分组
                        groupInfoName = getCellDate(sheet, lastColumn, num).trim();//得到当前的分组名称
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.setGroupName(groupInfoName);
                        groupInfo.setEstateNo("");
                        groupInfo.setRemark("Excel 导入");
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
                    //新建用户表数据
                    //向用户信息表内插入数据
                    UserInfo userInfo = new UserInfo();
                    userInfo.setTableNumber(getCellDate(sheet, 1, num).trim() + "");//表具编号
                    userInfo.setAddress(groupInfoName);//分组名称
                    userInfo.setUserPhone(getCellDate(sheet, 24, num).trim());//用户手机号码
                    UserInfoDao userInfoDao = new UserInfoDao(getContext());
                    userInfoDao.putUserInfo(userInfo);//插入用户信息


                    // 绑定数据
                    GroupBind groupBind = new GroupBind();
                    groupBind.setMeterName(copyDataICRF.getMeterName() + "");
                    groupBind.setGroupNo(groupNo);
                    groupBind.setMeterNo(copyDataICRF.getMeterNo());
                    groupBind.setMeterType("05");
                    groupBindBiz.addGroupBind(groupBind);

                }


            } else if (importType == 3) {
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
                    userInfo.setXiNingUnitPrice(getCellDate(sheet, 10, num).trim());
//                    userInfo.saveOrUpdate("tableNumber=?",110+"");//去保存
//                    userInfo.save();
                    UserInfoDao userInfoDao = new UserInfoDao(ImportExcelActivity.this);
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

    //获取表格里面的数据
    private String getCellDate(Sheet sheet, int i, int j) {
        Cell cell1 = sheet.getCell(i, j);
        String result = cell1.getContents();
        return result;
    }
}
