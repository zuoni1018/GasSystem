package com.pl.gassystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.bean.GroupInfoStatistic;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.ExportExcelActivity;
import com.pl.gassystem.GroupInfoUpdateActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.activity.base.BaseTitleActivity;
import com.pl.gassystem.adapter.main.LvMenuAdapter;
import com.pl.gassystem.adapter.main.RvGroupingDetailsAdapter;
import com.pl.gassystem.callback.RvItemOnClickListener;
import com.pl.gassystem.utils.LogUtil;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/21
 * 分组详情
 */

public class GroupingDetailsActivity extends BaseTitleActivity {
    @BindView(R.id.layoutMenu)
    LinearLayout layoutMenu;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvAllNum)
    TextView tvAllNum;
    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.tvNoCopyNum)
    TextView tvNoCopyNum;
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;

    private String bookNo;//所在账册编号
    private String bookName;//账册名称
    private String meterTypeNo;//表具类型

    private CopyBiz copyBiz;
    private GroupInfoBiz groupInfoBiz;

    private List<GroupInfoStatistic> showList;
    private List<GroupInfoStatistic> searchList;

    private boolean isRun = true;
    private LRecyclerViewAdapter mAdapter;


    private boolean isRefresh = false;//当前是否在刷新中

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showList.clear();
            showList.addAll(searchList);
            mAdapter.notifyDataSetChanged();

            if (msg.what == 1) {
                etSearch.setEnabled(true);
                isRefresh = false;
                mRecyclerView.refreshComplete(1);
                showToast("数据查询完毕");
                int all = 0;
                int copy = 0;
                int noCopy = 0;
                //计算总数
                for (int i = 0; i < showList.size(); i++) {
                    all = all + showList.get(i).getAllNum();
                    copy = copy + showList.get(i).getCopyNum();
                    noCopy = noCopy + showList.get(i).getNoNum();
                }
                tvAllNum.setText("总数(" + all + ")");
                tvCopyNum.setText("已抄(" + copy + ")");
                tvNoCopyNum.setText("未抄(" + noCopy + ")");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("分组详情");
        setViewAnimation(layoutMenu);

        showList = new ArrayList<>();
        searchList = new ArrayList<>();

        bookNo = getIntent().getStringExtra("BookNo");
        bookName = getIntent().getStringExtra("BookName");
        meterTypeNo = getIntent().getStringExtra("meterTypeNo");

        copyBiz = new CopyBiz(this);
        groupInfoBiz = new GroupInfoBiz(this);


        RvGroupingDetailsAdapter mRvGroupingDetailsAdapter = new RvGroupingDetailsAdapter(getContext(), showList);
        mRvGroupingDetailsAdapter.setRvItemOnClickListener(new RvItemOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent mIntent = new Intent(getContext(), CopyDetailsActivity.class);
                mIntent.putExtra("meterNos", showList.get(position).getMeterNos());//表号们
                mIntent.putExtra("meterTypeNo", meterTypeNo);//表计类型
                mIntent.putExtra("name", showList.get(position).getPoint());
                mIntent.putExtra("GroupInfo", showList.get(position).getmGroupInfo());//分组信息
                mIntent.putExtra("noCopy", showList.get(position).getNoNum());
                mIntent.putExtra("CopyNum", showList.get(position).getCopyNum());
                startActivity(mIntent);
            }
        });
        mAdapter = new LRecyclerViewAdapter(mRvGroupingDetailsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefresh) {
                    LogUtil.i("嘻嘻", "开启线程去查数据库");
                    etSearch.setEnabled(false);
                    //开启一个线程去搜索
                    new SearchThread().start();
                }
            }
        });

        mRecyclerView.refresh();//自动刷新

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
                    showList.addAll(searchList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showList.clear();
                    for (int i = 0; i < searchList.size(); i++) {
                        String myText = searchList.get(i).getPoint() + searchList.get(i).getMeterNos();
                        if (myText.contains(s.toString().trim())) {
                            showList.add(searchList.get(i));
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_grouping_details;
    }

    @OnClick(R.id.layoutMenu)
    public void onViewClicked() {

        List<String> mMenuList = new ArrayList<>();
        mMenuList.add("导出账册");
        mMenuList.add("新建抄表点");
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new LvMenuAdapter(mMenuList, getContext()));
        //给每个item设置监听事件
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(getContext(), ExportExcelActivity.class);
                        intent.putExtra("BookNo", bookNo);
                        intent.putExtra("BookName", bookName);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), GroupInfoUpdateActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
                        intent.putExtra("BookNo", bookNo);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        startActivity(intent);
                        break;
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutMenu.setEnabled(false);
                layoutMenu.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutMenu.setEnabled(true);
                    }
                }, 50);
            }
        });

        //设置ListPopupWindow的锚点,也就是弹出框的位置是相对当前参数View的位置来显示，
        listPopupWindow.setAnchorView(layoutMenu);
        //ListPopupWindow 距锚点的距离，也就是相对锚点View的位置
        listPopupWindow.setHorizontalOffset(0);
        listPopupWindow.setVerticalOffset(0);
        //设置对话框的宽高
        listPopupWindow.setWidth(300);
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setModal(false);
        listPopupWindow.show();
    }


    /**
     * 从数据库中查询信息
     */
    private class SearchThread extends Thread {
        @Override
        public void run() {
            searchList.clear();

            //获得账册下的所有分组编号
            ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);

            for (int i = 0; i < groupInfos.size() && isRun; i++) {

                //获得分组下的所有表编号
                ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());

                int noNum = 0;
                int copyNum = 0;
                int allNum = 0;

                if (meterTypeNo.equals("04")) {// IC卡无线
                    ArrayList<CopyDataICRF> copyDataICRFs = copyBiz.getCopyDataICRFByMeterNos(meterNos, 2);
                    if (copyDataICRFs != null) {
                        allNum = copyDataICRFs.size();
                        for (int j = 0; j < copyDataICRFs.size(); j++) {
                            if (copyDataICRFs.get(j).getCopyState() == 1) {
                                copyNum++;
                            } else {
                                noNum++;
                            }
                        }
                    }
                } else if (meterTypeNo.equals("05")) {// 纯无线
                    ArrayList<CopyData> copyDataList = copyBiz.getCopyDataByMeterNos(meterNos, 2);
                    allNum = copyDataList.size();
                    for (int j = 0; j < copyDataList.size(); j++) {
                        if (copyDataList.get(j).getCopyState() == 1) {
                            copyNum++;
                        } else {
                            noNum++;
                        }
                    }
                }

                //
                GroupInfoStatistic mGroupInfoStatistic = new GroupInfoStatistic();
                mGroupInfoStatistic.setAllNum(allNum);
                mGroupInfoStatistic.setCopyNum(copyNum);
                mGroupInfoStatistic.setNoNum(noNum);
                mGroupInfoStatistic.setPoint(groupInfos.get(i).getGroupName().trim() + "");
                mGroupInfoStatistic.setMeterNos(meterNos);
                mGroupInfoStatistic.setmGroupInfo(groupInfos.get(i));
                if (!mGroupInfoStatistic.getPoint().trim().equals("")) {
                    searchList.add(mGroupInfoStatistic);
                }
                //40条刷新一次
                if (i % 40 == 0) {
                    Message message = Message.obtain();
                    mHandler.sendMessage(message);
                }
            }
            //查询结束
            Message message = Message.obtain();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}
