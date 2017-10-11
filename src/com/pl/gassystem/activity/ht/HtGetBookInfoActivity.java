package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtGetBookInfoAdapter;
import com.pl.gassystem.bean.gson.HtGetBookInfo;
import com.pl.gassystem.bean.ht.HtBookInfoBean;
import com.pl.gassystem.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.pl.gassystem.utils.Xml2Json.xml2JSON2List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetBookInfoActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mAdapter;
    private List<HtBookInfoBean> mList;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_book_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("账册列表");
        ButterKnife.bind(this);
        mList=new ArrayList<>();

        mAdapter=new LRecyclerViewAdapter(new RvHtGetBookInfoAdapter(getContext(),mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.post()
                        .url(HtAppUrl.GET_BOOK_INFO)
                        .addParams("StaffNo","")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast(e.toString());
                                LogUtil.i("账册列表" + e.toString());
                                mRecyclerView.refreshComplete(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtil.i("账册列表1\n" + response);
                                LogUtil.i("账册列表2" + xml2JSON2List("ArrayOfModApp_bookinfo","ModApp_bookinfo",response));

                                Gson gson = new Gson();
                                HtGetBookInfo htGetBookInfo = gson.fromJson(xml2JSON2List("ArrayOfModApp_bookinfo","ModApp_bookinfo",response), HtGetBookInfo.class);
                                mList.clear();
                                mList.addAll(htGetBookInfo.getArrayOfModApp_bookinfo().getModApp_bookinfo());
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.refreshComplete(1);
                            }
                        });
            }
        });
        mRecyclerView.refresh();

    }


}
