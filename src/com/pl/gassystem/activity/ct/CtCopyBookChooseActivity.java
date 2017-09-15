package com.pl.gassystem.activity.ct;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.gassystem.adapter.ct.RvCopyBookChooseAdapter;
import com.pl.gassystem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/4
 * 集中器 蓝牙抄表界面
 * //暂时弃用
 */

public class CtCopyBookChooseActivity extends CtBaseTitleActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;
    @BindView(R.id.rvCopyBookChooseList)
    LRecyclerView rvCopyBookChooseList;
    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.btStartCopy)
    Button btStartCopy;


    private LRecyclerViewAdapter mAdapter;
    private List<String> mList;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_book_choose;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("XX集中器");
        initList();
    }

    private void initList() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        RvCopyBookChooseAdapter mRvCopyBookChooseAdapter = new RvCopyBookChooseAdapter(getContext(), mList);
        mAdapter = new LRecyclerViewAdapter(mRvCopyBookChooseAdapter);
        rvCopyBookChooseList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvCopyBookChooseList.setAdapter(mAdapter);

    }

    @OnClick(R.id.btStartCopy)
    public void onViewClicked() {
    }
}
