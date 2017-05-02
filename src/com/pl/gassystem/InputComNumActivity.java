package com.pl.gassystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.utils.GlobalConsts;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;

/**
 * �������Խ���
 */
public class InputComNumActivity extends Activity {

    private EditText etInputNum, etInputParam1, etInputParam2;
    private Button btnInputSubmit, btnScanNum;
    private ImageButton btnquit;
    private TextView tvTitlebar_name, tvInputParam1, tvInputParam2, tvInputParam3;
    private LinearLayout linInputParam;
    // ִ��ģʽ
    private static int operationType;
    private ArrayList<String> meterNos;
    private String meterTypeNo;
    private RadioGroup rgInputMeterType, rgInputParam3;
    private RadioButton rdoParam31, rdoParam32, rdoWX, rdoIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_input_com_num);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("����Ҫ�����ı��ͨѶ��");

        operationType = getIntent().getIntExtra("operationType", 1);

        etInputNum = (EditText) findViewById(R.id.etInputNum);
        btnInputSubmit = (Button) findViewById(R.id.btnInputSubmit);
        btnquit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        rgInputMeterType = (RadioGroup) findViewById(R.id.rgInputMeterType);
        rgInputParam3 = (RadioGroup) findViewById(R.id.rgInputParam3);
        btnScanNum = (Button) findViewById(R.id.btnScanNum);
        linInputParam = (LinearLayout) findViewById(R.id.linInputParam);
        tvInputParam1 = (TextView) findViewById(R.id.tvInputParam1);
        tvInputParam2 = (TextView) findViewById(R.id.tvInputParam2);
        tvInputParam3 = (TextView) findViewById(R.id.tvInputParam3);
        etInputParam1 = (EditText) findViewById(R.id.etInputParam1);
        etInputParam2 = (EditText) findViewById(R.id.etInputParam2);
        rdoParam31 = (RadioButton) findViewById(R.id.rdoParam31);
        rdoParam32 = (RadioButton) findViewById(R.id.rdoParam32);
        rdoWX = (RadioButton) findViewById(R.id.rdoWX);
        rdoIC = (RadioButton) findViewById(R.id.rdoIC);

        if (getIntent().getStringExtra("meterTNo") != null) {
            meterTypeNo = getIntent().getStringExtra("meterTNo").toString();
            if ("04".equals(meterTypeNo)) {
                rgInputMeterType.check(R.id.rdoIC);
            } else if ("05".equals(meterTypeNo)) {
                rgInputMeterType.check(R.id.rdoWX);
            }
        }

        if (getIntent().getStringExtra("meterNo") != null) {
            etInputNum
                    .setText(getIntent().getStringExtra("meterNo").toString());
        }

        if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
            tvInputParam1.setText("�޸�ͨѶ��Ϊ");
            tvInputParam2.setVisibility(View.GONE);
            tvInputParam3.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) {
            tvInputParam1.setText("������ID��");
            tvInputParam2.setText("�ŵ���");
            tvInputParam3.setText("״̬");
            rdoParam31.setText("ָ����������");
            rdoParam32.setText("��ָ��������");
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET
                || operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) {
            tvInputParam1.setText("�м̱�ID��");
            tvInputParam2.setVisibility(View.GONE);
            tvInputParam3.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) {
            tvInputParam1.setText("������");
            tvInputParam2.setText("����ʱ��");
            tvInputParam3.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) {
            tvInputParam1.setText("״̬��");
            tvInputParam2.setVisibility(View.GONE);
            tvInputParam3.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {
            tvInputParam1.setText("��Կ");
            tvInputParam2.setVisibility(View.GONE);
            tvInputParam3.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) {
            tvInputParam3.setText("����");
            rdoParam31.setText("�˳���ȫģʽ");
            rdoParam32.setText("���밲ȫģʽ");
            tvInputParam1.setVisibility(View.GONE);
            tvInputParam2.setVisibility(View.GONE);
            etInputParam1.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) {
            tvInputParam1.setText("��ʼ��");
            tvInputParam2.setText("������");
            tvInputParam3.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) {
            tvInputParam1.setText("�����");
            tvInputParam2.setVisibility(View.GONE);
            tvInputParam3.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            rgInputParam3.setVisibility(View.GONE);
        } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOPOINT) { // ��δ��
            tvInputParam1.setVisibility(View.GONE);
            etInputParam1.setVisibility(View.GONE);
            tvInputParam2.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            tvInputParam3.setText("���λ��:");
            rdoParam31.setText("��λ��");
            rdoParam32.setText("��λ��");
            rgInputParam3.check(R.id.rdoParam32);
            rdoWX.setText("�����");
            rgInputMeterType.check(R.id.rdoWX);
            rdoIC.setVisibility(View.INVISIBLE);
        } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) {
            tvInputParam1.setText("�޸�ͨѶ��Ϊ");
            tvInputParam2.setVisibility(View.GONE);
            etInputParam2.setVisibility(View.GONE);
            tvInputParam3.setText("�޸�Ŀ�꣺");
            rdoParam31.setText("��߶�");
            rdoParam32.setText("������");
            rgInputParam3.check(R.id.rdoParam31);
            rdoWX.setText("�����");
            rgInputMeterType.check(R.id.rdoWX);
            rdoIC.setVisibility(View.INVISIBLE);
        } else {
            linInputParam.setVisibility(View.GONE);
        }

        addListener();
    }

    private void addListener() {
        btnquit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnInputSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                meterNos = new ArrayList<String>();
                String meterNo = etInputNum.getText().toString();
                if (meterNo.length() < 10) {
                    Toast.makeText(getApplicationContext(), "ͨѶ��ű�����ڵ���10λ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    meterNos.add(meterNo);
                    if (rgInputMeterType.getCheckedRadioButtonId() == R.id.rdoIC) {
                        meterTypeNo = "04";
                    } else if (rgInputMeterType.getCheckedRadioButtonId() == R.id.rdoWX) {
                        meterTypeNo = "05";
                    }
                    Intent intent = new Intent(InputComNumActivity.this,
                            CopyingActivity.class);
                    intent.putExtra("meterNos", meterNos);
                    intent.putExtra("meterTypeNo", meterTypeNo);
                    intent.putExtra("copyType", GlobalConsts.COPY_TYPE_SINGLE);
                    intent.putExtra("operationType", operationType);
                    if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {// �޸�ͨѶ��
                        String setParam1 = etInputParam1.getText().toString();
                        if (setParam1.length() < 10) {
                            Toast.makeText(getApplicationContext(),
                                    "�޸ĵ�ͨѶ�ű�����ڵ���10λ!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            intent.putExtra("setParam1", setParam1);
                            startActivity(intent);
                        }
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) {
                        String state = "01"; // ָ����������
                        if (rgInputParam3.getCheckedRadioButtonId() == R.id.rdoParam32) {
                            state = "00"; // ��ָ��
                        }
                        String colNo = etInputParam1.getText().toString();
                        String channelNo = etInputParam2.getText().toString();
                        intent.putExtra("state", state);
                        intent.putExtra("colNo", colNo);
                        intent.putExtra("channelNo", channelNo);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET
                            || operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) {
                        String relayMeterNo = etInputParam1.getText()
                                .toString();// �м̱�ID��
                        if (relayMeterNo.length() != 10) {
                            Toast.makeText(getApplicationContext(),
                                    "�м̱�ID�ű���Ϊ10λ!", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra("relayMeterNo", relayMeterNo);
                            startActivity(intent);
                        }
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) {
                        String cmd = etInputParam1.getText().toString();
                        String date = etInputParam2.getText().toString();
                        intent.putExtra("cmd", cmd);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) {
                        String state = etInputParam1.getText().toString();
                        intent.putExtra("state", state);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {
                        String key = etInputParam1.getText().toString();
                        intent.putExtra("key", key);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) {
                        String cmd = "00";
                        if (rgInputParam3.getCheckedRadioButtonId() == R.id.rdoParam32) {
                            cmd = "01";
                        }
                        intent.putExtra("cmd", cmd);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) {
                        String startDay = etInputParam1.getText().toString();
                        String endDay = etInputParam2.getText().toString();
                        intent.putExtra("startDay", startDay);
                        intent.putExtra("endDay", endDay);
                        startActivity(intent);
                    } else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) {
                        String baseNum = etInputParam1.getText().toString();
                        if (baseNum.length() != 8) {
                            Toast.makeText(getApplicationContext(),
                                    "���������Ϊ8λ!", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra("baseNum", baseNum);
                            startActivity(intent);
                        }
                    } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOPOINT) { // ��δ��
                        String meterTypeNo = "8";
                        if (rgInputParam3.getCheckedRadioButtonId() == R.id.rdoParam31) {
                            meterTypeNo = "7";
                        } else if (rgInputParam3.getCheckedRadioButtonId() == R.id.rdoParam32) {
                            meterTypeNo = "8";
                        }
                        Intent intentPhoto = new Intent(
                                InputComNumActivity.this,
                                CopyPhotoActivity.class);
                        if (meterNo.length() > 10) {
                            meterNo = meterNo.substring(meterNo.length() - 10);
                        }
                        intentPhoto.putExtra("meterNo", meterNo);
                        intentPhoto.putExtra("meterTypeNo", meterTypeNo);
                        intentPhoto.putExtra("baseType", "");
                        intentPhoto.putExtra("YHTM", "");
                        intentPhoto.putExtra("XBDS", "");
                        intentPhoto.putExtra("MQBBH", "");
                        intentPhoto.putExtra("HUNANME", "");
                        intentPhoto.putExtra("OTEL", "");
                        intentPhoto.putExtra("ADDR", "");
                        intentPhoto.putExtra("copyType",
                                GlobalConsts.COPY_TYPE_SINGLE);
                        intentPhoto.putExtra("operationType", operationType);
                        startActivity(intentPhoto);
                    } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) {
                        boolean isCol = false;
                        if (rgInputParam3.getCheckedRadioButtonId() == R.id.rdoParam32) {
                            isCol = true;
                        }
                        Intent intentPhoto = new Intent(
                                InputComNumActivity.this,
                                CopyPhotoActivity.class);
                        if (meterNo.length() > 10) {
                            meterNo = meterNo.substring(meterNo.length() - 10);
                        }
                        intentPhoto.putExtra("meterNo", meterNo);
                        intentPhoto.putExtra("meterTypeNo", "8");
                        intentPhoto.putExtra("baseType", "");
                        intentPhoto.putExtra("YHTM", "");
                        intentPhoto.putExtra("XBDS", "");
                        intentPhoto.putExtra("MQBBH", "");
                        intentPhoto.putExtra("HUNANME", "");
                        intentPhoto.putExtra("OTEL", "");
                        intentPhoto.putExtra("ADDR", "");
                        intentPhoto.putExtra("isCol", isCol);// ����
                        intentPhoto.putExtra("copyType",
                                GlobalConsts.COPY_TYPE_SINGLE);
                        intentPhoto.putExtra("operationType", operationType);
                        startActivity(intentPhoto);
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });

        btnScanNum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(InputComNumActivity.this,
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO �Զ����ɵķ������.
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            etInputNum.setText(scanResult);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
