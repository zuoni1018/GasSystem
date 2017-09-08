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
 * 开始抄表界面 选择表具
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

    private int nowPageNum = 1;//当前的页码
    private int pageSize = 1;
    private String collectorNo;

    //RecyclerView
    private List<CtBookInfo> showList;
    private List<CtBookInfo> trueList;
    private LRecyclerViewAdapter mAdapter;


    private CtCopyDataDao ctCopyDataDao;
    private CtCopyDataICRFDao ctCopyDataICRFDao;
    private ProgressDialog progressDialog;

    private final int INSERT_OK = 1;//插入数据库成功
    private final int INSERT_ERROR = 2;//插入数据库失败

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
                        intent.putExtra("meterNos", meterNos);//表号集合
                        intent.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);//群抄
                        intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);//抄表
                        intent.putExtra("collectorNo", collectorNo);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("选择抄取的无线表类型");
                        final String[] cities = {"纯无线", "IC无线"};
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
                        showToast("您尚未选择任何一张表");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("开始抄表");
        collectorNo = getIntent().getStringExtra("CollectorNo");
        ctCopyDataICRFDao = new CtCopyDataICRFDao(getContext());
        meterNos = new ArrayList<>();
        showList = new ArrayList<>();
        trueList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("载入中...");
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
                    //所有选项取反
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
                //开启一个线程去插入数据库
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < showList.size(); i++) {
                            if (showList.get(i).isChoose()) {
                                meterNos.add(showList.get(i).getCommunicateNo());
                                //存起来
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
    /**
     * 搜索功能
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
