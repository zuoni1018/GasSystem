package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
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
 * ѡ��ȼ�������
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

    private String commandType = "";//��������

    private boolean isChooseAll=false;

    private CreateBookDialog createBookDialog;
    private List<HtBook> htBookList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_choose_books;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("ȼ����ѡ��");
        commandType = getIntent().getStringExtra("commandType");

        if(commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)){
            btSure.setText("�������ò���");
        }else if(commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)){
            btSure.setText("����������Կ");
        }


        mRecyclerView.setPullRefreshEnabled(false);//��ֹ����ˢ��


        htBookList = new ArrayList<>();
        htBookList.add(getHtBook("05170016", "������Ա�1"));
        htBookList.add(getHtBook("04000015", "������Ա�2"));
        htBookList.add(getHtBook("04160105", "������Ա�3"));
        mAdapter = new LRecyclerViewAdapter(new RvHtBookChooseAdapter(getContext(), htBookList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);


    }

    @OnClick({R.id.ivSelectAll, R.id.btSure, R.id.btAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSelectAll:
                isChooseAll=!isChooseAll;
                for (int i = 0; i < htBookList.size() ; i++) {
                    htBookList.get(i).setChoose(isChooseAll);
                }
                if(!isChooseAll){
                    ivSelectAll.setImageResource(R.mipmap.choose_02);
                }else {
                    ivSelectAll.setImageResource(R.mipmap.choose_01);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btSure:
                Intent mIntent=new Intent();
                //�ѱ���ȥ
                ArrayList<String> bookNoList = new ArrayList<>();
                for (int i = 0; i < htBookList.size(); i++) {
                    if (htBookList.get(i).isChoose()) {
                        bookNoList.add(htBookList.get(i).getBookNum());
                    }
                }
                if (bookNoList.size() == 0) {
                    showToast("����δѡ��ȼ����");
                } else {
                    if(commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)){
                        //���ò���
                        mIntent = new Intent(getContext(), HtSetBookParameterActivity.class);
                        mIntent.putStringArrayListExtra("bookNos", bookNoList);
                    }else  if(commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)){
                        mIntent = new Intent(getContext(), HtSetKeyActivity.class);
                        mIntent.putStringArrayListExtra("bookNos", bookNoList);
                    }
                }
                startActivity(mIntent);


                break;
            case R.id.btAdd:
                CreateBookDialog.Builder builder = new CreateBookDialog.Builder(getContext());
                builder.setCreateBookListener(new OnCreateBookListener() {
                    @Override
                    public void getBookInfo(String bookName, String bookNo) {
                        HtBook htBook = new HtBook();
                        htBook.setBookName(bookName);
                        htBook.setBookNum(bookNo);
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
