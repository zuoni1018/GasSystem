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
import com.pl.gassystem.adapter.ht.RvHtGetUpdateMeterInfoAdapter;
import com.pl.gassystem.bean.gson.HtGetUpdateMeterInfo;
import com.pl.gassystem.bean.ht.HtUpdateMeterInfoBean;
import com.pl.gassystem.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.pl.gassystem.utils.Xml2Json.xml2JSON;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetUpdateMeterInfoActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mAdapter;
    private List<HtUpdateMeterInfoBean> mList;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_group_info;
    }

    private String AreaNo = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("需要修改参数的表计(扩频表)");
        ButterKnife.bind(this);

        AreaNo = getIntent().getStringExtra("AreaNo");
        mList = new ArrayList<>();

        mAdapter = new LRecyclerViewAdapter(new RvHtGetUpdateMeterInfoAdapter(getContext(), mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);



        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.post()
                        .url(HtAppUrl.GET_UPDATE_METER_INFO)
                        .addParams("AreaNo", AreaNo)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast(e.toString());
                                LogUtil.i("需要修改参数的表计" + e.toString());
                                mRecyclerView.refreshComplete(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtil.i("需要修改参数的表计1\n" + response);
                                LogUtil.i("需要修改参数的表计2" + xml2JSON(response));

                                Gson gson = new Gson();
                                HtGetUpdateMeterInfo info = gson.fromJson(xml2JSON(response), HtGetUpdateMeterInfo.class);
                                mList.clear();
                                if(info.getArrayOfModApp_updatemeterinfo().getModApp_updatemeterinfo()!=null){
                                    mList.addAll(info.getArrayOfModApp_updatemeterinfo().getModApp_updatemeterinfo());
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
