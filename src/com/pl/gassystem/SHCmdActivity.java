package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pl.utils.GlobalConsts;

public class SHCmdActivity extends Activity {

    private TextView tvTitlebar_name;
    private ImageButton btnOnlybackQuit;
    private LinearLayout linSHCMDRegNet, linSHCMDRelayRegNet,
            linSHCMDCancelRelayRegNet, linSHCMDTestMode, linSHCMDGetParams,
            linSHCMDReSet, linSHCMDSetKey, linSHCMDSafeMode;
    private LinearLayout linSHCMDSetCopyDay, linSHCMDGetCopyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_shcmd);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("上海专用网络命令");

        setupView();
        addListener();

    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        linSHCMDRegNet = (LinearLayout) findViewById(R.id.linSHCMDRegNet);
        linSHCMDRelayRegNet = (LinearLayout) findViewById(R.id.linSHCMDRelayRegNet);
        linSHCMDCancelRelayRegNet = (LinearLayout) findViewById(R.id.linSHCMDCancelRelayRegNet);
        linSHCMDTestMode = (LinearLayout) findViewById(R.id.linSHCMDTestMode);
        linSHCMDGetParams = (LinearLayout) findViewById(R.id.linSHCMDGetParams);
        linSHCMDReSet = (LinearLayout) findViewById(R.id.linSHCMDReSet);
        linSHCMDSetKey = (LinearLayout) findViewById(R.id.linSHCMDSetKey);
        linSHCMDSafeMode = (LinearLayout) findViewById(R.id.linSHCMDSafeMode);
        linSHCMDSetCopyDay = (LinearLayout) findViewById(R.id.linSHCMDSetCopyDay);
        linSHCMDGetCopyDay = (LinearLayout) findViewById(R.id.linSHCMDGetCopyDay);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linSHCMDRegNet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 表计入网
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_REGNET);
                startActivity(intent);
            }
        });

        linSHCMDRelayRegNet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 中继入网
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_RELAYREGNET);
                startActivity(intent);
            }
        });

        linSHCMDCancelRelayRegNet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 取消中继入网
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET);
                startActivity(intent);
            }
        });

        linSHCMDTestMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 测试模式设置命令
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_TESTMODE);
                startActivity(intent);
            }
        });

        linSHCMDGetParams.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 运行参数查询
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_GETPAMARS);
                startActivity(intent);
            }
        });

        linSHCMDReSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 恢复出厂设置
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_RESET);
                startActivity(intent);
            }
        });

        linSHCMDSetKey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) { // 设定密钥
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_SETKEY);
                startActivity(intent);
            }
        });

        linSHCMDSafeMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_SAFEMODE);
                startActivity(intent);
            }
        });

        linSHCMDSetCopyDay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 设置抄表时间段
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_SETCOPYDAY);
                startActivity(intent);
            }
        });

        linSHCMDGetCopyDay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 查询抄表时间段
                Intent intent = new Intent(SHCmdActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPYSH_OPERATION_GETCOPYDAY);
                startActivity(intent);
            }
        });
    }

}
