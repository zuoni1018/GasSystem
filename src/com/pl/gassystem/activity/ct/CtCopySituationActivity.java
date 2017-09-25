package com.pl.gassystem.activity.ct;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pl.gassystem.AppUrl;
import com.pl.gassystem.MaintenanceActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.SettingActivity;
import com.pl.gassystem.bean.ct.ColletorMeterBean;
import com.pl.gassystem.bean.ct.Concentrator;
import com.pl.gassystem.bean.gson.GetCollectorInfoByCollectorNo;
import com.pl.gassystem.bean.gson.GetColletorMeter;
import com.pl.gassystem.bean.gson.MoveCommunicatesCtrlCmd;
import com.pl.gassystem.utils.DensityUtils;
import com.pl.gassystem.utils.LogUtil;
import com.pl.utils.GlobalConsts;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoni.zuoni_common.dialog.loading.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * ��ǰ�������ĳ�������
 */

public class CtCopySituationActivity extends CtBaseTitleActivity {

    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.ivCopy)
    ImageView ivCopy;
    @BindView(R.id.tvNoCopyNum)
    TextView tvNoCopyNum;
    @BindView(R.id.ivNoCopy)
    ImageView ivNoCopy;

    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private Intent mIntent;

    private String collectorNo;//���������

    private LoadingDialog loadingDialog;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_situation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        collectorNo = getIntent().getStringExtra("CollectorNo");
        setTitle(collectorNo + "�ĳ�������");

        if (collectorNo == null) {
            showToast("������������");
            progressDialog.dismiss();
            finish();
        } else {
            if (collectorNo.equals("")) {
                showToast("������������");
                progressDialog.dismiss();
                finish();
            } else {
                //ͨ�����������ȥ��ѯ����
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getInfo();
                    }
                });
                getInfo();
                LoadingDialog.Builder builder=new LoadingDialog.Builder(getContext());
                builder.setMessage("������...");
                loadingDialog=builder.create();
            }
        }

    }

    /**
     * ��ȡ��״ͼ��Ϣ
     */
    private void getInfo() {

        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.GET_COLLECTOR_INFO_BY_COLLECTOR_NO)
                .addParams("CollectorNo", collectorNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("������������Ϣ", e.toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                        showToast("������������Ϣ");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("������������Ϣ", response);
                        Gson gson = new Gson();
                        GetCollectorInfoByCollectorNo mGetCollectorInfoByCollectorNo = gson.fromJson(response, GetCollectorInfoByCollectorNo.class);
                        mSwipeRefreshLayout.setRefreshing(false);
                        setChart(mGetCollectorInfoByCollectorNo.getCollectorInfoByCollectorNo().get(0));
                    }
                });
    }

    //����ͼ��
    private void setChart(Concentrator concentrator) {
        tvCopyNum.setText(concentrator.getReadNum());
        tvNoCopyNum.setText(concentrator.getNotReadNum());

        //������״ͼ�߶�
        int height = DensityUtils.dp2px(this, 103);
        double c = (concentrator.getTrueNotReadNum() * 1.000 / (concentrator.getTrueAllNum()));
        double c2 = (concentrator.getTrueReadNum() * 1.000 / (concentrator.getTrueAllNum()));
        int noCopyHeight = (int) (c * height);
        int CopyHeight = (int) (c2 * height);
        ViewGroup.LayoutParams para1;
        para1 = ivNoCopy.getLayoutParams();
        para1.height = noCopyHeight + 4;
        ivNoCopy.setLayoutParams(para1);
        ViewGroup.LayoutParams para2;
        para2 = ivCopy.getLayoutParams();
        para2.height = CopyHeight + 4;
        ivCopy.setLayoutParams(para2);
    }


    private AlertDialog alertDialog;


    /**
     * ���¼�������ť
     */
    private void upDataConcentrator() {

        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.MOVE_COMMUNICATES_CTRL_CMD)
                .addParams("CollectorNo", collectorNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("���¼�����", e.toString());
                        showToast("�������쳣");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("���¼�����", response);
                        Gson gson = new Gson();
                        MoveCommunicatesCtrlCmd mMoveCommunicatesCtrlCmd = gson.fromJson(response, MoveCommunicatesCtrlCmd.class);
                        showToast(mMoveCommunicatesCtrlCmd.getMsg());
                        getInfo();

                    }
                });

    }

    /**
     * ��ȡ���б�
     */
    private void getAllBook(String readState) {
        if(loadingDialog!=null){
            loadingDialog.show();
        }

        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.GETCOLLETOR_METER)
                .addParams("CollectorNo", collectorNo)//������
                .addParams("readState",readState)//��ѯȫ��
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("���б�", e.toString());
                        showToast("�������쳣");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        loadingDialog.dismiss();
                        LogUtil.i("���б�", response);
                        Gson gson = new Gson();
                        GetColletorMeter info = gson.fromJson(response, GetColletorMeter.class);
                        if (info.getColletor_Meter() != null && info.getColletor_Meter().size() != 0) {
                            ArrayList<String> meterNos = new ArrayList<>();
                            List<ColletorMeterBean> mBooks = info.getColletor_Meter();
                            for (int i = 0; i < mBooks.size(); i++) {
                                meterNos.add(mBooks.get(i).getCommunicateNo());
                            }
                            final Intent intent = new Intent(getContext(), CtCopyingActivity.class);
                            intent.putExtra("meterNos", meterNos);//��ż���
//                            intent.putExtra("meterTypeNo", mBooks.get(0).getMeterTypeNo());//������
                            intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);//Ⱥ��
                            intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);//����
                            intent.putExtra("collectorNo", collectorNo);//���������


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("ѡ��ȡ�����߱�����");
                            final String[] cities = {"������", "IC����"};
                            builder.setItems(cities, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which==0){
                                        intent.putExtra("meterTypeNo", "05");
                                    }else {
                                        intent.putExtra("meterTypeNo", "04");
                                    }
                                    startActivity(intent);
                                }
                            });
                            builder.show();
