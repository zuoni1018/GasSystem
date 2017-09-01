package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvMoveBookAdapter;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 实抄组网 移表到其他集中器去
 */

public class CtMoveBookActivity extends CtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.btMove)
    Button btMove;
    private LRecyclerViewAdapter mAdapter;
    private List<String> mList;


    @Override
    protected int setLayout() {
        return R.layout.ct_activity_move_book;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("移动表");

        mList=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            mList.add("");
        }
        RvMoveBookAdapter mRvMoveBookAdapter=new RvMoveBookAdapter(getContext(),mList);
        mAdapter=new LRecyclerViewAdapter(mRvMoveBookAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                btMove.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(1);
                    }
                }, 1000);
            }
        });

    }

    @OnClick(R.id.btMove)
    public void onViewClicked() {
        Toast.makeText(getContext(),"对不起您还没有选择集中器",Toast.LENGTH_SHORT).show();
    }
}
