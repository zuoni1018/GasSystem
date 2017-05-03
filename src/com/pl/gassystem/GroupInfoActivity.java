package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.adapter.GroupInfoAdapter;
import com.pl.bll.CopyBiz;
import com.pl.bll.GroupInfoBiz;
import com.pl.entity.GroupInfo;
import com.pl.utils.GlobalConsts;

import java.util.ArrayList;


/**
 * 抄表分组界面
 */
public class GroupInfoActivity extends Activity {

    private ListView lvGroupInfo;
    private GroupInfoAdapter adapter;
    private GroupInfoBiz groupInfoBiz;
    private ImageButton btnquit, btnmenu;
    private String bookNo;
    private String bookName;
    private TextView tvTitlebar_name;
    private String meterTypeNo;
    private boolean isSelect;
    private CopyBiz copyBiz;

    private static final int MENU_CTX_UPDATE = 1;
    private static final int MENU_CTX_DELETE = 2;

    private void setupView() {
        lvGroupInfo = (ListView) findViewById(R.id.lvBookInfo);
        ArrayList<GroupInfo> groupInfos = groupInfoBiz.getGroupInfos(bookNo);
        adapter = new GroupInfoAdapter(this, groupInfos);
        lvGroupInfo.setAdapter(adapter);
        btnquit = (ImageButton) findViewById(R.id.btnquit);
        btnmenu = (ImageButton) findViewById(R.id.btnmenu);
        if (isSelect) {
            btnmenu.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_book_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_listview);

        bookNo = getIntent().getStringExtra("BookNo").toString();
        bookName = getIntent().getStringExtra("BookName").toString();
        meterTypeNo = getIntent().getStringExtra("meterTypeNo").toString();
        isSelect = getIntent().getBooleanExtra("isSelect", false);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_name);
        tvTitlebar_name.setText(""+bookName + "的分组");

        groupInfoBiz = new GroupInfoBiz(this);
        copyBiz = new CopyBiz(this);
        setupView();
        addListener();
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnmenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopupMenu(v);

            }
        });

        lvGroupInfo.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenuInfo menuInfo) {
                        if (isSelect == false) {
                            menu.setHeaderIcon(android.R.drawable.ic_dialog_info);
                            menu.setHeaderTitle("分组信息");
                            menu.add(1, MENU_CTX_UPDATE, 1, "修改分组");
                            menu.add(1, MENU_CTX_DELETE, 2, "删除分组");
                        }
                    }
                });

        lvGroupInfo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isSelect) {
                    Intent intent = new Intent(GroupInfoActivity.this, CopyGroupActivity.class);
                    GroupInfo gpInfo = adapter.getItem(position);
                    intent.putExtra("GroupInfo", gpInfo);
                    startActivity(intent);
                } else {
                    //从分类查询里面点进来的
                    GroupInfo gpInfo = adapter.getItem(position);
                    ArrayList<String> meterNos = copyBiz.GetCopyMeterNo(gpInfo.getGroupNo());//获得小区里所有的东西
                    if (meterNos != null && meterNos.size() > 0) {
                        Intent intent = new Intent(GroupInfoActivity.this, CopyResultActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                        intent.putExtra("meterNos", meterNos);
                        intent.putExtra("meterTypeNo", gpInfo.getMeterTypeNo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(GroupInfoActivity.this, "该分组内无表具", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 获取被长按项数据
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        GroupInfo groupInfo = adapter.getItem(info.position);

        // 执行操作
        switch (item.getItemId()) {
            case MENU_CTX_UPDATE:
                Intent intent = new Intent(this, GroupInfoUpdateActivity.class);
                intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_UPDATE);
                intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_DATA, groupInfo);
                intent.putExtra("BookNo", bookNo);
                intent.putExtra("meterTypeNo", meterTypeNo);
                startActivity(intent);
                break;
            case MENU_CTX_DELETE:
                int count = groupInfoBiz.removeGroupInfo(groupInfo.getGroupNo());
                if (count != 0) {
                    adapter.removeItem(info.position);
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.group_info);

        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_addgroupinfo:// 新建
                        Intent intent = new Intent(GroupInfoActivity.this, GroupInfoUpdateActivity.class);
                        intent.putExtra(GlobalConsts.EXTRA_BOOKINFO_OP_TYPE, GlobalConsts.TYPE_ADD);
                        intent.putExtra("BookNo", bookNo);
                        intent.putExtra("meterTypeNo", meterTypeNo);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onRestart() {
        // 返回该界面时刷新数据
        super.onRestart();
        adapter.changeData(groupInfoBiz.getGroupInfos(bookNo));
    }

}
