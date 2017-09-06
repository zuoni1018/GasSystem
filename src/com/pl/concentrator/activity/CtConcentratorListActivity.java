package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.LogUtil;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.concentrator.AppUrl;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvConcentratorListAdapter;
import com.pl.concentrator.bean.gson.GetCollectorInfo;
import com.pl.concentrator.bean.model.Concentrator;
import com.pl.gassystem.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 集中器列表
 */

public class CtConcentratorListActivity extends CtBaseTitleActivity {


    @BindView(R.id.etSearchConcentrator)
    EditText etSearchConcentrator;
    @BindView(R.id.ivSearchConcentrator)
    ImageView ivSearchConcentrator;
    @BindView(R.id.RvConcentratorList)
    LRecyclerView RvConcentratorList;
    @BindView(R.id.tvAllNum)
    TextView tvAllNum;
    @BindView(R.id.tvReadNum)
    TextView tvReadNum;
    @BindView(R.id.tvNoReadNum)
    TextView tvNoReadNum;
    private LRecyclerViewAdapter mAdapter;
    private List<Concentrator> mList;
    private List<Concentrator> trueList;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_concentrator_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.ct_activity_concentrator_list_title));
        initSearchEditText();
        initList();
        RvConcentratorList.refresh();

    }

    private void getInfo() {
        OkHttpUtils
                .post()
                .url(AppUrl.GET_COLLECTOR_INFO)
                .addParams("zangyi", "666")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("集中器列表", e.toString());
                        RvConcentratorList.refreshComplete(1);
                        showToast("服务器异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("集中器列表", response);
                        Gson gson = new Gson();
                        GetCollectorInfo mGetCollectorInfo = gson.fromJson(response, GetCollectorInfo.class);
                        if (mGetCollectorInfo.getCollectorInfo() != null) {
                            mList.clear();
                            trueList.clear();
                            mList.addAll(mGetCollectorInfo.getCollectorInfo());
                            trueList.addAll(mGetCollectorInfo.getCollectorInfo());
                            mAdapter.notifyDataSetChanged();
                            int allNum = 0;
                            int readNum = 0;
                            int noReadNum = 0;
                            for (int i = 0; i < mList.size(); i++) {
                                allNum = allNum + mList.get(i).getTrueAllNum();
                                readNum = readNum + mList.get(i).getTrueReadNum();
                                noReadNum = noReadNum + mList.get(i).getTrueNotReadNum();
                            }
                            tvAllNum.setText("总数(" + allNum + ")");
                            tvReadNum.setText("已抄(" + readNum + ")");
                            tvNoReadNum.setText("未抄(" + noReadNum + ")");
                        }
                        RvConcentratorList.refreshComplete(1);
                    }
                });
    }

    private void initList() {
        trueList = new ArrayList<>();
        mList = new ArrayList<>();
        RvConcentratorListAdapter mRvConcentratorListAdapter = new RvConcentratorListAdapter(getContext(), mList);
        RvConcentratorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LRecyclerViewAdapter(mRvConcentratorListAdapter);
        RvConcentratorList.setAdapter(mAdapter);
        RvConcentratorList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });
    }

    private void initSearchEditText() {
        etSearchConcentrator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = etSearchConcentrator.getText().toString().trim();
                if ("".equals(searchText)) {
                    mList.clear();
                    mList.addAll(trueList);
                } else {
                    mList.clear();
                    for (int i = 0; i < trueList.size(); i++) {
                        String myText = trueList.get(i).getCollectorNo();
                        if (myText.contains(searchText)) {
                            mList.add(trueList.get(i));
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
