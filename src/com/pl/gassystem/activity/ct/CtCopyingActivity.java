package com.pl.gassystem.activity.ct;

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

import com.pl.bll.CopyBiz;
import com.pl.bll.PreferenceBiz;
import com.pl.bll.SetBiz;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.gassystem.bean.ct.CtCopyData;
import com.pl.gassystem.bean.ct.CtCopyDataICRF;
import com.pl.gassystem.dao.CtCopyDataDao;
import com.pl.gassystem.dao.CtCopyDataICRFDao;
import com.pl.gassystem.CopyResultActivity;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.R;
import com.pl.protocol.CqueueData;
import com.pl.protocol.HhProtocol;
import com.pl.utils.GlobalConsts;
import com.pl.gassystem.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * 集中器 去抄表界面
 * 抄表结束后需要跳转到抄表结果界面
 */

public class CtCopyingActivity extends CtBaseTitleActivity {


    @BindView(R.id.btnCopyScan)
    ImageButton btnCopyScan;
    @BindView(R.id.tvBtInfo)
    TextView tvBtInfo;
    @BindView(R.id.btnCopyingRead)
    ImageButton btnCopyingRead;
    @BindView(R.id.btnCopyingStop)
    ImageButton btnCopyingStop;
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
    private AlertDialog alertDialog;


    // private Handler handler;
    private CopyBiz copyBiz;
    private SetBiz setBiz;
    private String meterTypeNo = "04";
    private ArrayList<String> meterNos;
    private ArrayList<String> unCopyMeterNos;
    private ArrayList<String> AllMeterNo;

    // private boolean isCopyUnRead;

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
    // 指令列表
    public static final String CMD_LIST = "wireless_cmd.xml";
    // 抄表总数
    private static int countAll = 0;
    // 当前第几台
    private static int loadingcount = 0;
    // 抄到标志
    private static boolean isCopy;
    // 抄表次数
    private static int copyTimes = 0;
    // 超时计时通知
    public static final int MESSAGE_COPYCANT = 6;
    private PreferenceBiz preferenceBiz;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat copydf = new SimpleDateFormat("yyMMddHHmm");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat copydf6 = new SimpleDateFormat("yyMMddHHmmss");
    // 抄表模式
    private static int copyType;
    // 执行模式
    private static int operationType;
    private static String cmdtype;
    // 中断标识
    private static boolean isStop;
    // 运行模式
    private static String runMode = "1";
    // 抄表参数
    private static int intervalTime; // 间隔时间
    private static String wakeupTime; // 唤醒时间
    private static int copyWait; // 抄表等待
    private static int wakeupWait; // 唤醒等待
    private static int repeatCount;// 补抄次数
    private static int loopCount = 0; //循环次数
    private static boolean isWakeUp = false; //重复唤醒标识



    private CtCopyDataDao ctCopyDataDao;
    private CtCopyDataICRFDao ctCopyDataICRFDao;

    private  String collectorNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("蓝牙抄表");
        addOnTouchListener();
        addListener();
        ctCopyDataDao=new CtCopyDataDao(getContext());
        ctCopyDataICRFDao=new CtCopyDataICRFDao(getContext());
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
        copyBiz = new CopyBiz(this);
        preferenceBiz = new PreferenceBiz(this);
        setBiz = new SetBiz(this);
        // 初始化参数
        runMode = setBiz.getRunMode();
        intervalTime = setBiz.getIntervalTime();
        wakeupTime = setBiz.getWakeupTime();
        copyWait = setBiz.getCopyWait();
        repeatCount = setBiz.getRepeatCount();
        wakeupWait = (Integer.parseInt(wakeupTime, 16) * 100 + copyWait) / 100;
        copyWait = copyWait / 10;
        // 初始化抄表数据
        meterTypeNo = getIntent().getStringExtra("meterTypeNo"); // 表具类型
        // if(meterTypeNo.equals("04")){ //IC卡无线未完成前先直接退出界面
        // finish();
        // }
        meterNos = getIntent().getStringArrayListExtra("meterNos"); // 表号组（单抄也传组）
        collectorNo=getIntent().getStringExtra("collectorNo");
        AllMeterNo = meterNos;
        unCopyMeterNos = new ArrayList<String>();
        copyType = getIntent().getIntExtra("copyType", 1); // 抄表方式（1单抄，2群抄）
        operationType = getIntent().getIntExtra("operationType", 1); // 操作类型
        switch (operationType) {
            case GlobalConsts.COPY_OPERATION_COPY:
                if (meterTypeNo.equals("04")) {
                    cmdtype = "06";
                } else {
                    cmdtype = "01";
                }
                break;
            case GlobalConsts.COPY_OPERATION_OPENVALVE:
                cmdtype = "02";
                break;
            case GlobalConsts.COPY_OPERATION_CLOSEVALVE:
                cmdtype = "03";
                break;
            case GlobalConsts.COPY_OPERATION_COMNUMBER:
                cmdtype = "05";
                break;
        }
        isStop = false;
        countAll = meterNos.size();
        loadingcount = 0;
        copyTimes = 0;
        tvLoadingAll.setText(countAll + "");
        pgbCopying.setMax(countAll);
        loopCount = 0;
        // 检测是否能自动连接蓝牙
        String address = preferenceBiz.getDeviceAddress();
        if (address != null) {
            connectDevice(address, true);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 如果蓝牙没开启，则开启蓝牙.
        // setupChat在onActivityResult()将被调用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
        return R.layout.ct_activity_copying;
    }

