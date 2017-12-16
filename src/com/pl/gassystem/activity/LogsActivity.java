package com.pl.gassystem.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.adapter.main.RvLogsAdapter;
import com.pl.gassystem.dao.HtLogDao;
import com.pl.gassystem.dao.LogMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/11/6
 */

public class LogsActivity extends BaseTitleActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bt01)
    Button bt01;

    private ProgressDialog progressDialog;
    private String logType = "";
    private List<LogMessage> mList;

    private RvLogsAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_logs;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
            if(mList.size()>10){
                mRecyclerView.scrollToPosition(mList.size() - 1);
            }
        }
    };
    private HtLogDao htLogDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("日志查看");

        logType = getIntent().getStringExtra("logType");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("查询中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        htLogDao=new HtLogDao(getContext());

        mList = new ArrayList<>();
        mAdapter = new RvLogsAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //查询日志
                mList.clear();
                mList.addAll(htLogDao.getLogList(logType));
                Message message = Message.obtain();
                mHandler.sendMessage(message);
            }
        });
    }

    @OnClick(R.id.bt01)
    public void onViewClicked() {
        htLogDao.deleteLogs(logType);
        finish();
    }
}
