package com.pl.gassystem;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.activity.SettingActivity;
import com.pl.gassystem.base.BaseTitleActivity;
import com.pl.gassystem.utils.DensityUtils;
import com.pl.gassystem.utils.JumpActivityUtils;
import com.pl.utils.GlobalConsts;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * ����ϸ�ڽ���
 */

public class CopyDetailsActivity extends BaseTitleActivity {

    //��״ͼ�ľ��κ�ֵ
    private int screenWidth;
    private double PerPixel;
    private ArrayList<String> meterNos;
    private String meterTypeNo;
    private GroupInfo gpInfo;
    private String groupName;
    private CopyBiz copyBiz;
    private ImageView ivNoCopy;
    private ImageView ivCopy;
    private TextView tvNoCopyNum;
    private TextView tvCopyNum;

    private boolean isRun = true;

    private ProgressBar mProgressBar;


    private boolean haveWarning = false;//�Ƿ���ڱ���
    private ImageView ivWarn;
    private LinearLayout layoutWarnBig;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            mProgressBar.setVisibility(View.GONE);
            LogUtil.i("���" + mNoCoypNum + "====" + mCoypNum);
            if (haveWarning) {
                ivWarn.setImageResource(R.mipmap.warning_big);
            } else {
                ivWarn.setImageResource(R.mipmap.warning_normal);
            }
            setHeight(mNoCoypNum, mCoypNum);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("onResume");
        mProgressBar.setVisibility(View.VISIBLE);
        mCoypNum = 0;
        mNoCoypNum = 0;
        isRun = true;
        t = new mySearchThread();
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRun = false;
    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();

    }

    @Override
    protected void initData() {

        meterNos = getIntent().getStringArrayListExtra("meterNos");

        meterTypeNo = getIntent().getStringExtra("meterTypeNo");
        setTitle("" + getIntent().getStringExtra("name") + " �������");
        gpInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");

        if (gpInfo != null) {
            groupName = gpInfo.getGroupName();
        } else {
            groupName = "δ֪����";
        }

        copyBiz = new CopyBiz(this);


        int noCopyNum = getIntent().getIntExtra("noCopy", 0);
        int CopyNum = getIntent().getIntExtra("CopyNum", 0);
//        setHeight(noCopyNum, CopyNum);

    }

    /**
     * ������״ͼ�߶�
     */
    public void setHeight(int noCopyNum, int CopyNum) {
//        noCopyNum=8;
//        CopyNum=5;
        int height = DensityUtils.dp2px(this, 103);
        double c = (noCopyNum * 1.000 / (noCopyNum + CopyNum));
        int noCopyHeight = (int) (c * height);
        int CopyHeight = (int) ((1 - c) * height);
        tvCopyNum.setText(CopyNum + "");
        tvNoCopyNum.setText(noCopyNum + "");
        ViewGroup.LayoutParams para1;
        para1 = ivNoCopy.getLayoutParams();
        para1.height = noCopyHeight + 4;
        ivNoCopy.setLayoutParams(para1);
        ViewGroup.LayoutParams para2;
        para2 = ivCopy.getLayoutParams();
        para2.height = CopyHeight + 4;
        ivCopy.setLayoutParams(para2);
    }


    private int mCoypNum = 0;
    private int mNoCoypNum = 0;

    private mySearchThread t;


    private class mySearchThread extends Thread {
        @Override
        public void run() {
//            super.run();
            if (isRun) {
                meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());//���ÿ��¥ ��ÿ��ס������Ϣ
                if (meterTypeNo.equals("04")) {// IC������
                    LogUtil.i("IC������");
                    ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                    if (copyDataICRFs != null) {
                        for (int j = 0; j < copyDataICRFs.size(); j++) {
//                            if (copyDataICRFs.get(j).getCopyState() == 1) {
//                                mCoypNum++;
//                            } else {
//                                mNoCoypNum++;
//                            }
                            //��ѯ����һ������

                            CopyDataICRF mCopyDataICRF = copyBiz.getCopyDataICRFById(copyDataICRFs.get(j).getId() + "");

                            if (mCopyDataICRF.getCopyState() == 1) {
                                mCoypNum++;
                            } else {
                                mNoCoypNum++;
                            }

                            //
                            try {
                                double dd = Double.valueOf(mCopyDataICRF.getCumulant());
                                if (dd > 10000) {
                                    haveWarning = true;
                                }
                            } catch (NumberFormatException e) {
//                                Toast.makeText(CopyDetailsActivity.this, "zzz", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                } else if (meterTypeNo.equals("05")) {// ������
                    ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, 2);
                    LogUtil.i("������" + copyDatas.size());
                    if (copyDatas != null) {
                        for (int j = 0; j < copyDatas.size(); j++) {


//                            if (copyDatas.get(j).getCopyState() == 1) {
//                                mCoypNum++;
//                            } else {
//                                mNoCoypNum++;
//                            }
                            CopyData mCopyData = copyBiz.getCopyDataById(copyDatas.get(j).getId() + "");//�õ����µ�һ������
                            if (mCopyData.getCopyState() == 1) {
                                mCoypNum++;
                            } else {
                                mNoCoypNum++;
                            }
                            try {
                                double dd = Double.valueOf(mCopyData.getCurrentShow());
                                if (dd > 10000) {
                                    haveWarning = true;
                                }
                            } catch (NumberFormatException e) {
//                                Toast.makeText(CopyDetailsActivity.this, "zzz", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                Message msg = Message.obtain();
                mHandler.sendMessage(msg);
                LogUtil.i("��ѯ������");
            }
        }
    }

    @Override
    protected void initOnClickListener() {
        findViewById(R.id.btBeginCopy).setOnClickListener(this);
        findViewById(R.id.btNoCopy).setOnClickListener(this);
        findViewById(R.id.btShowNoCopy).setOnClickListener(this);
        findViewById(R.id.btCopyAll).setOnClickListener(this);
        findViewById(R.id.btShowAll).setOnClickListener(this);
        findViewById(R.id.btMaintain).setOnClickListener(this);
        findViewById(R.id.btSetting).setOnClickListener(this);

        layoutWarnBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveWarning) {
                    if (gpInfo != null) {
                        meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                        if (meterNos != null && meterNos.size() > 0) {
                            Intent intent = new Intent(CopyDetailsActivity.this, CopyResultActivityWarnBig.class);
                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                            intent.putExtra("meterNos", meterNos);
                            intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                            startActivity(intent);
                        } else {
                            Toast.makeText(CopyDetailsActivity.this, "�÷������ޱ��", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "δ֪����", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "���ζ���û�д���һ��ı�", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initUI() {
//        viewCopyNum = findViewById(R.id.viewCopyNum);
//        viewNoCopyNum = findViewById(R.id.viewNoCopyNum);
//        tvNoCopyNum = (TextView) findViewById(R.id.tvNoCopyNum);
//        tvCopyNum = (TextView) findViewById(R.id.tvCopyNum);

        screenWidth = ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 19);
//        PerPixel = screenWidth * 4 / 1000.000000;
        PerPixel = DensityUtils.dp2px(this, 160) / 300.0000;
        LogUtil.d("PerPixel", PerPixel + "");
        ivCopy = (ImageView) findViewById(R.id.ivCopy);
        ivNoCopy = (ImageView) findViewById(R.id.ivNoCopy);

        tvNoCopyNum = (TextView) findViewById(R.id.tvNoCopyNum);
        tvCopyNum = (TextView) findViewById(R.id.tvCopyNum);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.GONE);


        ivWarn = (ImageView) findViewById(R.id.ivWarn);
        layoutWarnBig = (LinearLayout) findViewById(R.id.layoutWarnBig);
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_copy_detials;
    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.btBeginCopy://��ʼ����
                intent = new Intent(CopyDetailsActivity.this, GroupBindActivity.class);
                intent.putExtra("GroupInfo", gpInfo);
                startActivity(intent);
                break;

            case R.id.btNoCopy://��ȡδ��
                if (gpInfo != null) {
                    //��ȡδ�����б�
                    meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
                    meterTypeNo = gpInfo.getMeterTypeNo();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyingActivity.class);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "�÷����������������δ��ȡ��", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "δ֪�����޷�Ⱥ��", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btShowNoCopy://��ʾδ��
                if (gpInfo != null) {
                    meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWUNCOPY);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "�÷����������������δ��ȡ��", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "δ֪����",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCopyAll://��ȡȫ��
                if (gpInfo != null) {
                    meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                    meterTypeNo = gpInfo.getMeterTypeNo();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyingActivity.class);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                        // intent.putExtra("isCopyUnRead", false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "�÷������ޱ�ߣ��޷�����",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "δ֪�����޷�Ⱥ��",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btShowAll://��ʾȫ��

                if (gpInfo != null) {
                    meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "�÷������ޱ��", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "δ֪����", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btMaintain://���ά��
                JumpActivityUtils.jumpToMaintenanceActivity(CopyDetailsActivity.this);
                break;
            case R.id.btSetting://ϵͳ����
                intent = new Intent(CopyDetailsActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
