package com.pl.concentrator.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.common.utils.KeyBoardUtils;
import com.common.utils.LogUtil;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.concentrator.AppUrl;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.activity.copy.CtCopyingActivity;
import com.pl.concentrator.adapter.RvCopyBookAdapter;
import com.pl.concentrator.bean.gson.GetCollectorNetWorking;
import com.pl.concentrator.bean.model.CtBookInfo;
import com.pl.concentrator.dao.CtCopyDataDao;
import com.pl.concentrator.dao.CtCopyDataICRFDao;
import com.pl.gassystem.R;
import com.pl.utils.GlobalConsts;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * ��ʼ������� ѡ����
 */

public class CtCopyBookActivity extends CtBaseTitleActivity {


    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.ivTurnLeft)
    ImageView ivTurnLeft;
    @BindView(R.id.tvNowPageNum)
    TextView tvNowPageNum;
    @BindView(R.id.ivTurnRight)
    ImageView ivTurnRight;
    @BindView(R.id.tvAllPageNum)
    TextView tvAllPageNum;
    @BindView(R.id.etGoPageNum)
    EditText etGoPageNum;
    @BindView(R.id.btGoPageNum)
    Button btGoPageNum;

    @BindView(R.id.layoutChooseAll)
    LinearLayout layoutChooseAll;
    @BindView(R.id.ivChooseAll)
    ImageView ivChooseAll;
    @BindView(R.id.btCopy)
    Button btCopy;
    private ArrayList<String> meterNos;
    private boolean isChooseAll = false;

    private int nowPageNum = 1;//��ǰ��ҳ��
    private int pageSize = 1;
    private String collectorNo;

    //RecyclerView
    private List<CtBookInfo> showList;
    private List<CtBookInfo> trueList;
    private LRecyclerViewAdapter mAdapter;


    private CtCopyDataDao ctCopyDataDao;
    private CtCopyDataICRFDao ctCopyDataICRFDao;
    private ProgressDialog progressDialog;

    private final int INSERT_OK = 1;//�������ݿ�ɹ�
    private final int INSERT_ERROR = 2;//�������ݿ�ʧ��

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INSERT_OK:
                    btCopy.setClickable(true);
                    progressDialog.dismiss();
