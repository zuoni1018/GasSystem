package com.pl.gassystem;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.base.BaseTitleActivity;
import com.pl.utils.DensityUtils;
import com.pl.utils.GlobalConsts;
import com.pl.utils.LogUtil;
import com.pl.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 抄表细节界面
 */

public class CopyDetailsActivity extends BaseTitleActivity {

    //柱状图的矩形和值
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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            mProgressBar.setVisibility(View.GONE);
            LogUtil.i("结果"+mNoCoypNum+"===="+mCoypNum);
            setHeight(mNoCoypNum,mCoypNum);
        }
    };


    @Override
    protected void initData() {

        meterNos = getIntent().getStringArrayListExtra("meterNos");

        meterTypeNo = getIntent().getStringExtra("meterTypeNo");
        setTitle("" + getIntent().getStringExtra("name") + " 抄表情况");
        gpInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");

        if (gpInfo != null) {
            groupName = gpInfo.getGroupName();
        } else {
            groupName = "未知分组";
        }

        copyBiz = new CopyBiz(this);


        int noCopyNum = getIntent().getIntExtra("noCopy", 0);
        int CopyNum = getIntent().getIntExtra("CopyNum", 0);
//        setHeight(noCopyNum, CopyNum);

    }


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
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("onResume");
        mProgressBar.setVisibility(View.VISIBLE);
        mCoypNum = 0;
        mNoCoypNum = 0;
        isRun = true;
        t=new mySearchThread();
        t.start();
    }


    private class mySearchThread extends Thread{
        @Override
        public void run() {
//            super.run();
            if (isRun) {
                meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());//获得每栋楼 里每个住户的信息
                if (meterTypeNo.equals("04")) {// IC卡无线
                    LogUtil.i("IC卡无线");
                    ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                    if (copyDataICRFs != null) {
                        for (int j = 0; j < copyDataICRFs.size(); j++) {
                            if (copyDataICRFs.get(j).getCopyState() == 1) {
                                mCoypNum++;
                            } else {
                                mNoCoypNum++;
                            }
                        }
                    }
                } else if (meterTypeNo.equals("05")) {// 纯无线
                    ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, 2);
                    LogUtil.i("纯无线" + copyDatas.size());
                    if (copyDatas != null) {
                        for (int j = 0; j < copyDatas.size(); j++) {
                            if (copyDatas.get(j).getCopyState() == 1) {
                                mCoypNum++;
                            } else {
                                mNoCoypNum++;
                            }
                        }
                    }
                }
                Message msg = Message.obtain();
                mHandler.sendMessage(msg);
                LogUtil.i("查询结束啦");
            }
        }
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
    protected void initOnClickListener() {
        findViewById(R.id.btBeginCopy).setOnClickListener(this);
        findViewById(R.id.btNoCopy).setOnClickListener(this);
        findViewById(R.id.btShowNoCopy).setOnClickListener(this);
        findViewById(R.id.btCopyAll).setOnClickListener(this);
        findViewById(R.id.btShowAll).setOnClickListener(this);
        findViewById(R.id.btMaintain).setOnClickListener(this);
        findViewById(R.id.btSetting).setOnClickListener(this);
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

    }


    @Override
    protected int setLayout() {
        return R.layout.activity_copy_detials;
    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.btBeginCopy://开始抄表
                intent = new Intent(CopyDetailsActivity.this, GroupBindActivity.class);
                intent.putExtra("GroupInfo", gpInfo);
                startActivity(intent);
                break;

            case R.id.btNoCopy://抄取未抄
                if (gpInfo != null) {
                    //获取未抄了列表
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
                        Toast.makeText(CopyDetailsActivity.this, "该分组最近抄表结果中无未抄取表", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "未知分组无法群抄", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btShowNoCopy://显示未抄
                if (gpInfo != null) {
                    meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWUNCOPY);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "该分组最近抄表结果中无未抄取表", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "未知分组",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCopyAll://抄取全部
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
                        Toast.makeText(CopyDetailsActivity.this, "该分组内无表具，无法抄表",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "未知分组无法群抄",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btShowAll://显示全部

                if (gpInfo != null) {
                    meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(CopyDetailsActivity.this, "该分组内无表具", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CopyDetailsActivity.this, "未知分组", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btMaintain://表具维护
                intent = new Intent(CopyDetailsActivity.this, MaintenanceActivity.class);
                startActivity(intent);
                break;
            case R.id.btSetting://系统设置
                intent = new Intent(CopyDetailsActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
