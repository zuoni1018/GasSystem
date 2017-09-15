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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.bll.PreferenceBiz;
import com.pl.bll.SetBiz;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.protocol.CqueueData;
import com.pl.protocol.HhProtocol;
import com.pl.utils.GlobalConsts;
import com.pl.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CopyingActivity extends Activity {

    private TextView tvTitlebar_name, tvLoadingCount, tvLoadingAll, tvBtInfo, tvCopyingBackMsg, tvLoadingComNum, tvLoadingName;
    private ProgressBar pgbCopying;
    // private Handler handler;
    private CopyBiz copyBiz;
    private SetBiz setBiz;
    private String meterTypeNo;
    private ArrayList<String> meterNos;
    private ArrayList<String> unCopyMeterNos;
    private ArrayList<String> AllMeterNo;
    private ImageButton btnBtScan, btnCopyingRead, btnCopyingStop;
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

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_copying);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_main);

        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);

        setupView();
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

        addListener();
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

        btnCopyingRead.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCopyingRead.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCopyingRead.startAnimation(anim_btn_end);
                }
                return false;
            }
        });

        btnCopyingStop.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCopyingStop.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCopyingStop.startAnimation(anim_btn_end);
                }
                return false;
            }
        });

    }

    private void setupView() {
        tvTitlebar_name = (TextView) findViewById(R.id.tvMainTitleBarName);
        tvTitlebar_name.setText("正在操作，请勿离开此界面");

        tvBtInfo = (TextView) findViewById(R.id.tvBtInfo);
        btnBtScan = (ImageButton) findViewById(R.id.btnCopyScan);
        btnCopyingRead = (ImageButton) findViewById(R.id.btnCopyingRead);
        btnCopyingStop = (ImageButton) findViewById(R.id.btnCopyingStop);
        tvCopyingBackMsg = (TextView) findViewById(R.id.tvCopyingBackMsg);

        tvLoadingCount = (TextView) findViewById(R.id.tvLoadingCount);
        tvLoadingAll = (TextView) findViewById(R.id.tvLoadingAll);
        pgbCopying = (ProgressBar) findViewById(R.id.pgbCopying);
        tvLoadingComNum = (TextView) findViewById(R.id.tvLoadingComNum);
        tvLoadingName = (TextView) findViewById(R.id.tvLoadingName);
    }

    // 唤醒
    // private void wakeUp(String commNumber){
    // sendMessage("aadd50" + commNumber); //发送蓝牙消息
    // tvCopyingBackMsg.setText("正在唤醒表具");
    // }

    // 抄表
    private void query() {
        // 操作模式
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
        if (loadingcount < countAll) {
            String headMessage = "";
            String commNumber = meterNos.get(loadingcount);
            String meterName = copyBiz.getMeterNameByNo(commNumber);
            headMessage = "6808"; // 起始符68 代号08
            // 判断各类工作模式
            if (meterTypeNo.equals("05")) { // 纯无线表
                // 判断长度
                if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // 惠州FSK
                    headMessage += "14";
                } else {
                    headMessage += "19";
                }
                if (runMode.equals(GlobalConsts.RUNMODE_STANDARD)) { // 混合模式
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
                } else if (runMode.equals(GlobalConsts.RUNMODE_LORA) || runMode.equals(GlobalConsts.RUNMODE_ZHGT)) { // LORA模式、港泰模式
                    headMessage += "02";
                } else if (runMode.equals(GlobalConsts.RUNMODE_FSK)) { // FSK
                    headMessage += "01";
                } else if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // 惠州FSK
                    headMessage += "03";
                } else if (runMode.equals(GlobalConsts.RUNMODE_SHANGHAI)) { // 上海FSK
                    // headMessage += "04";
                }
            } else if (meterTypeNo.equals("04")) { // IC卡无线
                if (runMode.equals(GlobalConsts.RUNMODE_FSK)) { // FSK
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

            if (runMode.equals(GlobalConsts.RUNMODE_SHANGHAI)) { // 上海模式专用
                headMessage = ""; // 不发唤醒
                ArrayList<String> params = new ArrayList<String>();
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
                    String relayMeterNo = getIntent().getStringExtra(
                            "relayMeterNo"); // 中继表ID号
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
                        commNumber = commNumber
                                .substring(commNumber.length() - 10);
                    } else {
                        Toast.makeText(getApplicationContext(), "通讯编号错误!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                String timeString = copydf.format(new Date());
                // 操作模式
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                    if (meterTypeNo.equals("05")) { // 纯无线表
                        data.setCmdType("01"); // 抄表命令
                        if (runMode.equals(GlobalConsts.RUNMODE_ZHGT)) {
                            data.setCmdType("81");
                        }
                        timeString += ";01030713";
                        data.setDataBCD(timeString);
                    } else if (meterTypeNo.equals("04")) { // IC卡无线
                        data.setCmdType("06"); // 抄表命令
                        // timeString += ";01030713";
                        data.setBcdDate(timeString);
                    }
                } else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
                    data.setCmdType("02");
                    if (runMode.equals(GlobalConsts.RUNMODE_ZHGT)) {
                        data.setCmdType("82");
                    }
                } else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
                    data.setCmdType("03");
                    if (runMode.equals(GlobalConsts.RUNMODE_ZHGT)) {
                        data.setCmdType("83");
                    }
                } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {// 设置通讯号
                    if(runMode.equals(GlobalConsts.RUNMODE_ZHGT)){
                        //港泰
                        data.setCmdType("87");//87
                    }else {
                        //普通
                        data.setCmdType("05");
                    }

                    String setParam1 = getIntent().getStringExtra("setParam1");
                    data.set();
                    data.setDataBCD(setParam1);
                } else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) { // 设置表底数
                    data.setCmdType("0b");
                    data.set();
                    data.setDataBCD(getIntent().getStringExtra("baseNum"));
                } else if (operationType == GlobalConsts.COPY_OPERATION_COPYFROZEN) { // 抄取冻结量
                    data.setCmdType("81");
                }
                data.setTargetAddr(commNumber);
                if (meterTypeNo.equals("05")) {
                    if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // 惠州模式
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
                    Toast.makeText(getApplicationContext(), "不是查询参数!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }
            }
            LogUtil.i("蓝牙", message);
            sendMessage(message); // 发送蓝牙消息
            // Log.i("msg", message);
            // tvCopyingBackMsg.setText(message);

            // 抄表监听
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                isCopy = false;
                // if (meterTypeNo.equals("05")) {
                if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// 批抄
                    if (loadingcount == 0 || isWakeUp == true) {
                        isWakeUp = false;
                        new Thread() {
                            @Override
                            public void run() {
                                int time = 0;
                                for (int i = 0; i < wakeupWait; i++) { // 等待
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        // TODO 自动生成的 catch 块
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

                            ;
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
                                        // TODO 自动生成的 catch 块
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

                            ;
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
                                    // TODO 自动生成的 catch 块
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

                        ;
                    }.start();
                }
            }
        }
    }

    // 发送蓝牙消息
    private void sendMessage(String message) {
        LogUtil.i("嗷嗷啊",message);

        // 检查连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
            // .show();
            return;
        }

        // 检查实际上发送的东西
        if (message.length() > 0) {
            // 得到消息的字节，告诉bluetoothchatservice写
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    private void addListener() {
        btnBtScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("开始")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿改变蓝牙连接！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 启动DeviceListActivity看到设备和做扫描
                    Intent intent = new Intent(CopyingActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }

            }
        });

        btnCopyingRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("开始")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！",
                            Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("蓝牙设备未连接")
                        || tvBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loopCount = 0;
                    unCopyMeterNos = new ArrayList<String>();
                    query();
                }
                // query();
            }
        });

        btnCopyingStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isStop = true;
                finish();
            }
        });

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
        // TODO 自动生成的方法存根
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

    private void connectDevice(String address, boolean secure) {
        // // 获得设备地址
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // 获得蓝牙设备对象
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // 尝试连接到设备
        mChatService.connect(device);
    }

    // 使本机蓝牙可见
    // private void ensureDiscoverable() {
    // if (mBluetoothAdapter.getScanMode() !=
    // BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
    // Intent discoverableIntent = new Intent(
    // BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    // discoverableIntent.putExtra(
    // BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// 求可见时间为300秒
    // startActivity(discoverableIntent);
    // }
    // }

    // IC卡无线抄表数据
    private CopyDataICRF getCopyDataICRF(String meterNo, String dataString) {
        CopyDataICRF copyDataICRF = new CopyDataICRF();
        copyDataICRF.setMeterNo(meterNo);
        String[] strarray = dataString.split(";");
        String Cumulant = Integer.toString(Integer.parseInt(strarray[0], 10));
        if (Cumulant.length() > 2) {
            Cumulant = Cumulant.substring(0, Cumulant.length() - 2) + "."
                    + Cumulant.substring(Cumulant.length() - 2);
        } else {
            Cumulant = "0." + Cumulant;
        }
        copyDataICRF.setCumulant(Cumulant);// 累计量
        String SurplusMoney = Integer.toString(Integer
                .parseInt(strarray[1], 10));
        if (SurplusMoney.length() > 2) {
            SurplusMoney = SurplusMoney.substring(0, SurplusMoney.length() - 2)
                    + "." + SurplusMoney.substring(SurplusMoney.length() - 2);
        } else {
            SurplusMoney = "0." + SurplusMoney;
        }
        copyDataICRF.setSurplusMoney(SurplusMoney);// 剩余金额
        String OverZeroMoney = Integer.toString(Integer.parseInt(strarray[2],
                10));
        if (OverZeroMoney.length() > 2) {
            OverZeroMoney = OverZeroMoney.substring(0,
                    OverZeroMoney.length() - 2)
                    + "."
                    + OverZeroMoney.substring(OverZeroMoney.length() - 2);
        } else {
            OverZeroMoney = "0." + OverZeroMoney;
        }
        copyDataICRF.setOverZeroMoney(OverZeroMoney);// 过零金额
        int BuyTimes = Integer.parseInt(strarray[3], 10);// 购买次数
        copyDataICRF.setBuyTimes(BuyTimes);
        int OverFlowTimes = Integer.parseInt(strarray[4], 10);// 购买次数
        copyDataICRF.setOverFlowTimes(OverFlowTimes);
        int MagAttTimes = Integer.parseInt(strarray[5], 10);// 磁攻击次数
        copyDataICRF.setMagAttTimes(MagAttTimes);
        int CardAttTimes = Integer.parseInt(strarray[6], 10);// 卡攻击次数
        copyDataICRF.setCardAttTimes(CardAttTimes);
        copyDataICRF.setMeterState(Integer.parseInt(strarray[7], 16)); // 表具状态转换成16进制INT存入数据库
        copyDataICRF.setCurrMonthTotal(Integer.toString(Integer.parseInt(
                strarray[8], 10)));// 当月累计量
        copyDataICRF.setLast1MonthTotal(Integer.toString(Integer.parseInt(
                strarray[9], 10)));// 上月累计量


        if (strarray.length > 10) {


            String unitPrice = strarray[10].substring(1);// 当前单价（去除帧间分隔符"|"）
            unitPrice = Integer.toString(Integer.parseInt(unitPrice, 10));
            if (unitPrice.length() > 2) {
                unitPrice = unitPrice.substring(0, unitPrice.length() - 2) + "."
                        + unitPrice.substring(unitPrice.length() - 2);
            } else {
                unitPrice = "0." + unitPrice;
            }
            copyDataICRF.setUnitPrice(unitPrice);
            String accMoney = strarray[11].substring(0, 8);// 累计用气金额
            accMoney = Integer.toString(Integer.parseInt(accMoney, 10));
            if (accMoney.length() > 2) {
                accMoney = accMoney.substring(0, accMoney.length() - 2) + "."
                        + accMoney.substring(accMoney.length() - 2);
            } else {
                accMoney = "0." + accMoney;
            }
            copyDataICRF.setAccMoney(accMoney);
            String accBuyMoney = strarray[12].substring(0, 8);// 累计充值金额
            accBuyMoney = Integer.toString(Integer.parseInt(accBuyMoney, 10));
            if (accBuyMoney.length() > 2) {
                accBuyMoney = accBuyMoney.substring(0, accBuyMoney.length() - 2)
                        + "." + accBuyMoney.substring(accBuyMoney.length() - 2);
            } else {
                accBuyMoney = "0." + accBuyMoney;
            }
            copyDataICRF.setAccBuyMoney(accBuyMoney);
            String currentShow = Integer.toString(Integer
                    .parseInt(strarray[13], 10));
            copyDataICRF.setCurrentShow(currentShow);

        }

        copyDataICRF.setCopyTime(df.format(new Date()));
        copyDataICRF.setMeterName(tvLoadingName.getText().toString());


        return copyDataICRF;
    }

    private CopyData getCopyDataShangHai(String meterNo, String dataString) {
        CopyData copyData = new CopyData();
        copyData.setMeterNo(meterNo);
        copyData.setElec(dataString.substring(0, 2));
        String CurrentShow = dataString.substring(2, 8);
        CurrentShow = CurrentShow.substring(1);// 去掉F
        copyData.setCurrentShow(CurrentShow); // 累计量
        copyData.setMeterState(Integer.parseInt(dataString.substring(8, 10), 16));
        // 使用量计算
        return copyData;
    }

    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
    }

    // 无线表抄表数据
    private CopyData getCopyData(String meterNo, String dataString) {
        CopyData copyData = new CopyData();
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
        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // 查询上一次抄表记录
        if (copyDataLast != null) {
            String lastShow = copyDataLast.getCurrentShow();
            lastShow = getPrettyNumber(lastShow);
            if (lastShow == null) {
                copyData.setLastShow("0.00");
                copyData.setLastDosage("0");
                copyData.setCurrentDosage(CurrentShow);
            } else {
                copyData.setLastShow(lastShow);
                copyData.setLastDosage(copyDataLast.getCurrentDosage());
                BigDecimal c1 = new BigDecimal(CurrentShow);
                BigDecimal c2 = new BigDecimal(lastShow);
                copyData.setCurrentDosage(c1.subtract(c2).toString());
                // copyData.setUnitPrice(copyDataLast.getUnitPrice());
            }
        } else { // 初始值
            copyData.setLastShow("0.00");
            copyData.setLastDosage("0");
            copyData.setCurrentDosage(CurrentShow);
            copyData.setUnitPrice("2.5");
        }
        copyData.setPrintFlag(0);
        copyData.setCopyWay("S");
        copyData.setCopyTime(df.format(new Date()));
        copyData.setCopyState(1);
        copyData.setCopyMan(preferenceBiz.getUserName());
        copyData.setOperateTime(df.format(new Date()));
        copyData.setIsBalance(0);
        copyData.setRemark("");
        return copyData;
    }

    // 惠州无线表抄表数据
    private CopyData getCopyDataHuiZhou(String meterNo, CqueueData data) {
        CopyData copyData = new CopyData();
        copyData.setMeterNo(meterNo);
        String CurrentShow = Double.toString(data.getSumUseGas());
        copyData.setCurrentShow(CurrentShow); // 累计量
        copyData.setMeterState(Integer.parseInt(data.getMeterState(), 16)); // 表具状态转换成16进制INT存入数据库
        copyData.setMeterName(tvLoadingName.getText().toString());
        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // 查询上一次抄表记录
        if (copyDataLast != null) {
            String lastShow = copyDataLast.getCurrentShow();
            if (lastShow == null) {
                copyData.setLastShow("0.00");
                copyData.setLastDosage("0");
                copyData.setCurrentDosage(CurrentShow);
            } else {
                copyData.setLastShow(lastShow);
                copyData.setLastDosage(copyDataLast.getCurrentDosage());
                BigDecimal c1 = new BigDecimal(CurrentShow);
                BigDecimal c2 = new BigDecimal(lastShow);
                copyData.setCurrentDosage(c1.subtract(c2).toString());
            }
            // copyData.setUnitPrice(copyDataLast.getUnitPrice());
        } else { // 初始值
            copyData.setLastShow("0.00");
            copyData.setLastDosage("0");
            copyData.setCurrentDosage(CurrentShow);
            // copyData.setUnitPrice("2.5");
        }
        // copyData.setPrintFlag(0);
        copyData.setCopyWay("S");
        copyData.setCopyTime(df.format(new Date()));
        copyData.setCopyState(1);
        copyData.setCopyMan(preferenceBiz.getUserName());
        copyData.setOperateTime(df.format(new Date()));
        // copyData.setIsBalance(0);
        copyData.setRemark("");
        return copyData;
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
                            tvBtInfo.setText(getString(R.string.title_connected_to)
                                    + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                                // TODO 自动生成的 catch 块
                                e1.printStackTrace();
                            }
                            // 蓝牙连接后自动开始抄表
                            isStop = false;
                            query();
                            break;
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
                            if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // 惠州模式
                                msgDecode = hhP.decodeHuiZhou(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else if (runMode
                                    .equals(GlobalConsts.RUNMODE_SHANGHAI)) { // 上海模式
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
                            String loadingComNum = tvLoadingComNum.getText()
                                    .toString();
                            String loadingComNumActual = loadingComNum; // 超过10位的判断
                            if (loadingComNumActual.length() > 10) {
                                loadingComNumActual = loadingComNumActual
                                        .substring(loadingComNumActual.length() - 10);
                            }
                            switch (cmdtype) {
                                case "01": // 无线表抄表数据
                                    if (msgDecode.getSourceAddr().equals(
                                            loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CopyData copyData;
                                        if (runMode
                                                .equals(GlobalConsts.RUNMODE_HUIZHOU)) { // 惠州模式
                                            copyData = getCopyDataHuiZhou(
                                                    loadingComNum, msgDecode);
                                        } else {
                                            copyData = getCopyData(loadingComNum, dataString);
                                        }
                                        copyBiz.addCopyData(copyData);
                                        // 修改抄表状态
                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
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
                                                Intent intent = new Intent(CopyingActivity.this, CopyResultActivity.class);
                                                intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_COPY);
                                                intent.putExtra("meterNos", AllMeterNo);
                                                intent.putExtra("meterTypeNo", meterTypeNo);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                    break;
                                case "06": // IC卡无线抄表数据
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CopyDataICRF copyDataICRF;
                                        copyDataICRF = getCopyDataICRF(loadingComNum, dataString);
                                        copyBiz.addCopyDataICRF(copyDataICRF);
                                        // 修改抄表状态
                                        copyBiz.ChangeCopyState(copyDataICRF.getMeterNo(), 1, meterTypeNo);
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
                                            Intent intent = new Intent(CopyingActivity.this, CopyResultActivity.class);
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
                                    // if(dataString.equals("00")){
                                    // tvCopyingBackMsg.setText("表具已成功开阀");
                                    // // 进度更新
                                    // loadingcount++;
                                    // tvLoadingCount.setText(loadingcount + "");
                                    // pgbCopying.incrementProgressBy(1);
                                    // }
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
                                        tvLoadingComNum.setText(msgDecode.getSourceAddr());
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
                                    if (msgDecode.getSourceAddr().equals(
                                            loadingComNumActual)) { // 判断接收到的数据是否是当前正在操作的表
                                        CopyData copyData = getCopyDataShangHai(
                                                loadingComNum, dataString);
                                        copyBiz.addCopyData(copyData);
                                        // 修改抄表状态
                                        copyBiz.ChangeCopyState(copyData.getMeterNo(),
                                                1, meterTypeNo);
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
                                                // TODO 自动生成的 catch 块
                                                e.printStackTrace();
                                            }
                                            finish();
                                            Intent intent = new Intent(
                                                    CopyingActivity.this,
                                                    CopyResultActivity.class);
                                            intent.putExtra(
                                                    GlobalConsts.EXTRA_COPYRESULT_TYPE,
                                                    GlobalConsts.RE_TYPE_COPY);
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
                                        tvCopyingBackMsg.setText("入网结果:"
                                                + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08ab":// 取消中继入网
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("执行结果:"
                                                + dataString.substring(10, 12));
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
                        // TODO: handle exception
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
                            String loadingComNum = tvLoadingComNum.getText()
                                    .toString();
                            copyBiz.ChangeCopyState(loadingComNum, 0, meterTypeNo);// 修改状态为未抄到
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
                                    Intent intent = new Intent(CopyingActivity.this,
                                            CopyResultActivity.class);
                                    intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE,
                                            GlobalConsts.RE_TYPE_COPY);
                                    intent.putExtra("meterNos", AllMeterNo);
                                    intent.putExtra("meterTypeNo", meterTypeNo);
                                    startActivity(intent);
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
                                // TODO 自动生成的 catch 块
                                e.printStackTrace();
                            }
                            finish();
                            Intent intent = new Intent(CopyingActivity.this,
                                    CopyResultActivity.class);
                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE,
                                    GlobalConsts.RE_TYPE_COPY);
                            intent.putExtra("meterNos", AllMeterNo);
                            intent.putExtra("meterTypeNo", meterTypeNo);
                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    };

}
