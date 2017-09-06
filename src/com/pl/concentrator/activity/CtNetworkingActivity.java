package com.pl.concentrator.activity;

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
import com.pl.concentrator.adapter.RvNetworkingListAdapter;
import com.pl.concentrator.bean.gson.GetCollectorNetWorking;
import com.pl.concentrator.bean.model.BookInfo;
import com.pl.gassystem.R;
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
 * ʵ����������
 */

public class CtNetworkingActivity extends CtBaseTitleActivity {


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
    @BindView(R.id.btMove)
    Button btMove;
    @BindView(R.id.layoutChooseAll)
    LinearLayout layoutChooseAll;
    @BindView(R.id.ivChooseAll)
    ImageView ivChooseAll;
    @BindView(R.id.btCopy)
    Button btCopy;
    @BindView(R.id.ivDevState)
    ImageView ivDevState;
    @BindView(R.id.layoutDevState)
    LinearLayout layoutDevState;

    private boolean isChooseAll = false;
    private boolean isDown = true;//Ĭ��Ϊ����

    private int nowPageNum = 1;//��ǰ��ҳ��
    private int pageSize = 1;
    private String collectorNo;

    private List<BookInfo> mList;
    private List<BookInfo> trueList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_networking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("ʵ������");
        collectorNo = getIntent().getStringExtra("CollectorNo");
        LogUtil.i("��������", collectorNo);
        mList = new ArrayList<>();
        trueList = new ArrayList<>();
        RvNetworkingListAdapter mRvNetworkingListAdapter = new RvNetworkingListAdapter(getContext(), mList);
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

        btMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BookInfo> chooseBookInfoList = new ArrayList<>();

                for (int i = 0; i < trueList.size(); i++) {
                    if (trueList.get(i).isChoose()) {
                        chooseBookInfoList.add(trueList.get(i));
                    }
                }
                if (chooseBookInfoList.size() == 0) {
                    showToast("����δѡ���");
                } else {
                    Intent mIntent = new Intent(getContext(), CtMoveBookActivity.class);
                    ArrayList<String> mCommunicateNoList=new ArrayList<>();
                    for (int i = 0; i <chooseBookInfoList.size() ; i++) {
                        mCommunicateNoList.add(chooseBookInfoList.get(i).getCommunicateNo());
                    }
                    mIntent.putExtra("CommunicateNo", "10086");
                    mIntent.putStringArrayListExtra("mCommunicateNoList",mCommunicateNoList);
                    getContext().startActivity(mIntent);
                }
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
                if (trueList != null) {
                    for (int i = 0; i < trueList.size(); i++) {
                        trueList.get(i).setChoose(!isChooseAll);
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

            }
        });

        layoutDevState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDown) {
                    ivDevState.setImageResource(R.mipmap.up);
                    mList.clear();
                    for (int i = trueList.size() - 1; i >= 0; i--) {
                        mList.add(trueList.get(i));
                    }
                } else {
                    ivDevState.setImageResource(R.mipmap.down);
                    mList.clear();
                    mList.addAll(trueList);
                }
                mAdapter.notifyDataSetChanged();
                isDown = !isDown;

            }
        });

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
                            mList.clear();
                            mList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            trueList.clear();
                            trueList.addAll(mGetCollectorNetWorking.getCollectorNetWorking());
                            mAdapter.notifyDataSetChanged();

                            isDown = true;
                            ivDevState.setImageResource(R.mipmap.down);

                        }
                        CtNetworkingActivity.this.nowPageNum = mGetCollectorNetWorking.getPageNo();
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
