package com.pl.gassystem.activity.ht;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtGetReadMeterInfoAdapter;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.bean.ht.HtTimereadMeterInfoBean;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.Xml2Json;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/10/9
 */

public class HtGetReadMeterInfoActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mAdapter;
    private List<HtTimereadMeterInfoBean> mList;

    private String AreaNo = "";
    private String ReadState = "";

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_read_meter_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("查看需要补抄的表");
        ButterKnife.bind(this);


        AreaNo = getIntent().getStringExtra("AreaNo");
        ReadState = getIntent().getStringExtra("ReadState");
        switch (ReadState) {
            case "1":
                setTitle("查看需要补抄的表(未抄)");
                break;
            case "0":
                setTitle("查看需要补抄的表(已抄)");
                break;
            default:
                setTitle("查看需要补抄的表(全部)");
                break;
        }


        LogUtil.i("拿到AreaNo", AreaNo);
        mList = new ArrayList<>();

        mAdapter = new LRecyclerViewAdapter(new RvHtGetReadMeterInfoAdapter(getContext(), mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.post()
                        .url(HtAppUrl.GET_READ_METER_INFO)
                        .addParams("AreaNo", AreaNo)
                        .addParams("ReadState", ReadState)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast(e.toString());
                                LogUtil.i("获取需要补抄" + e.toString());
                                mRecyclerView.refreshComplete(1);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                LogUtil.i("获取需要补抄1\n" + response);
                                LogUtil.i("获取需要补抄2" + Xml2Json.xml2JSON2List("ArrayOfModCustomerinfo","ModCustomerinfo",response));

                                Gson gson = new Gson();
//                                HtGetReadMeterInfo info = gson.fromJson(Xml2Json.xml2JSON2List("ArrayOfModCustomerinfo","ModCustomerinfo",response), HtGetReadMeterInfo.class);

//                                if (info.getArrayOfModCustomerinfo().getModCustomerinfo() != null) {
//                                    mList.clear();
//                                    mList.addAll(info.getArrayOfModCustomerinfo().getModCustomerinfo());
//                                    mAdapter.notifyDataSetChanged();
//                                }
                                mRecyclerView.refreshComplete(1);

                            }
                        });
            }
        });
        mRecyclerView.refresh();

    }


    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        ArrayList<String> bookNos = new ArrayList<>();


        switch (view.getId()) {
            case R.id.bt1:
                for (int i = 0; i < mList.size(); i++) {
//                    if (mList.get(i).isCheck()) {
//                        bookNos.add(mList.get(i).getCommunicateNo());
//                    }
                }
                break;
            case R.id.bt2:
                for (int i = 0; i < mList.size(); i++) {
                    bookNos.add(mList.get(i).getCommunicateNo());
                }
                break;
        }

        if (bookNos.size() > 0) {
            final Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
            mIntent.putExtra("bookNos", bookNos);
            mIntent.putExtra("bookNo", bookNos.get(0));
            mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄

            mIntent.putExtra("YinZi", mList.get(0).getKPYZ());
            mIntent.putExtra("XinDao", mList.get(0).getKPXD());
            mIntent.putExtra("nowKey", "0102030405060708");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("选择抄取的无线表类型");
            final String[] cities = {"实时抄表", "抄取冻结量"};
            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                    }else {
                        mIntent.putExtra("commandType",HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                    }
                    startActivity(mIntent);
                }
            });
            builder.show();
        } else {
            showToast("请选择表具");
        }


    }
}
