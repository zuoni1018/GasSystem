package com.pl.gassystem.activity.ct;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.AppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ct.RvCopyBookAdapter;
import com.pl.gassystem.bean.ct.ColletorMeterBean;
import com.pl.gassystem.bean.gson.GetColletorMeter;
import com.pl.gassystem.utils.LogUtil;
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

    @BindView(R.id.ivChooseAll)
    ImageView ivChooseAll;
    @BindView(R.id.layoutChooseAll)
    LinearLayout layoutChooseAll;
    @BindView(R.id.btCopyChoose)
    Button btCopyChoose;
    @BindView(R.id.btCopyAll)
    Button btCopyAll;


    private ArrayList<String> meterNos;
    private boolean isChooseAll = false;

    private String collectorNo;
    private String readState = "-1";


    //RecyclerView
    private List<ColletorMeterBean> showList;
    private List<ColletorMeterBean> trueList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("��ʼ����");
        collectorNo = getIntent().getStringExtra("CollectorNo");
        readState = getIntent().getStringExtra("readState");
        meterNos = new ArrayList<>();
        showList = new ArrayList<>();
        trueList = new ArrayList<>();
        RvCopyBookAdapter mRvNetworkingListAdapter = new RvCopyBookAdapter(getContext(), showList);
        mAdapter = new LRecyclerViewAdapter(mRvNetworkingListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                etSearch.setText("");
                isChooseAll = false;
                ivChooseAll.setImageResource(R.mipmap.choose_02);
                mRecyclerView.scrollToPosition(0);
                getAllBook(readState);
            }
        });
        getAllBook(readState);

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
    }

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_book;
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

    @OnClick({R.id.btCopyChoose, R.id.btCopyAll, R.id.layoutChooseAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btCopyChoose:
                meterNos.clear();
                for (int i = 0; i < showList.size(); i++) {
                    if (showList.get(i).isShowMore()) {
                        meterNos.add(showList.get(i).getCommunicateNo());
                    }
                }
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
                            if (which == 0) {
                                intent.putExtra("meterTypeNo", "05");
                            } else {
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
            case R.id.btCopyAll:

                meterNos.clear();
                for (int i = 0; i < showList.size(); i++) {
                    meterNos.add(showList.get(i).getCommunicateNo());
                }
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
                            if (which == 0) {
                                intent.putExtra("meterTypeNo", "05");
                            } else {
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
            case R.id.layoutChooseAll:
                if (showList != null) {
                    //����ѡ��ȡ��
                    for (int i = 0; i < showList.size(); i++) {
                        showList.get(i).setShowMore(!isChooseAll);
                    }
                    isChooseAll = !isChooseAll;
                    if (isChooseAll) {
                        ivChooseAll.setImageResource(R.mipmap.choose_01);
                    } else {
                        ivChooseAll.setImageResource(R.mipmap.choose_02);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    /**
     * ��ȡ���б�
     */
    private void getAllBook(String readState) {
        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.GETCOLLETOR_METER)
                .addParams("CollectorNo", collectorNo)//������
                .addParams("readState", readState)//��ѯȫ��
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("���б�", e.toString());
                        showToast("�������쳣");
                        mRecyclerView.refreshComplete(1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mRecyclerView.refreshComplete(1);

                        LogUtil.i("���б�", response);
                        Gson gson = new Gson();
                        GetColletorMeter info = gson.fromJson(response, GetColletorMeter.class);
                        trueList.clear();
                        showList.clear();

                        trueList.addAll(info.getColletor_Meter());
                        showList.addAll(trueList);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }
}
