package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.pl.gassystem.R;
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
    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;
    @BindView(R.id.mRecyclerView)
    LRecyclerView mRecyclerView;
    @BindView(R.id.tvCopyNum)
    TextView tvCopyNum;
    @BindView(R.id.btSure)
    Button btSure;

    private String commandType = "";//命令类型

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_choose_books;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("燃气表选择");
        commandType=getIntent().getStringExtra("commandType");
    }

    @OnClick({R.id.tvSelectAll, R.id.btSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSelectAll:
                break;
            case R.id.btSure:
                break;
        }
    }
}
