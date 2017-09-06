package com.pl.concentrator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.LogUtil;
import com.google.gson.Gson;
import com.pl.concentrator.AppUrl;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.activity.copy.CtCopyingActivity;
import com.pl.concentrator.bean.gson.GetCollectorInfoByCollectorNo;
import com.pl.concentrator.bean.model.Concentrator;
import com.pl.gassystem.R;
import com.pl.utils.DensityUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 抄表详情界面
 */

public class CtCopySituationActivity extends CtBaseTitleActivity {

    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.ivCopy)
    ImageView ivCopy;
    @BindView(R.id.tvNoCopyNum)
    TextView tvNoCopyNum;
    @BindView(R.id.ivNoCopy)
    ImageView ivNoCopy;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;
    @BindView(R.id.btCopyAllBook)
    Button btCopyAllBook;
    @BindView(R.id.btShowAllBook)
    Button btShowAllBook;
    @BindView(R.id.layoutNetworking)
    LinearLayout layoutNetworking;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private Intent mIntent;

    private String collectorNo;//集中器编号

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_copy_situation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("XX的抄表详情");
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("载入中...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        collectorNo = getIntent().getStringExtra("CollectorNo");
        if (collectorNo == null) {
            showToast("集中器不存在");
            progressDialog.dismiss();
            finish();
        } else {
            if (collectorNo.equals("")) {
                showToast("集中器不存在");
                progressDialog.dismiss();
                finish();
            } else {
                //通过集中器编号去查询数据

            }
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });
        getInfo();
    }

    private void getInfo() {

        OkHttpUtils
                .post()
                .url(AppUrl.GET_COLLECTOR_INFO_BY_COLLECTOR_NO)
                .addParams("CollectorNo", collectorNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("集中器抄表信息", e.toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                        showToast("集中器抄表信息");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("集中器抄表信息", response);
                        Gson gson = new Gson();
                        GetCollectorInfoByCollectorNo mGetCollectorInfoByCollectorNo = gson.fromJson(response, GetCollectorInfoByCollectorNo.class);
                        mSwipeRefreshLayout.setRefreshing(false);
                        setChart(mGetCollectorInfoByCollectorNo.getCollectorInfoByCollectorNo().get(0));


                    }
                });


    }

    //设置图表
    private void setChart(Concentrator concentrator) {
        tvCopyNum.setText(concentrator.getReadNum());
        tvNoCopyNum.setText(concentrator.getNotReadNum());

        //设置柱状图高度
        int height = DensityUtils.dp2px(this, 103);
        double c = (concentrator.getTrueNotReadNum() * 1.000 / (concentrator.getTrueAllNum()));
        double c2=(concentrator.getTrueReadNum() * 1.000 / (concentrator.getTrueAllNum()));
        int noCopyHeight = (int) (c * height);
        int CopyHeight = (int) (c2 * height);
        ViewGroup.LayoutParams para1;
        para1 = ivNoCopy.getLayoutParams();
        para1.height = noCopyHeight + 4;
        ivNoCopy.setLayoutParams(para1);
        ViewGroup.LayoutParams para2;
        para2 = ivCopy.getLayoutParams();
        para2.height = CopyHeight + 4;
        ivCopy.setLayoutParams(para2);
    }


    @OnClick({R.id.btBeginCopy, R.id.btCopyAllBook, R.id.btShowAllBook, R.id.layoutNetworking})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBeginCopy:
                //开始抄表
                mIntent = new Intent(getContext(), CtCopyBookActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                startActivity(mIntent);
                break;
            case R.id.btCopyAllBook:
                mIntent = new Intent(getContext(), CtCopyingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btShowAllBook:

                mIntent = new Intent(getContext(), CtShowBookListActivity.class);
                startActivity(mIntent);
                break;
            case R.id.layoutNetworking:
                //实抄组网
                mIntent = new Intent(getContext(), CtNetworkingActivity.class);
                mIntent.putExtra("CollectorNo", collectorNo);
                startActivity(mIntent);
                break;
        }
    }
}




