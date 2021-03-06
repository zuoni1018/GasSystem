package com.pl.gassystem.activity.ht;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pl.bll.PreferenceBiz;
import com.pl.bll.SetBiz;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.common.Cfun;
import com.pl.common.NetWorkManager;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.HtAppUrl;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.gson.GetCopyDataPhoto;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.Xml2Json;
import com.pl.protocol.CqueueData;
import com.pl.protocol.HhProtocol;
import com.pl.utils.GlobalConsts;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zuoni.zuoni_common.utils.common.ToastUtils;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

import static java.lang.System.currentTimeMillis;

public class HtCopyPhotoActivity extends Activity {

    private TextView tvTitlebar_name, tvPhotoBtInfo, tvCopyPhotoBackMsg, tvLoadingPhotoComNum;
    private String meterNo;
    private String meterTypeNo;
    private ImageButton btnCopyPhotoScan, btnCopyPhotoRead, btnOnlybackQuit;
    private ImageLoader loader;
    private TextView tvCopyPhotoDevState, tvCopyPhotoDevPower;
    private ProgressBar pgbCopyPhoto;
    private EditText etCopyPhotoImgPosition, etCopyPhotoSetPoint;
    private Button btnCopyPhotoImgUp, btnCopyPhotoImgDown, btnCopyPhotoSetPoint;
    private SetBiz setBiz;
    private TextView tvTime;

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
    public static final int MESSAGE_COPYCANT = 6;
    public static final int MESSAGE_GETCOPYDATAPHOTO_SUCCESS = 10;
    public static final int MESSAGE_GETCOPYDATAPHOTO_FAILURT = 11;
    public static final int MESSAGE_GETCOPYDATAPHOTO_ERROR = 12;
    public static final int MESSAGE_SERVER_CONNECT_SUCCESS = 13;
    public static final int MESSAGE_SERVER_CONNECT_FAILURT = 14;

    // 从BluetoothChatService处理程序接收到的密钥名
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // 抄到标志
    private static boolean isCopy;
    // 维护标志
    private static boolean isMaintenance = false;
    private static int p;// 偏移量
    private static String point;// 频点
    private static int maintenMode;// 维护内容

    private static String firstString = "";
    private static String ImgString = "";
    private static int pageConut = 0;// 总图片包数
    private static int pageCurrent = 0;// 当前图片包号
    private static int gettimes = 0; // 图片包接收次数
    // private static boolean isHead = false; //中断包标识
    private static int upCount = 0; // 服务器上传次数
    private static ArrayList<Integer> tempPages;
    private static boolean isFill = false;// 补帧标识
    private static String tempMsg = "";// 临时包存放

    // 抄表模式
    private static int copyType;
    // 执行模式
    private static int operationType;
    boolean isCol = false;
    private static String serverUrl;
    private static String ImgServerUrl;

    private PreferenceBiz preferenceBiz;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat copydf = new SimpleDateFormat("yyMMddHHmm");

    private static Animation anim_btn_begin;
    private static Animation anim_btn_end;


    private int nowNum = 0;
    private List<String> meterNos;
    private List<String> meterTypeNos;
    private ArrayList<GetCopyDataPhoto.ModTimereadmeterinfoBean> copyResults;


    //抄表结果
    private TextView CommunicateNo, OcrRead, DevPower, JSQD, DevState;
    private ImageView imageView;


