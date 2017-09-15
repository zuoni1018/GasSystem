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

    private List<HtBook> mList;
    private LRecyclerViewAdapter mAdapter;

    private String nowCommandType = "";//��ǰ��������

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("����ȼ�������");
        mList = new ArrayList<>();
        mList.add(getHtBook("04000015"));
        mList.add(getHtBook("05170016"));
        mList.add(getHtBook("04160105"));
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
                }
            }
        });

        tvAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addbook=etAddBook.getText().toString().trim();
                if(addbook.length()!=8){
                    showToast("������Ϊ8λ ����������");
                }else {
                    HtBook htBook=new HtBook();
                    htBook.setBookNum(addbook);
                    htBook.setChoose(false);
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
            showToast("����ѡ����������");
        } else {
            ArrayList<String> BookNoList = new ArrayList<>();
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isChoose()) {
                    BookNoList.add(mList.get(i).getBookNum());
                }
            }
            if (BookNoList.size() == 0) {
                showToast("����δѡ��ȼ����");
            } else {
                Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                mIntent.putExtra("commandType", nowCommandType);//��������ָ��
                if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_DOOR_STATE)) {
                    //��ѯ����״̬
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//��ȡ��

                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_CLOSE_DOOR)) {
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//��ȡ��

                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_OPEN_DOOR)) {
                    mIntent.putExtra("bookNo", BookNoList.get(0).trim());//��ȡ��

                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {
                    showToast("δ����");
                    return;

                } else if (nowCommandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)) {
                    if (BookNoList.size() > 1) {
                        mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//Ⱥ��
                        mIntent.putStringArrayListExtra("bookNos", BookNoList);
                    } else {
                        mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_SINGLE);//����
                        mIntent.putExtra("bookNo", BookNoList.get(0).trim());//��ȡ��
                    }
                }
                startActivity(mIntent);
            }


        }


    }
}