//                    meterNos.add("1705060001");
                    if (meterNos.size() > 0) {
                        final Intent intent = new Intent(CtCopyBookActivity.this, CtCopyingActivity.class);
                        intent.putExtra("meterNos", meterNos);//��ż���
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);//Ⱥ��
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);//����
                        intent.putExtra("collectorNo", collectorNo);
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

                    } else {
                        showToast("����δѡ���κ�һ�ű�");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("��ʼ����");
        collectorNo = getIntent().getStringExtra("CollectorNo");
        ctCopyDataICRFDao = new CtCopyDataICRFDao(getContext());
        meterNos = new ArrayList<>();
        showList = new ArrayList<>();
        trueList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("������...");
        progressDialog.setCancelable(false);
        RvCopyBookAdapter mRvNetworkingListAdapter = new RvCopyBookAdapter(getContext(), showList);
        mAdapter = new LRecyclerViewAdapter(mRvNetworkingListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                etSearch.setText("");
                isChooseAll=false;
                ivChooseAll.setImageResource(R.mipmap.choose_02);
                mRecyclerView.scrollToPosition(0);
                getListInfo(nowPageNum);
            }
        });

        getListInfo(nowPageNum);

        setTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText(s.toString().trim());
            }
        });

        layoutChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showList != null) {
                    //����ѡ��ȡ��
                    for (int i = 0; i < showList.size(); i++) {
                        showList.get(i).setChoose(!isChooseAll);
                    }
                    isChooseAll = !isChooseAll;
                    if (isChooseAll) {
                        ivChooseAll.setImageResource(R.mipmap.choose_01);
                    } else {
                        ivChooseAll.setImageResource(R.mipmap.choose_02);
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }
        });


        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCopy.setClickable(false);
                progressDialog.show();
                meterNos.clear();
                ctCopyDataDao = new CtCopyDataDao(getContext());
                //����һ���߳�ȥ�������ݿ�
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < showList.size(); i++) {
                            if (showList.get(i).isChoose()) {
                                meterNos.add(showList.get(i).getCommunicateNo());
                                //������
                                if (showList.get(i).getMeterTypeNo().equals("05")) {
                                    ctCopyDataDao.putCtCopyDataOtherInfo(showList.get(i));
                                } else {
                                    ctCopyDataICRFDao.putCtCopyDataOtherInfo(showList.get(i));
                                }
                            }
                        }
                        Message message = Message.obtain();
                        message.what = INSERT_OK;
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_book;
    }


    /**
     * ��ȡ���б�
     * CollectorNo:
     * PageNo
     */
    private void getListInfo(int nowPageNum) {
        OkHttpUtils
                .post()
                .url(AppUrl.GET_COLLECTOR_NET_WORKING)
                .addParams("CollectorNo", collectorNo)
                .addParams("PageNo", nowPageNum + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("���б�", e.toString());
                        mRecyclerView.refreshComplete(1);
                        showToast("�������쳣");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("���б�", response);
                        Gson gson = new Gson();
                        GetCollectorNetWorking mGetCollectorNetWorking = gson.fromJson(response, GetCollectorNetWorking.class);
                        if (mGetCollectorNetWorking.getCollectorNetWorking() != null) {
                            showList.clear();
                            showList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            trueList.clear();
                            trueList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            mAdapter.notifyDataSetChanged();
                        }
                        CtCopyBookActivity.this.nowPageNum = mGetCollectorNetWorking.getPageNo();
                        tvNowPageNum.setText(mGetCollectorNetWorking.getPageNo() + "");
                        if (mGetCollectorNetWorking.getTotleNum() % 15 == 0) {
                            pageSize = mGetCollectorNetWorking.getTotleNum() / 15;
                        } else {
                            pageSize = mGetCollectorNetWorking.getTotleNum() / 15 + 1;
                        }
                        tvAllPageNum.setText("��" + pageSize + "ҳ");
                        mRecyclerView.refreshComplete(1);
                    }
                });
    }

    @OnClick({R.id.ivTurnLeft, R.id.ivTurnRight, R.id.btGoPageNum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivTurnLeft:
                nowPageNum--;
                if (nowPageNum > 0) {
                    mRecyclerView.refresh();
                } else {
                    nowPageNum++;
                }
                break;
            case R.id.ivTurnRight:
                nowPageNum++;
                if (nowPageNum <= pageSize) {
                    mRecyclerView.refresh();
                } else {
                    nowPageNum--;
                }
                break;
            case R.id.btGoPageNum:
                KeyBoardUtils.closeKeybord(etGoPageNum, getContext());
                String inputNum = etGoPageNum.getText().toString().trim();
                if (inputNum.equals("")) {
                    showToast("������ҳ��");
                } else {
                    int num = Integer.parseInt(inputNum);
                    if (num > pageSize | num == 0) {
                        showToast("��������Чҳ��");
                        etGoPageNum.setText("");
                    } else {
                        nowPageNum = num;
                        mRecyclerView.refresh();
                        etGoPageNum.setText("");
                    }
                }
                break;
        }
    }
    /**
     * ��������
     */
    private void searchText(String text) {
        if (text.equals("")) {
            showList.clear();
            showList.addAll(trueList);
            mAdapter.notifyDataSetChanged();
        } else {
            showList.clear();
            mAdapter.notifyDataSetChanged();
            for (int i = 0; i < trueList.size(); i++) {
                String myText = trueList.get(i).getAddress() + trueList.get(i).getCommunicateNo();
                if (myText.contains(text)) {
                    showList.add(trueList.get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
