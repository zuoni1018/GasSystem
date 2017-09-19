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
 * ������ ȥ�������
 * �����������Ҫ��ת������������
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



    private CtCopyDataDao ctCopyDataDao;
    private CtCopyDataICRFDao ctCopyDataICRFDao;

    private  String collectorNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("��������");
        addOnTouchListener();
        addListener();
        ctCopyDataDao=new CtCopyDataDao(getContext());
        ctCopyDataICRFDao=new CtCopyDataICRFDao(getContext());
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
        collectorNo=getIntent().getStringExtra("collectorNo");
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
            builder.setTitle("��ʾ");
            builder.setMessage("�Ƿ��˳�������������?");
            builder.setPositiveButton("�ǵ�", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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


    //////////////////////////////////////  �����Ǵ�ԭ����������������   ///////////////////////////


    /**
     * ���ô����¼�
     */
    private void addOnTouchListener() {
        setViewAnimation(btnCopyScan);
        setViewAnimation(btnCopyingRead);
        setViewAnimation(btnCopyingStop);
    }


    /**
     * ǰȥ����
     */
    private void query() {
        // �ж�ִ������


        //������ʾ������
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


        //ƴ����������
        if (loadingcount < countAll) {
            String headMessage;
            String commNumber = meterNos.get(loadingcount);
            String meterName = copyBiz.getMeterNameByNo(commNumber);
            headMessage = "6808"; // ��ʼ��68 ����08
            // �жϸ��๤��ģʽ
            if (meterTypeNo.equals("05")) { // �����߱�
                // �жϳ���
                if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // ����FSK
                    headMessage += "14";
                } else {
                    headMessage += "19";
                }
                if (runMode.equals(GlobalConsts.RUN_MODE_STANDARD)) { // ���ģʽ
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
                } else if (runMode.equals(GlobalConsts.RUN_MODE_LORA) || runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) { // LORAģʽ����̩ģʽ
                    headMessage += "02";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_FSK)) { // FSK
                    headMessage += "01";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // ����FSK
                    headMessage += "03";
                } else if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // �Ϻ�FSK
                    // headMessage += "04";
                }
            } else if (meterTypeNo.equals("04")) { // IC������
                if (runMode.equals(GlobalConsts.RUN_MODE_FSK)) { // FSK
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

            if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // �Ϻ�ģʽר��
                headMessage = ""; // ��������
                ArrayList<String> params = new ArrayList<>();
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
                    String relayMeterNo = getIntent().getStringExtra("relayMeterNo"); // �м̱�ID��
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
                        commNumber = commNumber.substring(commNumber.length() - 10);
                    } else {
                        Toast.makeText(getApplicationContext(), "ͨѶ��Ŵ���!", Toast.LENGTH_SHORT).show();
                    }
                }
                String timeString = copydf.format(new Date());
                // ����ģʽ

                //������������
                //ִ�г������
                if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                    if (meterTypeNo.equals("05")) { // �����߱�
                        data.setCmdType("01"); // ��������
                        if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                            data.setCmdType("81");
                        }
                        timeString += ";01030713";
                        data.setDataBCD(timeString);
                    } else if (meterTypeNo.equals("04")) { // IC������
                        data.setCmdType("06"); // ��������
                        // timeString += ";01030713";
                        data.setBcdDate(timeString);
                    }
                }
                //ִ�п�������
                else if (operationType == GlobalConsts.COPY_OPERATION_OPENVALVE) {
                    data.setCmdType("02");
                    if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                        data.setCmdType("82");
                    }
                }
                //ִ�йط�����
                else if (operationType == GlobalConsts.COPY_OPERATION_CLOSEVALVE) {
                    data.setCmdType("03");
                    if (runMode.equals(GlobalConsts.RUN_MODE_ZHGT)) {
                        data.setCmdType("83");
                    }
                }
                //�޸�ͨѶ��
                else if (operationType == GlobalConsts.COPY_OPERATION_COMNUMBER) {
                    data.setCmdType("05");
                    String setParam1 = getIntent().getStringExtra("setParam1");
                    data.set();
                    data.setDataBCD(setParam1);
                }
                // ���ñ����
                else if (operationType == GlobalConsts.COPY_OPERATION_SETBASENUM) {
                    data.setCmdType("0b");
                    data.set();
                    data.setDataBCD(getIntent().getStringExtra("baseNum"));
                }
                // ��ȡ������
                else if (operationType == GlobalConsts.COPY_OPERATION_COPYFROZEN) {
                    data.setCmdType("81");
                }


                data.setTargetAddr(commNumber);


                if (meterTypeNo.equals("05")) {
                    if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // ����ģʽ
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
                    Toast.makeText(getApplicationContext(), "���ǲ�ѯ����!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            LogUtil.i("����", message);

            //��������������
            sendMessage(message); // ����������Ϣ

            // �������
            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                isCopy = false;
                // if (meterTypeNo.equals("05")) {
                if (copyType == GlobalConsts.COPY_TYPE_BATCH) {// ����
                    if (loadingcount == 0 || isWakeUp) {
                        isWakeUp = false;
                        new Thread() {
                            @Override
                            public void run() {
                                int time = 0;
                                for (int i = 0; i < wakeupWait; i++) { // �ȴ�
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
                                for (int i = 0; i < copyWait; i++) { // �ȴ�
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
                } else if (copyType == GlobalConsts.COPY_TYPE_SINGLE) {// ����
                    new Thread() {
                        @Override
                        public void run() {
                            int time = 0;
                            for (int i = 0; i < wakeupWait; i++) { // �ȴ�
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

    // ����������Ϣ
    private void sendMessage(String message) {
        // �������
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // ���ʵ���Ϸ��͵Ķ���
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }


    /**
     * ���ð�ť����¼�
     */
    private void addListener() {
        btnCopyScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("��ʼ")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У�����ı��������ӣ�", Toast.LENGTH_SHORT).show();
                } else {
                    // ����DeviceListActivity�����豸����ɨ��
                    Intent intent = new Intent(CtCopyingActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }

            }
        });

        btnCopyingRead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyingBackMsg.getText().toString().substring(0, 2).equals("��ʼ")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������", Toast.LENGTH_SHORT).show();
                } else if (tvBtInfo.getText().toString().equals("�����豸δ����") || tvBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��", Toast.LENGTH_SHORT).show();
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
     * �豸ɨ�践��
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // ��devicelistactivity�������豸����
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
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

    /**
     * �����豸
     */
    private void connectDevice(String address, boolean secure) {
        // // ����豸��ַ
        // String address = data.getExtras().getString(
        // DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // ��������豸����
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // �������ӵ��豸
        mChatService.connect(device);
    }


    /**
     * ����IC�����߳�������
     */
    private CtCopyDataICRF getCopyDataICRF(String meterNo, String dataString) {


        //������IC����
        CtCopyDataICRF copyDataICRF = new CtCopyDataICRF();


        //���ñ��
        copyDataICRF.setMeterNo(meterNo);


        //�����ۼ���
        String[] strarray = dataString.split(";");
        String Cumulant = Integer.toString(Integer.parseInt(strarray[0], 10));
        if (Cumulant.length() > 2) {
            Cumulant = Cumulant.substring(0, Cumulant.length() - 2) + "." + Cumulant.substring(Cumulant.length() - 2);
        } else {
            Cumulant = "0." + Cumulant;
        }
        copyDataICRF.setCumulant(Cumulant);


        // ʣ����
        String SurplusMoney = Integer.toString(Integer.parseInt(strarray[1], 10));
        if (SurplusMoney.length() > 2) {
            SurplusMoney = SurplusMoney.substring(0, SurplusMoney.length() - 2) + "." + SurplusMoney.substring(SurplusMoney.length() - 2);
        } else {
            SurplusMoney = "0." + SurplusMoney;
        }
        copyDataICRF.setSurplusMoney(SurplusMoney);

        // ������
        String OverZeroMoney = Integer.toString(Integer.parseInt(strarray[2], 10));
        if (OverZeroMoney.length() > 2) {
            OverZeroMoney = OverZeroMoney.substring(0, OverZeroMoney.length() - 2) + "." + OverZeroMoney.substring(OverZeroMoney.length() - 2);
        } else {
            OverZeroMoney = "0." + OverZeroMoney;
        }
        copyDataICRF.setOverZeroMoney(OverZeroMoney);

        // �������
        int BuyTimes = Integer.parseInt(strarray[3], 10);
        copyDataICRF.setBuyTimes(BuyTimes);

        // ��������
        int OverFlowTimes = Integer.parseInt(strarray[4], 10);
        copyDataICRF.setOverFlowTimes(OverFlowTimes);

        // �Ź�������
        int MagAttTimes = Integer.parseInt(strarray[5], 10);
        copyDataICRF.setMagAttTimes(MagAttTimes);

        // ����������
        int CardAttTimes = Integer.parseInt(strarray[6], 10);
        copyDataICRF.setCardAttTimes(CardAttTimes);

        // ���״̬ת����16����INT�������ݿ�
        copyDataICRF.setMeterState(Integer.parseInt(strarray[7], 16));
        // �����ۼ���
        copyDataICRF.setCurrMonthTotal(Integer.toString(Integer.parseInt(strarray[8], 10)));
        // �����ۼ���
        copyDataICRF.setLast1MonthTotal(Integer.toString(Integer.parseInt(strarray[9], 10)));

        if (strarray.length > 10) {

            // ��ǰ���ۣ�ȥ��֡��ָ���"|"��
            String unitPrice = strarray[10].substring(1);
            unitPrice = Integer.toString(Integer.parseInt(unitPrice, 10));
            if (unitPrice.length() > 2) {
                unitPrice = unitPrice.substring(0, unitPrice.length() - 2) + "." + unitPrice.substring(unitPrice.length() - 2);
            } else {
                unitPrice = "0." + unitPrice;
            }
            copyDataICRF.setUnitPrice(unitPrice);

            // �ۼ��������
            String accMoney = strarray[11].substring(0, 8);
            accMoney = Integer.toString(Integer.parseInt(accMoney, 10));
            if (accMoney.length() > 2) {
                accMoney = accMoney.substring(0, accMoney.length() - 2) + "." + accMoney.substring(accMoney.length() - 2);
            } else {
                accMoney = "0." + accMoney;
            }
            copyDataICRF.setAccMoney(accMoney);

            // �ۼƳ�ֵ���
            String accBuyMoney = strarray[12].substring(0, 8);
            accBuyMoney = Integer.toString(Integer.parseInt(accBuyMoney, 10));
            if (accBuyMoney.length() > 2) {
                accBuyMoney = accBuyMoney.substring(0, accBuyMoney.length() - 2) + "." + accBuyMoney.substring(accBuyMoney.length() - 2);
            } else {
                accBuyMoney = "0." + accBuyMoney;
            }
            copyDataICRF.setAccBuyMoney(accBuyMoney);

            // ������ʹ����
            String currentShow = Integer.toString(Integer.parseInt(strarray[13], 10));
            copyDataICRF.setCurrentShow(currentShow);
        }
        //����ʱ��
        copyDataICRF.setCopyTime(df.format(new Date()));
        //�������
        copyDataICRF.setMeterName(tvLoadingName.getText().toString());
        copyDataICRF.setCommunicateNo(meterNo);
        copyDataICRF.setCollectorNo(collectorNo);//������

        return copyDataICRF;
    }


    /**
     * �����Ϻ������߳�������
     */
    private CtCopyData getCopyDataShangHai(String meterNo, String dataString) {
        CtCopyData copyData = new CtCopyData();
        copyData.setMeterNo(meterNo);
        copyData.setElec(dataString.substring(0, 2));
        String CurrentShow = dataString.substring(2, 8);
        CurrentShow = CurrentShow.substring(1);// ȥ��F
        copyData.setCurrentShow(CurrentShow); // �ۼ���
        copyData.setMeterState(Integer.parseInt(dataString.substring(8, 10), 16));
        copyData.setCollectorNo(collectorNo);//���ü��������
        copyData.setCommunicateNo(meterNo);
        // ʹ��������
        return copyData;
    }

    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros().toPlainString();
    }

    /**
     * �������߱�������
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
        copyData.setCollectorNo(collectorNo);//���ü��������
        copyData.setCommunicateNo(meterNo);
//        copyData.setMeterNo(meterNo);
        return copyData;
    }

    /**
     * �����������߱�������
     */
    private CtCopyData getCopyDataHuiZhou(String meterNo, CqueueData data) {
        CtCopyData copyData = new CtCopyData();
        copyData.setMeterNo(meterNo);
        String CurrentShow = Double.toString(data.getSumUseGas());
        copyData.setCurrentShow(CurrentShow); // �ۼ���
        copyData.setMeterState(Integer.parseInt(data.getMeterState(), 16)); // ���״̬ת����16����INT�������ݿ�
        copyData.setMeterName(tvLoadingName.getText().toString());
//        CopyData copyDataLast = copyBiz.getLastCopyDataByMeterNo(meterNo); // ��ѯ��һ�γ����¼
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
//        } else { // ��ʼֵ
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
        copyData.setCollectorNo(collectorNo);//���ü��������
        copyData.setCommunicateNo(meterNo);
        return copyData;
    }

    // ��BluetoothChatService�����������Ϣ����
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //��������״̬(�ı��ʱ����յ���ߵ�ָ��)
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        //���������ϵ�ʱ��
                        case BluetoothChatService.STATE_CONNECTED:
                            tvBtInfo.setText(getString(R.string.title_connected_to) + mConnectedDeviceName);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            // �������Ӻ��Զ���ʼ����
                            isStop = false;
                            query();//�����˾�ȥ����
                            break;

                        //��������
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
                            if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // ����ģʽ
                                msgDecode = hhP.decodeHuiZhou(readMessage);
                                cmdtype = msgDecode.getCmdType();
                            } else if (runMode.equals(GlobalConsts.RUN_MODE_SHANGHAI)) { // �Ϻ�ģʽ
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
                            String loadingComNum = tvLoadingComNum.getText().toString();
                            String loadingComNumActual = loadingComNum; // ����10λ���ж�
                            if (loadingComNumActual.length() > 10) {
                                loadingComNumActual = loadingComNumActual.substring(loadingComNumActual.length() - 10);
                            }


                            //��������
                            switch (cmdtype) {
                                case "01": // ���߱�������
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CtCopyData copyData;
                                        if (runMode.equals(GlobalConsts.RUN_MODE_HUI_ZHOU)) { // ����ģʽ
                                            copyData = getCopyDataHuiZhou(loadingComNum, msgDecode);
                                        } else {
                                            copyData = getCopyData(loadingComNum, dataString);
                                        }
                                        //�ѳ��������浽���ݿ���ȥ
                                        ctCopyDataDao.putCtCopyData(copyData);
//                                        copyBiz.addCopyData(copyData);
//                                        // �޸ĳ���״̬
//                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
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
                                                //�������
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
                                case "06": // IC�����߳�������
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CtCopyDataICRF copyDataICRF;
                                        copyDataICRF = getCopyDataICRF(loadingComNum, dataString);
                                        //���ȥ
                                        ctCopyDataICRFDao.putCtCopyData(copyDataICRF);
                                        //�������ݿ�
//                                        copyBiz.addCopyDataICRF(copyDataICRF);
//                                        // �޸ĳ���״̬
//                                        copyBiz.ChangeCopyState(copyDataICRF.getMeterNo(), 1, meterTypeNo);


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
                                            Intent intent = new Intent(CtCopyingActivity.this, CopyResultActivity.class);
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
                                        tvLoadingComNum.setText(msgDecode
                                                .getSourceAddr());
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
                                    if (msgDecode.getSourceAddr().equals(loadingComNumActual)) { // �жϽ��յ��������Ƿ��ǵ�ǰ���ڲ����ı�
                                        CtCopyData copyData = getCopyDataShangHai(loadingComNum, dataString);
//                                        copyBiz.addCopyData(copyData);
//                                        // �޸ĳ���״̬
//                                        copyBiz.ChangeCopyState(copyData.getMeterNo(), 1, meterTypeNo);
                                        ctCopyDataDao.putCtCopyData(copyData);
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
                                        tvCopyingBackMsg.setText("�������:" + dataString.substring(10, 12));
                                        loadingcount++;
                                        tvLoadingCount.setText(loadingcount + "");
                                        pgbCopying.incrementProgressBy(1);
                                    }
                                    break;
                                case "08ab":// ȡ���м�����
                                    if (dataString != null && dataString.length() > 11) {
                                        tvCopyingBackMsg.setText("ִ�н��:" + dataString.substring(10, 12));
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
                            String loadingComNum = tvLoadingComNum.getText().toString();
//                            copyBiz.ChangeCopyState(loadingComNum, 0, meterTypeNo);// �޸�״̬Ϊδ����
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