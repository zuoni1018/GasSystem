package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/21
 */

public class HtSetKeyActivity extends HtBaseTitleActivity {
    @BindView(R.id.etKey01)
    EditText etKey01;
    @BindView(R.id.etKey02)
    EditText etKey02;
    @BindView(R.id.btSure)
    Button btSure;
    @BindView(R.id.tvNum)
    TextView tvNum;
    private ArrayList<String> bookNos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("����������Կ");
        bookNos = getIntent().getStringArrayListExtra("bookNos");
        if (bookNos != null) {
            tvNum.setText("��ѡ����" + bookNos.size() + "��ȼ����");
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_set_key;
    }

    @OnClick(R.id.btSure)
    public void onViewClicked() {
        String key01 = etKey01.getText().toString().trim();
        String key02 = etKey02.getText().toString().trim();

        if (key01.length() == 2) {
            if (key02.length() == 16) {
                Intent mIntent = new Intent(getContext(), HtCopyingActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_SET_KEY);//��������
                mIntent.putStringArrayListExtra("bookNos", bookNos);//������
                mIntent.putExtra("newKey", key01 + key02);
                startActivity(mIntent);
            } else {
                showToast("��������ȷ����Կ");
            }
        } else {
            showToast("��������ȷ����Կ�汾��");
        }


    }
}
