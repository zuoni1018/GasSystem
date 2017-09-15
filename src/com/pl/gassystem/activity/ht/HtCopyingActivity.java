package com.pl.gassystem.activity.ht;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.LogUtil;
import com.common.utils.SPUtils;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtGetMessage;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.command.HtCommand;
import com.pl.gassystem.utils.CalendarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 杭天燃气抄表界面
 */

public class HtCopyingActivity extends HtBaseTitleActivity {


    @BindView(R.id.tvCopyingBackMsg)
    TextView tvCopyingBackMsg;
    @BindView(R.id.tvLoadingComNum)
    TextView tvLoadingComNum;
    @BindView(R.id.tvLoadingName)
    TextView tvLoadingName;
    @BindView(R.id.tvLoadingCount)
    TextView tvLoadingCount;
    @BindView(R.id.tvLoadingAll)
    TextView tvLoadingAll;
    @BindView(R.id.pgbCopying)
    ProgressBar pgbCopying;
    @BindView(R.id.btnCopyScan)
    ImageButton btnCopyScan;
    @BindView(R.id.btnCopyingRead)
    ImageButton btnCopyingRead;
    @BindView(R.id.btnCopyingStop)
    ImageButton btnCopyingStop;
    @BindView(R.id.tvDeviceState)
    TextView tvDeviceState;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    //蓝牙
    private BluetoothAdapter mBluetoothAdapter;// 本地蓝牙适配器
    private BluetoothChatService mChatService; // 蓝牙服务
    private String mConnectedDeviceName;// 连接设备的名称
    //蓝牙 Intent请求代码
    private static final int REQUEST_CONNECT_DEVICE = 1;//蓝牙设备返回码
    private static final int REQUEST_ENABLE_BT = 2;//打开蓝牙
    //当前蓝牙设备连接状态
    private static final int DEVICE_STATE_NONE = 0;//设备未连接
    private static final int DEVICE_STATE_CONNECTING = 1;//设备正在连接
    private static final int DEVICE_STATE_CONNECTED = 2;//设备已经连接
    private int nowConnectState = 0;//当前设备连接状态


    // 从BluetoothChatService处理程序发送的消息类型
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // 从BluetoothChatService处理程序接收到的密钥名
    public static final String DEVICE_NAME = "device_name";


    private String commandType;//命令类型
    private String bookNo;//单抄 表号

    private String copyType = "";//抄表方式为群抄还是单抄
    private ArrayList<String> bookNos;//一大堆表号

    private int num = 0;//当前返回的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setTitle("蓝牙抄表");
        addOnTouchListener();


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// 获取本地蓝牙适配器

        if (mBluetoothAdapter == null) {
            showToast("当前设备不支持蓝牙");
            finish();//退出当前界面并不执行下面代码
            return;
        }
        //获取Intent传过来的信息
        commandType = getIntent().getStringExtra("commandType");
        bookNos = new ArrayList<>();

