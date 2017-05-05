package com.pl.gassystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.KeyBoardUtils;
import com.common.utils.ToastUtils;
import com.pl.bean.GroupInfoStatistic;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.GroupInfo;
import com.pl.gassystem.base.BaseTitleActivity;
import com.pl.utils.GlobalConsts;
import com.pl.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/4/19
 * 账册分组统计界面
 */

public class GroupInfoStatisticActivity extends BaseTitleActivity implements SwipeRefreshLayout.OnRefreshListener {


    private ListView mListView;
    private ListViewAdapter mAdapter;
    private String bookNo;
    private String bookName;
    private String meterTypeNo;
    private boolean isSelect;
    private CopyBiz copyBiz;
    private GroupInfoBiz groupInfoBiz;
    private List<GroupInfoStatistic> mGroupInfoStatisticList;
    private List<GroupInfoStatistic> mGroupInfoStatisticList2;
    private boolean isSearch = true;//搜索是否完成
    private boolean isRun = false;//是否运行查询线程
    private EditText etSearch;
    private ImageView ivSearch;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeLayout;
    private mySearchThread t;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mGroupInfoStatisticList.clear();
            mGroupInfoStatisticList.addAll(mGroupInfoStatisticList2);
            mAdapter.notifyDataSetChanged();
            if (msg.what == 1) {
                isRun = false;
                mProgressBar.setVisibility(View.GONE);
                mSwipeLayout.setRefreshing(false);
                Toast.makeText(GroupInfoStatisticActivity.this, "数据查询完毕", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRun = true;
        //从数据库里查询所有数据
        mySearchThread t = new mySearchThread();
        t.start();
    }

    /**
     * 显示界面的时候重新获取数据刷新一次界面
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        mGroupInfoStatisticList = new ArrayList<>();
        mGroupInfoStatisticList2 = new ArrayList<>();
        isRun = true;
        copyBiz = new CopyBiz(this);
        groupInfoBiz = new GroupInfoBiz(this);

        bookNo = getIntent().getStringExtra("BookNo").toString();
        bookName = getIntent().getStringExtra("BookName").toString();
        meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();
        isSelect = getIntent().getBooleanExtra("isSelect", false);

        mAdapter = new ListViewAdapter(mGroupInfoStatisticList, GroupInfoStatisticActivity.this);
        mListView.setAdapter(mAdapter);
    }


    @Override
    protected void initUI() {
        setTitle("分组详情");
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mListView = (ListView) findViewById(R.id.mListView);
        mSwipeLayout = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.colorPrimaryDark); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
    }


    @Override
    protected void initOnClickListener() {
        ivSearch.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(GroupInfoStatisticActivity.this, CopyDetailsActivity.class);
//                mIntent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                mIntent.putExtra("meterNos", mGroupInfoStatisticList.get(position).getMeterNos());
                mIntent.putExtra("meterTypeNo", meterTypeNo);
                mIntent.putExtra("name", mGroupInfoStatisticList.get(position).getPoint());
                mIntent.putExtra("GroupInfo", mGroupInfoStatisticList.get(position).getmGroupInfo());
                mIntent.putExtra("noCopy", mGroupInfoStatisticList.get(position).getNoNum());
                mIntent.putExtra("CopyNum", mGroupInfoStatisticList.get(position).getCopyNum());
                startActivity(mIntent);
            }
        });

        findViewById(R.id.btAddGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoStatisticActivity.this, GroupInfoUpdateActivity.class);
                intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
                intent.putExtra("BookNo", bookNo);
                intent.putExtra("meterTypeNo", meterTypeNo);
                startActivity(intent);
            }
        });


        findViewById(R.id.btExportExcel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GroupInfoStatisticActivity.this, ExportExcelActivity.class);
                intent.putExtra("BookNo", bookNo);
                intent.putExtra("BookName", bookName);
                intent.putExtra("meterTypeNo", meterTypeNo);
                intent.putExtra("isSelect", false);
                startActivity(intent);

            }
        });
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_group_info_statistic;
    }


    /**
     * 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                searchData();
                break;
        }

    }

    /**
     * 查询数据
     */
    private void searchData() {

        String searchText = etSearch.getText().toString().trim();
        KeyBoardUtils.closeKeybord(etSearch, GroupInfoStatisticActivity.this);

        if (isSearch) {
            Toast.makeText(GroupInfoStatisticActivity.this, "正在查询全部数据，请稍后查询问", Toast.LENGTH_SHORT).show();
        } else {
            //查询全部
            if (searchText.equals("")) {
                mGroupInfoStatisticList.clear();
                mGroupInfoStatisticList.addAll(mGroupInfoStatisticList2);
                mAdapter.notifyDataSetChanged();
            } else {
                //查询搜索内容
                List<GroupInfoStatistic> searchList = new ArrayList<>();
                for (int i = 0; i < mGroupInfoStatisticList2.size(); i++) {
                    if (mGroupInfoStatisticList2.get(i).getPoint().contains(searchText)) {
                        searchList.add(mGroupInfoStatisticList2.get(i));
                    }
                }
                if (searchList.size() == 0) {
                    Toast.makeText(GroupInfoStatisticActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                } else {
                    mGroupInfoStatisticList.clear();
                    mGroupInfoStatisticList.addAll(searchList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        //从数据库中重新读取数据
        LogUtil.i("下拉刷新重新获取数据库");
        //如果当前线程正在运行
        if (!isRun) {
            isRun = true;
            t = new mySearchThread();
            t.start();
        } else {
            ToastUtils.showToast(getApplication(), "正在刷新数据，请稍后再试");
            mSwipeLayout.setRefreshing(false);
        }
    }

    private class mySearchThread extends Thread {
        @Override
        public void run() {
//        super.run();

            mGroupInfoStatisticList2.clear();
            //通过 bookNo 去查询获得所有楼的信息
            ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);
            //获得每栋楼的里面信息
            if (isRun) {
                for (int i = 0; i < groupInfos.size(); i++) {
                    ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(groupInfos.get(i).getGroupNo());//获得每栋楼 里每个住户的信息
                    int noNum = 0;
                    int copyNum = 0;
                    int allNum = 0;
                    if (meterTypeNo.equals("04")) {// IC卡无线
                        LogUtil.i("查表", "IC卡无线");
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
                        ArrayList<CopyData> copyDatas = copyBiz.getCopyDataByMeterNos(meterNos, 2);
                        LogUtil.i("查表", "纯无线" + copyDatas.size());
                        if (copyDatas != null) {
                            allNum = copyDatas.size();
                            for (int j = 0; j < copyDatas.size(); j++) {
                                if (copyDatas.get(j).getCopyState() == 1) {
                                    copyNum++;
                                } else {
                                    noNum++;
                                }
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
                    mGroupInfoStatisticList2.add(mGroupInfoStatistic);
                    //40条刷新一次
                    if (i % 40 == 0) {
                        Message message = Message.obtain();
                        mHandler.sendMessage(message);
                    }
                }
            }
            Message message = Message.obtain();
            message.what = 1;
            mHandler.sendMessage(message);
            isSearch = false;


        }
    }
}


class ListViewAdapter extends BaseAdapter {
    private List<GroupInfoStatistic> mList;
    private Context mContext;

    ListViewAdapter(List<GroupInfoStatistic> pList, Context pContext) {
        mList = pList;
        mContext = pContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item_group_info_statistic, null);
            holder.tvPoint = (TextView) convertView.findViewById(R.id.tvPoint);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            holder.tvDorP = (TextView) convertView.findViewById(R.id.tvDorP);
            holder.tvNot = (TextView) convertView.findViewById(R.id.tvNot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvPoint.setText(mList.get(position).getPoint());
        holder.tvNum.setText("" + mList.get(position).getAllNum() + "");
        holder.tvDorP.setText("" + mList.get(position).getCopyNum() + "");
        holder.tvNot.setText("" + mList.get(position).getNoNum() + "");
        return convertView;
    }

    private class ViewHolder {
        private TextView tvPoint;
        private TextView tvNum;
        private TextView tvDorP;
        private TextView tvNot;
    }

}