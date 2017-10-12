package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtGroupInfoBean;
import com.pl.gassystem.bean.ht.HtSendMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/10/10
 */

public class HtGroupActivity extends HtBaseTitleActivity {
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;


    private HtGroupInfoBean mHtGroupInfoBean;
    private Intent mIntent;

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("分组信息");
        mHtGroupInfoBean = (HtGroupInfoBean) getIntent().getSerializableExtra("HtGroupInfoBean");
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.bt6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_DOOR_STATE);
                startActivity(mIntent);
                break;
            case R.id.bt2:
                mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_CLOSE_DOOR);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);
                startActivity(mIntent);
                break;
            case R.id.bt3:
                mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_OPEN_DOOR);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);
                startActivity(mIntent);
                break;
            case R.id.bt4:
                mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_FROZEN);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);
                startActivity(mIntent);
                break;
            case R.id.bt5:
                mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_COPY_NORMAL);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);
                startActivity(mIntent);
                break;
            case R.id.bt6:
                Intent mIntent = new Intent(getContext(), HtGetGroupBindActivity.class);
                mIntent.putExtra("commandType", HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER);
                mIntent.putExtra("HtGroupInfoBean", mHtGroupInfoBean);

                startActivity(mIntent);
                break;
        }
    }
}
