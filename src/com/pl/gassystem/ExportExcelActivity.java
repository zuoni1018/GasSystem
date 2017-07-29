package com.pl.gassystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.FileUtil;
import com.common.utils.LogUtil;
import com.pl.MyDatePickerDialog;
import com.pl.adapter.DateListViewAdapter;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.base.BaseTitleActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static com.common.utils.FileUtil.getSDPath;

/**
 * Created by zangyi_shuai_ge on 2017/5/4
 */

public class ExportExcelActivity extends BaseTitleActivity {

    private TextView tvTime01, tvTime02;


    private ProgressDialog mProgressDialog;
    private boolean isRun;

    int mYear, mMonth, mDay;
    Button btn;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;

    private String bookNo;
    private String bookName;
    private String meterTypeNo;
    private boolean isSelect;
    private CopyBiz copyBiz;
    private GroupInfoBiz groupInfoBiz;


    private Button btSearch, btGetTime;
    private EditText etSearchTime;


    private List<String> mList;
    private ListView mListView;
    private DateListViewAdapter mAdapter;

    private String filePath;


    private Button btXiNingIC;
    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ExportExcelActivity.this);
            switch (msg.what) {

                case EXCEL_ERROR:

                    dialog.setTitle("EXCEL 导出失败");
                    num = 1;
                    dialog.show();
                    break;
                case EXCEL_OK:


                    dialog.setTitle("EXCEL 导出完成");
                    num = 1;
                    dialog.show();

                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Uri uri = Uri.fromFile(new File(filePath));
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                    startActivity(intent);

                    break;
                case EXCEL_PROGRESS:

                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = 1;
        isRun = true;
//        exportCopyDataExcelThread t = new exportCopyDataExcelThread();
//        t.start();

    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();
    }

    @Override
    protected void initData() {
        copyBiz = new CopyBiz(this);
        groupInfoBiz = new GroupInfoBiz(this);
        bookNo = getIntent().getStringExtra("BookNo").toString();
        bookName = getIntent().getStringExtra("BookName").toString();
        meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();
        isSelect = getIntent().getBooleanExtra("isSelect", false);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        mList = new ArrayList<>();
        mAdapter = new DateListViewAdapter(mList, ExportExcelActivity.this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new MyDatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    protected void initOnClickListener() {
        btSearch.setOnClickListener(this);
        btGetTime.setOnClickListener(this);
        btXiNingIC.setOnClickListener(this);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mList.remove(position);
                mAdapter.notifyDataSetChanged();

                return false;
            }
        });

    }

    private DatePickerDialog mDatePickerDialog;

    private void showDatePickerDialog(final TextView tv) {
        if(mDatePickerDialog!=null&&mDatePickerDialog.isShowing()){
            mDatePickerDialog.dismiss();
        }
        mDatePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + getDateString(monthOfYear + 1) + "-" + getDateString(dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        mDatePickerDialog.show();
    }

    @Override
    protected void initUI() {
        setTitle("导出Excel");
        btSearch = (Button) findViewById(R.id.btSearch);
        mListView = (ListView) findViewById(R.id.mListView);
        btGetTime = (Button) findViewById(R.id.btGetTime);
        btXiNingIC = (Button) findViewById(R.id.btXiNingIC);
        initDialog();


        tvTime01 = (TextView) findViewById(R.id.tvTime01);
        tvTime02 = (TextView) findViewById(R.id.tvTime02);

        tvTime01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvTime01);
            }
        });
        tvTime02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvTime02);
            }
        });
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_export_excel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btXiNingIC:
                mProgressDialog.setMessage("正在导出excel，请不要退出App或其他操作");
                mProgressDialog.show();
                exportXiNingICExcelThread t = new exportXiNingICExcelThread();
                t.start();

                break;

            case R.id.btSearch:
