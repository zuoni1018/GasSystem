package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/19
 * 阀门维护
 */

public class HtValveMaintainActivity extends HtBaseTitleActivity {
    @BindView(R.id.etInputNo)
    EditText etInputNo;
    @BindView(R.id.rbValveState)
    RadioButton rbValveState;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    private String commandType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("阀门维护");
        commandType= HtSendMessage.COMMAND_TYPE_DOOR_STATE;
        rbValveState.setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbValveOpen:
                        commandType = HtSendMessage.COMMAND_TYPE_OPEN_DOOR;
                        break;
                    case R.id.rbValveClose:
                        commandType = HtSendMessage.COMMAND_TYPE_CLOSE_DOOR;
                        break;
                    case R.id.rbValveState:
                        commandType = HtSendMessage.COMMAND_TYPE_DOOR_STATE;
                        break;
                }
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_valve_maintain;
    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String bookNo = etInputNo.getText().toString().trim();
        if (bookNo.length() != 8) {
            showToast("杭天表号为8位,请检查");
        } else {
            Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
            mIntent.putExtra("commandType", commandType);
            mIntent.putExtra("bookNo", bookNo);
            startActivity(mIntent);
        }
    }
}
