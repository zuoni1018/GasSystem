package com.pl.gassystem.activity.ct;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.AppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ct.RvBookCopyDataListAdapter;
import com.pl.gassystem.bean.ct.ColletorMeterBean;
import com.pl.gassystem.bean.gson.GetColletorMeter;
import com.pl.gassystem.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 需要判断表定的类型
 * 纯无线和IC无线所使用的Adapter不一样
 */

public class CtShowBookListCopyDataActivity extends CtBaseTitleActivity {
    @BindView(R.id.tvBookNum)
    TextView tvBookNum;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvBookList)
    LRecyclerView rvBookList;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;


    private String CollectorNo;//集中器编号

    private LRecyclerViewAdapter mAdapter;

    private List<ColletorMeterBean> showList;//显示的列表
    private List<ColletorMeterBean> trueList;


    private String readState="-1";

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_show_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        readState=getIntent().getStringExtra("readState");
        if(readState.equals("-1")){
            setTitle("显示全部");
        }else {
            setTitle("显示未抄");
        }
        CollectorNo = getIntent().getStringExtra("CollectorNo");
        if (CollectorNo != null && !"".equals(CollectorNo)) {

            //通过集中器编号去查询表信息
            showList = new ArrayList<>();
            trueList = new ArrayList<>();

            RvBookCopyDataListAdapter mRvBookCopyDataListAdapter = new RvBookCopyDataListAdapter(getContext(), showList);
            mAdapter = new LRecyclerViewAdapter(mRvBookCopyDataListAdapter);
            rvBookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvBookList.setAdapter(mAdapter);

            rvBookList.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getCopyInfo();
                }
            });

            rvBookList.refresh();//刷新一下

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
                        showList.clear();
                        showList.addAll(trueList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showList.clear();
                        for (int i = 0; i < trueList.size(); i++) {
                            String myText = trueList.get(i).getCommunicateNo() + trueList.get(i).getAddress();
                            if (myText.contains(s.toString().trim())) {
                                showList.add(trueList.get(i));
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private AlertDialog alertDialog;

    private void getCopyInfo() {
        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.GETCOLLETOR_METER)
                .addParams("CollectorNo", CollectorNo)//集中器
                .addParams("readState",readState)//查询内容
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("查询抄表内容", e.toString());
                        showToast("服务器异常");
                        rvBookList.refreshComplete(1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("查询抄表内容", response);
                        Gson gson = new Gson();
                        GetColletorMeter info = gson.fromJson(response, GetColletorMeter.class);
                        List<ColletorMeterBean> mList=info.getColletor_Meter();
                        if(mList!=null&&mList.size()!=0){
                            trueList.clear();
                            trueList.addAll(mList);

                            showList.clear();
                            showList.addAll(trueList);

                            mAdapter.notifyDataSetChanged();

                            etSearch.setText("");
                            tvBookNum.setText("共" + showList.size() + "表");
                        }
                        rvBookList.refreshComplete(1);
                    }
                });


    }
}
