package com.pl.gassystem.activity.ht;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.pl.gassystem.CopyPhotoActivity;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtGetReadMeterInfoAdapter;
import com.pl.gassystem.bean.gson.HtGetReadMeterInfo;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.utils.DensityUtils;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.Xml2Json;
import com.pl.utils.GlobalConsts;
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
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.layoutSearchBar)
    LinearLayout layoutSearchBar;
    @BindView(R.id.bt2)
    Button bt2;

    private LRecyclerViewAdapter mAdapter;
    private List<HtGetReadMeterInfo.ArrayOfModCustomerinfoBean.ModCustomerinfoBean> mList;
    private List<HtGetReadMeterInfo.ArrayOfModCustomerinfoBean.ModCustomerinfoBean> showList;

    private String AreaNo = "";
    private String ReadState = "";


    private String MeterFacNo = "3";//ȫ��

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_get_read_meter_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("�鿴��Ҫ�����ı�");
        ButterKnife.bind(this);


        AreaNo = getIntent().getStringExtra("AreaNo");
        ReadState = getIntent().getStringExtra("ReadState");
        switch (ReadState) {
            case "1":
                setTitle("�鿴��Ҫ�����ı�(δ��)");
                break;
            case "0":
                setTitle("�鿴��Ҫ�����ı�(�ѳ�)");
                break;
            default:
                setTitle("�鿴��Ҫ�����ı�(ȫ��)");
                break;
        }


        LogUtil.i("�õ�AreaNo", AreaNo);
        mList = new ArrayList<>();
        showList = new ArrayList<>();

        mAdapter = new LRecyclerViewAdapter(new RvHtGetReadMeterInfoAdapter(getContext(), showList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetReadMeterInfo();
            }
        });
        mRecyclerView.refresh();


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                showList.clear();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getCommunicateNo().contains(s)) {
                        showList.add(mList.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void GetReadMeterInfo() {
        String mMeterFacNo;
        if (MeterFacNo.equals("3")) {
            mMeterFacNo = "";
        } else {
            mMeterFacNo = MeterFacNo;
        }

        OkHttpUtils.post()
                .url(HtAppUrl.GET_READ_METER_INFO)
                .addParams("AreaNo", AreaNo)
                .addParams("ReadState", ReadState)
                .addParams("MeterFacNo", mMeterFacNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToast(e.toString());
                        LogUtil.i("��ȡ��Ҫ����" + e.toString());
                        mRecyclerView.refreshComplete(1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("��ȡ��Ҫ����1\n" + response);
                        LogUtil.i("��ȡ��Ҫ����2" + Xml2Json.xml2JSON2List("ArrayOfModCustomerinfo", "ModCustomerinfo", response));

                        Gson gson = new Gson();
                        HtGetReadMeterInfo info = gson.fromJson(Xml2Json.xml2JSON2List("ArrayOfModCustomerinfo", "ModCustomerinfo", response), HtGetReadMeterInfo.class);

                        mList.clear();
                        showList.clear();
                        if (info.getArrayOfModCustomerinfo().getModCustomerinfo() != null) {
                            mList.addAll(info.getArrayOfModCustomerinfo().getModCustomerinfo());
                            showList.addAll(mList);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete(1);

                        if (MeterFacNo.equals("3")) {
                            bt2.setVisibility(View.GONE);
                        } else {
                            bt2.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }


    @OnClick({R.id.bt2})
    public void onViewClicked(View view) {
        ArrayList<String> bookNos = new ArrayList<>();
        ArrayList<String> meterTypeNos = new ArrayList<>();

        switch (view.getId()) {
            case R.id.bt2:
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isCheck()) {
                        bookNos.add(mList.get(i).getCommunicateNo());
                        meterTypeNos.add(mList.get(i).getMeterType());
                    }
                }
                break;
//            case R.id.bt2:
//                for (int i = 0; i < mList.size(); i++) {
//                    bookNos.add(mList.get(i).getCommunicateNo());
//                }
//                break;
        }

        if (bookNos.size() > 0) {

            if (MeterFacNo.equals("1") | MeterFacNo.equals("2")) {
                final Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                mIntent.putExtra("bookNos", bookNos);
                mIntent.putExtra("bookNo", bookNos.get(0));
                mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//Ⱥ��

                mIntent.putExtra("YinZi", mList.get(0).getKPYZ());
                mIntent.putExtra("XinDao", mList.get(0).getKPXD());
                mIntent.putExtra("nowKey", mList.get(0).getKEYCODE());
                mIntent.putExtra("MeterFacNo", MeterFacNo);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ѡ��ȡ�����߱�����");
                final String[] cities = {"ʵʱ����", "��ȡ������"};
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                        } else {
                            mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                        }
                        startActivity(mIntent);
                    }
                });
                builder.show();
            } else {
                //�������
                Intent intent = new Intent(getContext(), CopyPhotoActivity.class);
                intent.putExtra("meterNos", bookNos);
                intent.putExtra("meterTypeNos", meterTypeNos);
                intent.putExtra("meterTypeNo", "0");
                intent.putExtra("baseType", "1");
                intent.putExtra("YHTM", "");
                intent.putExtra("XBDS", "");
                intent.putExtra("MQBBH", "");
                intent.putExtra("HUNANME", "");
                intent.putExtra("OTEL", "");
                intent.putExtra("ADDR", "");
                intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
                intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                startActivity(intent);
//                showToast("���������δ����");
            }
        } else {
            showToast("��ѡ����");
        }

    }

    public void showListPopup(View view) {
        final String items[] = {"�����", "������", "��Ƶ��", "ȫ��"};
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        //����ListView���͵�������
        listPopupWindow.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items));

        //��ÿ��item���ü����¼�
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvChoose.setText(items[position]);
                MeterFacNo = position + "";
                GetReadMeterInfo();
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvChoose.setEnabled(false);
                tvChoose.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvChoose.setEnabled(true);
                    }
                }, 200);
            }
        });

        //����ListPopupWindow��ê��,Ҳ���ǵ������λ������Ե�ǰ����View��λ������ʾ��
        listPopupWindow.setAnchorView(view);
        //ListPopupWindow ��ê��ľ��룬Ҳ�������ê��View��λ��
        listPopupWindow.setHorizontalOffset(0);
        listPopupWindow.setVerticalOffset(0);
        //���öԻ���Ŀ��
        listPopupWindow.setWidth(DensityUtils.dp2px(getContext(), 80));
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setModal(false);
        listPopupWindow.show();

    }


    @OnClick(R.id.tvChoose)
    public void onViewClicked() {
        showListPopup(tvChoose);
    }


}
