package com.pl.gassystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.PreferenceBiz;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.common.Cfun;

/**
 * ͸��ģ������
 */
public class CopySetMjcActivity extends Activity {

    private TextView tvTitlebar_name, tvBtInfo, tvCopyingBackMsg;
    private ImageButton btnOnlybackQuit;
    private ImageButton btnBtScan;
    private Button btnMjcSetSubmit;
    private EditText etMjcSetPoint, etMjcSetBaudrate, etMjcSetPower, etMjcSetSpeed;

    // ��������������
    private BluetoothAdapter mBluetoothAdapter = null;
    // ��������
    private BluetoothChatService mChatService = null;
    // �����豸������
    private String mConnectedDeviceName = null;

    // Intent�������
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // ��BluetoothChatService��������͵���Ϣ����
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // ��BluetoothChatService���������յ�����Կ��
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;

    private PreferenceBiz preferenceBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_copy_set_mjc);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);

        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("͸��ģ������");
        setupView();
        addListener();
        addOnTouchListener();

        // ��ȡ��������������
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // �����������null,��ô��֧������
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // ��ʼ��bluetoothchatserviceִ����������
        mChatService = new BluetoothChatService(this, mHandler);

        preferenceBiz = new PreferenceBiz(this);

        // ����Ƿ����Զ���������
        String address = preferenceBiz.getDeviceAddress();
        if (address != null) {
            connectDevice(address, true);
        }
    }

    private void setupView() {
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);
        btnBtScan = (ImageButton) findViewById(R.id.btnCopyScan);
        tvBtInfo = (TextView) findViewById(R.id.tvBtInfo);
        tvCopyingBackMsg = (TextView) findViewById(R.id.tvCopyingBackMsg);
        btnMjcSetSubmit = (Button) findViewById(R.id.btnMjcSetSubmit);
        etMjcSetPoint = (EditText) findViewById(R.id.etMjcSetPoint);
        etMjcSetBaudrate = (EditText) findViewById(R.id.etMjcSetBaudrate);
        etMjcSetPower = (EditText) findViewById(R.id.etMjcSetPower);
        etMjcSetSpeed = (EditText) findViewById(R.id.etMjcSetSpeed);
    }

    private void addListener() {
        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBtScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ����DeviceListActivity�����豸����ɨ��
                Intent intent = new Intent(CopySetMjcActivity.this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
        });

        btnMjcSetSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().equals("��ǰ״̬�������ѷ��� �ȴ�����")) {
                    Toast.makeText(getApplicationContext(), "�����ѷ�����ȴ���رյ�ǰ����", Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("�����豸δ����") || tvBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��", Toast.LENGTH_SHORT).show();
                } else {
                    doSendMsg();
                }
            }
        });

    }

    private void addOnTouchListener() {
        btnBtScan.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnBtScan.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnBtScan.startAnimation(anim_btn_end);
                }
                return false;
            }
        });
    }

    private void connectDevice(String address, boolean secure) {
        // // ����豸��ַ
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // ��������豸����
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // �������ӵ��豸
        mChatService.connect(device);
    }

    // ����������Ϣ
    private void sendMessage(String message) {

        // �������
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // ���ʵ���Ϸ��͵Ķ���
        if (message.length() > 0) {
            // �õ���Ϣ���ֽڣ�����bluetoothchatserviceд
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    private void doSendMsg() {
        String point = Cfun.ToHEXString(Integer.parseInt(etMjcSetPoint.getText().toString() + "000"));
        String baudrate = Cfun.ToHEXString(Integer.parseInt(etMjcSetBaudrate.getText().toString()));
        if (baudrate.length() == 6) {
            baudrate = "00" + baudrate;
        } else if (baudrate.length() == 4) {
            baudrate = "0000" + baudrate;
        }
        String power = Cfun.ToHEXString(Integer.parseInt(etMjcSetPower.getText().toString()));
        String speed = Cfun.ToHEXString(Integer.parseInt(etMjcSetSpeed.getText().toString()));
        if (speed.length() == 6) {
            speed = "00" + speed;
        } else if (speed.length() == 4) {
            speed = "0000" + speed;
        }
        String checkSum = Cfun.accSum(Cfun.x16Str2Byte(point + baudrate + power + speed)).toLowerCase();// �ۼӺ�У�飬1�ֽ�
        String message = "68000000000068" + point + baudrate + power + speed + checkSum + "16";
        sendMessage(message);
        tvCopyingBackMsg.setText("��ǰ״̬�������ѷ��� �ȴ�����");
    }

    // ��BluetoothChatService�����������Ϣ����
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            tvBtInfo.setText(getString(R.string.title_connected_to) + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                            }
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            tvBtInfo.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            tvBtInfo.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    // byte[] writeBuf = (byte[]) msg.obj;
                    // // ����һ���ַ���������
                    // String writeMessage = new String(writeBuf);
                    // Log.i("bluepost", writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // ����һ���ַ�����Ч�ֽڵĻ�����
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    // Log.i("blueget", readMessage);
                    if (readMessage.length() > 0) {
                        tvCopyingBackMsg.setText("�����ѳɹ�");
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // ���������豸������
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // �������û��������������.
        // setupChat��onActivityResult()��������
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // ��������ͨ�ŻỰ
        } else {
            if (mChatService == null)
                // setupChat();
                Toast.makeText(this, "���������Ự", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mChatService.stop();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // ��devicelistactivity�������豸����
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // �����豸��ַ
                    preferenceBiz.saveDeviceAddress(address);
                    connectDevice(address, true);
                }
                break;
            case REQUEST_ENABLE_BT:
                // ������������������
                if (resultCode == Activity.RESULT_OK) {
                    // ���������ã����Խ���һ���Ự
                    // setupChat();
                    Toast.makeText(this, "���������ɹ���", Toast.LENGTH_SHORT).show();
                } else {
                    // �û�δ����������������
                    Toast.makeText(this, "����δ���á������˳�", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
