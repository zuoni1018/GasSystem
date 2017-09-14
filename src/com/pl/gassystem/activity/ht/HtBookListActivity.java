package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.pl.gassystem.R;
import com.pl.gassystem.adapter.ht.RvHtBookChooseAdapter;
import com.pl.gassystem.bean.ht.HtBook;

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

    private List<HtBook> mList;
    private LRecyclerViewAdapter mAdapter;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_book_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("杭天表列表");
        mList = new ArrayList<>();
        mList.add(getHtBook("04000015"));
        mList.add(getHtBook("05170016"));
        mList.add(getHtBook("04160105"));
        mAdapter = new LRecyclerViewAdapter(new RvHtBookChooseAdapter(getContext(), mList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    private HtBook getHtBook(String bookNum) {
        HtBook htBook = new HtBook();
        htBook.setChoose(false);
        htBook.setBookNum(bookNum);

        return htBook;
    }

    @OnClick(R.id.btGoCopy)
    public void onViewClicked() {
        ArrayList<String> BookNoList = new ArrayList<>();
        for (int i = 0; i <mList.size() ; i++) {
            if(mList.get(i).isChoose()){
                BookNoList.add(mList.get(i).getBookNum());
            }
        }
        if(BookNoList.size()==0){
            showToast("您还未选择燃气表");
        }else {
            Intent mIntent=new Intent(getContext(),HtCopyingActivity.class);
            mIntent.putStringArrayListExtra("books",BookNoList);
            startActivity(mIntent);
        }
    }
}
