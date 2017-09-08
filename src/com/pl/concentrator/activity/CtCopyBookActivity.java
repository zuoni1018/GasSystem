package com.pl.concentrator.activity;

import android.app.ProgressDialog;
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
 * 实抄组网界面
 */

public class CtCopyBookActivity extends CtBaseTitleActivity {


    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
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

    private String meterTypeNo;


    private ArrayList<String> meterNos;


    private boolean isChooseAll = false;
    private boolean isDown = true;//默认为降序

    private int nowPageNum = 1;//当前的页码
    private int pageSize = 1;
    private String collectorNo;

    private List<CtBookInfo> mList;
    private List<CtBookInfo> trueList;
    private LRecyclerViewAdapter mAdapter;




    private CtCopyDataDao ctCopyDataDao;
    private ProgressDialog progressDialog;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_book;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("开始抄表");
        collectorNo = getIntent().getStringExtra("CollectorNo");
        meterNos = new ArrayList<>();
//        meterNos.add("1");
        mList = new ArrayList<>();
        trueList = new ArrayList<>();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("载入中...");
        progressDialog.setCancelable(false);
        RvCopyBookAdapter mRvNetworkingListAdapter = new RvCopyBookAdapter(getContext(), mList);
        mAdapter = new LRecyclerViewAdapter(mRvNetworkingListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.scrollToPosition(0);
                getListInfo(nowPageNum);
            }
        });

        getListInfo(nowPageNum);

        setTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
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
                if (s.equals("")) {
                    mList.clear();
                    mList.addAll(trueList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    for (int i = 0; i < trueList.size(); i++) {
                        String myText = trueList.get(i).getAddress() + trueList.get(i).getCommunicateNo();
                        if (myText.contains(s)) {
                            mList.add(trueList.get(i));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        layoutChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setChoose(!isChooseAll);
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
                progressDialog.show();
                btCopy.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        meterNos.clear();
                        ctCopyDataDao=new CtCopyDataDao(getContext());

                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).isChoose()) {
                                meterNos.add(mList.get(i).getCommunicateNo());
                                //存起来
                                ctCopyDataDao.putCtCopyDataOtherInfo(mList.get(i));
                            }
                        }
                        progressDialog.dismiss();
//                        meterNos.add("2017090602");
//                        meterNos.add("2017090605");
                        if (meterNos.size() > 0) {
                            Intent intent = new Intent(CtCopyBookActivity.this, CtCopyingActivity.class);
                            intent.putExtra("meterNos", meterNos);//表号集合
//                    intent.putExtra("meterTypeNo", trueList.get(0).getMeterTypeNo());//表类型
                            intent.putExtra("meterTypeNo", "05");//表类型
                            intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);//群抄
                            intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);//抄表
                            intent.putExtra("collectorNo",collectorNo);
                            startActivity(intent);
                        } else {
                            showToast("您未选择任何一张表");
                        }
                    }
                },100);

            }
        });


    }

    /**
     * 获取表列表
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
                        LogUtil.i("表列表", e.toString());
                        mRecyclerView.refreshComplete(1);
                        showToast("服务器异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("表列表", response);
                        Gson gson = new Gson();
                        GetCollectorNetWorking mGetCollectorNetWorking = gson.fromJson(response, GetCollectorNetWorking.class);
                        if (mGetCollectorNetWorking.getCollectorNetWorking() != null) {
                            mList.clear();
                            mList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            trueList.clear();
                            trueList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            mAdapter.notifyDataSetChanged();

                            isDown = true;


                        }
                        CtCopyBookActivity.this.nowPageNum = mGetCollectorNetWorking.getPageNo();
                        tvNowPageNum.setText(mGetCollectorNetWorking.getPageNo() + "");
                        if (mGetCollectorNetWorking.getTotleNum() % 15 == 0) {
                            pageSize = mGetCollectorNetWorking.getTotleNum() / 15;
                        } else {
                            pageSize = mGetCollectorNetWorking.getTotleNum() / 15 + 1;
                        }
                        tvAllPageNum.setText("共" + pageSize + "页");
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
                    showToast("请输入页码");
                } else {
                    int num = Integer.parseInt(inputNum);
                    if (num > pageSize | num == 0) {
                        showToast("请输入有效页码");
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

    @OnClick(R.id.ivSearch)
    public void onViewClicked() {
        String searchText = etSearch.getText().toString().trim();
        KeyBoardUtils.closeKeybord(etSearch, getContext());
        if (searchText.equals("")) {
            mList.clear();
            mList.addAll(trueList);
            mAdapter.notifyDataSetChanged();
        } else {
            mList.clear();
            mAdapter.notifyDataSetChanged();

            for (int i = 0; i < trueList.size(); i++) {
                String myText = trueList.get(i).getAddress() + trueList.get(i).getCommunicateNo();
                if (myText.contains(searchText)) {
                    mList.add(trueList.get(i));
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
