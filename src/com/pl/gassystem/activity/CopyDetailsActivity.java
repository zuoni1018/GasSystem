package com.pl.gassystem.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.bll.CopyBiz;
import com.pl.bll.SetBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.CopyResultActivity;
import com.pl.gassystem.CopyResultActivityWarnBig;
import com.pl.gassystem.GroupBindActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.utils.DensityUtils;
import com.pl.gassystem.utils.JumpActivityUtils;
import com.pl.utils.GlobalConsts;
import com.zuoni.zuoni_common.dialog.loading.LoadingDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * ����ϸ�ڽ���
 */

public class CopyDetailsActivity extends BaseTitleActivity {
    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.ivCopy)
    ImageView ivCopy;
    @BindView(R.id.tvNoCopyNum)
    TextView tvNoCopyNum;
    @BindView(R.id.ivNoCopy)
    ImageView ivNoCopy;
    @BindView(R.id.layoutBeginCopy)
    LinearLayout layoutBeginCopy;
    @BindView(R.id.layoutNoCopy)
    LinearLayout layoutNoCopy;
    @BindView(R.id.layoutShowNoCopy)
    LinearLayout layoutShowNoCopy;
    @BindView(R.id.layoutCopyAll)
    LinearLayout layoutCopyAll;
    @BindView(R.id.layoutShowAll)
    LinearLayout layoutShowAll;
    @BindView(R.id.layoutMaintain)
    LinearLayout layoutMaintain;
    @BindView(R.id.layoutSetting)
    LinearLayout layoutSetting;
    @BindView(R.id.ivWarn)
    ImageView ivWarn;
    @BindView(R.id.layoutWarnBig)
    LinearLayout layoutWarnBig;



    private ArrayList<String> meterNos;
    private String meterTypeNo;
    private GroupInfo gpInfo;
    private String groupName;
    private CopyBiz copyBiz;

    private boolean isRun = true;

    private int mCopyNum;
    private int mNoCopyNum;
    private boolean haveWarning = false;

    private SetBiz setBiz;
    private String runMode = "00";
    private String commandType=HtSendMessage.COMMAND_TYPE_COPY_NORMAL;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (haveWarning) {
                ivWarn.setImageResource(R.mipmap.warning_big);
            } else {
                ivWarn.setImageResource(R.mipmap.warning_normal);
            }
            setHeight(mNoCopyNum, mCopyNum);
            loadingDialog.dismiss();
        }
    };

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

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
        setHeight(noCopyNum, CopyNum);

        LoadingDialog.Builder builder = new LoadingDialog.Builder(getContext());
        builder.setMessage("������...");
        loadingDialog = builder.create();
        setBiz = new SetBiz(getContext());
        runMode = setBiz.getRunMode();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_copy_details;
    }

    @OnClick({
            R.id.layoutBeginCopy, R.id.layoutNoCopy, R.id.layoutShowNoCopy,
            R.id.layoutCopyAll, R.id.layoutShowAll, R.id.layoutMaintain,
            R.id.layoutSetting, R.id.layoutWarnBig})
    public void onViewClicked(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.layoutBeginCopy:
                //��ʼ���� ���������Ϣ
                intent = new Intent(CopyDetailsActivity.this, GroupBindActivity.class);
                intent.putExtra("GroupInfo", gpInfo);
                startActivity(intent);
                break;
            case R.id.layoutNoCopy:
                if (gpInfo != null) {
                    loadingDialog.show();
                    //��ȡδ�����б�
                    meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
                    loadingDialog.dismiss();
                    meterTypeNo = gpInfo.getMeterTypeNo();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, JumpActivityUtils.getCopyingActivity(getContext()));
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                        //�������Ҫ���Ĳ���(����ûͳһ��)
                        intent.putExtra("bookNos", meterNos);
                        intent.putExtra("commandType", commandType);//��������ָ��
                        intent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//Ⱥ��
                        if(setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("ѡ��ȡ�����߱�����");
                            final String[] cities = {"ʵʱ����", "��ȡ������"};
                            builder.setItems(cities, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0){
                                        intent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                                    }else {
                                        intent.putExtra("commandType",HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                                    }
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        }else {
                            startActivity(intent);
                        }
                    } else {
                        showToast("�÷������ޱ��");
                    }
                } else {
                    showToast("δ֪����");
                }
                break;
            case R.id.layoutShowNoCopy:
                if (gpInfo != null) {
                    loadingDialog.show();
                    meterNos = copyBiz.GetCopyUnReadMeterNo(gpInfo.getGroupNo(), gpInfo.getMeterTypeNo());
                    loadingDialog.dismiss();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWUNCOPY);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        showToast("�÷������ޱ��");
                    }
                } else {
                    showToast("δ֪����");
                }
                break;
            case R.id.layoutCopyAll:
                if (gpInfo != null) {
                    loadingDialog.show();
                    meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                    loadingDialog.dismiss();
                    meterTypeNo = gpInfo.getMeterTypeNo();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, JumpActivityUtils.getCopyingActivity(getContext()));
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);

                        //�������Ҫ���Ĳ���(����ûͳһ��)
                        intent.putExtra("bookNos", meterNos);
                        intent.putExtra("commandType", commandType);//��������ָ��
                        intent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//Ⱥ��

                        if(setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("ѡ��ȡ�����߱�����");
                            final String[] cities = {"ʵʱ����", "��ȡ������"};
                            builder.setItems(cities, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0){
                                        intent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                                    }else {
                                        intent.putExtra("commandType",HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                                    }
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        }else {
                            startActivity(intent);
                        }

                    } else {
                        showToast("�÷������ޱ��");
                    }
                } else {
                    showToast("δ֪����");
                }
                break;
            case R.id.layoutShowAll:
                //��ʾȫ��
                if (gpInfo != null) {
                    // ��������ȥ��ѯ�÷����°󶨵ı���
                    // ��ʼ������������ӻ�ɾ����
                    loadingDialog.show();
                    meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                    loadingDialog.dismiss();
                    if (meterNos != null && meterNos.size() > 0) {
                        intent = new Intent(CopyDetailsActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                        intent.putExtra("meterNos", meterNos);//���
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        showToast("�÷������ޱ��");
                    }
                } else {
                    showToast("δ֪����");
                }
                break;
            case R.id.layoutMaintain:
                //���ά��
                JumpActivityUtils.jumpToMaintenanceActivity(CopyDetailsActivity.this);
                break;
            case R.id.layoutSetting:
                //ϵͳ����
                intent = new Intent(CopyDetailsActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.layoutWarnBig:
                if (haveWarning) {
                    if (gpInfo != null) {
                        loadingDialog.show();
                        meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());
                        loadingDialog.dismiss();
                        if (meterNos != null && meterNos.size() > 0) {
                            intent = new Intent(CopyDetailsActivity.this, CopyResultActivityWarnBig.class);
                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                            intent.putExtra("meterNos", meterNos);
                            intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                            startActivity(intent);
                        } else {
                            showToast("�÷������ޱ��");
                        }
                    } else {
                        showToast("δ֪����");
                    }
                } else {
                    showToast("���ζ���û�д���һ��ı�");
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //������ʾ��ʱ��ˢ������
        mCopyNum = 0;
        mNoCopyNum = 0;
        isRun = true;
        loadingDialog.show();
        new SearchThread().start();//����һ���߳�ȥˢ��
    }

    /**
     * ������״ͼ�߶�
     */
    public void setHeight(int noCopyNum, int CopyNum) {
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

    private class SearchThread extends Thread {
        @Override
        public void run() {
            if (isRun) {
                meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());//���ÿ��¥ ��ÿ��ס������Ϣ
                if (meterTypeNo.equals("04")) {// IC������
                    ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                    if (copyDataICRFs != null) {
                        for (int j = 0; j < copyDataICRFs.size(); j++) {
                            if (copyDataICRFs.get(j).getCopyState() == 1) {
                                mCopyNum++;
                            } else {
                                mNoCopyNum++;
                            }
                            try {
                                double dd = Double.valueOf(copyDataICRFs.get(j).getCumulant());
                                if (dd > 10000) {
                                    haveWarning = true;
                                }
                            } catch (NumberFormatException ignored) {

                            }
                        }
                    }
                } else if (meterTypeNo.equals("05")) {// ������
                    ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, 2);
                    if (copyDatas != null) {
                        for (int j = 0; j < copyDatas.size(); j++) {

                            if (copyDatas.get(j).getCopyState() == 1) {
                                mCopyNum++;
                            } else {
                                mNoCopyNum++;
                            }
                            try {
                                double dd = Double.valueOf(copyDatas.get(j).getCurrentShow());
                                if (dd > 10000) {
                                    haveWarning = true;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
                Message msg = Message.obtain();
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
            }
        }
    }
}
