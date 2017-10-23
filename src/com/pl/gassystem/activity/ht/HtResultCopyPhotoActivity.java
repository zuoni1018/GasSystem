package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtResultCopyPhotoAdapter;
import com.pl.gassystem.bean.gson.GetCopyDataPhoto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/10/13
 */

public class HtResultCopyPhotoActivity extends HtBaseTitleActivity {


    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private ArrayList<GetCopyDataPhoto.ModTimereadmeterinfoBean> mList;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_result_copy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("³­±í½á¹û");

        mList= (ArrayList<GetCopyDataPhoto.ModTimereadmeterinfoBean>) getIntent().getSerializableExtra("ModTimereadmeterinfoBean");

        if(mList==null){
            mList=new ArrayList<>();
        }
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new LRecyclerViewAdapter(new RvHtResultCopyPhotoAdapter(getContext(),mList)));
    }


    @OnClick(R.id.bt)
    public void onViewClicked() {
        finishActivity();
    }
}
