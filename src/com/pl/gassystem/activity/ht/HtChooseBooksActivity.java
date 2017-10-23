package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.bll.GroupBindBiz;
import com.pl.entity.GroupBind;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtBookChooseAdapter;
import com.pl.gassystem.bean.ht.HtBook;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.zuoni.zuoni_common.dialog.other.CreateBookDialog;
import com.zuoni.zuoni_common.dialog.other.callback.OnCreateBookListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/19
 * 选择燃气表界面
 */

public class HtChooseBooksActivity extends HtBaseTitleActivity {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.btSure)
    Button btSure;

    private String commandType = "";//命令类型

    private boolean isChooseAll = false;

    private CreateBookDialog createBookDialog;
    private List<GroupBind> htBookList;
    private List<GroupBind> showList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_choose_books;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("燃气表选择");
        commandType = getIntent().getStringExtra("commandType");

        if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)) {
            btSure.setText("批量设置参数");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)) {
            btSure.setText("批量设置密钥");
        }


        mRecyclerView.setPullRefreshEnabled(false);//禁止下拉刷新

        htBookList = new GroupBindBiz(this).getGroupBindAll();
        showList = new ArrayList<>();
        showList.addAll(htBookList);

        mAdapter = new LRecyclerViewAdapter(new RvHtBookChooseAdapter(getContext(), showList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showList.clear();
                mAdapter.notifyDataSetChanged();
                for (int i = 0; i < htBookList.size(); i++) {
                    if (htBookList.get(i).getMeterNo().contains(s.toString())) {
                        showList.add(htBookList.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
        });


    }

    @OnClick({R.id.ivSelectAll, R.id.btSure, R.id.btAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSelectAll:
                isChooseAll = !isChooseAll;
                for (int i = 0; i < htBookList.size(); i++) {
                    htBookList.get(i).setCheck(isChooseAll);
                }
                if (!isChooseAll) {
                    ivSelectAll.setImageResource(R.mipmap.choose_02);
                } else {
                    ivSelectAll.setImageResource(R.mipmap.choose_01);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btSure:
                Intent mIntent = new Intent();
                //把表传过去
                ArrayList<String> bookNoList = new ArrayList<>();
                for (int i = 0; i < htBookList.size(); i++) {
                    if (htBookList.get(i).isCheck()) {
                        bookNoList.add(htBookList.get(i).getMeterNo());
                    }
                }
                if (bookNoList.size() == 0) {
                    showToast("您还未选择燃气表");
                } else {
                    if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)) {
                        //设置参数
                        mIntent = new Intent(getContext(), HtSetBookParameterActivity.class);
                        mIntent.putStringArrayListExtra("bookNos", bookNoList);
                    } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)) {
                        mIntent = new Intent(getContext(), HtSetKeyActivity.class);
                        mIntent.putStringArrayListExtra("bookNos", bookNoList);
                    }
                    startActivity(mIntent);
                }
                break;
            case R.id.btAdd:
                CreateBookDialog.Builder builder = new CreateBookDialog.Builder(getContext());
                builder.setCreateBookListener(new OnCreateBookListener() {
                    @Override
                    public void getBookInfo(String bookName, String bookNo) {
                        GroupBind htBook = new GroupBind();
                        htBook.setMeterName(bookName);
                        htBook.setMeterNo(bookNo);
                        htBookList.add(htBook);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                createBookDialog = builder.create();
                createBookDialog.show();
                break;
        }
    }

    private HtBook getHtBook(String bookNum, String bookName) {
        HtBook htBook = new HtBook();
        htBook.setChoose(false);
        htBook.setBookNum(bookNum);
        htBook.setBookName(bookName);
        return htBook;
    }
}
