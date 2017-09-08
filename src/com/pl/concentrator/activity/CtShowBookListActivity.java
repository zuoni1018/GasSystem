package com.pl.concentrator.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.LogUtil;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvBookCopyDataListAdapter;
import com.pl.concentrator.bean.model.CtCopyData;
import com.pl.concentrator.dao.CtCopyDataDao;
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

    private String CollectorNo;//集中器编号

    private List<String> mList;
    private LRecyclerViewAdapter mAdapter;


    private CtCopyDataDao ctCopyDataDao;

    private List<CtCopyData> mCCtCopyDataList;
    private List<CtCopyData> trueList;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_show_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("显示全部");
        CollectorNo = getIntent().getStringExtra("CollectorNo");
        if (CollectorNo != null && !"".equals(CollectorNo)) {
//            ctBookInfoDao = new CtBookInfoDao(getContext());
            ctCopyDataDao = new CtCopyDataDao(getContext());

            //通过集中器编号去查询表信息
            mCCtCopyDataList = new ArrayList<>();
            trueList = new ArrayList<>();
            trueList = ctCopyDataDao.getCtCopyDataListByCollectorNo(CollectorNo);
            mCCtCopyDataList.addAll(trueList);
            RvBookCopyDataListAdapter mRvBookCopyDataListAdapter = new RvBookCopyDataListAdapter(getContext(), mCCtCopyDataList);
            mAdapter = new LRecyclerViewAdapter(mRvBookCopyDataListAdapter);
            rvBookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            DividerDecoration divider = new DividerDecoration.Builder(this).setColorResource(R.color.color_blue).build();
            rvBookList.addItemDecoration(divider);
            rvBookList.setAdapter(mAdapter);
            rvBookList.setPullRefreshEnabled(false);

            tvBookNum.setText("共" + mCCtCopyDataList.size() + "表");

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().trim().equals("")) {
                        mCCtCopyDataList.clear();
                        mCCtCopyDataList.addAll(trueList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mCCtCopyDataList.clear();
                        for (int i = 0; i < trueList.size(); i++) {
                            String myText = trueList.get(i).getCommunicateNo() + trueList.get(i).getMeterName();
                            if (myText.contains(s.toString().trim())) {
                                mCCtCopyDataList.add(trueList.get(i));
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btBeginCopy)
    public void onViewClicked() {
        List<CtCopyData> mList = new ArrayList<>();
        for (int i = 0; i < mCCtCopyDataList.size(); i++) {
            if (mCCtCopyDataList.get(i).isChoose()) {
                mList.add(mCCtCopyDataList.get(i));
            }
        }
        if (mList.size() == 0) {
            showToast("您尚未选择一张表");
        } else {
            String json = new Gson().toJson(mList);
            LogUtil.i("上传数据" + json);
        }
    }
}
