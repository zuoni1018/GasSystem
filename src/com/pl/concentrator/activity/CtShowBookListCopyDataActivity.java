package com.pl.concentrator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.pl.concentrator.adapter.RvBookCopyDataListAdapter;
import com.pl.concentrator.bean.gson.MoveCommunicates;
import com.pl.concentrator.bean.model.CtCopyData;
import com.pl.concentrator.dao.CtCopyDataDao;
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

public class CtShowBookListCopyDataActivity extends CtBaseTitleActivity {
    @BindView(R.id.tvBookNum)
    TextView tvBookNum;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvBookList)
    LRecyclerView rvBookList;
    @BindView(R.id.btBeginCopy)
    Button btBeginCopy;

    private String type = "";

    private String CollectorNo;//���������

    private List<String> mList;
    private LRecyclerViewAdapter mAdapter;


    private CtCopyDataDao ctCopyDataDao;

    private List<CtCopyData> mCCtCopyDataList;
    private List<CtCopyData> trueList;

    @Override
    protected int setLayout() {
        return R.layout.ct_activity_show_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("��ʾȫ��(������)");
        type = getIntent().getStringExtra("type");
        CollectorNo = getIntent().getStringExtra("CollectorNo");
        if (CollectorNo != null && !"".equals(CollectorNo)) {
//            ctBookInfoDao = new CtBookInfoDao(getContext());
            ctCopyDataDao = new CtCopyDataDao(getContext());

            //ͨ�����������ȥ��ѯ����Ϣ
            mCCtCopyDataList = new ArrayList<>();
            trueList = new ArrayList<>();
            trueList = ctCopyDataDao.getCtCopyDataListByCollectorNo(CollectorNo);
            mCCtCopyDataList.addAll(trueList);
            RvBookCopyDataListAdapter mRvBookCopyDataListAdapter = new RvBookCopyDataListAdapter(getContext(), mCCtCopyDataList);
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

    private AlertDialog alertDialog;

    @OnClick(R.id.btBeginCopy)
    public void onViewClicked() {
        List<CtCopyData> mList = new ArrayList<>();
        for (int i = 0; i < mCCtCopyDataList.size(); i++) {
            if (mCCtCopyDataList.get(i).isChoose() && mCCtCopyDataList.get(i).getCopyState() == 1) {
                mList.add(mCCtCopyDataList.get(i));
            }
        }
        if (mList.size() == 0) {
            showToast("����δѡ���κ�һ�� �ѳ� �ı�");
        } else {
            final String json = new Gson().toJson(mList);
            LogUtil.i("�ϴ�����" + json);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle("��ʾ");
            builder2.setMessage("�Ƿ��ύͬ������ѡ�ı�?"+"\n��ע: ����ѡ���д���δ��״̬�ı�,�ñ����ύ����");
            builder2.setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    upData(json);
                    alertDialog.dismiss();
                }
            });
            builder2.setNegativeButton("ȡ��", null);
            alertDialog = builder2.create();
            alertDialog.show();

        }

    }

    private void upData(String json) {
        OkHttpUtils
                .post()
                .url(setBiz.getBookInfoUrl() + AppUrl.UPDATE_COMMUNICATES)
                .addParams("Communicates", json)
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
                        if (info.getMsg().trim().equals("���³������ݳɹ�")) {
                            showToast("���³������ݳɹ�");
                            finishActivity();
                        } else {
                            showToast("���³�������ʧ��");
                        }
                    }
                });


    }
}