//                            startActivity(intent);
                        } else {
                            showToast("û�в鵽����");
                        }

                    }
                });

    }



    @OnClick({R.id.btBeginCopy, R.id.btCopyAllBook, R.id.btShowAllBook,
            R.id.btMaintain, R.id.layoutNetworking, R.id.layoutUpData,
            R.id.btSetting, R.id.layoutShowNoCopy, R.id.layoutShowCopy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBeginCopy:
                //��ʼ����
                mIntent = new Intent(getContext(), CtCopyBookActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                mIntent.putExtra("readState","-1");
                startActivity(mIntent);
                break;
            case R.id.btCopyAllBook:
                //��ȡȫ����
                getAllBook("-1");
                break;

            case R.id.btMaintain:
                mIntent = new Intent(getContext(), MaintenanceActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutNetworking:
                //ʵ������
                mIntent = new Intent(getContext(), CtNetworkingActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                startActivity(mIntent);
                break;
            case R.id.layoutUpData:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("��ʾ");
                builder2.setMessage("�Ƿ��������¼�����?");
                builder2.setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upDataConcentrator();
                        alertDialog.dismiss();
                    }
                });
                builder2.setNegativeButton("ȡ��", null);
                alertDialog = builder2.create();
                alertDialog.show();
                break;
            case R.id.btSetting:
                mIntent = new Intent(getContext(), SettingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutShowNoCopy:
                //��ѯδ��
                mIntent = new Intent(getContext(), CtShowBookListCopyDataActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                mIntent.putExtra("readState", "0");
                startActivity(mIntent);
                break;
            case R.id.btShowAllBook:
                mIntent = new Intent(getContext(), CtShowBookListCopyDataActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                mIntent.putExtra("readState", "-1");
                startActivity(mIntent);
                break;

            case R.id.layoutShowCopy:
                //��ʼ����
                mIntent = new Intent(getContext(), CtCopyBookActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                mIntent.putExtra("readState","0");
                startActivity(mIntent);
//                getAllBook("0");
                break;
        }
    }
}




