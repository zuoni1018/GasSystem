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
import android.widget.Toast;

import com.pl.bll.SetBiz;
import com.pl.utils.GlobalConsts;

/**
 * ���ά������
 */
public class MaintenanceActivity extends Activity {

    private TextView tvTitlebar_name;
    private ImageButton btnOnlybackQuit;
    private LinearLayout linMaintenOpenValve, linMaintenComnumer,
            linMaintenCloseValve;
    private LinearLayout linMaintenSHCMD, linMaintenSetBaseNum,
            linMaintenCopyFrozen, linMaintenSetPhotoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_maintenance);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("���ά��");

        setupView();
        addListener();
    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        linMaintenOpenValve = (LinearLayout) findViewById(R.id.linMaintenOpenValve);
        linMaintenComnumer = (LinearLayout) findViewById(R.id.linMaintenComnumer);
        linMaintenCloseValve = (LinearLayout) findViewById(R.id.linMaintenCloseValve);
        linMaintenSHCMD = (LinearLayout) findViewById(R.id.linMaintenSHCMD);
        linMaintenSetBaseNum = (LinearLayout) findViewById(R.id.linMaintenSetBaseNum);
        linMaintenCopyFrozen = (LinearLayout) findViewById(R.id.linMaintenCopyFrozen);
        linMaintenSetPhotoPoint = (LinearLayout) findViewById(R.id.linMaintenSetPhotoPoint);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linMaintenOpenValve.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// ����
                Intent intent = new Intent(MaintenanceActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_OPENVALVE);
                startActivity(intent);
            }
        });

        linMaintenCloseValve.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// �ط�
                Intent intent = new Intent(MaintenanceActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_CLOSEVALVE);
                startActivity(intent);
            }
        });

        linMaintenComnumer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// �޸�ͨѶ��
                Intent intent = new Intent(MaintenanceActivity.this, SelectMeterActivity.class);
                intent.putExtra("operationType", GlobalConsts.COPY_OPERATION_COMNUMBER);
                startActivity(intent);
            }
        });

        linMaintenSHCMD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SetBiz setBiz = new SetBiz(MaintenanceActivity.this); // �Ϻ�ר������
                if (setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_SHANGHAI)) {
                    Intent intent = new Intent(MaintenanceActivity.this, SHCmdActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "���Ƚ���������ģʽ���ó��Ϻ�ģʽ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linMaintenSetBaseNum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SetBiz setBiz = new SetBiz(MaintenanceActivity.this); // ����ר������
                if (setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) {
                    Intent intent = new Intent(MaintenanceActivity.this,
                            SelectMeterActivity.class);
                    intent.putExtra("operationType",
                            GlobalConsts.COPY_OPERATION_SETBASENUM);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "������ֻ֧�ֻ���ģʽ���������ó���ģʽ��", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linMaintenCopyFrozen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SetBiz setBiz = new SetBiz(MaintenanceActivity.this); // ����ר������
                if (setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) {
                    Intent intent = new Intent(MaintenanceActivity.this,
                            SelectMeterActivity.class);
                    intent.putExtra("operationType",
                            GlobalConsts.COPY_OPERATION_COPYFROZEN);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "������ֻ֧�ֻ���ģʽ���������ó���ģʽ��", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linMaintenSetPhotoPoint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // �����Ƶ���޸�
                // SetBiz setBiz = new SetBiz(MaintenanceActivity.this);
                // if(setBiz.getRunMode().equals(GlobalConsts.RUNMODE_PHOTO)){
                // Intent intent = new Intent(MaintenanceActivity.this,
                // SelectMeterActivity.class);
                // intent.putExtra("operationType",
                // GlobalConsts.COPY_OPERATION_PHOTOPOINT);
                // startActivity(intent);
                // }else {
                // Toast.makeText(getApplicationContext(), "���Ƚ���������ģʽ���ó������ģʽ",
                // Toast.LENGTH_SHORT).show();
                // }
                Intent intent = new Intent(MaintenanceActivity.this,
                        InputComNumActivity.class);
                intent.putExtra("operationType",
                        GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER);
                startActivity(intent);
            }
        });

    }

}