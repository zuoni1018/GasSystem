package com.pl.gassystem.activity.ht;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pl.gassystem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/14
 * ���쳭����Խ���
 */

public class HtFunctionTestActivity extends HtBaseTitleActivity {
    @BindView(R.id.btQueryState)
    Button btQueryState;
    @BindView(R.id.btSwitch)
    Button btSwitch;
    @BindView(R.id.btCopy)
    Button btCopy;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_function_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("���쳭�����");
    }

    @OnClick({R.id.btQueryState, R.id.btSwitch, R.id.btCopy})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btQueryState:
                //��ȡ����״̬
                go2Activity(HtBookListActivity.class);
                break;
            case R.id.btSwitch:
                //���ط���
                break;
            case R.id.btCopy:
                //����
                break;
        }
    }
}