    @Override
    protected void finishActivity() {
        if (alertDialog == null) {
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
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();
        }
        alertDialog.show();
//        super.finishActivity();
    }


    //////////////////////////////////////  以下是从原来代码里拷贝过来的   ///////////////////////////


    /**
     * 设置触摸事件
     */
    private void addOnTouchListener() {
        setViewAnimation(btnCopyScan);
        setViewAnimation(btnCopyingRead);
        setViewAnimation(btnCopyingStop);
    }


    /**
     * 前去抄表
     */
    private void query() {
        // 判断执行类型


        //设置显示的文字
        if (copyTimes == 0) {
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                if (loopCount != 0) {
                    tvCopyingBackMsg.setText("开始第" + loopCount + "次补抄");
                } else {
                    tvCopyingBackMsg.setText("开始抄表中...");
                }
            } else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
                tvCopyingBackMsg.setText("开阀中");
            } else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
                tvCopyingBackMsg.setText("关阀中");
            } else if (operationType == GlobalConsts.COPY_OPERATION_BUY) {
                tvCopyingBackMsg.setText("远程充值中");
            } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
                tvCopyingBackMsg.setText("修改通讯号中");
            } else if (operationType == GlobalConsts.COPY_OPERATION_PRICE) {
                tvCopyingBackMsg.setText("调价中");
            } else if (operationType == GlobalConsts.COPY_OPERATION_PWD) {
                tvCopyingBackMsg.setText("修改密码中");
            }
        }


        //拼接蓝牙命令
        if (loadingcount < countAll) {
            String headMessage;
            String commNumber = meterNos.get(loadingcount);
            String meterName = copyBiz.getMeterNameByNo(commNumber);
            headMessage = "6808"; // 起始符68 代号08
            // 判断各类工作模式
            if (meterTypeNo.equals("05")) { // 纯无线表
                // 判断长度
                if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州FSK
                    headMessage += "14";
                } else {
                    headMessage += "19";
                }
                if (runMode.equals(GlobalConsts.RUN_MODE_STANDARD)) { // 混合模式
                    String meterWIType = copyBiz.getMeterWITypeByNo(commNumber);
                    if (meterWIType == null || meterWIType.equals("") || meterWIType.equals("2")) { // LORA以及默认
                        headMessage += "02";
                    } else if (meterWIType.equals("1")) { // FSK
                        headMessage += "01";
                    } else if (meterWIType.equals("3")) { // 惠州FSK
                        headMessage += "03";
                    } else if (meterWIType.equals("4")) { // 上海FSK
                        headMessage += "04";
                    }
                } else if (runMode.equals(GlobalConsts.RUN_MODE_LORA) || runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) { // LORA模式、港泰模式
                    headMessage += "02";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_FSK)) { // FSK
                    headMessage += "01";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州FSK
                    headMessage += "03";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // 上海FSK
                    // headMessage += "04";
                }
            } else if (meterTypeNo.equals("04")) { // IC卡无线
                if (runMode.equals(GlobalConsts.RUN_MODE_FSK)) { // FSK
                    headMessage += "1731";
                } else {
                    headMessage += "1732"; // 除了选定FSK模式外，其余不管选什么都按LORA发，上海惠州没有IC卡无线表
                }
            }

            // 唤醒模式
            if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// 批抄
                if (loadingcount == 0 || isWakeUp == true) {
                    if (countAll == 1) {
                        headMessage += "01" + wakeupTime;
                    } else {
                        headMessage += "02" + wakeupTime;
                    }
                } else {
                    headMessage += "00" + wakeupTime;
                }
            } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// 单抄
                headMessage += "01" + wakeupTime;
            }

            // 数据区域开始
            tvLoadingComNum.setText(commNumber);
            tvLoadingName.setText(meterName);
            String message = "";
            HhProtocol cchmp = new HhProtocol();

            if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // 上海模式专用
                headMessage = ""; // 不发唤醒
                ArrayList<String> params = new ArrayList<>();
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) { // 抄表
                    params.add(copydf.format(new Date()));
                } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) { // 修改通讯号
                    params.add(getIntent().getStringExtra("setParam1"));
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) { // 入网命令
                    String state = getIntent().getStringExtra("state"); // 状态字
                    String colNo = getIntent().getStringExtra("colNo"); // 集中器ID
                    String channelNo = getIntent().getStringExtra("channelNo"); // 信道号
                    params.add(state);
                    params.add(colNo);
                    params.add(channelNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET) { // 中继入网命令
                    String relayMeterNo = getIntent().getStringExtra("relayMeterNo"); // 中继表ID号
                    params.add(relayMeterNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) { // 取消中继入网命令
                    String relayMeterNo = getIntent().getStringExtra("relayMeterNo"); // 中继表ID号
                    params.add(relayMeterNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) { // 测试模式设置命令
                    String cmd = getIntent().getStringExtra("cmd"); // 命令字
                    // String date = copydf6.format(new Date()); //时间
                    String date = getIntent().getStringExtra("date"); // 时间
                    params.add(cmd);
                    params.add(date);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) { // 运行参数查询命令
                    String state = getIntent().getStringExtra("state"); // 状态字
                    params.add(state);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_RESET) { // 恢复出厂设置命令
                    params.add(commNumber); // 表号
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {// 设定密钥命令
                    String key = getIntent().getStringExtra("key");// 密钥
                    params.add(commNumber); // 表号
                    params.add(key);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) { // 安全模式设置命令
                    String cmd = getIntent().getStringExtra("cmd"); // 命令字
                    params.add(commNumber);
                    params.add(cmd);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) { // 设置抄表时间段
                    String startDay = getIntent().getStringExtra("startDay");
                    String endDay = getIntent().getStringExtra("endDay");
                    params.add(startDay);
                    params.add(endDay);
                }
                message = cchmp.encodeShangHai(operationType, commNumber, params);

            } else {
                CqueueData data = new CqueueData();
                if ((commNumber.length() != 10)) {
                    if (commNumber.length() > 10) {
                        commNumber = commNumber.substring(commNumber.length() - 10);
                    } else {
                        Toast.makeText(getApplicationContext(), "通讯编号错误!", Toast.LENGTH_SHORT).show();
                    }
                }
                String timeString = copydf.format(new Date());
                // 操作模式

                //设置命令类型
                //执行抄表操作
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                    if (meterTypeNo.equals("05")) { // 纯无线表
                        data.setCmdType("01"); // 抄表命令
                        if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                            data.setCmdType("81");
                        }
                        timeString += ";01030713";
                        data.setDataBCD(timeString);
                    } else if (meterTypeNo.equals("04")) { // IC卡无线
                        data.setCmdType("06"); // 抄表命令
                        // timeString += ";01030713";
                        data.setBcdDate(timeString);
                    }
                }
                //执行开阀操作
                else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
                    data.setCmdType("02");
                    if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                        data.setCmdType("82");
                    }
                }
                //执行关阀操作
                else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
                    data.setCmdType("03");
                    if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                        data.setCmdType("83");
                    }
                }
                //修改通讯号
                else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
                    data.setCmdType("05");
                    String setParam1 = getIntent().getStringExtra("setParam1");
                    data.set();
                    data.setDataBCD(setParam1);
                }
                // 设置表底数
                else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) {
                    data.setCmdType("0b");
                    data.set();
                    data.setDataBCD(getIntent().getStringExtra("baseNum"));
                }
                // 抄取冻结量
                else if (operationType == GlobalConsts.COPY_OPERATION_COPYFROZEN) {
                    data.setCmdType("81");
                }


                data.setTargetAddr(commNumber);


                if (meterTypeNo.equals("05")) {
                    if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州模式
                        message = cchmp.encodeHuiZhou(data);
                    } else {
                        message = cchmp.encode(data);
                    }
                } else if (meterTypeNo.equals("04")) {
                    message = cchmp.encodeIC(data);
                }
                message = headMessage + message + "16";
                try {
                    message.getBytes("ISO_8859_1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (message.equals("noQuery")) {
                    Toast.makeText(getApplicationContext(), "不是查询参数!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            LogUtil.i("蓝牙", message);

            //向蓝牙发送命令
            sendMessage(message); // 发送蓝牙消息

            // 抄表监听
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                isCopy = false;
                // if (meterTypeNo.equals("05")) {
                if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// 批抄
                    if (loadingcount == 0 || isWakeUp) {
                        isWakeUp = false;
                        new Thread() {
                            @Override
                            public void run() {
                                int time = 0;
                                for (int i = 0; i < wakeupWait; i++) { // 等待
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    time++;
                                    while (isCopy) {
                                        time = 0;
                                        break;
                                    }
                                }
                                if (time > wakeupWait - 1) {
                                    Message msg = new Message();
                                    msg.what = MESSAGE_COPYCANT;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }.start();
                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                int time = 0;
                                for (int i = 0; i < copyWait; i++) { // 等待
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    time++;
                                    while (isCopy) {
                                        time = 0;
                                        break;
                                    }
                                }
                                if (time > copyWait - 1) {
                                    Message msg = new Message();
                                    msg.what = MESSAGE_COPYCANT;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }.start();
                    }
                } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// 单抄
                    new Thread() {
                        @Override
                        public void run() {
                            int time = 0;
                            for (int i = 0; i < wakeupWait; i++) { // 等待
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                time++;
                                while (isCopy) {
                                    time = 0;
                                    break;
                                }
                            }
                            if (time > wakeupWait - 1) {
                                Message msg = new Message();
                                msg.what = MESSAGE_COPYCANT;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }.start();
                }
            }
        }
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


    /**
     * 设置按钮点击事件
     */
    private void addListener() {
        btnCopyScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("开始")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿改变蓝牙连接！", Toast.LENGTH_SHORT).show();
                } else {
                    // 启动DeviceListActivity看到设备和做扫描
                    Intent intent = new Intent(CtCopyingActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }

            }
        });

        btnCopyingRead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("开始")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！", Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("蓝牙设备未连接") || tvBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！", Toast.LENGTH_SHORT).show();
                } else {
                    loopCount = 0;
                    unCopyMeterNos = new ArrayList<>();
                    query();
                }
                // query();
            }
        });

        btnCopyingStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isStop = true;
                finish();
            }
        });

    }

    /**
     * 设备扫描返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // 当devicelistactivity返回与设备连接
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
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

    /**
     * 连接设备
     */
    private void connectDevice(String address, boolean secure) {
        // // 获得设备地址
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // 获得蓝牙设备对象
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // 尝试连接到设备
        mChatService.connect(device);
    }


    /**
     * 解析IC卡无线抄表数据
     */
    private CtCopyDataICRF getCopyDataICRF(String meterNo, String dataString) {


        //集中器IC无线
        CtCopyDataICRF copyDataICRF = new CtCopyDataICRF();


        //设置表号
        copyDataICRF.setMeterNo(meterNo);


        //设置累计量
        String[] strarray = dataString.split(";");
        String Cumulant = Integer.toString(Integer.parseInt(strarray[0], 10));
        if (Cumulant.length() > 2) {
            Cumulant = Cumulant.substring(0, Cumulant.length() - 2) + "." + Cumulant.substring(Cumulant.length() - 2);
        } else {
            Cumulant = "0." + Cumulant;
        }
        copyDataICRF.setCumulant(Cumulant);


        // 剩余金额
        String SurplusMoney = Integer.toString(Integer.parseInt(strarray[1], 10));
        if (SurplusMoney.length() > 2) {
            SurplusMoney = SurplusMoney.substring(0, SurplusMoney.length() - 2) + "." + SurplusMoney.substring(SurplusMoney.length() - 2);
        } else {
            SurplusMoney = "0." + SurplusMoney;
        }
        copyDataICRF.setSurplusMoney(SurplusMoney);

        // 过零金额
        String OverZeroMoney = Integer.toString(Integer.parseInt(strarray[2], 10));
        if (OverZeroMoney.length() > 2) {
            OverZeroMoney = OverZeroMoney.substring(0, OverZeroMoney.length() - 2) + "." + OverZeroMoney.substring(OverZeroMoney.length() - 2);
        } else {
            OverZeroMoney = "0." + OverZeroMoney;
        }
        copyDataICRF.setOverZeroMoney(OverZeroMoney);

        // 购买次数
        int BuyTimes = Integer.parseInt(strarray[3], 10);
        copyDataICRF.setBuyTimes(BuyTimes);

        // 过流次数
        int OverFlowTimes = Integer.parseInt(strarray[4], 10);
        copyDataICRF.setOverFlowTimes(OverFlowTimes);

        // 磁攻击次数
        int MagAttTimes = Integer.parseInt(strarray[5], 10);
        copyDataICRF.setMagAttTimes(MagAttTimes);

        // 卡攻击次数
        int CardAttTimes = Integer.parseInt(strarray[6], 10);
        copyDataICRF.setCardAttTimes(CardAttTimes);

        // 表具状态转换成16进制INT存入数据库
        copyDataICRF.setMeterState(Integer.parseInt(strarray[7], 16));
        // 当月累计量
        copyDataICRF.setCurrMonthTotal(Integer.toString(Integer.parseInt(strarray[8], 10)));
        // 上月累计量
        copyDataICRF.setLast1MonthTotal(Integer.toString(Integer.parseInt(strarray[9], 10)));

        if (strarray.length > 10) {

            // 当前单价（去除帧间分隔符"|"）
            String unitPrice = strarray[10].substring(1);
            unitPrice = Integer.toString(Integer.parseInt(unitPrice, 10));
            if (unitPrice.length() > 2) {
                unitPrice = unitPrice.substring(0, unitPrice.length() - 2) + "." + unitPrice.substring(unitPrice.length() - 2);
            } else {
                unitPrice = "0." + unitPrice;
            }
            copyDataICRF.setUnitPrice(unitPrice);

            // 累计用气金额
            String accMoney = strarray[11].substring(0, 8);
            accMoney = Integer.toString(Integer.parseInt(accMoney, 10));
            if (accMoney.length() > 2) {
                accMoney = accMoney.substring(0, accMoney.length() - 2) + "." + accMoney.substring(accMoney.length() - 2);
            } else {
                accMoney = "0." + accMoney;
            }
            copyDataICRF.setAccMoney(accMoney);

            // 累计充值金额
            String accBuyMoney = strarray[12].substring(0, 8);
            accBuyMoney = Integer.toString(Integer.parseInt(accBuyMoney, 10));
            if (accBuyMoney.length() > 2) {
                accBuyMoney = accBuyMoney.substring(0, accBuyMoney.length() - 2) + "." + accBuyMoney.substring(accBuyMoney.length() - 2);
            } else {
                accBuyMoney = "0." + accBuyMoney;
            }
            copyDataICRF.setAccBuyMoney(accBuyMoney);

            // 本周期使用量
            String currentShow = Integer.toString(Integer.parseInt(strarray[13], 10));
            copyDataICRF.setCurrentShow(currentShow);
        }
        //抄表时间
        copyDataICRF.setCopyTime(df.format(new Date()));
        //表具名称
        copyDataICRF.setMeterName(tvLoadingName.getText().toString());
        copyDataICRF.setCommunicateNo(meterNo);
        copyDataICRF.setCollectorNo(collectorNo);//集中器

        return copyDataICRF;
    }


    /**
     * 解析上海纯无线抄表数据
     */
    private CtCopyData getCopyDataShangHai(String meterNo, String dataString) {
        CtCopyData copyData = new CtCopyData();
        copyData.setMeterNo(meterNo);
        copyData.setElec(dataString.substring(0, 2));
        String CurrentShow = dataString.substring(2, 8);
        CurrentShow = CurrentShow.substring(1);// 去掉F
        copyData.setCurrentShow(CurrentShow); // 累计量
        copyData.setMeterState(Integer.parseInt(dataString.substring(8, 10), 16));
        copyData.setCollectorNo(collectorNo);//设置集中器编号
        copyData.setCommunicateNo(meterNo);
        // 使用量计算
        return copyData;
    }

    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros().toPlainString();
    }

    /**
     * 解析无线表抄表数据
     */
    private CtCopyData getCopyData(String meterNo, String dataString) {
        CtCopyData copyData = new CtCopyData();
        copyData.setMeterNo(meterNo);
        String[] strarray = dataString.split(";");
        String CurrentShow = Integer
                .toString(Integer.parseInt(strarray[0], 10));
        if (CurrentShow.length() > 2) {
            CurrentShow = CurrentShow.substring(0, CurrentShow.length() - 2)
                    + "." + CurrentShow.substring(CurrentShow.length() - 2);
        } else {
            CurrentShow = "0." + CurrentShow;
        }
        copyData.setCurrentShow(CurrentShow); // 累计量
        if (strarray.length > 2) {
            copyData.setMeterState(Integer.parseInt(strarray[1], 16)); // 表具状态转换成16进制INT存入数据库
        }
        if (strarray.length > 3) {
            String dBm = strarray[3].substring(1);// 无线信号强度（去除帧间分隔符"|"）
            copyData.setdBm(dBm);
            String elec = strarray[4]; // 电量
            copyData.setElec(elec);
        }
        copyData.setMeterName(tvLoadingName.getText().toString());
        copyData.setLastShow("0.00");
        copyData.setLastDosage("0");
        copyData.setCurrentDosage(CurrentShow);
        copyData.setUnitPrice("2.5");
        copyData.setPrintFlag(0);
        copyData.setCopyWay("S");
        copyData.setCopyTime(df.format(new Date()));
        copyData.setCopyState(1);
        copyData.setCopyMan(preferenceBiz.getUserName());
        copyData.setOperateTime(df.format(new Date()));
        copyData.setIsBalance(0);
        copyData.setRemark("");
        copyData.setCollectorNo(collectorNo);//设置集中器编号
        copyData.setCommunicateNo(meterNo);
//        copyData.setMeterNo(meterNo);
        return copyData;
    }

    /**
     * 解析惠州无线表抄表数据
     */
    private CtCopyData getCopyDataHuiZhou(String meterNo, CqueueData data) {
        CtCopyData copyData = new CtCopyData();
        copyData.setMeterNo(meterNo);
        String CurrentShow = Double.toString(data.getSumUseGas());
        copyData.setCurrentShow(CurrentShow); // 累计量
        copyData.setMeterState(Integer.parseInt(data.getMeterState(), 16)); // 表具状态转换成16进制INT存入数据库
        copyData.setMeterName(tvLoadingName.getText().toString());
//        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // 查询上一次抄表记录
//        if (copyDataLast != null) {
//            String lastShow = copyDataLast.getCurrentShow();
//            if (lastShow == null) {
//                copyData.setLastShow("0.00");
//                copyData.setLastDosage("0");
//                copyData.setCurrentDosage(CurrentShow);
//            } else {
//                copyData.setLastShow(lastShow);
//                copyData.setLastDosage(copyDataLast.getCurrentDosage());
//                BigDecimal c1 = new BigDecimal(CurrentShow);
//                BigDecimal c2 = new BigDecimal(lastShow);
//                copyData.setCurrentDosage(c1.subtract(c2).toString());
//            }
//            // copyData.setUnitPrice(copyDataLast.getUnitPrice());
//        } else { // 初始值
        copyData.setLastShow("0.00");
        copyData.setLastDosage("0");
        copyData.setCurrentDosage(CurrentShow);
        // copyData.setUnitPrice("2.5");
//        }
        // copyData.setPrintFlag(0);
        copyData.setCopyWay("S");
        copyData.setCopyTime(df.format(new Date()));
        copyData.setCopyState(1);
        copyData.setCopyMan(preferenceBiz.getUserName());
        copyData.setOperateTime(df.format(new Date()));
        // copyData.setIsBalance(0);
        copyData.setRemark("");
        copyData.setCollectorNo(collectorNo);//设置集中器编号
        copyData.setCommunicateNo(meterNo);
        return copyData;
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
                            tvBtInfo.setText(getString(R.string.title_connected_to) + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            // 蓝牙连接后自动开始抄表
                            isStop = false;
                            query();//连上了就去抄表
                            break;

                        //正在连接
                        case BluetoothChatService.STATE_CONNECTING:
                            tvBtInfo.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            tvBtInfo.setText(R.string.title_not_connected);
                            isStop = true;
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
//				 byte[] writeBuf = (byte[]) msg.obj;
//				 // 构建一个字符串缓冲区
//				 String writeMessage = new String(writeBuf);
//				 LogUtil.i("蓝牙,bluepost", writeMessage);
                    break;
                case MESSAGE_READ:
                    if (isStop) { // 手工停止或蓝牙断开后阻止一切读操作
                        break;
                    }
                    try {
                        byte[] readBuf = (byte[]) msg.obj;
                        // 构建一个字符串有效字节的缓冲区
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        // Log.i("blueget", readMessage);
                        if (readMessage.length() < 12) {
                            tvCopyingBackMsg.setText("接收数据错误！");
                            break;
                        }
                        readMessage = readMessage.substring(12);
                        HhProtocol hhP = new HhProtocol();
                        CqueueData msgDecode = new CqueueData(); // 协议类
                        if (meterTypeNo.equals("05")) { // 纯无线表
                            if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州模式
                                msgDecode = hhP.decodeHuiZhou(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // 上海模式
                                msgDecode = hhP.decodeShangHai(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else {
                                msgDecode = hhP.decode(readMessage, cmdtype); // 解包
                            }
                        } else if (meterTypeNo.equals("04")) { // IC卡无线表
                            msgDecode = hhP.decodeIC(readMessage);
                        }

                        if (msgDecode == null) {
                            tvCopyingBackMsg.setText("接收数据错误！");
                            break;
                        } else {
                            // 接收返回的信息
                            String dataString = msgDecode.getCmdData();
                            // tvCopyingBackMsg.setText("");
                            // String cmdtype = msgDecode.getCmdType();
                            String loadingComNum = tvLoadingComNum.getText().toString();
                            String loadingComNumActual = loadingComNum; // 超过10位的判断
                            if (loadingComNumActual.length() > 10) {
                                loadingComNumActual = loadingComNumActual.substring(loadingComNumActual.length() - 10);
                            }


                            //操作类型
                            switch (cmdtype) {
                                case "01": // 无线表抄表数据
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CtCopyData copyData;
                                        if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // 惠州模式
                                            copyData = getCopyDataHuiZhou(loadingComNum, msgDecode);
                                        } else {
                                            copyData = getCopyData(loadingComNum, dataString);
                                        }
                                        //把抄表结果保存到数据库中去
                                        ctCopyDataDao.putCtCopyData(copyData);
//                                        copyBiz.addCopyData(copyData);
//                                        // 修改抄表状态
//                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// 标识为已抄到
                                        copyTimes = 0;
                                        // 判断是否继续抄
                                        if (loadingcount < countAll) {
                                            query();
                                        } else {
                                            // 抄表完成跳转
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            //判断是否需要循环抄第二轮
                                            if (loopCount < 2 && unCopyMeterNos.size() > 0) {
                                                meterNos = unCopyMeterNos;
                                                countAll = meterNos.size();
                                                loadingcount = 0;
                                                copyTimes = 0;
                                                tvLoadingAll.setText(countAll + "");
                                                tvLoadingCount.setText("0");
                                                pgbCopying.setMax(countAll);
                                                pgbCopying.setProgress(0);
                                                loopCount++;
                                                unCopyMeterNos = new ArrayList<String>();
                                                query();
                                            } else {
                                                //抄表结束
                                                finish();
//                                                Intent intent = new Intent(CtCopyingActivity.this, CopyResultActivity.class);
//                                                intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_COPY);
//                                                intent.putExtra("meterNos", AllMeterNo);
//                                                intent.putExtra("meterTypeNo", meterTypeNo);
//                                                startActivity(intent);
                                            }
                                        }
                                    }
                                    break;
                                case "06": // IC卡无线抄表数据
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CtCopyDataICRF copyDataICRF;
                                        copyDataICRF = getCopyDataICRF(loadingComNum, dataString);
                                        //存进去
                                        ctCopyDataICRFDao.putCtCopyData(copyDataICRF);
                                        //插入数据库
//                                        copyBiz.addCopyDataICRF(copyDataICRF);
//                                        // 修改抄表状态
//                                        copyBiz.ChangeCopyState(copyDataICRF.getMeterNo(), 1, meterTypeNo);


                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// 标识为已抄到
                                        copyTimes = 0;
                                        // 判断是否继续抄
                                        if (loadingcount < countAll) {
                                            query();
                                        } else {
                                            // 抄表完成跳转
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                // TODO 自动生成的 catch 块
                                                e.printStackTrace();
                                            }
                                            finish();
                                            Intent intent = new Intent(CtCopyingActivity.this, CopyResultActivity.class);
                                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_COPY);
                                            intent.putExtra("meterNos", AllMeterNo);
                                            intent.putExtra("meterTypeNo", meterTypeNo);
                                            startActivity(intent);
                                        }
                                    }
                                    break;
                                case "02":// 开阀
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("表具已成功开阀");
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "03":// 关阀
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("表具已成功关阀");
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "05":// 修改通讯号
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("修改通讯号成功");
                                        tvLoadingComNum.setText(msgDecode
                                                .getSourceAddr());
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08":// 惠州修改通讯号
                                    if (readMessage.length() > 1) {
                                        tvCopyingBackMsg.setText("修改通讯号成功");
                                        tvLoadingComNum.setText(msgDecode.getSourceAddr());
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "0B": // 惠州设置表底数
                                    if (readMessage.length() > 1) {
                                        tvCopyingBackMsg.setText("设置表底数成功");
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "81":// 惠州抄取冻结量
                                    if (msgDecode != null
                                            && msgDecode.getDataBCD().length() > 7) {
                                        tvCopyingBackMsg.setTextSize(14);
                                        tvCopyingBackMsg.setText("冻结时间:"
                                                + msgDecode.getDataBCD()
                                                .substring(0, 2)
                                                + " 冻结量:"
                                                + msgDecode.getDataBCD()
                                                .substring(2, 8));
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "0785": // 上海表抄表数据
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CtCopyData copyData = getCopyDataShangHai(loadingComNum, dataString);
//                                        copyBiz.addCopyData(copyData);
//                                        // 修改抄表状态
//                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
                                        ctCopyDataDao.putCtCopyData(copyData);
                                        // 进度更新
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// 标识为已抄到
                                        copyTimes = 0;
                                        // 判断是否继续抄
                                        if (loadingcount < countAll) {
                                            query();
                                        } else { // 抄表完成跳转
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            finish();
                                            Intent intent = new Intent(CtCopyingActivity.this, CopyResultActivity.class);
                                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_COPY);
                                            intent.putExtra("meterNos", AllMeterNo);
                                            intent.putExtra("meterTypeNo", meterTypeNo);
                                            startActivity(intent);
                                        }
                                    }
                                    break;
                                case "0283":// 上海修改表计编号
                                    tvCopyingBackMsg.setText("修改通讯号成功");
                                    tvLoadingComNum.setText(msgDecode.getSourceAddr());
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0287": // 上海开关阀
                                    tvCopyingBackMsg.setText("表具操作成功");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0288":// 上海设置抄表时间段
                                    tvCopyingBackMsg.setText("设置抄表时间段成功");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0489": // 上海查询抄表时间
                                    if (dataString != null && dataString.length() > 3) {
                                        tvCopyingBackMsg.setText("起始日:"
                                                + dataString.substring(0, 2) + " 结束日:"
                                                + dataString.substring(2, 4));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "03a8": // 入网命令
                                    if (dataString != null && dataString.length() > 1) {
                                        tvCopyingBackMsg
                                                .setText("入网注册结果:" + dataString);
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08a9":// 中继入网命令
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("入网结果:" + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08ab":// 取消中继入网
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("执行结果:" + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "03ac":// 测试模式设置
                                    if (dataString != null && dataString.length() > 1) {
                                        if (dataString.equals("00")) {
                                            tvCopyingBackMsg.setText("已退出测试模式");
                                        } else {
                                            tvCopyingBackMsg.setText("已进入测试模式");
                                        }
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "09ae":// 运行参数查询
                                    if (dataString != null && dataString.length() > 13) {
                                        tvCopyingBackMsg.setText("信道号:"
                                                + dataString.substring(0, 2)
                                                + " 集中器ID号:"
                                                + dataString.substring(2, 12)
                                                + " 工作状态:"
                                                + dataString.substring(12, 14));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "07af":// 恢复出厂设置
                                    tvCopyingBackMsg.setText("恢复出厂设置成功");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "17a1":// 设定密钥
                                    tvCopyingBackMsg.setText("密钥设定成功");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "08a2":// 安全模式设置
                                    if (dataString != null && dataString.length() > 11) {
                                        String cmd = dataString.substring(10, 12);
                                        if (cmd.equals("00")) {
                                            tvCopyingBackMsg.setText("已设置为明文模式");
                                        } else {
                                            tvCopyingBackMsg.setText("已设置为安全模式");
                                        }
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        tvCopyingBackMsg.setText(e.getMessage());
                        break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    // Toast.makeText(getApplicationContext(),
                    // "已连接 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                    // .show();
                    break;
                // case MESSAGE_TOAST:
                // Toast.makeText(getApplicationContext(),
                // msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                // .show();
                // break;
                case MESSAGE_COPYCANT: // 未抄到处理
                    if (isStop) { // 手工停止后阻止一切读操作
                        break;
                    }
                    if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// 批抄
                        if (loadingcount != 0 && copyTimes < repeatCount) { // 重复最多2次
                            tvCopyingBackMsg.setText("表具无响应,重新唤醒抄表");
                            copyTimes++;
                            isWakeUp = true;//重新唤醒
                            query();
                        } else { // 继续
                            String loadingComNum = tvLoadingComNum.getText().toString();
//                            copyBiz.ChangeCopyState(loadingComNum, 0, meterTypeNo);// 修改状态为未抄到
                            loadingcount++;
                            tvLoadingCount.setText(loadingcount + "");
                            pgbCopying.incrementProgressBy(1);
                            copyTimes = 0;
                            unCopyMeterNos.add(loadingComNum);//添加至未抄临时集合
                            // 抄下一台
                            if (loadingcount < countAll) {
                                query();
                            } else {
                                // 抄表完成跳转
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    // TODO 自动生成的 catch 块
                                    e.printStackTrace();
                                }
                                //判断是否需要循环抄第二轮
                                if (loopCount < 2 && unCopyMeterNos.size() > 0) {
                                    meterNos = unCopyMeterNos;
                                    countAll = meterNos.size();
                                    loadingcount = 0;
                                    copyTimes = 0;
                                    tvLoadingAll.setText(countAll + "");
                                    tvLoadingCount.setText("0");
                                    pgbCopying.setMax(countAll);
                                    pgbCopying.setProgress(0);
                                    loopCount++;
                                    unCopyMeterNos = new ArrayList<String>();
                                    query();
                                } else {
                                    finish();
//                                    Intent intent = new Intent(CtCopyingActivity.this,
//                                            CopyResultActivity.class);
//                                    intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE,
//                                            GlobalConsts.RE_TYPE_COPY);
//                                    intent.putExtra("meterNos", AllMeterNo);
//                                    intent.putExtra("meterTypeNo", meterTypeNo);
//                                    startActivity(intent);
                                }
                            }
                        }
                    } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// 单抄
                        copyBiz.ChangeCopyState(meterNos.get(loadingcount), 0,
                                meterTypeNo);// 修改状态为未抄到
                        loadingcount++;
                        tvLoadingCount.setText(loadingcount + "");
                        pgbCopying.incrementProgressBy(1);
                        copyTimes = 0;
                        // 抄下一台
                        if (loadingcount < countAll) {
                            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                                tvCopyingBackMsg.setText("抄表失败，继续下一台抄表");
                            }
                            query();
                        } else {
                            // 抄表完成跳转
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
//                            Intent intent = new Intent(CtCopyingActivity.this, CopyResultActivity.class);
//                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_COPY);
//                            intent.putExtra("meterNos", AllMeterNo);
//                            intent.putExtra("meterTypeNo", meterTypeNo);
//                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    };

}