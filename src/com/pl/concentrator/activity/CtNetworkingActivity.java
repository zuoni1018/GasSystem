package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvNetworkingListAdapter;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 实抄组网界面
 */

public class CtNetworkingActivity extends CtBaseTitleActivity {


    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private List <String> mList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_networking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("实抄组网");
        mList=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            mList.add("");
        }
        RvNetworkingListAdapter mRvNetworkingListAdapter=new RvNetworkingListAdapter(getContext(),mList);
        mAdapter=new LRecyclerViewAdapter(mRvNetworkingListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }
}