    String myMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_copyphoto);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_onlyback);
        final TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(HtCopyPhotoActivity.this, HtMessageActivity.class);
                mIntent.putExtra("message", myMessage);
                startActivity(mIntent);
            }
        });

        anim_btn_begin = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_begin);
        anim_btn_end = AnimationUtils.loadAnimation(this, R.anim.btn_alpha_scale_end);
        // 禁止自动弹出输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        preferenceBiz = new PreferenceBiz(this);
        setBiz = new SetBiz(this);

        // 初始化抄表数据
        meterNo = getIntent().getStringExtra("meterNo"); // 表号
        meterTypeNo = getIntent().getStringExtra("meterTypeNo");// 7,8位表
        tvLoadingPhotoComNum.setText(meterNo);

        //拿到很多表号码
        meterNos = getIntent().getStringArrayListExtra("meterNos");
        if (meterNos != null) {
            nowNum = 0;
            meterNo = meterNos.get(0);
            tvLoadingPhotoComNum.setText(meterNo + "(1" + "/" + meterNos.size() + ")");
        }
        meterTypeNos = getIntent().getStringArrayListExtra("meterTypeNos");
        if (meterTypeNos != null) {
            meterTypeNo = meterTypeNos.get(0);
        }
        copyResults = new ArrayList<>();


        copyType = getIntent().getIntExtra("copyType", 1); // 抄表方式（1单抄，2群抄）
        operationType = getIntent().getIntExtra("operationType", 1); // 操作类型
        if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) { // 是否设置集中器
            isCol = getIntent().getBooleanExtra("isCol", false);
        }


        serverUrl = setBiz.getCopyPhotoUrl();
        ImgServerUrl = serverUrl + "Images/";

        // 检测网络
        if (NetWorkManager.isConnect(this) == false) {
            new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog)
                    .setTitle("网络错误")
                    .setMessage("网络连接失败，摄像表抄表必须连接到网络，请检查。")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            finish();
                        }
                    }).show();
        } else { // 检测能否连接识别服务器
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(serverUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    Message msg = Message.obtain(mHandler, MESSAGE_SERVER_CONNECT_FAILURT);
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    Message msg = Message.obtain(mHandler, MESSAGE_SERVER_CONNECT_SUCCESS);
                    mHandler.sendMessage(msg);
                }
            });
        }

    }

    private void addOnTouchListener() {
        btnCopyPhotoScan.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCopyPhotoScan.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCopyPhotoScan.startAnimation(anim_btn_end);
                }
                return false;
            }
        });

        btnCopyPhotoRead.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCopyPhotoRead.startAnimation(anim_btn_begin);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCopyPhotoRead.startAnimation(anim_btn_end);
                }
                return false;
            }
        });

    }

    private void setupView() {
        tvTitlebar_name = (TextView) findViewById(R.id.tvTitlebar_onlyback_name);
        tvTitlebar_name.setText("摄像表安装抄表测试");

        tvPhotoBtInfo = (TextView) findViewById(R.id.tvPhotoBtInfo);
        btnCopyPhotoScan = (ImageButton) findViewById(R.id.btnCopyPhotoScan);
        btnCopyPhotoRead = (ImageButton) findViewById(R.id.btnCopyPhotoRead);
        tvCopyPhotoBackMsg = (TextView) findViewById(R.id.tvCopyPhotoBackMsg);
        tvLoadingPhotoComNum = (TextView) findViewById(R.id.tvLoadingPhotoComNum);
        btnOnlybackQuit = (ImageButton) findViewById(R.id.btn_onlyback_quit);

        tvCopyPhotoDevState = (TextView) findViewById(R.id.tvCopyPhotoDevState);
        tvCopyPhotoDevPower = (TextView) findViewById(R.id.tvCopyPhotoDevPower);
        pgbCopyPhoto = (ProgressBar) findViewById(R.id.pgbCopyPhoto);
        etCopyPhotoImgPosition = (EditText) findViewById(R.id.etCopyPhotoImgPosition);
        btnCopyPhotoImgUp = (Button) findViewById(R.id.btnCopyPhotoImgUp);
        btnCopyPhotoImgDown = (Button) findViewById(R.id.btnCopyPhotoImgDown);
        etCopyPhotoSetPoint = (EditText) findViewById(R.id.etCopyPhotoSetPoint);
        btnCopyPhotoSetPoint = (Button) findViewById(R.id.btnCopyPhotoSetPoint);

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        loader = ImageLoader.getInstance();


        tvTime = (TextView) findViewById(R.id.tvTime);
    }

    // 唤醒
    private void wakeUp(String commNumber) {
        sendMessage("aadd50aaaaaa" + commNumber.substring(6));
        tvCopyPhotoBackMsg.setText("正在唤醒表具");
        firstString = "";
        ImgString = "";
        pageConut = 0;// 总图片包数
        pageCurrent = 0;// 当前图片包号
        gettimes = 0; // 图片包接收次数
        tempMsg = "";
        // isHead = false; //中断包标识
        isFill = false;
        upCount = 0;
        pgbCopyPhoto.setProgress(0);
        tvCopyPhotoDevPower.setText("");

        tvCopyPhotoDevState.setText("");
    }

    private void wakeUp2(String commNumber) {
        sendMessage("aadd50aaaaaa" + commNumber.substring(6));
        tvCopyPhotoBackMsg.setText("正在唤醒表具");
        firstString = "";
        ImgString = "";
        pageConut = 0;// 总图片包数
        pageCurrent = 0;// 当前图片包号
        gettimes = 0; // 图片包接收次数
        tempMsg = "";
        // isHead = false; //中断包标识
        isFill = false;
        upCount = 0;
        pgbCopyPhoto.setProgress(0);

    }


    // 抄表
    private void query() {
        String commNumber = meterNo;
        CqueueData data = new CqueueData();
        if ((commNumber.length() != 10)) {
            Toast.makeText(getApplicationContext(), "通讯编号错误，非10位!", Toast.LENGTH_SHORT).show();
        }
        String timeString = copydf.format(new Date());
        data.setCmdType("23");
        data.setDataBCD(timeString);
        HhProtocol cchmp = new HhProtocol();
        data.setTargetAddr(commNumber);
        String message = cchmp.encode(data);
        try {
            message.getBytes("ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (message.equals("noQuery")) {
            Toast.makeText(getApplicationContext(), "不是查询参数!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // 发送蓝牙消息
            isCopy = false;
            new Thread() {
                @Override
                public void run() {
                    int time = 0;
                    for (int i = 0; i < 40; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time++;
                        if (isCopy) {
                            time = 0;
                            break;
                        }
                    }
                    if (time > 39) {
                        Message msg = new Message();
                        msg.what = MESSAGE_COPYCANT;
                        mHandler.sendMessage(msg);
                    }
                }

            }.start();
        }
    }

    private void fill(String num) {
        CqueueData data = new CqueueData();
        data.setCmdType("25");
        data.set();
        if (num.length() < 2) {
            num = "0" + num;
        }
        String databcd = num;
        data.setDataBCD(databcd);
        data.setTargetAddr(meterNo);
        HhProtocol cchmp = new HhProtocol();
        String message = cchmp.encode(data);
        try {
            message.getBytes("ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (message.equals("noQuery")) {
            Toast.makeText(getApplicationContext(), "不是查询参数!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // 发送蓝牙消息
        }
    }

    private void maintenance() { // 维护
        String databcd = "";
        CqueueData data = new CqueueData();
        if (maintenMode == 1) {// 图片位置调整
            databcd = Integer.toHexString(p);
            data.setCmdType("28");
            data.set();
            if (p > 0) {
                databcd = patchHexString(databcd, 2);
            } else if (p < 0) {
                databcd = databcd.substring(6);
            }
        } else if (maintenMode == 2) { // 频点设置
            databcd = point;
            data.setCmdType("35");
            data.set();
        }

        data.setDataBCD(databcd);
        data.setTargetAddr(meterNo);
        HhProtocol cchmp = new HhProtocol();
        String message = cchmp.encode(data);
        try {
            message.getBytes("ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (message.equals("noQuery")) {
            Toast.makeText(getApplicationContext(), "不是查询参数!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // 发送蓝牙消息
            isCopy = false;
            new Thread() {
                @Override
                public void run() {
                    int time = 0;
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time++;
                        if (isCopy) {
                            time = 0;
                            break;
                        }
                    }
                    if (time > 29) {
                        Message msg = new Message();
                        msg.what = MESSAGE_COPYCANT;
                        mHandler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }

    private static String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    // 发送蓝牙消息
    private void sendMessage(String message) {
        // 检查连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // 检查实际上发送的东西
        if (message.length() > 0) {

            myMessage = myMessage + "\n\n\n" + currentTimeMillis() + "发送命令\n" + message;
            // 得到消息的字节，告诉bluetoothchatservice写
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }


    private void addListener() {
        btnCopyPhotoScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("表具已唤醒正在抄表")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿改变蓝牙连接！", Toast.LENGTH_SHORT).show();
                } else {
                    // 启动DeviceListActivity看到设备和做扫描
                    Intent intent = new Intent(HtCopyPhotoActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
            }
        });

        btnCopyPhotoRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("表具已唤醒正在抄表") || tvCopyPhotoBackMsg.getText().toString().equals("正在唤醒表具")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("蓝牙设备未连接") || tvPhotoBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    isMaintenance = false;
                    wakeUp(meterNo);
                }
            }
        });

        btnOnlybackQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mChatService.stop();
                finish();
            }
        });

        btnCopyPhotoImgUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("表具已唤醒正在抄表")
                        || tvCopyPhotoBackMsg.getText().toString()
                        .equals("正在唤醒表具")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！",
                            Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("蓝牙设备未连接")
                        || tvPhotoBtInfo.getText().toString()
                        .equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String position = etCopyPhotoImgPosition.getText()
                            .toString();
                    try {
                        p = Integer.parseInt(position);
                    } catch (NumberFormatException e) {
                    }
                    if (p >= 1 && p <= 10) {
                        isMaintenance = true;
                        maintenMode = 1;// 调整图片为止
                        wakeUp(meterNo);
                    } else {
                        Toast.makeText(getApplicationContext(), "输入错误，请输入1-10范围内整数！", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnCopyPhotoImgDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("表具已唤醒正在抄表") || tvCopyPhotoBackMsg.getText().toString().equals("正在唤醒表具")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("蓝牙设备未连接") || tvPhotoBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！", Toast.LENGTH_SHORT).show();
                } else {
                    String position = etCopyPhotoImgPosition.getText().toString();
                    position = "-" + position;
                    try {
                        p = Integer.parseInt(position);
                    } catch (NumberFormatException e) {
                    }
                    if (p >= -10 && p <= -1) {
                        isMaintenance = true;
                        maintenMode = 1;// 调整图片为止
                        wakeUp(meterNo);
                    } else {
                        Toast.makeText(getApplicationContext(), "输入错误，请输入1-10范围内整数！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCopyPhotoSetPoint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tvCopyPhotoBackMsg.getText().toString().equals("表具已唤醒正在抄表") || tvCopyPhotoBackMsg.getText().toString().equals("正在唤醒表具")) {
                    Toast.makeText(getApplicationContext(), "正在抄表中，请勿重复操作！", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("蓝牙设备未连接") || tvPhotoBtInfo.getText().toString().equals("正在连接蓝牙设备")) {
                    Toast.makeText(getApplicationContext(), "请先连接蓝牙设备！", Toast.LENGTH_SHORT).show();
                } else {
                    String poi = etCopyPhotoSetPoint.getText().toString();
                    if (poi.length() != 6) {
                        Toast.makeText(getApplicationContext(), "输入错误，必须6位！", Toast.LENGTH_SHORT).show();
                    } else {
                        point = Cfun.ToHEXString(Integer.parseInt(poi + "000"));
                        isMaintenance = true;
                        maintenMode = 2;// 设置频点
                        wakeUp(meterNo);
                    }

                }
            }
        });
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
        if (timer != null) {
            timer.cancel();
        }
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

    private String lastMeterNo;

    private void getCopyDataPhoto(String postData, String meterTypeNo) {
        GetCopyDataPhoto(postData, meterTypeNo);//传给杭天
    }

    private void doNext() {

        ToastUtils.showToast(HtCopyPhotoActivity.this, "执行下一步");
        lastMeterNo = meterNo;
        //到这一步了说明抄表成功了
        nowNum++;
        if (meterNos != null && meterNos.size() > nowNum) {
            meterNo = meterNos.get(nowNum);
            meterTypeNo = meterTypeNos.get(nowNum);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wakeUp2(meterNo);
                    tvLoadingPhotoComNum.setText(meterNo + "(" + (nowNum + 1) + "/" + (meterNos.size()) + ")");//设置当前抄表表号
                }
            }, 3000);
        } else if (meterNos != null && meterNos.size() <= nowNum) {
            LogUtil.i("抄完了");
            ToastUtils.showToast(HtCopyPhotoActivity.this, "抄完了");
            tvCopyPhotoBackMsg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mIntent = new Intent(HtCopyPhotoActivity.this, HtResultCopyPhotoActivity.class);
                    mIntent.putExtra("ModTimereadmeterinfoBean", copyResults);
                    startActivity(mIntent);
                    finish();
                }
            }, 3000);
        }
    }

    private void GetCopyDataPhoto(String postData, String meterTypeNo) {
        LogUtil.i("杭天GetCopyDataPhoto" + postData + "meterTypeNo" + meterTypeNo);
        OkHttpUtils.post()
                .url(HtAppUrl.GET_COPY_DATA_PHOTO)
                .addParams("data", postData)
                .addParams("MeterType", meterTypeNo)
                .addParams("UserName", "admin")
                .addParams("ReadType", "0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("摄像表" + e.toString());
                        Message msg = Message.obtain(mHandler, MESSAGE_GETCOPYDATAPHOTO_FAILURT);
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("摄像表" + response);
                        LogUtil.i("摄像表2" + Xml2Json.xml2JSON(response));
                        Gson gson = new Gson();
                        GetCopyDataPhoto info = gson.fromJson(Xml2Json.xml2JSON(response), GetCopyDataPhoto.class);
                        if (info.getModTimereadmeterinfo() != null) {
                            copyResults.add(info.getModTimereadmeterinfo());
                            CommunicateNo.setText(info.getModTimereadmeterinfo().getCommunicateNo());
                            OcrRead.setText(info.getModTimereadmeterinfo().getOcrRead());
                            DevPower.setText(info.getModTimereadmeterinfo().getDevPower());
                            JSQD.setText(info.getModTimereadmeterinfo().getJSQD());
                            DevState.setText(info.getModTimereadmeterinfo().getDevState());

                            Glide.with(HtCopyPhotoActivity.this)
                                    .load("http://116.62.6.184:8089/" + info.getModTimereadmeterinfo().getDeafault().replace("_", "\\"))
                                    .into(imageView);


                            Message msg = Message.obtain(mHandler, MESSAGE_GETCOPYDATAPHOTO_SUCCESS);
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = Message.obtain(mHandler, MESSAGE_GETCOPYDATAPHOTO_ERROR);
                            mHandler.sendMessage(msg);
                        }

                    }
                });
    }

    private boolean checkImgPage(String getMsg) {
        String len = getMsg.substring(2, 4);
        int length = Integer.parseInt(len, 16);
        length = length * 2 + 2;
        if (getMsg.length() == length) {
            return true;
        } else {
            return false;
        }
    }

    private void getCopy(String getMsg) {
        try {
            if (gettimes == 0) { // 获取第一包数据
                if (getMsg.substring(0, 2).equals("68") && getMsg.substring(getMsg.length() - 2).equals("16")) { // 第一包数据。
                    firstString = getMsg;
                    gettimes++;
                }
            } else {
                if (gettimes == 1) { // 初始化总包数信息
                    pageConut = Integer.parseInt(getMsg.substring(4, 6), 16); // 获取总包数
                    pgbCopyPhoto.setMax(pageConut - 1);
                    tempPages = new ArrayList<>();
                    tempMsg = "";
                }
                if (checkImgPage(getMsg)) { // 标准一包图片
                    doGetCopy(getMsg);
                } else { // 中断包的处理
                    tempMsg = tempMsg + getMsg;
                    if (checkImgPage(tempMsg)) { // 中断包拼接验证通过
                        doGetCopy(tempMsg);
                        tempMsg = "";
                    }
                }
            }
        } catch (Exception e) {
            tvCopyPhotoBackMsg.setText("表具数据接收错误 请重试");
        }
    }

    private void doGetCopy(String getMsg) {
        pageCurrent = Integer.parseInt(getMsg.substring(6, 8), 16); // 获取当前包号
        tempPages.add(pageCurrent);// 存储
        // Log.i("pageCurrent", tempPages.toString());
        if (pageCurrent < pageConut) { // 不是最后一包
            ImgString += getMsg;
            ImgString += ";";
            gettimes++;
            pgbCopyPhoto.incrementProgressBy(1);
        } else if (pageCurrent == pageConut) {// 最后一包
            ImgString += getMsg;
            ImgString += ";";
            if (gettimes == pageConut) {// 数据完整
                pgbCopyPhoto.incrementProgressBy(1);
                tvCopyPhotoBackMsg.setText("成功 开始上传服务器识别");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            } else {
                tvCopyPhotoBackMsg.setText("数据不完整 补帧中");
                checkFill();
            }
        }
    }

    private void fillCopy(String getMsg) {

        LogUtil.i("fillCopy", getMsg);

        if (getMsg.length() == 116) {
            int page = Integer.parseInt(getMsg.substring(6, 8), 16); // 获取当前包号
            page = page - 1;
            ImgString = ImgString.substring(0, 117 * page) + getMsg + ";" + ImgString.substring(117 * page);
            isFill = false;
            pgbCopyPhoto.incrementProgressBy(1);
            // 再次检测
            if (!checkFill()) {
                // Log.i("filled", ImgString);
                tvCopyPhotoBackMsg.setText("成功 开始上传服务器识别");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            }
        } else if (getMsg.length() == 484) {
            int page = Integer.parseInt(getMsg.substring(6, 8), 16); // 获取当前包号
            page = page - 1;
            ImgString = ImgString.substring(0, 485 * page) + getMsg + ";" + ImgString.substring(485 * page);
            isFill = false;
            pgbCopyPhoto.incrementProgressBy(1);
            // 再次检测
            if (!checkFill()) {
                // Log.i("filled", ImgString);
                tvCopyPhotoBackMsg.setText("成功 开始上传服务器识别");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            }
        }
    }

    private boolean checkFill() {
        for (int i = 1; i <= pageConut; i++) {
            if (!tempPages.contains(i)) { // 不存在数组中的，需补帧
                isFill = true;
                fill(Integer.toHexString(i));
                tempPages.add(i);
                // Log.i("pageCurrent", tempPages.toString());
                return true;
            }
        }
        return false;
    }

    long lastGetMessageTime = 0;
    private CountDownTimer timer;
    // 从BluetoothChatService处理程序获得信息返回
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            tvPhotoBtInfo.setText(getString(R.string.title_connected_to) + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            // 蓝牙连接后自动开始抄表
                            // wakeUp(meterNo);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            tvPhotoBtInfo.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            tvPhotoBtInfo.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // 构建一个字符串有效字节的缓冲区
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    myMessage = myMessage + "\n\n\n\n" + currentTimeMillis() + "收到命令\n" + readMessage;
                    lastGetMessageTime = System.currentTimeMillis();
                    LogUtil.i("收到数据" + readMessage);

                    if (timer != null) {
                        timer.cancel();
                    }
                    //计时15秒
                    timer = new CountDownTimer(15000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvTime.setText("等待超时时间:" + (int) (millisUntilFinished / 1000) + "s");

                        }

                        @Override
                        public void onFinish() {
                            LogUtil.i("结束计时");
                            tvTime.setText("即将抄取下一只表");
                            //时间到
                            doNext();
                        }
                    };
                    timer.start();
                    if (readMessage.equals("16161616")) {
                        if (!isMaintenance) {
                            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                                tvCopyPhotoBackMsg.setText("表具已唤醒正在抄表");
                                query();
                            } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) {
                                tvCopyPhotoBackMsg.setText("表具已唤醒正在设置");
                                isMaintenance = true;
                                maintenance();
                            }
                            break;
                        } else {
                            tvCopyPhotoBackMsg.setText("表具已唤醒开始设置");
                            maintenance();
                            break;
                        }
                    }
                    isCopy = true;
                    if (!isMaintenance) {
                        if (!isFill) {
                            getCopy(readMessage);
                        } else {
                            fillCopy(readMessage);
                        }
                    } else {
                        if (readMessage.length() == 32) {
                            if (readMessage.substring(26, 28).equals("ff")) {
                                tvCopyPhotoBackMsg.setText("图片位置调整成功");
                            } else {
                                tvCopyPhotoBackMsg.setText("图片位置调整失败");
                            }
                        } else if (readMessage.length() == 38) {
                            tvCopyPhotoBackMsg.setText("表计频点设置成功");
                        } else {
                            tvCopyPhotoBackMsg.setText("设置失败");
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_SUCCESS:
                    tvCopyPhotoBackMsg.setText("抄表完成 请核对识别结果");
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_FAILURT:
                    if (upCount < 1) {
                        tvCopyPhotoBackMsg.setText("数据上传失败 重试中");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String postData = firstString + ";" + ImgString;
                        getCopyDataPhoto(postData, meterTypeNo);
                        upCount++;
                    } else {
                        tvCopyPhotoBackMsg.setText("数据上传失败 请检查网络");
                    }
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_ERROR:
                    tvCopyPhotoBackMsg.setText("数据解析失败 请尝试重抄");
                    break;

                //直接下一个
                case MESSAGE_COPYCANT:
                    tvCopyPhotoBackMsg.setText("抄表失败表具无响应");
//                    doNext();
                    break;
                case MESSAGE_SERVER_CONNECT_SUCCESS:
                    // 检测是否能自动连接蓝牙
                    String address = preferenceBiz.getDeviceAddress();
                    if (address != null) {
                        connectDevice(address, true);
                    }
                    break;
                case MESSAGE_SERVER_CONNECT_FAILURT:
                    new AlertDialog.Builder(HtCopyPhotoActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog)
                            .setTitle("网络错误")
                            .setMessage("连接识别服务器失败，请检查。")
                            .setCancelable(false)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    }).show();
                    break;
            }
        }
    };

}