        //一下三种命令下 只接收燃气表号 和 抄表命令类型 就可以了
        if (commandType.equals(HtSendMessage.COMMAND_TYPE_DOOR_STATE)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_OPEN_DOOR)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_CLOSE_DOOR)) {
            bookNo = getIntent().getStringExtra("bookNo");
            tvLoadingAll.setText("1");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)
                |commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {

            copyType = getIntent().getStringExtra("copyType");
            if (copyType.equals(HtSendMessage.COPY_TYPE_SINGLE)) {
                //单抄
                bookNo = getIntent().getStringExtra("bookNo");
                tvLoadingAll.setText("1");
            } else {
                //群抄
                bookNos = getIntent().getStringArrayListExtra("bookNos");
                tvLoadingAll.setText(bookNos.size() + "");
            }


        }


        mChatService = new BluetoothChatService(this, mHandler);//蓝牙可用则开启一个蓝牙服务


        // 检测是否能自动连接蓝牙
        String address = (String) SPUtils.get(getContext(), "htDevice", "");
        assert address != null;
        if (!address.trim().equals("")) {
            connectDevice(address, true);
        }

    }

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
    protected int setLayout() {
        return R.layout.ht_activity_copying;
    }


    /**
     * 拼接一条蓝牙命令
     */
    private void query() {
        // 判断执行类型
        String message = "";
        sendMessage(message); // 发送蓝牙消息
    }

    // 发送蓝牙消息
    private void sendMessage(String message) {
        // 检查连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // 检查实际上发送的东西
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }


    @OnClick({R.id.btnCopyScan, R.id.btnCopyingRead, R.id.btnCopyingStop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCopyScan:
                //搜索设备
                if (nowConnectState == DEVICE_STATE_CONNECTED) {
                    showToast("蓝牙设备已连接");
                } else {
                    Intent intent = new Intent(HtCopyingActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
                break;
            case R.id.btnCopyingRead:

                if (nowConnectState == DEVICE_STATE_CONNECTED) {

                    num = 0;
                    tvLoadingCount.setText(num + "");

                    //向蓝牙发送一条命令
                    createCommand();
                } else {
                    showToast("请先连接蓝牙");
                }

                break;
            case R.id.btnCopyingStop:

                break;
        }
    }

    private void createCommand() {
        HtSendMessage htSendMessage = new HtSendMessage();

        htSendMessage.setCommandType(commandType);//设置命令类型


        if (copyType.equals(HtSendMessage.COPY_TYPE_GROUP)) {
            //群抄
            htSendMessage.setBookNo("FFFFFFFF");//设置表号

            htSendMessage.setBookNos(bookNos);//设置一大堆表号

            htSendMessage.setSetTime(CalendarUtils.getHtTime()); // 17 09 15 14 10 00

            htSendMessage.setWakeUpTime(6000);

            htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_GROUP);

            htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);


        } else {
            // 单抄 查看阀门状态 开关阀门 都只用执行以下代码即可

            htSendMessage.setBookNo(bookNo);//设置表号

            htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);

            htSendMessage.setWakeUpTime(6000);

            htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_SINGLE);
        }
        String message = HtCommand.encodeHangTian(htSendMessage);
        sendMessage(message);
    }



    // 从BluetoothChatService处理程序获得信息返回
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                //蓝牙连接状态(改变的时候会收到这边的指令)
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        //蓝牙连接上的时候
                        case BluetoothChatService.STATE_CONNECTED:
                            LogUtil.i("蓝牙设备", "蓝牙链接上了");
                            setBluetoothState(DEVICE_STATE_CONNECTED);
                            createCommand();
                            break;

                        //正在连接
                        case BluetoothChatService.STATE_CONNECTING:
                            LogUtil.i("蓝牙设备", "蓝牙正在连接...");
                            setBluetoothState(DEVICE_STATE_CONNECTING);
                            break;

                        //断开连接 连接失败
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            LogUtil.i("蓝牙设备", "蓝牙未连接...");
                            setBluetoothState(DEVICE_STATE_NONE);
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    //收到蓝牙结果
                    byte[] readBuf = (byte[]) msg.obj;
                    // 构建一个字符串有效字节的缓冲区
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    LogUtil.i("蓝牙得到结果", readMessage);
                    readMessage(readMessage);

                    break;

                case MESSAGE_DEVICE_NAME:
                    // 保存连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    tvDeviceState.setText("已连接" + mConnectedDeviceName);

                    break;


            }
        }
    };

    private void readMessage(String readMessage) {
        if (readMessage.length() > 25) {
            HtGetMessage htGetMessage = HtCommand.readMessage(readMessage);
            if (htGetMessage != null) {
                LogUtil.i("抄表结果", htGetMessage.getResult());
                num++;
                tvLoadingCount.setText(num + "");
                tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessage.getResult());
            }
        }

    }

    /**
     * 根据蓝牙链接状态设置对应的UI
     */
    private void setBluetoothState(int deviceState) {
        nowConnectState = deviceState;
        switch (deviceState) {
            case DEVICE_STATE_NONE:
                tvDeviceState.setText("蓝牙设备未连接");
                break;
            case DEVICE_STATE_CONNECTING:
                tvDeviceState.setText("蓝牙设备正在连接...");
                break;
            case DEVICE_STATE_CONNECTED:
                tvDeviceState.setText("已连接  :" + mConnectedDeviceName);
                break;
        }
    }

    /**
     * 连接设备
     */
    private void connectDevice(String address, boolean secure) {
        SPUtils.put(getContext(), "htDevice", address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device);
    }

    /**
     * 设备扫描返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                //获取一个蓝牙设备
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    connectDevice(address, true);//连接蓝牙设备

                }
                break;
            case REQUEST_ENABLE_BT:
                // 当请求启用蓝牙返回
                if (resultCode == Activity.RESULT_OK) {
                    showToast("蓝牙开启成功！");
                } else {
                    showToast("蓝牙未启用。程序退出");
                    finish();
                }
        }
    }

    /**
     * 退出界面
     */
    private AlertDialog finishDialog;

    @Override
    protected void finishActivity() {
        if (finishDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("是否退出蓝牙操作界面?");
            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishDialog.dismiss();
                }
            });
            finishDialog = builder.create();
        }
        finishDialog.show();
    }

    /**
     * 设置触摸事件
     */
    private void addOnTouchListener() {
        setViewAnimation(btnCopyScan);
        setViewAnimation(btnCopyingRead);
        setViewAnimation(btnCopyingStop);
    }


}
