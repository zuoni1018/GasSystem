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
 * �����˲�
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
    //�����˲��ʱ��ͬʱ��Ҫ�����û���Ϣ

    private String filePath = "";
    private boolean isRun = false;
    private final int EXCEL_ERROR = 0;
    private final int EXCEL_OK = 1;
    private final int EXCEL_PROGRESS = 3;
    private final int EXCEL_FALSE = 4;


    //��ǰ�Ѵ����˲���Ҫ��ִ���Ƿ�ɾ���˲��ж�
    private final int EXCEL_IMPORT_DELETE = 5;
    private final int EXCEL_IMPORT = 6;

    //�����Excel��ʽ������Ҫ��
    private final int EXCEL_FORMAT_ERROR = 7;

    private String bookNo;//�˲���

    private String groupNo;

    private boolean isIC = true;


    private GroupInfoBiz groupInfoBiz;
    private GroupBindBiz groupBindBiz;
    private BookInfoBiz bookInfoBiz;
    private CopyBiz copyBiz;


    private String meterType = "04";


    private int lastColumn;//Excel��Ҫ��������
    private String fileName = "";//��Ҫ������˲�����
    private Workbook book;//���Excel�ļ�
    private Sheet sheet;//���Excel������
    private int Rows;//Excel������


    //IC����
    private final int IMPORT_TYPE_IC_WU_XIAN = 1;
    //����IC����
    private final int IMPORT_TYPE_XI_NING_IC_WU_XIAN = 3;
    //������
    private final int IMPORT_TYPE_WU_XIAN = 2;
    //����������
    private int importType;


    //��ʾ��
    private AlertDialog alertDialog;
    private ProgressDialog mProgressDialog;


    //��Ҫ������˲��Ƿ���ڣ��������Ƿ���Ҫɾ����
    private boolean needDelete = false;
    //����Ҫɾ��  ɾ�����˲���
    private String deleteBookNo;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Dialog dialog = new Dialog(ImportExcelActivity.this);
            switch (msg.what) {
                case EXCEL_ERROR:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("EXCEL ����ʧ�ܣ�����Excel��ʽ�Ƿ���ȷ����������");
                    alertDialog.show();
                    break;
                case EXCEL_OK:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("EXCEL�������");
                    alertDialog.show();
                    break;
                case EXCEL_PROGRESS:
                    break;
                case EXCEL_FALSE:
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("ѡȡ�ļ������ڣ�������ѡ��");
                    alertDialog.show();
                    break;
                case EXCEL_IMPORT_DELETE:
                    //��Ҫ�ж�ɾ���˲����
                    mProgressDialog.dismiss();
                    AlertDialog.Builder bulider = new AlertDialog.Builder(ImportExcelActivity.this);
                    bulider.setTitle("�˲��Ѵ���");
                    bulider.setMessage("���˲��Ѿ����ڣ��Ƿ���Ҫ����ԭ�����˲�");
                    bulider.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.setMessage("����ɾ���ɵ��˲���Ϣ");
                            mProgressDialog.show();
                            //��ø��˲��µ����з���
                            ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(deleteBookNo);
                            for (int i = 0; i < groupInfos.size(); i++) {
                                groupBindBiz.removeGroupBindByGroupNo(groupInfos.get(i).getGroupNo()); // ɾ������󶨱����������
                            }
                            groupInfoBiz.removeGroupInfoByBookNo(deleteBookNo);// ɾ��������Ϣ
                            bookInfoBiz.removeBookInfo(deleteBookNo);// ɾ���˲�����
                            //ɾ�����֮��,�����˲�
                            Message message = Message.obtain();
                            message.what = EXCEL_IMPORT;
                            mHandler.sendMessage(message);
                        }
                    });
                    bulider.setNegativeButton("ȡ��", null);
                    bulider.setCancelable(false);
                    bulider.create().show();
                    break;
                case EXCEL_IMPORT:
                    //ֱ�ӵ���
                    mProgressDialog.dismiss();
                    mProgressDialog.setMessage("���ڴ�Excel�������ݿ��벻Ҫ�˳�App������������");
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
                    //Excel��ʽ����
                    mProgressDialog.dismiss();
                    alertDialog.setMessage("ѡ���Excel��ʽ����ѡ������Ͳ�һ��,������ѡ��");
                    alertDialog.show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("�˲ᵼ��");
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
        mProgressDialog.setMessage("���������Ժ�...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("��ʾ");
        builder.setPositiveButton("ȷ��", null);
        alertDialog = builder.create();
        filePath = (String) SPUtils.get(ImportExcelActivity.this, "filePath", "");
        tvFilePath.setText(filePath);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_import_excel;
    }

    /**
     * ѡ��·������
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                filePath = uri.getPath();
                filePath = FileUtils.getRealFilePath(ImportExcelActivity.this, uri);//��ȡ��ʵ·��
                tvFilePath.setText(filePath);
                SPUtils.put(ImportExcelActivity.this, "filePath", filePath);
            }
        }
    }


    @OnClick({R.id.layout01, R.id.layout02, R.id.btImport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout01:
                //ѡ���ļ�·��
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//�������ͣ����������������ͣ������׺�Ŀ�������д��
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.layout02:
                //ѡ��������
                DataPickerSingleDialog.Builder builder = new DataPickerSingleDialog.Builder(getContext());
                List<String> mList = new ArrayList<>();
                mList.add("IC����");
                mList.add("����IC����");
                mList.add("������");
                builder.setData(mList);
                builder.setOnDataSelectedListener(new OnSingleDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        tv02.setText(itemValue);
                        switch (itemValue) {
                            case "IC����":
                                importType = IMPORT_TYPE_IC_WU_XIAN;
                                isIC = true;
                                break;
                            case "����IC����":
                                isIC = true;
                                importType = IMPORT_TYPE_XI_NING_IC_WU_XIAN;
                                break;
                            case "������":
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
                    if (tv02.getText().toString().trim().equals("��ѡ��������")) {
                        showToast("��ѡ��������");
                        return;
                    }
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
     * ��ȡExcel���������
     */
    public void readExcel() {
        try {
            InputStream is = new FileInputStream(filePath);//���ļ����������
            File file = new File(filePath);
            fileName = file.getName().substring(0, file.getName().length() - 4);
            book = Workbook.getWorkbook(file);//���excel
            book.getNumberOfSheets();
            // ��õ�һ�����������
            sheet = book.getSheet(0);
            Rows = sheet.getRows();//���������
            int Cols = sheet.getColumns();//���������


            //�жϱ������
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

            //�ж������Ƿ����Ҫ��
            if (Cols < lastColumn) {
                Message message = Message.obtain();
                message.what = EXCEL_FORMAT_ERROR;
                mHandler.sendMessage(message);
                return;//���������ִ��
            }

            //�ж��˲��Ƿ��Ѿ�����

            //��ȡ�����˲�
            ArrayList<BookInfo> bookInfos = bookInfoBiz.getBookInfos();
            for (int i = 0; i < bookInfos.size(); i++) {
                if (fileName.equals(bookInfos.get(i).getBookName())) {
                    needDelete = true;
                    deleteBookNo = bookInfos.get(i).getBookNo();//�����Ҫɾ����bookNo
                    break;
                } else {
                    needDelete = false;
                }
            }

            //�Ƿ���Ҫִ��ɾ������
            if (needDelete) {
                //���ͽ��е���
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
     * ��Excel����App�ڵ��뵼������
     */
    private void importExcel() {

        //�����ݿ�������˵�����˵��½��˲�
        BookInfo bkInfo = new BookInfo();
        bkInfo.setBookName(fileName);
        bkInfo.setEstateNo("");//С�����
        bkInfo.setRemark("Excel �˲ᵼ��");
        bkInfo.setStaffNo("");
        //���ñ������
        bkInfo.setMeterTypeNo(meterType);//04 IC���߱�  05�����߱�

        //�����˲���
        bkInfo.setBookNo(StringFormatter.getAddStringNo(bookInfoBiz.getLastBookNo()));
        bookNo = bkInfo.getBookNo();
        //���ݿ�����˲�
        bookInfoBiz.addBookInfo(bkInfo);

        String groupInfoName = "";//��ǰ��������

        //��ӷ���
        if (meterType.equals("05")) {
            //�����߸�ʽ
            //��Ʊ��0 	�ϴζ���1	�ϴ�����2	���ζ���3	��������4	���״̬5
            //����״̬6 	����ʱ��7	����Ա����8 	�������9	�ź�ǿ��10	����11	�ֻ�����12	��������13

            //�� num=1 �п�ʼȡ���� num=0Ϊtab
            for (int num = 1; num < Rows && isRun; num++) {
                //�õ���ǰ�е�21�������ݲ�Ϊ�� ˵��Ϊ�µķ���
                if (!getCellDate(sheet, lastColumn, num).trim().equals("")) {
                    //�����µķ���
                    groupInfoName = getCellDate(sheet, lastColumn, num).trim();//�õ���ǰ�ķ�������
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
                    groupInfoBiz.addGroupInfo(groupInfo);//��ӷ���
                }
                //���������
                //������ڲ�������Ϣ
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

                //���û���Ϣ���ڲ�������
                UserInfo userInfo = new UserInfo();
                userInfo.setTableNumber(getCellDate(sheet, 0, num).trim() + "");//��߱��
                userInfo.setAddress(groupInfoName);//��������
                userInfo.setUserPhone(getCellDate(sheet, 12, num).trim());//�û��ֻ�����
                UserInfoDao userInfoDao = new UserInfoDao(getContext());
                userInfoDao.putUserInfo(userInfo);//�����û���Ϣ

                //�����ݺͷ�����а�
                GroupBind groupBind = new GroupBind();
                groupBind.setMeterName(copyData.getMeterName() + "");
                groupBind.setGroupNo(groupNo);
                groupBind.setMeterNo(copyData.getMeterNo());
                groupBind.setMeterType("05");//������
                groupBindBiz.addGroupBind(groupBind);
            }
        } else if (meterType.equals("04")) {

            if (importType == 1) {
                //��ͨIC����
                //����0 	��Ʊ��1	�������2	�ۼ���3	 ʣ����4	 ������5	 �������6	 ��������7
                //�Ź�������8	����������9 	��״̬10	��״̬������Ϣ11	�����ۼ���12	�����ۼ���13
                //�������ۼ���14	���������ۼ���15	����ʽ16	����ʱ��17	����״̬18	��ǰ����19
                //�ۼ��������20	�ۼƳ�ֵ���21	������ʹ����22	�ź�ǿ��23	�ֻ���24	��������25

                for (int num = 1; num < Rows && isRun; num++) {
                    //�õ���ǰ�е�25��������
                    if (!getCellDate(sheet, lastColumn, num).trim().equals("")) {
                        //�����µķ���
                        groupInfoName = getCellDate(sheet, lastColumn, num).trim();//�õ���ǰ�ķ�������
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.setGroupName(groupInfoName);
                        groupInfo.setEstateNo("");
                        groupInfo.setRemark("Excel ����");
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
                        groupInfoBiz.addGroupInfo(groupInfo);//��ӷ���
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
                    //�½��û�������
                    //���û���Ϣ���ڲ�������
                    UserInfo userInfo = new UserInfo();
                    userInfo.setTableNumber(getCellDate(sheet, 1, num).trim() + "");//��߱��
                    userInfo.setAddress(groupInfoName);//��������
                    userInfo.setUserPhone(getCellDate(sheet, 24, num).trim());//�û��ֻ�����
                    UserInfoDao userInfoDao = new UserInfoDao(getContext());
                    userInfoDao.putUserInfo(userInfo);//�����û���Ϣ


                    // ������
                    GroupBind groupBind = new GroupBind();
                    groupBind.setMeterName(copyDataICRF.getMeterName() + "");
                    groupBind.setGroupNo(groupNo);
                    groupBind.setMeterNo(copyDataICRF.getMeterNo());
                    groupBind.setMeterType("05");
                    groupBindBiz.addGroupBind(groupBind);

                }


            } else if (importType == 3) {
                //��������
                for (int num = 1; num < Rows && isRun; num++) {

                    String address = getCellDate(sheet, 4, num).trim();//���Ƿ�����

                    if (!address.equals(groupInfoName) && !address.equals("")) {
                        //�½�һ������

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
                        groupInfoBiz.addGroupInfo(groupInfo);//��ӷ���
                    }

                    CopyDataICRF copyDataICRF = new CopyDataICRF();
                    copyDataICRF.setNo01(getCellDate(sheet, 0, num).trim());
                    copyDataICRF.setNo02(getCellDate(sheet, 1, num).trim());
                    copyDataICRF.setMeterNo(getCellDate(sheet, 2, num).trim());//���ñ����
                    copyDataICRF.setMeterName(getCellDate(sheet, 5, num).trim() + getCellDate(sheet, 6, num).trim());//�û���
                    copyDataICRF.setName(getCellDate(sheet, 6, num).trim());
                    copyDataICRF.setUnitPrice(getCellDate(sheet, 10, num).trim());//���õ���
                    copyDataICRF.setCopyState(0);

                    //�½�һ���û���Ϣ��
                    UserInfo userInfo = new UserInfo();
                    userInfo.setTableNumber(getCellDate(sheet, 2, num).trim() + "");//��߱��
                    userInfo.setUserNum(getCellDate(sheet, 0, num).trim());//�û����
                    userInfo.setXiNingTableNumber(getCellDate(sheet, 1, num).trim());//������߱��
                    userInfo.setUserName(getCellDate(sheet, 6, num).trim());//�û�����
                    userInfo.setAddress(getCellDate(sheet, 5, num).trim());//�����ַ
                    userInfo.setXiNingUnitPrice(getCellDate(sheet, 10, num).trim());
//                    userInfo.saveOrUpdate("tableNumber=?",110+"");//ȥ����
//                    userInfo.save();
                    UserInfoDao userInfoDao = new UserInfoDao(ImportExcelActivity.this);
                    userInfoDao.putUserInfo(userInfo);

                    if (!copyDataICRF.getMeterNo().equals("")) {
                        copyBiz.addCopyDataICRF(copyDataICRF);
                        // ������
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

        //����ɹ�
        Message message = Message.obtain();
        message.what = EXCEL_OK;
        mHandler.sendMessage(message);
        //�ر�
        book.close();
    }

    private int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }

    }

    //��ȡ������������
    private String getCellDate(Sheet sheet, int i, int j) {
        Cell cell1 = sheet.getCell(i, j);
        String result = cell1.getContents();
        return result;
    }
}
