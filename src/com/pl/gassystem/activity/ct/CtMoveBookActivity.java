package com.pl.gassystem.activity.ct;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import com.common.utils.LogUtil;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.AppUrl;
import com.pl.gassystem.adapter.ct.RvMoveBookAdapter;
import com.pl.gassystem.bean.gson.GetCollectorInfo;
import com.pl.gassystem.bean.gson.MoveCommunicates;
import com.pl.gassystem.bean.ct.Concentrator;
import com.pl.gassystem.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * ʵ������ �Ʊ�����������ȥ
 */

public class CtMoveBookActivity extends CtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.btMove)
    Button btMove;


    private ProgressDialog  mProgressDialog;
    private String communicateNo;

    private LRecyclerViewAdapter mAdapter;
    private List<Concentrator> mList;
    private AlertDialog alertDialog;

    private ArrayList<String> mCommunicateNoList;


    @Override
    protected int setLayout() {
        return R.layout.ct_activity_move_book;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mProgressDialog=new ProgressDialog(getContext());
        communicateNo = getIntent().getStringExtra("CommunicateNo");
        setTitle("�ƶ���");
        mCommunicateNoList=getIntent().getStringArrayListExtra("mCommunicateNoList");
        mList = new ArrayList<>();
        RvMoveBookAdapter mRvMoveBookAdapter = new RvMoveBookAdapter(getContext(), mList);
        mAdapter = new LRecyclerViewAdapter(mRvMoveBookAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                btMove.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(1);
                    }
                }, 1000);
            }
        });


        getListInfo();
    }

    private void getListInfo() {
        post()
                .url(setBiz.getBookInfoUrl()+AppUrl.GET_COLLECTOR_INFO)
                .addParams("zangyi", "666")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("�������б�", e.toString());
                        mRecyclerView.refreshComplete(1);
                        showToast("�������쳣");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("�������б�", response);
                        Gson gson = new Gson();
                        GetCollectorInfo mGetCollectorInfo = gson.fromJson(response, GetCollectorInfo.class);
                        if (mGetCollectorInfo.getCollectorInfo() != null) {
                            mList.clear();
                            mList.addAll(mGetCollectorInfo.getCollectorInfo());
                            mAdapter.notifyDataSetChanged();
                        }
                        mRecyclerView.refreshComplete(1);
                    }
                });
    }

    private String myCollectorNo="";
    @OnClick(R.id.btMove)
    public void onViewClicked() {
        String collectorNo = "";
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChoose()) {
                collectorNo = mList.get(i).getCollectorNo();
                myCollectorNo=collectorNo;
                break;
            }
        }
        if (collectorNo.equals("")) {
            showToast("����ѡ������");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("��ʾ");
            String message="";
            for (int i = 0; i <mCommunicateNoList.size() ; i++) {
                if(message.length()>40){
                    break;
                }
                message=message+mCommunicateNoList.get(i)+"��";
            }
            message=message+"��"+mCommunicateNoList.size()+"�ű�";
            builder.setMessage("ȷ����" + message + "  �ƶ��������� " + collectorNo + " ����");
            builder.setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    moveBook();
                }
            });
            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void moveBook() {
        String books="";
        for (int i = 0; i <mCommunicateNoList.size() ; i++) {
            books=books+mCommunicateNoList.get(i)+"&";
        }
        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl()+AppUrl.MOVE_COMMUNICATES)
                .addParams("Communicates", books)
                .addParams("CollectorNo", myCollectorNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("�ƶ���", e.toString());
                        showToast("�������쳣");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("�ƶ���", response);
                        Gson gson = new Gson();
                        MoveCommunicates mMoveCommunicates = gson.fromJson(response, MoveCommunicates.class);
                        showToast(mMoveCommunicates.getMsg());
                        finishActivity();
                    }
                });



    }


}