//                showDialog(DATE_DIALOG);
                if (mList.size() == 0) {

                    showMyDialog("您未选择日期，将为您查询所有日期的内容");
                } else {
                    String message = "将为您查询以下选择的日期\n";
                    if (mList.size() < 5) {
                        for (int i = 0; i < mList.size(); i++) {
                            message = message + "\n" + mList.get(i);
                        }
                    } else {
                        for (int i = 0; i < 5; i++) {
                            message = message + "\n" + mList.get(i);
                        }
                    }
                    showMyDialog(message);
                }


                break;
            case R.id.btGetTime:
                if (mList.size() > 10) {
                    Toast.makeText(this, "最多查询10条", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(DATE_DIALOG);
                }
                break;
        }
    }

    Dialog dialog;

    private void showMyDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mProgressDialog.setMessage("正在导出excel，请不要退出App或其他操作");
                mProgressDialog.show();
                exportCopyDataExcelThread t = new exportCopyDataExcelThread();
                t.start();


            }
        });
        dialog = builder.create();
        dialog.show();
    }


    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("还车中请稍后...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new MyDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mList.add(year + "-" + getDateString(monthOfYear + 1) + "-" + getDateString(dayOfMonth));
            mAdapter.notifyDataSetChanged();
//            LogUtil.i("zzzzzzzz");
        }
    };

    private String getDateString(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 导出excel
     * 流程：
     * 通过账册查询到所有抄表点（保存账册名称用来创建Excel表 表名）
     * 通过抄表点查询所有的抄表信息
     * 文件名（账册名.xls）
     * 先抄标点作为标题
     * 再分组名
     * 再每个表的信息
     */

    private String booksName;


    private String[] CopyDataTab = {
            "表计编号", "上次读数", "上次用量", "本次读数",
            "本次用量", "表具状态", "抄表状态", "抄表时间",
            "抄表员姓名", "表具名称", "信号强度", "电量", "", "分组名称"};


    private String[] CopyXiNingTab = {
            "用户编号", "表具编号", "表具钢号", "地址描述", "用户名称", "表具累计用量", "表具累计用气金额",
            "表具剩余金额", "当前运行价格", "采集方式", "采集时间", "采集成功标志", "失败原因", "表具时间",
            "阀门状态", "IC卡购气次数", "远程购气次数", "累计上表购气金额", "表具故障状态"
    };
    private String[] CopyDataICRFTab = {
            "电量", "表计编号", "表具名称", "累计量", "剩余金额",
            "过零金额", "购买次数", "过流次数", "磁攻击次数", "卡攻击次数",
            "表状态", "表状态解析信息", "当月累计量", "上月累计量", "上上月累计量",
            "上上上月累计量", "抄表方式", "抄表时间", "抄表状态", "当前单价",
            "累计用气金额", "累计充值金额", "本周期使用量", "信号强度", "", "分组名称"};


    private WritableWorkbook mWritableWorkbook;
    private WritableSheet ws;
    public int num = 1;

    private class exportXiNingICExcelThread extends Thread {

        @Override
        public void run() {

            File file = new File(getSDPath() + "/HongHuDate"); //创建文件目录
            FileUtil.makeDir(file);//先创建当前账册的excel表
            //创建excel文件
            File file2 = new File(file.toString() + "/" + bookName + mYear + getDateString(mMonth + 1) + getDateString(mDay) + ".xls");
            filePath = file2.toString();
            //每次都重新生成当前文件
            if (file2.exists()) {
                file2.delete();
            }
            try {
                mWritableWorkbook = Workbook.createWorkbook(file2);
                ws = mWritableWorkbook.createSheet("测试表", 0);
                ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);

                if (isRun) {

                    //创建tab

                    //在第二行创建tab
                    for (int j = 0; j < CopyXiNingTab.length; j++) {
                        Label mLabel2 = new Label(j, 0, CopyXiNingTab[j]);
                        ws.addCell(mLabel2);
                    }

//
                    for (int i = 0; i < groupInfos.size(); i++) {

                        String groupName = groupInfos.get(i).getGroupName();
                        //编写分组名称在每个分组的第一个表的最后一列添加分组名称
                        ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());//获得每栋楼 里每个住户的信息
                        ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);

                        if (copyDataICRFs != null) {
                            for (int j = 0; j < copyDataICRFs.size(); j++) {
                                createALabel(0, copyDataICRFs.get(j).getNo01() + "");
                                createALabel(1, copyDataICRFs.get(j).getNo02() + "");
                                createALabel(2, copyDataICRFs.get(j).getMeterNo() + "");//填写表具钢号
                                createALabel(3, groupName + "");//填写表具钢号
                                createALabel(4, copyDataICRFs.get(j).getName() + "");//用户名称
                                createALabel(5, copyDataICRFs.get(j).getCumulant() + "");//表具累计用量
                                createALabel(6, copyDataICRFs.get(j).getAccMoney() + "");//累计用气金额
                                createALabel(7, copyDataICRFs.get(j).getSurplusMoney() + "");//累计用气金额
                                createALabel(8, copyDataICRFs.get(j).getUnitPrice() + "");
                                createALabel(9, copyDataICRFs.get(j).getCopyWay() + "");
                                createALabel(10, copyDataICRFs.get(j).getCopyTime() + "");//抄表时间
                                createALabel(12, copyDataICRFs.get(j).getStateMessage() + "");
                                createALabel(13, copyDataICRFs.get(j).getCopyTime() + "");
                                createALabel(15, copyDataICRFs.get(j).getBuyTimes() + "");
                                createALabel(17, copyDataICRFs.get(j).getAccBuyMoney() + "");
                                num++;
                            }
                        }


                    }
                }
                mWritableWorkbook.write();
                mWritableWorkbook.close();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = EXCEL_ERROR;
                mHandler.sendMessage(message);
            } catch (WriteException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = EXCEL_ERROR;
                mHandler.sendMessage(message);
            } finally {
                mProgressDialog.dismiss();
            }
            // 创建表单,其中sheet表示该表格的名字,0表示第一个表格,

            Message message = Message.obtain();
            message.what = EXCEL_OK;
            mHandler.sendMessage(message);

        }
    }


    private class exportCopyDataExcelThread extends Thread {

        @Override
        public void run() {


            File file = new File(getSDPath() + "/HongHuDate"); //创建文件目录
            FileUtil.makeDir(file);//先创建当前账册的excel表
            //创建excel文件
            File file2 = new File(file.toString() + "/" + bookName + mYear + getDateString(mMonth + 1) + getDateString(mDay) + ".xls");
            filePath = file2.toString();
            //每次都重新生成当前文件
            if (file2.exists()) {
                file2.delete();
            }
            try {
                mWritableWorkbook = Workbook.createWorkbook(file2);
                ws = mWritableWorkbook.createSheet("测试表", 0);
                ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);

                if (isRun) {

                    //创建tab
                    if (meterTypeNo.equals("05")) {
                        //在第二行创建tab
                        for (int j = 0; j < CopyDataTab.length; j++) {
                            Label mLabel2 = new Label(j, 0, CopyDataTab[j]);
                            ws.addCell(mLabel2);
                        }
//                        //在(0,0)位置创建meterTypeNo
//                        Label mLabel2 = new Label(0, 0, "");
//                        ws.addCell(mLabel2);
                    } else if (meterTypeNo.equals("04")) {
                        //在第二行创建tab
                        for (int j = 0; j < CopyDataICRFTab.length; j++) {
                            Label mLabel2 = new Label(j, 0, CopyDataICRFTab[j]);
                            ws.addCell(mLabel2);
                        }
//                        Label mLabel2 = new Label(0, 0, "4");
//                        ws.addCell(mLabel2);
                    }


                    for (int i = 0; i < groupInfos.size(); i++) {

                        //编写分组名称在每个分组的第一个表的最后一列添加分组名称
                        if (meterTypeNo.equals("05")) {
                            //在第CopyDataTab.length+1处
                            Label mLabel2 = new Label(CopyDataTab.length - 1, num, groupInfos.get(i).getGroupName());
                            ws.addCell(mLabel2);
                        } else if (meterTypeNo.equals("04")) {
                            String a = groupInfos.get(i).getGroupName();
                            //在第CopyDataICRFTab.length+1处
                            Label mLabel2 = new Label(CopyDataICRFTab.length - 1, num, groupInfos.get(i).getGroupName());
                            ws.addCell(mLabel2);
                        }

                        ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());//获得每栋楼 里每个住户的信息

                        if (meterTypeNo.equals("04")) {// IC卡无线

                            ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                            if (copyDataICRFs != null) {
                                for (int j = 0; j < copyDataICRFs.size(); j++) {
                                    //判断是否满足选择条件
                                    if (checkDate(copyDataICRFs.get(j).getCopyTime())) {
                                        //满足条件
//                                        createALabel(0, copyDataICRFs.get(j).getId() + "");
                                        createALabel(0, copyDataICRFs.get(j).getElec() + "");
                                        createALabel(1, copyDataICRFs.get(j).getMeterNo() + "");
                                        createALabel(2, copyDataICRFs.get(j).getMeterName() + "");
                                        createALabel(3, copyDataICRFs.get(j).getCumulant() + "");
                                        createALabel(4, copyDataICRFs.get(j).getSurplusMoney() + "");

                                        createALabel(5, copyDataICRFs.get(j).getOverZeroMoney() + "");
                                        createALabel(6, copyDataICRFs.get(j).getBuyTimes() + "");
                                        createALabel(7, copyDataICRFs.get(j).getOverFlowTimes() + "");
                                        createALabel(8, copyDataICRFs.get(j).getMagAttTimes() + "");
                                        createALabel(9, copyDataICRFs.get(j).getCardAttTimes() + "");

                                        createALabel(10, copyDataICRFs.get(j).getMeterState() + "");
                                        createALabel(11, copyDataICRFs.get(j).getStateMessage() + "");
                                        createALabel(12, copyDataICRFs.get(j).getCurrMonthTotal() + "");
                                        createALabel(13, copyDataICRFs.get(j).getLast1MonthTotal() + "");
                                        createALabel(14, copyDataICRFs.get(j).getLast2MonthTotal() + "");

                                        createALabel(15, copyDataICRFs.get(j).getLast3MonthTotal() + "");
                                        createALabel(16, copyDataICRFs.get(j).getCopyWay() + "");
                                        createALabel(17, copyDataICRFs.get(j).getCopyTime() + "");
                                        createALabel(18, copyDataICRFs.get(j).getCopyMan() + "");
                                        createALabel(19, copyDataICRFs.get(j).getCopyState() + "");

                                        createALabel(20, copyDataICRFs.get(j).getLast3MonthTotal() + "");
                                        createALabel(21, copyDataICRFs.get(j).getAccBuyMoney() + "");
                                        createALabel(22, copyDataICRFs.get(j).getCurrentShow() + "");
                                        createALabel(23, copyDataICRFs.get(j).getdBm() + "");

                                        num++;
                                    }
                                }
                            }
                        } else if (meterTypeNo.equals("05")) {// 纯无线
                            ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, 2);

                            if (copyDatas != null) {
                                for (int j = 0; j < copyDatas.size(); j++) {
                                    if (checkDate(copyDatas.get(j).getCopyTime())) {

                                        createALabel(0, copyDatas.get(j).getMeterNo() + "");
                                        createALabel(1, copyDatas.get(j).getLastShow() + "");
                                        createALabel(2, copyDatas.get(j).getLastDosage() + "");
                                        createALabel(3, copyDatas.get(j).getCurrentShow() + "");
                                        createALabel(4, copyDatas.get(j).getCurrentDosage() + "");
                                        createALabel(5, copyDatas.get(j).getMeterState() + "");

                                        createALabel(6, copyDatas.get(j).getCopyState() + "");
                                        createALabel(7, copyDatas.get(j).getCopyTime() + "");
                                        createALabel(8, copyDatas.get(j).getCopyMan() + "");
                                        createALabel(9, copyDatas.get(j).getMeterName() + "");
                                        createALabel(10, copyDatas.get(j).getdBm() + "");
                                        createALabel(11, copyDatas.get(j).getElec() + "");
                                        num++;
                                    }

                                }
                            }
                        }

                    }
                }
                mWritableWorkbook.write();
                mWritableWorkbook.close();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = EXCEL_ERROR;
                mHandler.sendMessage(message);
            } catch (WriteException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = EXCEL_ERROR;
                mHandler.sendMessage(message);
            } finally {
                mProgressDialog.dismiss();
            }
            // 创建表单,其中sheet表示该表格的名字,0表示第一个表格,

            Message message = Message.obtain();
            message.what = EXCEL_OK;
            mHandler.sendMessage(message);

        }
    }

    private boolean checkDate(String copyTime) {
        LogUtil.i("检查日期",copyTime);
        if (mList.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (copyTime.contains(mList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDate2(String copyTime) {
        if (mList.size() == 0) {
            return true;
        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (copyTime.contains(mList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
    private void createALabel(int y, String s) {
        //成功插入一条数据的时候行号加1
        try {
            Label mLabel = new Label(y, num, s);
            ws.addCell(mLabel);
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }


}
///////////////////////////////////////////////////////////////////////////////////////////////

