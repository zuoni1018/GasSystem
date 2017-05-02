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
 * 透传模块设置
 */
public class CopySetMjcActivity extends Activity {

    private TextView tvTitlebar_name, tvBtInfo, tvCopyingBackMsg;
    private ImageButton btnOnlybackQuit;
    private ImageButton btnBtScan;
    private Button btnMjcSetSubmit;
    private EditText etMjcSetPoint, etMjcSetBaudrate, etMjcSetPower, etMjcSetSpeed;

    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    // 蓝牙服务
    private BluetoothChatService mChatService = null;
    // 连接设备的名称
    private String mConnectedDeviceName = null;

    // Intent请求代码
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // 从BluetoothChatService处理程序发送的消息类型
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // 从BluetoothChatService处理程序接收到的密钥名
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
        tvTitlebar_name.setText("透传模块设置");
        setupView();
        addListener();
        addOnTouchListener();

        // 获取本地蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 如果适配器是null,那么不支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 初始化bluetoothchatservice执行蓝牙连接
        mChatService = new BluetoothChatService(this, mHandler);

        preferenceBiz = new PreferenceBiz(this);

        // 检测是否能自动连接蓝牙
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
                // 启动DeviceListActivity看到设备和做扫描
                Intent intent = new Intent(CopySetMjcActivity.this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
            }
        });

        btnMjcSetSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().equals("当前状态：命令已发送 等待返回")) {
                    Toast.makeText(getApplicationContext(), "命令已发送请等待或关闭当前界面", Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("蓝牙设备未连接") || tvBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！", Toast.LENGTH_SHORT).show();
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
        // // 获得设备地址
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // 获得蓝牙设备对象
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // 尝试连接到设备
        mChatService.connect(device);
    }

    // 发送蓝牙消息
    private void sendMessage(String message) {

        // 检查连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // 检查实际上发送的东西
        if (message.length() > 0) {
            // 得到消息的字节，告诉bluetoothchatservice写
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
        String checkSum = Cfun.accSum(Cfun.x16Str2Byte(point + baudrate + power + speed)).toLowerCase();// 累加和校验，1字节
        String message = "68000000000068" + point + baudrate + power + speed + checkSum + "16";
        sendMessage(message);
        tvCopyingBackMsg.setText("当前状态：命令已发送 等待返回");
    }

    // 从BluetoothChatService处理程序获得信息返回
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
                    // // 构建一个字符串缓冲区
                    // String writeMessage = new String(writeBuf);
                    // Log.i("bluepost", writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // 构建一个字符串有效字节的缓冲区
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    // Log.i("blueget", readMessage);
                    if (readMessage.length() > 0) {
                        tvCopyingBackMsg.setText("设置已成功");
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 如果蓝牙没开启，则开启蓝牙.
        // setupChat在onActivityResult()将被调用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // 否则，设置通信会话
        } else {
            if (mChatService == null)
                // setupChat();
                Toast.makeText(this, "开启蓝牙会话", Toast.LENGTH_SHORT).show();
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
                // 当devicelistactivity返回与设备连接
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 保存设备地址
                    preferenceBiz.saveDeviceAddress(address);
                    connectDevice(address, true);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 当请求启用蓝牙返回
                if (resultCode == Activity.RESULT_OK) {
                    // 蓝牙已启用，所以建立一个会话
                    // setupChat();
                    Toast.makeText(this, "蓝牙开启成功！", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户未启用蓝牙或发生错误
                    Toast.makeText(this, "蓝牙未启用。程序退出", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
