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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.bll.CopyBiz;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.entity.CopyData;
import com.pl.gassystem.CopyResultActivity;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtGetMessage;
import com.pl.gassystem.bean.ht.HtGetMessageChangeBookNoOrCumulant;
import com.pl.gassystem.bean.ht.HtGetMessageQueryParameter;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.bean.ht.HtSendMessageChange;
import com.pl.gassystem.bean.ht.HtSendMessageSetNewKey;
import com.pl.gassystem.bean.ht.HtSendMessageSetParameter;
import com.pl.gassystem.command.HtCommand;
import com.pl.gassystem.utils.CalendarUtils;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.SPUtils;
import com.pl.utils.GlobalConsts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.tvNeedNum)
    TextView tvNeedNum;
    @BindView(R.id.tvNowNum)
    TextView tvNowNum;
    @BindView(R.id.tvTime)
    TextView tvTime;
    CountDownTimer timer;
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
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String commandType;//命令类型
    private String bookNo;//单抄 表号

    private String copyType = "";//抄表方式为群抄还是单抄
    private ArrayList<String> bookNos;//一大堆表号

    private int num = 0;//当前返回的数据


    //设置表号需要的参数
    private String changeType;//修改类型
    private String newBookNo;//新号
    private String cumulant;//累计量

    //批量设置参数所需要的参数
    private String setYinZi = "";
    private String setXinDao = "";
    private String setDongJieRi = "";
    private String setKaiChuangShiJian = "";
    private boolean isSetYinZi;

    //设置密钥所需要的参数
    private String newKey = "";

    //群抄参数设置
    private int needCopyNum = 1;//循环抄表次数
    private int nowCopyNum = 1;//当前发送命令次数
    private int waitTime = 0;//每次抄表的等待时间
    private int copySize = 0;//抄表数量


    private int wakeUpTime = 6000;//唤醒时间


    private CopyBiz copyBiz;


    //扩频因子和信道
    private String nowYinZi = "09";
    private String nowXinDao = "14";


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
                | commandType.equals(HtSendMessage.COMMAND_TYPE_CLOSE_DOOR)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {

            bookNo = getIntent().getStringExtra("bookNo");
            tvLoadingAll.setText("1");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {
            //抄表 冻结量抄表
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
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)) {
            //改表号功能
            bookNo = getIntent().getStringExtra("nowBookNo");
            newBookNo = getIntent().getStringExtra("newBookNo");
            cumulant = getIntent().getStringExtra("cumulant");
            changeType = getIntent().getStringExtra("changeType");//修改类型
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)) {
            bookNos = getIntent().getStringArrayListExtra("bookNos");
            setYinZi = getIntent().getStringExtra("yinzi");
            setXinDao = getIntent().getStringExtra("xindao");
            setDongJieRi = getIntent().getStringExtra("dongjieri");
            setKaiChuangShiJian = getIntent().getStringExtra("kaichuangshijian");
            isSetYinZi = getIntent().getBooleanExtra("isSetYinZi", false);
            LogUtil.i("批量设置参数" + setYinZi + "===" + setXinDao + "===" + setDongJieRi + "===" + setKaiChuangShiJian + "===" + isSetYinZi);
            tvLoadingAll.setText(bookNos.size() + "");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)) {
            bookNos = getIntent().getStringArrayListExtra("bookNos");
            newKey = getIntent().getStringExtra("newKey");
        }
        //计算
        if (bookNos.size() != 0) {
            LogUtil.i("杭天抄表", "抄表个数" + bookNos.size());
            copySize = bookNos.size();
            //计算抄表次数
            if (copySize % 20 == 0) {
                //是20的整数倍
                needCopyNum = copySize / 20;
            } else {
                needCopyNum = copySize / 20 + 1;
            }
            LogUtil.i("杭天抄表", "需要下发命令次数" + needCopyNum);
            tvNeedNum.setText(needCopyNum + "");
        } else {
            tvNeedNum.setText("1");
        }


        mChatService = new BluetoothChatService(this, mHandler);//蓝牙可用则开启一个蓝牙服务

        copyBiz = new CopyBiz(getContext());
        //把需要抄表的 都设置为未抄
        for (int i = 0; i < bookNos.size(); i++) {
            copyBiz.ChangeCopyState(bookNos.get(i), 0, "05");
        }

        nowXinDao = (String) SPUtils.get(getContext(), "HtKuoPinXinDao", "14");
        nowYinZi = (String) SPUtils.get(getContext(), "HtKuoPinYinZi", "09");
        //10进制转16进制
        String nowXinDao2 = Integer.toHexString(Integer.parseInt(nowXinDao));
        if (nowXinDao2.length() == 1) {
            nowXinDao = "0" + nowXinDao2;
        } else {
            nowXinDao = nowXinDao2;
        }
        String nowYinZi2 = Integer.toHexString(Integer.parseInt(nowYinZi));
        if (nowYinZi2.length() == 1) {
            nowYinZi = "0" + nowYinZi2;
        } else {
            nowYinZi = nowYinZi2;
        }
        LogUtil.i("当前因子", "nowXinDao=" + nowXinDao + "nowYinZi=" + nowYinZi);

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


    // 发送蓝牙消息
    private void sendMessage(String message) {
        // 检查连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);

            //蓝牙命令发送了我就开始倒计时
            //计算本次超时时间
            int countDownTime = 0;

            //间隔上传时间
            int intervalTime = 800;
            //唤醒周期
            int wakeCycle = 6000;
            //获取扩频因子
            String yinzi = (String) SPUtils.get(getContext(), "HtKuoPinYinZi", "09");
            assert yinzi != null;
            switch (yinzi) {
                case "07":
                    intervalTime = 200;
                    wakeCycle = 3000;
                    break;
                case "08":
                    intervalTime = 400;
                    wakeCycle = 4000;
                    break;
                case "09":
                    intervalTime = 800;
                    wakeCycle = 6000;
                    break;
                case "10":
                    intervalTime = 1600;
                    wakeCycle = 8000;
                    break;
                case "11":
                    intervalTime = 800;
                    wakeCycle = 10000;
                    break;
                case "12":
                    intervalTime = 6400;
                    wakeCycle = 12000;
                    break;
            }

            if (copySize == 0) {
                countDownTime = 200; //说明是单抄只传了 bookNo过来
            } else if (copySize > 20) {
                countDownTime = 200 + 20 * intervalTime;
            } else {
                countDownTime = 200 + 20 * copySize;
            }

            timer = new CountDownTimer(countDownTime + wakeCycle + 2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTime.setText((millisUntilFinished / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    //结束了 如果有就进行下一次抄表任务
                    tvTime.setText("时间到啦");
                    //发送下一条命令
                    nowCopyNum++;
                    if (nowCopyNum <= needCopyNum) {
                        createCommand(nowCopyNum);
                        tvNowNum.setText(nowCopyNum + "");
                    } else {
                        if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN) | commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)) {
                            Intent intent = new Intent(getContext(), CopyResultActivity.class);
                            intent.putExtra(GlobalConsts.EXTRA_COPYRESULT_TYPE, GlobalConsts.RE_TYPE_SHOWALL);
                            intent.putExtra("meterNos", bookNos);
                            intent.putExtra("meterTypeNo", "05");
                            startActivity(intent);
                            finish();
                        }

                        tvTime.setText("抄表结束");
                    }
                }
            };
            timer.start();

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
                    createCommand(1);
                } else {
                    showToast("请先连接蓝牙");
                }

                break;
            case R.id.btnCopyingStop:

                break;
        }
    }


    /**
     * 创建一条修改表号的命令
     */
    private void createChangeBookNoOrCumulantCommand() {
        HtSendMessageChange htSendMessage = new HtSendMessageChange();

        htSendMessage.setCommandType(commandType);//设置命令类型
        htSendMessage.setBookNo(bookNo);
        htSendMessage.setNewBookNo(newBookNo);
        htSendMessage.setWakeUpTime(wakeUpTime);
        htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessage.setKuoPinYinZi(nowYinZi);//如果是查询参数 那么信道号固定为00
        htSendMessage.setKuoPinXinDao(nowXinDao);
        LogUtil.i("修改累计量" + cumulant);
        htSendMessage.setCumulant(cumulant);
        htSendMessage.setChangeType(changeType);

        String message = HtCommand.encodeHangTianChangeBookNoOrCumulant(htSendMessage);
        sendMessage(message);

    }


    private void createCommand(int nowNum) {

        switch (commandType) {
            case HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT:
                createChangeBookNoOrCumulantCommand();
                break;
            case HtSendMessage.COMMAND_TYPE_SET_PARAMETER:
                createSetParameterCommand(nowNum);
                break;
            case HtSendMessage.COMMAND_TYPE_SET_KEY:
                createSetKeyCommand(nowNum);
                break;
            default:
                HtSendMessage htSendMessage = new HtSendMessage();
                htSendMessage.setCommandType(commandType);//设置命令类型

                if (copyType.equals(HtSendMessage.COPY_TYPE_GROUP)) {
                    //群抄
                    htSendMessage.setBookNo("FFFFFFFF");//设置表号

                    htSendMessage.setSetTime(CalendarUtils.getHtTime()); // 17 09 15 14 10 00

                    htSendMessage.setWakeUpTime(wakeUpTime);

                    htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_GROUP);

                    htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
                    htSendMessage.setKuoPinYinZi(nowYinZi);//如果是查询参数 那么信道号固定为00
                    htSendMessage.setKuoPinXinDao(nowXinDao);

                    List<String> mBookNos = new ArrayList<>();
                    mBookNos.addAll(bookNos.subList((nowNum - 1) * 20, getLast(nowNum)));
                    LogUtil.i("截取列表", (nowNum - 1) * 20 + "==>" + getLast(nowNum));
                    htSendMessage.setBookNos(mBookNos);

                } else {
                    // 单抄 查看阀门状态 开关阀门 都只用执行以下代码即可

                    htSendMessage.setBookNo(bookNo);//设置表号

                    htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);

                    htSendMessage.setWakeUpTime(wakeUpTime);

                    htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_SINGLE);

                    //设置扩频因子和信道
                    htSendMessage.setKuoPinYinZi(nowYinZi);
                    if (commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {
                        htSendMessage.setKuoPinXinDao("00");//如果是查询参数 那么信道号固定为00
                    } else {
                        htSendMessage.setKuoPinXinDao(nowXinDao);
                    }
                }
                String message = HtCommand.encodeHangTian(htSendMessage);
                sendMessage(message);
                break;
        }
    }

    /**
     * 创建一条批量修改密钥的命令
     */
    private void createSetKeyCommand(int nowNum) {
        HtSendMessageSetNewKey htSendMessageSetNewKey = new HtSendMessageSetNewKey();
        htSendMessageSetNewKey.setCommandType(commandType);//设置命令类型
        htSendMessageSetNewKey.setBookNo("FFFFFFFF");
        htSendMessageSetNewKey.setWakeUpTime(wakeUpTime);
        htSendMessageSetNewKey.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessageSetNewKey.setKuoPinYinZi(nowYinZi);
        htSendMessageSetNewKey.setKuoPinXinDao(nowXinDao);

        List<String> mBookNos = new ArrayList<>();
        mBookNos.addAll(bookNos.subList((nowNum - 1) * 20, getLast(nowNum)));
        htSendMessageSetNewKey.setBookNos(mBookNos);

        //设置新的密钥
        htSendMessageSetNewKey.setNewKey(newKey);

        String message = HtCommand.encodeHangTianSetKey(htSendMessageSetNewKey);
        sendMessage(message);
    }

    /**
     * 创建一条批量修改表信息的命令
     */
    private void createSetParameterCommand(int nowNum) {

        HtSendMessageSetParameter htSendMessageSetParameter = new HtSendMessageSetParameter();
        htSendMessageSetParameter.setCommandType(commandType);//设置命令类型
        htSendMessageSetParameter.setBookNo("FFFFFFFF");
        htSendMessageSetParameter.setWakeUpTime(wakeUpTime);
        htSendMessageSetParameter.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessageSetParameter.setKuoPinYinZi(nowYinZi);
        htSendMessageSetParameter.setKuoPinXinDao(nowXinDao);

        //获得抄表
        List<String> mBookNos = new ArrayList<>();
        mBookNos.addAll(bookNos.subList((nowNum - 1) * 20, getLast(nowNum)));
        htSendMessageSetParameter.setBookNos(mBookNos);


        //修改的参数
        htSendMessageSetParameter.setNeedKuoPinYinZi(isSetYinZi);
        String mySetYinZi = Integer.toHexString(Integer.parseInt(setYinZi));
        if (mySetYinZi.length() == 1) {
            htSendMessageSetParameter.setKuo_pin_yin_zi("0" + mySetYinZi);
        } else {
            htSendMessageSetParameter.setKuo_pin_yin_zi(mySetYinZi);
        }
        String mySetXinDao = Integer.toHexString(Integer.parseInt(setXinDao));
        if (mySetXinDao.length() == 1) {
            htSendMessageSetParameter.setKuo_pin_xin_dao("0" + mySetXinDao);
        } else {
            htSendMessageSetParameter.setKuo_pin_xin_dao(mySetXinDao);
        }
        htSendMessageSetParameter.setDong_jie_ri(setDongJieRi);
        htSendMessageSetParameter.setKai_chuang_qi_zhi_shi_jian(setKaiChuangShiJian);
        String message = HtCommand.encodeHangTianSetParameter(htSendMessageSetParameter);
        sendMessage(message);
    }

    private int getLast(int nowNum) {

        if (nowNum * 20 < bookNos.size()) {
            return nowNum * 20;
        } else {
            return bookNos.size();
        }
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
                            createCommand(1);
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

        if (commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {
            //查询表参数
            HtGetMessageQueryParameter htGetMessageQueryParameter = HtCommand.readQueryParameterMessage(readMessage);
            LogUtil.i("抄表结果", htGetMessageQueryParameter.getResult());
            num++;
            tvLoadingCount.setText(num + "");
            tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessageQueryParameter.getResult());
        } else if (
                commandType.equals(HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)
                        | commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)
                        | commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)
                ) {
            //修改表号 或者累计量 修改密钥
            HtGetMessageChangeBookNoOrCumulant htGetMessageQueryParameter = HtCommand.readChangeBookNoOrCumulantMessage(readMessage);
            assert htGetMessageQueryParameter != null;
            LogUtil.i("抄表结果", htGetMessageQueryParameter.getResult());
            num++;
            tvLoadingCount.setText(num + "");
            tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessageQueryParameter.getResult());
        } else {
            if (readMessage.length() > 25) {
                HtGetMessage htGetMessage = HtCommand.readMessage(readMessage);
                if (htGetMessage != null) {
                    LogUtil.i("抄表结果", htGetMessage.getResult());
                    num++;
                    tvLoadingCount.setText(num + "");
                    tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessage.getResult());
                    //
                    LogUtil.i("嗯呐", commandType);

                    if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL) | commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {
                        CopyData copyData = getCopyDate(htGetMessage);
                        copyBiz.addCopyData(copyData);
                        // 修改抄表状态
                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, "05");
                    }
                }
            }
        }


    }

    //把接收到的命令转换为CopyData
    private CopyData getCopyDate(HtGetMessage htGetMessage) {

        CopyData copyData = new CopyData();

        copyData.setMeterNo(htGetMessage.getBookNo());
        copyData.setElec(htGetMessage.getVoltage());
        copyData.setdBm(htGetMessage.getSignal());

        copyData.setRemark(htGetMessage.getCommandType() + "\n" + htGetMessage.getValveState());
        copyData.setCopyTime(df.format(new Date()));

        String CopyValue = htGetMessage.getCopyValue().substring(0, 4) + "." + htGetMessage.getCopyValue().substring(4, 8);
        CopyValue = Double.parseDouble(CopyValue) + "";
        copyData.setCurrentShow(CopyValue);
        return copyData;
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
