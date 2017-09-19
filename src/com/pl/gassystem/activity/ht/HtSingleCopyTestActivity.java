package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/19
 * 单抄测试
 */

public class HtSingleCopyTestActivity extends HtBaseTitleActivity {
    @BindView(R.id.etInputNo)
    EditText etInputNo;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.rbCopyNormal)
    RadioButton rbCopyNormal;
    private String commandType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("单抄测试");
        //默认为普通抄表
        rbCopyNormal.setChecked(true);
        commandType = HtSendMessage.COMMAND_TYPE_COPY_NORMAL;
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbCopyNormal:
                        commandType = HtSendMessage.COMMAND_TYPE_COPY_NORMAL;
                        break;
                    case R.id.rbCopyFrozen:
                        commandType = HtSendMessage.COMMAND_TYPE_COPY_FROZEN;
                        break;
                }
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_single_copy_test;
    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String bookNo = etInputNo.getText().toString().trim();
        if (bookNo.length() != 8) {
            showToast("杭天表号为8位,请检查");
        } else {
            ArrayList<String > bookNoList=new ArrayList<>();
            bookNoList.add(bookNo);
            Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
            mIntent.putExtra("commandType", commandType);//输入命令指令
            mIntent.putExtra("copyType", HtSendMessage.COPY_TYPE_GROUP);//群抄
            mIntent.putStringArrayListExtra("bookNos", bookNoList);
            startActivity(mIntent);
        }
    }
}
