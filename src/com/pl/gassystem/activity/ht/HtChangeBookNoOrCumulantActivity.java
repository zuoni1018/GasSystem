package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.bean.ht.HtSendMessageChange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 设置表号或者累计量
 */

public class HtChangeBookNoOrCumulantActivity extends HtBaseTitleActivity {

    @BindView(R.id.etNowBookNo)
    EditText etNowBookNo;
    @BindView(R.id.etNewBookNo)
    EditText etNewBookNo;
    @BindView(R.id.etCumulant)
    EditText etCumulant;
    @BindView(R.id.btSure)
    Button btSure;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;


    private String changeType = "";//修改类型

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_book_no;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String bookNo = getIntent().getStringExtra("bookNo");
        etNowBookNo.setText(bookNo);
        etNewBookNo.setText(bookNo);
        setTitle("设置表号、累计量");
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbChangeCumulant:
                        changeType = HtSendMessageChange.CHANGE_TYPE_CUMULANT;
                        break;
                    case R.id.rbChangeBookNo:
                        changeType = HtSendMessageChange.CHANGE_TYPE_BOOK_NO;
                        break;
                    case R.id.rbChangeAll:
                        changeType = HtSendMessageChange.CHANGE_TYPE_ALL;
                        break;
                }
            }
        });

    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String nowBookNo = etNowBookNo.getText().toString().trim();
        String newBookNo = etNewBookNo.getText().toString().trim();
        String cumulant = etCumulant.getText().toString().trim();

        if (changeType.equals("")) {
            showToast("请选择修改类型");
        } else {

            if (changeType.equals(HtSendMessageChange.CHANGE_TYPE_ALL)) {
                if (nowBookNo.length() == 8) {
                    if (newBookNo.length() == 8) {
                        if (cumulant.length() == 8) {
                            Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                            mIntent.putExtra("newBookNo", newBookNo);
                            mIntent.putExtra("nowBookNo", nowBookNo);
                            mIntent.putExtra("cumulant", cumulant);
                            mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT);
                            mIntent.putExtra("changeType", changeType);
                            startActivity(mIntent);
                        } else {
                            showToast("累计量必须为8位");
                        }
                    } else {
                        showToast("杭天表号必须为8位");
                    }
                } else {
                    showToast("杭天表号必须为8位");
                }
            } else if (changeType.equals(HtSendMessageChange.CHANGE_TYPE_BOOK_NO)) {
                if (nowBookNo.length() == 8) {
                    if (newBookNo.length() == 8) {
                        Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                        mIntent.putExtra("newBookNo", newBookNo);
                        mIntent.putExtra("nowBookNo", nowBookNo);
                        mIntent.putExtra("cumulant", cumulant);
                        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT);
                        mIntent.putExtra("changeType", changeType);
                        startActivity(mIntent);

                    } else {
                        showToast("杭天表号必须为8位");
                    }
                } else {
                    showToast("杭天表号必须为8位");
                }
            } else {
                if (nowBookNo.length() == 8) {
                    if (cumulant.length() == 8) {
                        Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                        mIntent.putExtra("newBookNo", newBookNo);
                        mIntent.putExtra("nowBookNo", nowBookNo);
                        mIntent.putExtra("cumulant", cumulant);
                        mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT);
                        mIntent.putExtra("changeType", changeType);
                        startActivity(mIntent);
                    } else {
                        showToast("累计量必须为8位");
                    }
                } else {
                    showToast("杭天表号必须为8位");
                }
            }


        }


    }
}
