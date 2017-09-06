package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvBookCopyDataListAdapter;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 需要判断表定的类型
 * 纯无线和IC无线所使用的Adapter不一样
 */

public class CtShowBookListActivity extends CtBaseTitleActivity {
    @BindView(R.id.tvBookNum)
    TextView tvBookNum;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvBookList)
    LRecyclerView rvBookList;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;


    private List<String> mList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_show_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("显示全部");

        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("");

        }
        RvBookCopyDataListAdapter mRvBookCopyDataListAdapter = new RvBookCopyDataListAdapter(getContext(), mList);
        mAdapter = new LRecyclerViewAdapter(mRvBookCopyDataListAdapter);
        rvBookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerDecoration divider = new DividerDecoration.Builder(this).setColorResource(R.color.color_blue).build();
        rvBookList.addItemDecoration(divider);
        rvBookList.setAdapter(mAdapter);
        rvBookList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                rvBookList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvBookList.refreshComplete(1);
                    }
                }, 1000);
            }
        });

    }

    @OnClick(R.id.btBeginCopy)
    public void onViewClicked() {
    }
}
