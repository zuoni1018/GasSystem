package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/19
 */

public class HtQueryParameterActivity extends HtBaseTitleActivity {
    @BindView(R.id.etInputNo)
    EditText etInputNo;
    @BindView(R.id.btSure)
    Button btSure;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_query_parameter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("参数查询");
    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String bookNo = etInputNo.getText().toString().trim();
        if (bookNo.length() != 8) {
            showToast("杭天表号为8位,请检查");
        } else {
            Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
            mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER);
            mIntent.putExtra("bookNo", bookNo);
            startActivity(mIntent);
        }
    }
}
