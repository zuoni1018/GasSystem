package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 批量设置表具参数
 */

public class HtSetBookParameterActivity extends HtBaseTitleActivity {
    @BindView(R.id.etKuoPinXinDao)
    EditText etKuoPinXinDao;
    @BindView(R.id.etKuoPinYinZi)
    EditText etKuoPinYinZi;
    @BindView(R.id.etSheZhiDongJieRi)
    EditText etSheZhiDongJieRi;
    @BindView(R.id.etKaiChuangShiJian)
    EditText etKaiChuangShiJian;
    @BindView(R.id.rbNeed)
    RadioButton rbNeed;
    @BindView(R.id.btSure)
    Button btSure;

    private ArrayList<String > bookNos;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_book_parameter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("批量设置参数");
        bookNos=getIntent().getStringArrayListExtra("bookNos");

    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        Intent mIntent =new Intent(getContext(),HtCopyingActivity.class);
        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_SET_PARAMETER);
        mIntent.putExtra("bookNos", bookNos);
        startActivity(mIntent);
    }
}
