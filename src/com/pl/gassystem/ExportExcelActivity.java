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

                    dialog.setTitle("EXCEL ����ʧ��");
                    num = 1;
                    dialog.show();
                    break;
                case EXCEL_OK:


                    dialog.setTitle("EXCEL �������");
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
        setTitle("����Excel");
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
                mProgressDialog.setMessage("���ڵ���excel���벻Ҫ�˳�App����������");
                mProgressDialog.show();
                exportXiNingICExcelThread t = new exportXiNingICExcelThread();
                t.start();

                break;

            case R.id.btSearch:
//                showDialog(DATE_DIALOG);
                if (mList.size() == 0) {

                    showMyDialog("��δѡ�����ڣ���Ϊ����ѯ�������ڵ�����");
                } else {
                    String message = "��Ϊ����ѯ����ѡ�������\n";
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
                    Toast.makeText(this, "����ѯ10��", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(DATE_DIALOG);
                }
                break;
        }
    }

    Dialog dialog;

    private void showMyDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("��ʾ");
        builder.setMessage(message);
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mProgressDialog.setMessage("���ڵ���excel���벻Ҫ�˳�App����������");
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
        mProgressDialog.setMessage("���������Ժ�...");
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
     * ����excel
     * ���̣�
     * ͨ���˲��ѯ�����г���㣨�����˲�������������Excel�� ������
     * ͨ��������ѯ���еĳ�����Ϣ
     * �ļ������˲���.xls��
     * �ȳ������Ϊ����
     * �ٷ�����
     * ��ÿ�������Ϣ
     */

    private String booksName;


    private String[] CopyDataTab = {
            "��Ʊ��", "�ϴζ���", "�ϴ�����", "���ζ���",
            "��������", "���״̬", "����״̬", "����ʱ��",
            "����Ա����", "�������", "�ź�ǿ��", "����", "", "��������"};


    private String[] CopyXiNingTab = {
            "�û����", "��߱��", "��߸ֺ�", "��ַ����", "�û�����", "����ۼ�����", "����ۼ��������",
            "���ʣ����", "��ǰ���м۸�", "�ɼ���ʽ", "�ɼ�ʱ��", "�ɼ��ɹ���־", "ʧ��ԭ��", "���ʱ��",
            "����״̬", "IC����������", "Զ�̹�������", "�ۼ��ϱ������", "��߹���״̬"
    };
    private String[] CopyDataICRFTab = {
            "����", "��Ʊ��", "�������", "�ۼ���", "ʣ����",
            "������", "�������", "��������", "�Ź�������", "����������",
            "��״̬", "��״̬������Ϣ", "�����ۼ���", "�����ۼ���", "�������ۼ���",
            "���������ۼ���", "����ʽ", "����ʱ��", "����״̬", "��ǰ����",
            "�ۼ��������", "�ۼƳ�ֵ���", "������ʹ����", "�ź�ǿ��", "", "��������"};


    private WritableWorkbook mWritableWorkbook;
    private WritableSheet ws;
    public int num = 1;

    private class exportXiNingICExcelThread extends Thread {

        @Override
        public void run() {

            File file = new File(getSDPath() + "/HongHuDate"); //�����ļ�Ŀ¼
            FileUtil.makeDir(file);//�ȴ�����ǰ�˲��excel��
            //����excel�ļ�
            File file2 = new File(file.toString() + "/" + bookName + mYear + getDateString(mMonth + 1) + getDateString(mDay) + ".xls");
            filePath = file2.toString();
            //ÿ�ζ��������ɵ�ǰ�ļ�
            if (file2.exists()) {
                file2.delete();
            }
            try {
                mWritableWorkbook = Workbook.createWorkbook(file2);
                ws = mWritableWorkbook.createSheet("���Ա�", 0);
                ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);

                if (isRun) {

                    //����tab

                    //�ڵڶ��д���tab
                    for (int j = 0; j < CopyXiNingTab.length; j++) {
                        Label mLabel2 = new Label(j, 0, CopyXiNingTab[j]);
                        ws.addCell(mLabel2);
                    }

//
                    for (int i = 0; i < groupInfos.size(); i++) {

                        String groupName = groupInfos.get(i).getGroupName();
                        //��д����������ÿ������ĵ�һ��������һ����ӷ�������
                        ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());//���ÿ��¥ ��ÿ��ס������Ϣ
                        ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);

                        if (copyDataICRFs != null) {
                            for (int j = 0; j < copyDataICRFs.size(); j++) {
                                createALabel(0, copyDataICRFs.get(j).getNo01() + "");
                                createALabel(1, copyDataICRFs.get(j).getNo02() + "");
                                createALabel(2, copyDataICRFs.get(j).getMeterNo() + "");//��д��߸ֺ�
                                createALabel(3, groupName + "");//��д��߸ֺ�
                                createALabel(4, copyDataICRFs.get(j).getName() + "");//�û�����
                                createALabel(5, copyDataICRFs.get(j).getCumulant() + "");//����ۼ�����
                                createALabel(6, copyDataICRFs.get(j).getAccMoney() + "");//�ۼ��������
                                createALabel(7, copyDataICRFs.get(j).getSurplusMoney() + "");//�ۼ��������
                                createALabel(8, copyDataICRFs.get(j).getUnitPrice() + "");
                                createALabel(9, copyDataICRFs.get(j).getCopyWay() + "");
                                createALabel(10, copyDataICRFs.get(j).getCopyTime() + "");//����ʱ��
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
            // ������,����sheet��ʾ�ñ�������,0��ʾ��һ�����,

            Message message = Message.obtain();
            message.what = EXCEL_OK;
            mHandler.sendMessage(message);

        }
    }


    private class exportCopyDataExcelThread extends Thread {

        @Override
        public void run() {


            File file = new File(getSDPath() + "/HongHuDate"); //�����ļ�Ŀ¼
            FileUtil.makeDir(file);//�ȴ�����ǰ�˲��excel��
            //����excel�ļ�
            File file2 = new File(file.toString() + "/" + bookName + mYear + getDateString(mMonth + 1) + getDateString(mDay) + ".xls");
            filePath = file2.toString();
            //ÿ�ζ��������ɵ�ǰ�ļ�
            if (file2.exists()) {
                file2.delete();
            }
            try {
                mWritableWorkbook = Workbook.createWorkbook(file2);
                ws = mWritableWorkbook.createSheet("���Ա�", 0);
                ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);

                if (isRun) {

                    //����tab
                    if (meterTypeNo.equals("05")) {
                        //�ڵڶ��д���tab
                        for (int j = 0; j < CopyDataTab.length; j++) {
                            Label mLabel2 = new Label(j, 0, CopyDataTab[j]);
                            ws.addCell(mLabel2);
                        }
//                        //��(0,0)λ�ô���meterTypeNo
//                        Label mLabel2 = new Label(0, 0, "");
//                        ws.addCell(mLabel2);
                    } else if (meterTypeNo.equals("04")) {
                        //�ڵڶ��д���tab
                        for (int j = 0; j < CopyDataICRFTab.length; j++) {
                            Label mLabel2 = new Label(j, 0, CopyDataICRFTab[j]);
                            ws.addCell(mLabel2);
                        }
//                        Label mLabel2 = new Label(0, 0, "4");
//                        ws.addCell(mLabel2);
                    }


                    for (int i = 0; i < groupInfos.size(); i++) {

                        //��д����������ÿ������ĵ�һ��������һ����ӷ�������
                        if (meterTypeNo.equals("05")) {
                            //�ڵ�CopyDataTab.length+1��
                            Label mLabel2 = new Label(CopyDataTab.length - 1, num, groupInfos.get(i).getGroupName());
                            ws.addCell(mLabel2);
                        } else if (meterTypeNo.equals("04")) {
                            String a = groupInfos.get(i).getGroupName();
                            //�ڵ�CopyDataICRFTab.length+1��
                            Label mLabel2 = new Label(CopyDataICRFTab.length - 1, num, groupInfos.get(i).getGroupName());
                            ws.addCell(mLabel2);
                        }

                        ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());//���ÿ��¥ ��ÿ��ס������Ϣ

                        if (meterTypeNo.equals("04")) {// IC������

                            ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                            if (copyDataICRFs != null) {
                                for (int j = 0; j < copyDataICRFs.size(); j++) {
                                    //�ж��Ƿ�����ѡ������
                                    if (checkDate(copyDataICRFs.get(j).getCopyTime())) {
                                        //��������
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
                        } else if (meterTypeNo.equals("05")) {// ������
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
            // ������,����sheet��ʾ�ñ�������,0��ʾ��һ�����,

            Message message = Message.obtain();
            message.what = EXCEL_OK;
            mHandler.sendMessage(message);

        }
    }

    private boolean checkDate(String copyTime) {
        LogUtil.i("�������",copyTime);
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
        //�ɹ�����һ�����ݵ�ʱ���кż�1
        try {
            Label mLabel = new Label(y, num, s);
            ws.addCell(mLabel);
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }


}
///////////////////////////////////////////////////////////////////////////////////////////////

