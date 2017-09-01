package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.utils.LogUtil;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvConcentratorListAdapter;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private LRecyclerViewAdapter mAdapter;
    private List<String> mList;

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
    }

    private void initList() {
        mList=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            mList.add("");
        }
        RvConcentratorListAdapter mRvConcentratorListAdapter=new RvConcentratorListAdapter(getContext(),mList);
        RvConcentratorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter=new LRecyclerViewAdapter(mRvConcentratorListAdapter);
        RvConcentratorList.setAdapter(mAdapter);

        RvConcentratorList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RvConcentratorList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RvConcentratorList.refreshComplete(1);
                    }
                }, 1000);
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
                LogUtil.i("Ct","EditText"+s.toString());
            }
        });
    }
}
