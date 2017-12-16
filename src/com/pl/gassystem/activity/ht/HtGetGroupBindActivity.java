package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
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
import com.pl.gassystem.CopyingActivity;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtGetGroupBindAdapter;
import com.pl.gassystem.bean.gson.HtGetGroupBind;
import com.pl.gassystem.bean.ht.HtCustomerInfoBean;
import com.pl.gassystem.bean.ht.HtGroupInfoBean;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.utils.DensityUtils;
import com.pl.gassystem.utils.LogUtil;
import com.pl.utils.GlobalConsts;
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
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.layoutSearchBar)
    LinearLayout layoutSearchBar;
    @BindView(R.id.btGoCopy)
    Button btGoCopy;
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.tvNum)
    TextView tvNum;
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
            bt1.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new LRecyclerViewAdapter(new RvHtGetGroupBindAdapter(getContext(), mList, true));//单选
            bt1.setVisibility(View.GONE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetGroupBind();
            }
        });
        mRecyclerView.refresh();

    }

    private void GetGroupBind() {

        String mMeterFacNo;
        if (MeterFacNo.equals("3")) {
            mMeterFacNo = "";
        } else {
            mMeterFacNo = MeterFacNo;
        }

        OkHttpUtils.post()
                .url(HtAppUrl.GET_GROUP_BIND)
                .addParams("GroupNo", GroupNo)
                .addParams("MeterFacNo", mMeterFacNo)
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
                        mList.clear();
                        if (info.getArrayOfModCustomerinfo().getModCustomerinfo() != null) {
                            mList.addAll(info.getArrayOfModCustomerinfo().getModCustomerinfo());
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete(1);
                        tvNum.setText("共"+mList.size()+"张表");
                        if (MeterFacNo.equals("3")) {
                            btGoCopy.setVisibility(View.GONE);
                            bt1.setVisibility(View.GONE);
                        } else {
                            btGoCopy.setVisibility(View.VISIBLE);
                            bt1.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }


    @OnClick({R.id.btGoCopy, R.id.bt1})
    public void onViewClicked(View view) {
        final Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
        ArrayList<String> bookNos = new ArrayList<>();
        ArrayList<String> meterTypeNos = new ArrayList<>();

        switch (view.getId()) {
            case R.id.btGoCopy:
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isChoose()) {
                        bookNos.add(mList.get(i).getCommunicateNo());//获取表号码
                        meterTypeNos.add(mList.get(i).getMeterType());
                    }
                }
                break;
            case R.id.bt1:
                for (int i = 0; i < mList.size(); i++) {
                    bookNos.add(mList.get(i).getCommunicateNo());
                    meterTypeNos.add(mList.get(i).getMeterType());
                }
                break;
        }


        if (bookNos.size() > 0) {
            if (MeterFacNo.equals("2")) {
                mIntent.putExtra("bookNos", bookNos);
                mIntent.putExtra("bookNo", bookNos.get(0));
                mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄

                mIntent.putExtra("YinZi", mList.get(0).getKPYZ());
                mIntent.putExtra("XinDao", mList.get(0).getKPXD());
                mIntent.putExtra("nowKey", mList.get(0).getKEYCODE());
                mIntent.putExtra("commandType", commandType);

                mIntent.putExtra("MeterFacNo", MeterFacNo);
                startActivity(mIntent);
            } else if (MeterFacNo.equals("1")) {
                Intent mIntent2 = new Intent(getContext(), CopyingActivity.class);
                mIntent2.putExtra("meterNos", bookNos);
                mIntent2.putExtra("meterTypeNo", "05");
                mIntent2.putExtra("copyType", GlobalConsts.COPY_TYPE_BATCH);
                mIntent2.putExtra("operationType", GlobalConsts.COPY_OPERATION_COPY);
                startActivity(mIntent2);
            } else {
                //摄像表抄表
                Intent intent = new Intent(getContext(), CopyPhotoActivity.class);
                intent.putExtra("meterNos", bookNos);
                intent.putExtra("meterTypeNos", meterTypeNos);
                intent.putExtra("meterTypeNo", "");
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
            }

        } else {
            showToast("请选择表");
        }
    }

    @OnClick(R.id.tvChoose)
    public void onViewClicked2() {
        showListPopup(tvChoose);
    }


    private String MeterFacNo = "3";

    public void showListPopup(View view) {
        final String items[] = {"摄像表", "纯无线", "扩频表", "全部"};
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        //设置ListView类型的适配器
        listPopupWindow.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items));

        //给每个item设置监听事件
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvChoose.setText(items[position]);
                MeterFacNo = position + "";
                GetGroupBind();
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

        //设置ListPopupWindow的锚点,也就是弹出框的位置是相对当前参数View的位置来显示，
        listPopupWindow.setAnchorView(view);
        //ListPopupWindow 距锚点的距离，也就是相对锚点View的位置
        listPopupWindow.setHorizontalOffset(0);
        listPopupWindow.setVerticalOffset(0);
        //设置对话框的宽高
        listPopupWindow.setWidth(DensityUtils.dp2px(getContext(), 80));
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setModal(false);
        listPopupWindow.show();

    }
}
