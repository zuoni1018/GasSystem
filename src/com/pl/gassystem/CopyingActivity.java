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
    // ָ���б�
    public static final String CMD_LIST = "wireless_cmd.xml";
    // ��������
    private static int countAll = 0;
    // ��ǰ�ڼ�̨
    private static int loadingcount = 0;
    // ������־
    private static boolean isCopy;
    // �������
    private static int copyTimes = 0;
    // ��ʱ��ʱ֪ͨ
    public static final int MESSAGE_COPYCANT = 6;
    private PreferenceBiz preferenceBiz;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat copydf = new SimpleDateFormat("yyMMddHHmm");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat copydf6 = new SimpleDateFormat("yyMMddHHmmss");
    // ����ģʽ
    private static int copyType;
    // ִ��ģʽ
    private static int operationType;
    private static String cmdtype;
    // �жϱ�ʶ
    private static boolean isStop;
    // ����ģʽ
    private static String runMode = "1";
    // �������
    private static int intervalTime; // ���ʱ��
    private static String wakeupTime; // ����ʱ��
    private static int copyWait; // ����ȴ�
    private static int wakeupWait; // ���ѵȴ�
    private static int repeatCount;// ��������
    private static int loopCount = 0; //ѭ������
    private static boolean isWakeUp = false; //�ظ����ѱ�ʶ

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

        addListener();
        copyBiz = new CopyBiz(this);
        preferenceBiz = new PreferenceBiz(this);
        setBiz = new SetBiz(this);
        // ��ʼ������
        runMode = setBiz.getRunMode();
        intervalTime = setBiz.getIntervalTime();
        wakeupTime = setBiz.getWakeupTime();
        copyWait = setBiz.getCopyWait();
        repeatCount = setBiz.getRepeatCount();
        wakeupWait = (Integer.parseInt(wakeupTime, 16) * 100 + copyWait) / 100;
        copyWait = copyWait / 10;
        // ��ʼ����������
        meterTypeNo = getIntent().getStringExtra("meterTypeNo"); // �������
        // if(meterTypeNo.equals("04")){ //IC������δ���ǰ��ֱ���˳�����
        // finish();
        // }
        meterNos = getIntent().getStringArrayListExtra("meterNos"); // ����飨����Ҳ���飩
        AllMeterNo = meterNos;
        unCopyMeterNos = new ArrayList<String>();
        copyType = getIntent().getIntExtra("copyType", 1); // ����ʽ��1������2Ⱥ����
        operationType = getIntent().getIntExtra("operationType", 1); // ��������
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
        // ����Ƿ����Զ���������
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
        tvTitlebar_name.setText("���ڲ����������뿪�˽���");

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

    // ����
    // private void wakeUp(String commNumber){
    // sendMessage("aadd50" + commNumber); //����������Ϣ
    // tvCopyingBackMsg.setText("���ڻ��ѱ��");
    // }

    // ����
    private void query() {
        // ����ģʽ
        if (copyTimes == 0) {
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                if (loopCount != 0) {
                    tvCopyingBackMsg.setText("��ʼ��" + loopCount + "�β���");
                } else {
                    tvCopyingBackMsg.setText("��ʼ������...");
                }
            } else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
                tvCopyingBackMsg.setText("������");
            } else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
                tvCopyingBackMsg.setText("�ط���");
            } else if (operationType == GlobalConsts.COPY_OPERATION_BUY) {
                tvCopyingBackMsg.setText("Զ�̳�ֵ��");
            } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
                tvCopyingBackMsg.setText("�޸�ͨѶ����");
            } else if (operationType == GlobalConsts.COPY_OPERATION_PRICE) {
                tvCopyingBackMsg.setText("������");
            } else if (operationType == GlobalConsts.COPY_OPERATION_PWD) {
                tvCopyingBackMsg.setText("�޸�������");
            }
        }
        if (loadingcount < countAll) {
            String headMessage = "";
            String commNumber = meterNos.get(loadingcount);
            String meterName = copyBiz.getMeterNameByNo(commNumber);
            headMessage = "6808"; // ��ʼ��68 ����08
            // �жϸ��๤��ģʽ
            if (meterTypeNo.equals("05")) { // �����߱�
                // �жϳ���
                if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // ����FSK
                    headMessage += "14";
                } else {
                    headMessage += "19";
                }
                if (runMode.equals(GlobalConsts.RUNMODE_STANDARD)) { // ���ģʽ
                    String meterWIType = copyBiz.getMeterWITypeByNo(commNumber);
                    if (meterWIType == null || meterWIType.equals("") || meterWIType.equals("2")) { // LORA�Լ�Ĭ��
                        headMessage += "02";
                    } else if (meterWIType.equals("1")) { // FSK
                        headMessage += "01";
                    } else if (meterWIType.equals("3")) { // ����FSK
                        headMessage += "03";
                    } else if (meterWIType.equals("4")) { // �Ϻ�FSK
                        headMessage += "04";
                    }
                } else if (runMode.equals(GlobalConsts.RUNMODE_LORA) || runMode.equals(GlobalConsts.RUNMODE_ZHGT)) { // LORAģʽ����̩ģʽ
                    headMessage += "02";
                } else if (runMode.equals(GlobalConsts.RUNMODE_FSK)) { // FSK
                    headMessage += "01";
                } else if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // ����FSK
                    headMessage += "03";
                } else if (runMode.equals(GlobalConsts.RUNMODE_SHANGHAI)) { // �Ϻ�FSK
                    // headMessage += "04";
                }
            } else if (meterTypeNo.equals("04")) { // IC������
                if (runMode.equals(GlobalConsts.RUNMODE_FSK)) { // FSK
                    headMessage += "1731";
                } else {
                    headMessage += "1732"; // ����ѡ��FSKģʽ�⣬���಻��ѡʲô����LORA�����Ϻ�����û��IC�����߱�
                }
            }

            // ����ģʽ
            if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// ����
                if (loadingcount == 0 || isWakeUp == true) {
                    if (countAll == 1) {
                        headMessage += "01" + wakeupTime;
                    } else {
                        headMessage += "02" + wakeupTime;
                    }
                } else {
                    headMessage += "00" + wakeupTime;
                }
            } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// ����
                headMessage += "01" + wakeupTime;
            }

            // ��������ʼ
            tvLoadingComNum.setText(commNumber);
            tvLoadingName.setText(meterName);
            String message = "";
            HhProtocol cchmp = new HhProtocol();

            if (runMode.equals(GlobalConsts.RUNMODE_SHANGHAI)) { // �Ϻ�ģʽר��
                headMessage = ""; // ��������
                ArrayList<String> params = new ArrayList<String>();
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) { // ����
                    params.add(copydf.format(new Date()));
                } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) { // �޸�ͨѶ��
                    params.add(getIntent().getStringExtra("setParam1"));
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_REGNET) { // ��������
                    String state = getIntent().getStringExtra("state"); // ״̬��
                    String colNo = getIntent().getStringExtra("colNo"); // ������ID
                    String channelNo = getIntent().getStringExtra("channelNo"); // �ŵ���
                    params.add(state);
                    params.add(colNo);
                    params.add(channelNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_RELAYREGNET) { // �м���������
                    String relayMeterNo = getIntent().getStringExtra(
                            "relayMeterNo"); // �м̱�ID��
                    params.add(relayMeterNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_CANCELRELAYREGNET) { // ȡ���м���������
                    String relayMeterNo = getIntent().getStringExtra("relayMeterNo"); // �м̱�ID��
                    params.add(relayMeterNo);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_TESTMODE) { // ����ģʽ��������
                    String cmd = getIntent().getStringExtra("cmd"); // ������
                    // String date = copydf6.format(new Date()); //ʱ��
                    String date = getIntent().getStringExtra("date"); // ʱ��
                    params.add(cmd);
                    params.add(date);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_GETPAMARS) { // ���в�����ѯ����
                    String state = getIntent().getStringExtra("state"); // ״̬��
                    params.add(state);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_RESET) { // �ָ�������������
                    params.add(commNumber); // ���
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETKEY) {// �趨��Կ����
                    String key = getIntent().getStringExtra("key");// ��Կ
                    params.add(commNumber); // ���
                    params.add(key);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SAFEMODE) { // ��ȫģʽ��������
                    String cmd = getIntent().getStringExtra("cmd"); // ������
                    params.add(commNumber);
                    params.add(cmd);
                } else if (operationType == GlobalConsts.COPYSH_OPERATION_SETCOPYDAY) { // ���ó���ʱ���
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
                        Toast.makeText(getApplicationContext(), "ͨѶ��Ŵ���!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                String timeString = copydf.format(new Date());
                // ����ģʽ
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                    if (meterTypeNo.equals("05")) { // �����߱�
                        data.setCmdType("01"); // ��������
                        if (runMode.equals(GlobalConsts.RUNMODE_ZHGT)) {
                            data.setCmdType("81");
                        }
                        timeString += ";01030713";
                        data.setDataBCD(timeString);
                    } else if (meterTypeNo.equals("04")) { // IC������
                        data.setCmdType("06"); // ��������
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
                } else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {// ����ͨѶ��
                    if(runMode.equals(GlobalConsts.RUNMODE_ZHGT)){
                        //��̩
                        data.setCmdType("87");//87
                    }else {
                        //��ͨ
                        data.setCmdType("05");
                    }

                    String setParam1 = getIntent().getStringExtra("setParam1");
                    data.set();
                    data.setDataBCD(setParam1);
                } else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) { // ���ñ����
                    data.setCmdType("0b");
                    data.set();
                    data.setDataBCD(getIntent().getStringExtra("baseNum"));
                } else if (operationType == GlobalConsts.COPY_OPERATION_COPYFROZEN) { // ��ȡ������
                    data.setCmdType("81");
                }
                data.setTargetAddr(commNumber);
                if (meterTypeNo.equals("05")) {
                    if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // ����ģʽ
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
                    Toast.makeText(getApplicationContext(), "���ǲ�ѯ����!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        // TODO �Զ����ɵ� catch ��
                        e.printStackTrace();
                    }
                }
            }
            LogUtil.i("����", message);
            sendMessage(message); // ����������Ϣ
            // Log.i("msg", message);
            // tvCopyingBackMsg.setText(message);

            // �������
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                isCopy = false;
                // if (meterTypeNo.equals("05")) {
                if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// ����
                    if (loadingcount == 0 || isWakeUp == true) {
                        isWakeUp = false;
                        new Thread() {
                            @Override
                            public void run() {
                                int time = 0;
                                for (int i = 0; i < wakeupWait; i++) { // �ȴ�
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        // TODO �Զ����ɵ� catch ��
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
                                for (int i = 0; i < copyWait; i++) { // �ȴ�
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        // TODO �Զ����ɵ� catch ��
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
                } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// ����
                    new Thread() {
                        @Override
                        public void run() {
                            int time = 0;
                            for (int i = 0; i < wakeupWait; i++) { // �ȴ�
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    // TODO �Զ����ɵ� catch ��
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

    // ����������Ϣ
    private void sendMessage(String message) {
        LogUtil.i("�໰�",message);

        // �������
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            // Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
            // .show();
            return;
        }

        // ���ʵ���Ϸ��͵Ķ���
        if (message.length() > 0) {
            // �õ���Ϣ���ֽڣ�����bluetoothchatserviceд
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    private void addListener() {
        btnBtScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("��ʼ")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У�����ı��������ӣ�",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // ����DeviceListActivity�����豸����ɨ��
                    Intent intent = new Intent(CopyingActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }

            }
        });

        btnCopyingRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("��ʼ")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������",
                            Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("�����豸δ����")
                        || tvBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��",
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
        // �������û��������������.
        // setupChat��onActivityResult()��������
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
        // TODO �Զ����ɵķ������
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

    private void connectDevice(String address, boolean secure) {
        // // ����豸��ַ
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // ��������豸����
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // �������ӵ��豸
        mChatService.connect(device);
    }

    // ʹ���������ɼ�
    // private void ensureDiscoverable() {
    // if (mBluetoothAdapter.getScanMode() !=
    // BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
    // Intent discoverableIntent = new Intent(
    // BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    // discoverableIntent.putExtra(
    // BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// ��ɼ�ʱ��Ϊ300��
    // startActivity(discoverableIntent);
    // }
    // }

    // IC�����߳�������
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
        copyDataICRF.setCumulant(Cumulant);// �ۼ���
        String SurplusMoney = Integer.toString(Integer
                .parseInt(strarray[1], 10));
        if (SurplusMoney.length() > 2) {
            SurplusMoney = SurplusMoney.substring(0, SurplusMoney.length() - 2)
                    + "." + SurplusMoney.substring(SurplusMoney.length() - 2);
        } else {
            SurplusMoney = "0." + SurplusMoney;
        }
        copyDataICRF.setSurplusMoney(SurplusMoney);// ʣ����
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
        copyDataICRF.setOverZeroMoney(OverZeroMoney);// ������
        int BuyTimes = Integer.parseInt(strarray[3], 10);// �������
        copyDataICRF.setBuyTimes(BuyTimes);
        int OverFlowTimes = Integer.parseInt(strarray[4], 10);// �������
        copyDataICRF.setOverFlowTimes(OverFlowTimes);
        int MagAttTimes = Integer.parseInt(strarray[5], 10);// �Ź�������
        copyDataICRF.setMagAttTimes(MagAttTimes);
        int CardAttTimes = Integer.parseInt(strarray[6], 10);// ����������
        copyDataICRF.setCardAttTimes(CardAttTimes);
        copyDataICRF.setMeterState(Integer.parseInt(strarray[7], 16)); // ���״̬ת����16����INT�������ݿ�
        copyDataICRF.setCurrMonthTotal(Integer.toString(Integer.parseInt(
                strarray[8], 10)));// �����ۼ���
        copyDataICRF.setLast1MonthTotal(Integer.toString(Integer.parseInt(
                strarray[9], 10)));// �����ۼ���


        if (strarray.length > 10) {


            String unitPrice = strarray[10].substring(1);// ��ǰ���ۣ�ȥ��֡��ָ���"|"��
            unitPrice = Integer.toString(Integer.parseInt(unitPrice, 10));
            if (unitPrice.length() > 2) {
                unitPrice = unitPrice.substring(0, unitPrice.length() - 2) + "."
                        + unitPrice.substring(unitPrice.length() - 2);
            } else {
                unitPrice = "0." + unitPrice;
            }
            copyDataICRF.setUnitPrice(unitPrice);
            String accMoney = strarray[11].substring(0, 8);// �ۼ��������
            accMoney = Integer.toString(Integer.parseInt(accMoney, 10));
            if (accMoney.length() > 2) {
                accMoney = accMoney.substring(0, accMoney.length() - 2) + "."
                        + accMoney.substring(accMoney.length() - 2);
            } else {
                accMoney = "0." + accMoney;
            }
            copyDataICRF.setAccMoney(accMoney);
            String accBuyMoney = strarray[12].substring(0, 8);// �ۼƳ�ֵ���
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
        CurrentShow = CurrentShow.substring(1);// ȥ��F
        copyData.setCurrentShow(CurrentShow); // �ۼ���
        copyData.setMeterState(Integer.parseInt(dataString.substring(8, 10), 16));
        // ʹ��������
        return copyData;
    }

    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
    }

    // ���߱�������
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
        copyData.setCurrentShow(CurrentShow); // �ۼ���
        if (strarray.length > 2) {
            copyData.setMeterState(Integer.parseInt(strarray[1], 16)); // ���״̬ת����16����INT�������ݿ�
        }
        if (strarray.length > 3) {
            String dBm = strarray[3].substring(1);// �����ź�ǿ�ȣ�ȥ��֡��ָ���"|"��
            copyData.setdBm(dBm);
            String elec = strarray[4]; // ����
            copyData.setElec(elec);
        }
        copyData.setMeterName(tvLoadingName.getText().toString());
        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // ��ѯ��һ�γ����¼
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
        } else { // ��ʼֵ
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

    // �������߱�������
    private CopyData getCopyDataHuiZhou(String meterNo, CqueueData data) {
        CopyData copyData = new CopyData();
        copyData.setMeterNo(meterNo);
        String CurrentShow = Double.toString(data.getSumUseGas());
        copyData.setCurrentShow(CurrentShow); // �ۼ���
        copyData.setMeterState(Integer.parseInt(data.getMeterState(), 16)); // ���״̬ת����16����INT�������ݿ�
        copyData.setMeterName(tvLoadingName.getText().toString());
        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // ��ѯ��һ�γ����¼
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
        } else { // ��ʼֵ
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
                            tvBtInfo.setText(getString(R.string.title_connected_to)
                                    + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                                // TODO �Զ����ɵ� catch ��
                                e1.printStackTrace();
                            }
                            // �������Ӻ��Զ���ʼ����
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
//				 // ����һ���ַ���������
//				 String writeMessage = new String(writeBuf);
//				 LogUtil.i("����,bluepost", writeMessage);
                    break;
                case MESSAGE_READ:
                    if (isStop) { // �ֹ�ֹͣ�������Ͽ�����ֹһ�ж�����
                        break;
                    }
                    try {
                        byte[] readBuf = (byte[]) msg.obj;
                        // ����һ���ַ�����Ч�ֽڵĻ�����
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        // Log.i("blueget", readMessage);
                        if (readMessage.length() < 12) {
                            tvCopyingBackMsg.setText("�������ݴ���");
                            break;
                        }
                        readMessage = readMessage.substring(12);
                        HhProtocol hhP = new HhProtocol();
                        CqueueData msgDecode = new CqueueData(); // Э����
                        if (meterTypeNo.equals("05")) { // �����߱�
                            if (runMode.equals(GlobalConsts.RUNMODE_HUIZHOU)) { // ����ģʽ
                                msgDecode = hhP.decodeHuiZhou(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else if (runMode
                                    .equals(GlobalConsts.RUNMODE_SHANGHAI)) { // �Ϻ�ģʽ
                                msgDecode = hhP.decodeShangHai(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else {
                                msgDecode = hhP.decode(readMessage, cmdtype); // ���
                            }
                        } else if (meterTypeNo.equals("04")) { // IC�����߱�
                            msgDecode = hhP.decodeIC(readMessage);
                        }

                        if (msgDecode == null) {
                            tvCopyingBackMsg.setText("�������ݴ���");
                            break;
                        } else {
                            // ���շ��ص���Ϣ
                            String dataString = msgDecode.getCmdData();
                            // tvCopyingBackMsg.setText("");
                            // String cmdtype = msgDecode.getCmdType();
                            String loadingComNum = tvLoadingComNum.getText()
                                    .toString();
                            String loadingComNumActual = loadingComNum; // ����10λ���ж�
                            if (loadingComNumActual.length() > 10) {
                                loadingComNumActual = loadingComNumActual
                                        .substring(loadingComNumActual.length() - 10);
                            }
                            switch (cmdtype) {
                                case "01": // ���߱�������
                                    if (msgDecode.getSourceAddr().equals(
                                            loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CopyData copyData;
                                        if (runMode
                                                .equals(GlobalConsts.RUNMODE_HUIZHOU)) { // ����ģʽ
                                            copyData = getCopyDataHuiZhou(
                                                    loadingComNum, msgDecode);
                                        } else {
                                            copyData = getCopyData(loadingComNum, dataString);
                                        }
                                        copyBiz.addCopyData(copyData);
                                        // �޸ĳ���״̬
                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// ��ʶΪ�ѳ���
                                        copyTimes = 0;
                                        // �ж��Ƿ������
                                        if (loadingcount < countAll) {
                                            query();
                                        } else {
                                            // ���������ת
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                // TODO �Զ����ɵ� catch ��
                                                e.printStackTrace();
                                            }
                                            //�ж��Ƿ���Ҫѭ�����ڶ���
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
                                case "06": // IC�����߳�������
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CopyDataICRF copyDataICRF;
                                        copyDataICRF = getCopyDataICRF(loadingComNum, dataString);
                                        copyBiz.addCopyDataICRF(copyDataICRF);
                                        // �޸ĳ���״̬
                                        copyBiz.ChangeCopyState(copyDataICRF.getMeterNo(), 1, meterTypeNo);
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// ��ʶΪ�ѳ���
                                        copyTimes = 0;
                                        // �ж��Ƿ������
                                        if (loadingcount < countAll) {
                                            query();
                                        } else {
                                            // ���������ת
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                // TODO �Զ����ɵ� catch ��
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
                                case "02":// ����
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("����ѳɹ�����");
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    // if(dataString.equals("00")){
                                    // tvCopyingBackMsg.setText("����ѳɹ�����");
                                    // // ���ȸ���
                                    // loadingcount++;
                                    // tvLoadingCount.setText(loadingcount + "");
                                    // pgbCopying.incrementProgressBy(1);
                                    // }
                                    break;
                                case "03":// �ط�
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("����ѳɹ��ط�");
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "05":// �޸�ͨѶ��
                                    if (readMessage.length() > 2) {
                                        tvCopyingBackMsg.setText("�޸�ͨѶ�ųɹ�");
                                        tvLoadingComNum.setText(msgDecode.getSourceAddr());
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08":// �����޸�ͨѶ��
                                    if (readMessage.length() > 1) {
                                        tvCopyingBackMsg.setText("�޸�ͨѶ�ųɹ�");
                                        tvLoadingComNum.setText(msgDecode.getSourceAddr());
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "0B": // �������ñ����
                                    if (readMessage.length() > 1) {
                                        tvCopyingBackMsg.setText("���ñ�����ɹ�");
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "81":// ���ݳ�ȡ������
                                    if (msgDecode != null
                                            && msgDecode.getDataBCD().length() > 7) {
                                        tvCopyingBackMsg.setTextSize(14);
                                        tvCopyingBackMsg.setText("����ʱ��:"
                                                + msgDecode.getDataBCD()
                                                .substring(0, 2)
                                                + " ������:"
                                                + msgDecode.getDataBCD()
                                                .substring(2, 8));
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "0785": // �Ϻ���������
                                    if (msgDecode.getSourceAddr().equals(
                                            loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CopyData copyData = getCopyDataShangHai(
                                                loadingComNum, dataString);
                                        copyBiz.addCopyData(copyData);
                                        // �޸ĳ���״̬
                                        copyBiz.ChangeCopyState(copyData.getMeterNo(),
                                                1, meterTypeNo);
                                        // ���ȸ���
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                        isCopy = true;// ��ʶΪ�ѳ���
                                        copyTimes = 0;
                                        // �ж��Ƿ������
                                        if (loadingcount < countAll) {
                                            query();
                                        } else { // ���������ת
                                            try {
                                                Thread.sleep(300);
                                            } catch (InterruptedException e) {
                                                // TODO �Զ����ɵ� catch ��
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
                                case "0283":// �Ϻ��޸ı�Ʊ��
                                    tvCopyingBackMsg.setText("�޸�ͨѶ�ųɹ�");
                                    tvLoadingComNum.setText(msgDecode.getSourceAddr());
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0287": // �Ϻ����ط�
                                    tvCopyingBackMsg.setText("��߲����ɹ�");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0288":// �Ϻ����ó���ʱ���
                                    tvCopyingBackMsg.setText("���ó���ʱ��γɹ�");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "0489": // �Ϻ���ѯ����ʱ��
                                    if (dataString != null && dataString.length() > 3) {
                                        tvCopyingBackMsg.setText("��ʼ��:"
                                                + dataString.substring(0, 2) + " ������:"
                                                + dataString.substring(2, 4));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "03a8": // ��������
                                    if (dataString != null && dataString.length() > 1) {
                                        tvCopyingBackMsg
                                                .setText("����ע����:" + dataString);
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08a9":// �м���������
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("�������:"
                                                + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08ab":// ȡ���м�����
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("ִ�н��:"
                                                + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "03ac":// ����ģʽ����
                                    if (dataString != null && dataString.length() > 1) {
                                        if (dataString.equals("00")) {
                                            tvCopyingBackMsg.setText("���˳�����ģʽ");
                                        } else {
                                            tvCopyingBackMsg.setText("�ѽ������ģʽ");
                                        }
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "09ae":// ���в�����ѯ
                                    if (dataString != null && dataString.length() > 13) {
                                        tvCopyingBackMsg.setText("�ŵ���:"
                                                + dataString.substring(0, 2)
                                                + " ������ID��:"
                                                + dataString.substring(2, 12)
                                                + " ����״̬:"
                                                + dataString.substring(12, 14));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "07af":// �ָ���������
                                    tvCopyingBackMsg.setText("�ָ��������óɹ�");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "17a1":// �趨��Կ
                                    tvCopyingBackMsg.setText("��Կ�趨�ɹ�");
                                    loadingcount++;
                                    tvLoadingCount.setText(loadingcount + "");
                                    pgbCopying.incrementProgressBy(1);
                                    break;
                                case "08a2":// ��ȫģʽ����
                                    if (dataString != null && dataString.length() > 11) {
                                        String cmd = dataString.substring(10, 12);
                                        if (cmd.equals("00")) {
                                            tvCopyingBackMsg.setText("������Ϊ����ģʽ");
                                        } else {
                                            tvCopyingBackMsg.setText("������Ϊ��ȫģʽ");
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
                    // ���������豸������
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    // Toast.makeText(getApplicationContext(),
                    // "������ " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                    // .show();
                    break;
                // case MESSAGE_TOAST:
                // Toast.makeText(getApplicationContext(),
                // msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                // .show();
                // break;
                case MESSAGE_COPYCANT: // δ��������
                    if (isStop) { // �ֹ�ֹͣ����ֹһ�ж�����
                        break;
                    }
                    if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// ����
                        if (loadingcount != 0 && copyTimes < repeatCount) { // �ظ����2��
                            tvCopyingBackMsg.setText("�������Ӧ,���»��ѳ���");
                            copyTimes++;
                            isWakeUp = true;//���»���
                            query();
                        } else { // ����
                            String loadingComNum = tvLoadingComNum.getText()
                                    .toString();
                            copyBiz.ChangeCopyState(loadingComNum, 0, meterTypeNo);// �޸�״̬Ϊδ����
                            loadingcount++;
                            tvLoadingCount.setText(loadingcount + "");
                            pgbCopying.incrementProgressBy(1);
                            copyTimes = 0;
                            unCopyMeterNos.add(loadingComNum);//�����δ����ʱ����
                            // ����һ̨
                            if (loadingcount < countAll) {
                                query();
                            } else {
                                // ���������ת
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    // TODO �Զ����ɵ� catch ��
                                    e.printStackTrace();
                                }
                                //�ж��Ƿ���Ҫѭ�����ڶ���
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
                    } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// ����
                        copyBiz.ChangeCopyState(meterNos.get(loadingcount), 0,
                                meterTypeNo);// �޸�״̬Ϊδ����
                        loadingcount++;
                        tvLoadingCount.setText(loadingcount + "");
                        pgbCopying.incrementProgressBy(1);
                        copyTimes = 0;
                        // ����һ̨
                        if (loadingcount < countAll) {
                            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                                tvCopyingBackMsg.setText("����ʧ�ܣ�������һ̨����");
                            }
                            query();
                        } else {
                            // ���������ת
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                // TODO �Զ����ɵ� catch ��
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
