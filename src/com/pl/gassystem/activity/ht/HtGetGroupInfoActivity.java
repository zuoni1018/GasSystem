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
import com.pl.gassystem.adapter.ht.RvHtGetGroupInfoAdapter;
import com.pl.gassystem.bean.gson.HtGetGroupInfo;
import com.pl.gassystem.bean.ht.HtGroupInfoBean;
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

public class HtGetGroupInfoActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mAdapter;
    private List<HtGroupInfoBean> mList;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_group_info;
    }

    private String BookNo = "";
    private String AreaNo = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("分组列表");
        ButterKnife.bind(this);

        BookNo = getIntent().getStringExtra("BookNo");
        AreaNo = getIntent().getStringExtra("AreaNo");
        mList = new ArrayList<>();

        mAdapter = new LRecyclerViewAdapter(new RvHtGetGroupInfoAdapter(getContext(), mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.post()
                        .url(HtAppUrl.GET_GROUP_INFO)
                        .addParams("GroupNo", "")
                        .addParams("BookNo", BookNo)
                        .addParams("AreaNo", AreaNo)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast(e.toString());
                                LogUtil.i("分组列表" + e.toString());
                                mRecyclerView.refreshComplete(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtil.i("分组列表1\n" + response);
                                LogUtil.i("分组列表2" + xml2JSON2List("ArrayOfModApp_groupinfo","ModApp_groupinfo",response));

                                Gson gson = new Gson();
                                HtGetGroupInfo info = gson.fromJson(xml2JSON2List("ArrayOfModApp_groupinfo","ModApp_groupinfo",response), HtGetGroupInfo.class);
                                mList.clear();
                                if(info.getArrayOfModApp_groupinfo().getModApp_groupinfo()!=null){
                                    mList.addAll(info.getArrayOfModApp_groupinfo().getModApp_groupinfo());
                                }
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.refreshComplete(1);
                            }
                        });
            }
        });
        mRecyclerView.refresh();

    }


}
