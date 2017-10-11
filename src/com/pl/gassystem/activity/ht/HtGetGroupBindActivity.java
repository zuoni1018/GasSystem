package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtGetGroupBindAdapter;
import com.pl.gassystem.bean.gson.HtGetGroupBind;
import com.pl.gassystem.bean.ht.HtCustomerInfoBean;
import com.pl.gassystem.bean.ht.HtGroupInfoBean;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.pl.gassystem.utils.Xml2Json.xml2JSON2List;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetGroupBindActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    private HtGroupInfoBean mHtGroupInfoBean;

    private LRecyclerViewAdapter mAdapter;
    private List<HtCustomerInfoBean> mList;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_group_bind;
    }

    private String GroupNo = "";
    private String commandType = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mHtGroupInfoBean = (HtGroupInfoBean) getIntent().getSerializableExtra("HtGroupInfoBean");
        GroupNo = mHtGroupInfoBean.getGroupNo();
        commandType = getIntent().getStringExtra("commandType");
        setTitle("燃气表列表(" + HtSendMessage.getCommandString(commandType) + ")");
        mList = new ArrayList<>();

        if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN) | commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)) {
            mAdapter = new LRecyclerViewAdapter(new RvHtGetGroupBindAdapter(getContext(), mList, false));//多选
        } else {
            mAdapter = new LRecyclerViewAdapter(new RvHtGetGroupBindAdapter(getContext(), mList, true));//单选
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.post()
                        .url(HtAppUrl.GET_GROUP_BIND)
                        .addParams("GroupNo", GroupNo)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast(e.toString());
                                LogUtil.i("表计列表" + e.toString());
                                mRecyclerView.refreshComplete(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtil.i("表计列表1\n" + response);
                                LogUtil.i("表计列表2" + xml2JSON2List("ArrayOfModCustomerinfo", "ModCustomerinfo", response));

                                Gson gson = new Gson();
                                HtGetGroupBind info = gson.fromJson(xml2JSON2List("ArrayOfModCustomerinfo", "ModCustomerinfo", response), HtGetGroupBind.class);
                                if (info.getArrayOfModCustomerinfo().getModCustomerinfo() != null) {
                                    mList.clear();
                                    mList.addAll(info.getArrayOfModCustomerinfo().getModCustomerinfo());
                                    mAdapter.notifyDataSetChanged();
                                }
                                mRecyclerView.refreshComplete(1);
                            }
                        });
            }
        });
        mRecyclerView.refresh();

    }


    @OnClick(R.id.btGoCopy)
    public void onViewClicked() {
        final Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
        ArrayList<String> bookNos = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChoose()) {
                bookNos.add(mList.get(i).getCommunicateNo());//获取表号码
            }
        }

        if (bookNos.size() > 0) {
            mIntent.putExtra("bookNos", bookNos);
            mIntent.putExtra("bookNo", bookNos.get(0));
            mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄

            mIntent.putExtra("YinZi", mList.get(0).getKPYZ());
            mIntent.putExtra("XinDao", mList.get(0).getKPXD());
            mIntent.putExtra("nowKey", mList.get(0).getKEYCODE());
            mIntent.putExtra("commandType", commandType);
            startActivity(mIntent);

        } else {
            showToast("请选择表");
        }
    }
}
