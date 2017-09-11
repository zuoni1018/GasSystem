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
import com.pl.concentrator.AppUrl;
import com.pl.concentrator.activity.base.CtBaseTitleActivity;
import com.pl.concentrator.adapter.RvBookCopyDataICRFListAdapter;
import com.pl.concentrator.bean.gson.MoveCommunicates;
import com.pl.concentrator.bean.model.CtCopyDataICRF;
import com.pl.concentrator.dao.CtCopyDataICRFDao;
import com.pl.gassystem.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * ��Ҫ�жϱ�������
 * �����ߺ�IC������ʹ�õ�Adapter��һ��
 */

public class CtShowBookListCopyDataICRFActivity extends CtBaseTitleActivity {
    @BindView(R.id.tvBookNum)
    TextView tvBookNum;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvBookList)
    LRecyclerView rvBookList;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;

    private String type="";

    private String CollectorNo;//���������

    private List<String> mList;
    private LRecyclerViewAdapter mAdapter;


//    private CtCopyDataDao ctCopyDataDao;
    private CtCopyDataICRFDao ctCopyDataICRFDao;

    private List<CtCopyDataICRF> mCCtCopyDataList;
    private List<CtCopyDataICRF> trueList;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_show_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("��ʾȫ��(IC����)");
        type=getIntent().getStringExtra("type");
        CollectorNo = getIntent().getStringExtra("CollectorNo");
        if (CollectorNo != null && !"".equals(CollectorNo)) {
//            ctBookInfoDao = new CtBookInfoDao(getContext());
            ctCopyDataICRFDao = new CtCopyDataICRFDao(getContext());

            //ͨ�����������ȥ��ѯ����Ϣ
            mCCtCopyDataList = new ArrayList<>();
            trueList = new ArrayList<>();
            trueList = ctCopyDataICRFDao.getCtCopyDataICRFListByCollectorNo(CollectorNo);
            mCCtCopyDataList.addAll(trueList);
            RvBookCopyDataICRFListAdapter mRvBookCopyDataListAdapter = new RvBookCopyDataICRFListAdapter(getContext(), mCCtCopyDataList);
            mAdapter = new LRecyclerViewAdapter(mRvBookCopyDataListAdapter);
            rvBookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            DividerDecoration divider = new DividerDecoration.Builder(this).setColorResource(R.color.color_blue).build();
            rvBookList.addItemDecoration(divider);
            rvBookList.setAdapter(mAdapter);
            rvBookList.setPullRefreshEnabled(false);

            tvBookNum.setText("��" + mCCtCopyDataList.size() + "��");

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
        List<CtCopyDataICRF> mList = new ArrayList<>();
        for (int i = 0; i < mCCtCopyDataList.size(); i++) {
            if (mCCtCopyDataList.get(i).isChoose()&&mCCtCopyDataList.get(i).getCopyState()==1) {
                mList.add(mCCtCopyDataList.get(i));
            }
        }
        if (mList.size() == 0) {
            showToast("����δѡ��һ�ű�");
        } else {
            String json = new Gson().toJson(mList);
            LogUtil.i("�ϴ�����" + json);
            upData(json);
        }
    }

    private void upData(String json) {
        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl()+AppUrl.UPDATE_COMMUNICATES)
                .addParams("Communicates",json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("�ϴ���������", e.toString());
                        showToast("�������쳣");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("�ϴ���������", response);
                        Gson gson = new Gson();
                        MoveCommunicates info = gson.fromJson(response, MoveCommunicates.class);
                        if(info.getMsg().trim().equals("���³������ݳɹ�")){
                            showToast("���³������ݳɹ�");
                            finishActivity();
                        }else {
                            showToast("���³�������ʧ��");
                        }
                    }
                });


    }
}
