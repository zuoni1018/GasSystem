package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.bll.GroupBindBiz;
import com.pl.entity.GroupBind;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtBookChooseAdapter;
import com.pl.gassystem.bean.ht.HtBook;
import com.pl.gassystem.bean.ht.HtSendMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 */

public class HtBookListActivity extends HtBaseTitleActivity {
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.btGoCopy)
    Button btGoCopy;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.etAddBook)
    EditText etAddBook;
    @BindView(R.id.tvAddBook)
    Button tvAddBook;

    private List<GroupBind> mList;
    private LRecyclerViewAdapter mAdapter;

    private String nowCommandType = "";//当前操作类型

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("杭天燃气表测试");
        mList = new GroupBindBiz(this).getGroupBindAll();
//        mList.add(getHtBook("04000015"));
//        mList.add(getHtBook("05170016"));
//        mList.add(getHtBook("04160105"));
        mAdapter = new LRecyclerViewAdapter(new RvHtBookChooseAdapter(getContext(), mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbValveState:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_DOOR_STATE;
                        break;
                    case R.id.rbOpenValve:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_OPEN_DOOR;
                        break;
                    case R.id.rbCloseValve:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_CLOSE_DOOR;
                        break;
                    case R.id.rbCopyFrozen:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_COPY_FROZEN;
                        break;
                    case R.id.rbCopy:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_COPY_NORMAL;
                        break;
                    case R.id.rbSetBookNo:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT;
                        break;
                    case R.id.rbQueryParameter:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER;
                        break;
                    case R.id.rbSetParameter:
                        nowCommandType = HtSendMessage.COMMAND_TYPE_SET_PARAMETER;
                        break;

                }
            }
        });

        tvAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addbook = etAddBook.getText().toString().trim();
                if (addbook.length() != 8) {
                    showToast("杭天表号为8位 请重新输入");
                } else {
                    GroupBind htBook = new GroupBind();
                    htBook.setGroupNo(addbook);
                    htBook.setCheck(false);
                    mList.add(htBook);
                    mAdapter.notifyDataSetChanged();
                    etAddBook.setText("");
                }

            }
        });
    }

    private HtBook getHtBook(String bookNum) {
        HtBook htBook = new HtBook();
        htBook.setChoose(false);
        htBook.setBookNum(bookNum);

        return htBook;
    }

    @OnClick(R.id.btGoCopy)
    public void onViewClicked() {


        if (nowCommandType.equals("")) {
            showToast("请先选择命令类型");
        } else {
            ArrayList<String> BookNoList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isCheck()) {
                    BookNoList.add(mList.get(i).getMeterNo());
                }
            }
            if (BookNoList.size() == 0) {
                showToast("您还未选择燃气表");
            } else {
                Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                mIntent.putExtra("commandType", nowCommandType);//输入命令指令
                if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_DOOR_STATE)) {
                    //查询阀门状态
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_CLOSE_DOOR)) {
                    //关闭阀门
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_OPEN_DOOR)) {
                    //打开阀门
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {
                    //查询参数
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)
                        | nowCommandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {
                    if (BookNoList.size() > 1) {
                        mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄
                        mIntent.putStringArrayListExtra("bookNos", BookNoList);
                    } else {
                        mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_SINGLE);//单抄
                        mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                    }
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)) {
                    //设置表号需要跳转到另外一个界面
                    mIntent = new Intent(getContext(), HtChangeBookNoOrCumulantActivity.class);
//                    mIntent.putExtra("commandType", nowCommandType);//输入命令指令
//                    mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_SINGLE);//单抄
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//获取表
                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)) {
                    //设置表号需要跳转到另外一个界面
                    mIntent = new Intent(getContext(), HtSetBookParameterActivity.class);
                    mIntent.putExtra("bookNos", BookNoList);
                }
                startActivity(mIntent);
            }


        }


    }
}
