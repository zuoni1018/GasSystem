package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.command.HtCommand;
import com.pl.gassystem.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/21
 */

public class HtSetNewCopyKeyActivity extends HtBaseTitleActivity {
    @BindView(R.id.tvNowKey)
    TextView tvNowKey;
    @BindView(R.id.etNewCopyKey)
    EditText etNewCopyKey;
    @BindView(R.id.btSure)
    Button btSure;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_copy_key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("设置抄表密钥");
        String nowCopyKey = (String) SPUtils.get(getContext(), "HtCopyKey", "0102030405060708");
        tvNowKey.setText(nowCopyKey);
    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String newCopyKey = etNewCopyKey.getText().toString().trim();
        if (newCopyKey.length() == 16) {
            SPUtils.put(getContext(), "HtCopyKey", newCopyKey);
            showToast("保存成功");

            //杭天密钥
            String htCopyKey= (String) SPUtils.get(getContext(),"HtCopyKey","0102030405060708");
            HtCommand.HT_PASSWORD=htCopyKey+"0000000000000000";
            finishActivity();
        } else {
            showToast("请输入正确的密钥");
        }
    }
}
