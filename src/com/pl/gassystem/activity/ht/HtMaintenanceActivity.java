package com.pl.gassystem.activity.ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtSendMessage;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zangyi_shuai_ge on 2017/9/18
 * 杭天表具维护
 * 在当前界面选择命令类型
 */

public class HtMaintenanceActivity extends HtBaseTitleActivity {
    private String commandType = "";//命令类型

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_maintenance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("表具维护");


    }

    @OnClick({R.id.layoutOpenValve, R.id.layoutCloseValve, R.id.layoutSeeValveState,
            R.id.layoutQueryParameter, R.id.layoutSetParameter, R.id.layoutCopyNormal,
            R.id.layoutCopyFrozen, R.id.layoutChangeBookNoOrCumulant})
    public void onViewClicked(View view) {
        Intent mIntent = new Intent(getContext(), HtChooseBooksActivity.class);
        switch (view.getId()) {
            case R.id.layoutOpenValve:
                commandType = HtSendMessage.COMMAND_TYPE_OPEN_DOOR;
                break;
            case R.id.layoutCloseValve:
                commandType = HtSendMessage.COMMAND_TYPE_CLOSE_DOOR;
                break;
            case R.id.layoutSeeValveState:
                commandType = HtSendMessage.COMMAND_TYPE_DOOR_STATE;
                break;
            case R.id.layoutQueryParameter:
                commandType = HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER;
                break;
            case R.id.layoutSetParameter:
                commandType = HtSendMessage.COMMAND_TYPE_SET_PARAMETER;
                break;
            case R.id.layoutCopyNormal:
                commandType = HtSendMessage.COMMAND_TYPE_COPY_NORMAL;
                break;
            case R.id.layoutCopyFrozen:
                commandType = HtSendMessage.COMMAND_TYPE_COPY_FROZEN;
                break;
            case R.id.layoutChangeBookNoOrCumulant:
                commandType = HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT;
                break;
        }
        startActivity(mIntent);
    }
}
