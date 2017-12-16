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

    // ��������������
    private BluetoothAdapter mBluetoothAdapter = null;
    // ��������
    private BluetoothChatService mChatService = null;
    // �����豸������
    private String mConnectedDeviceName = null;

    // Intent�������
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // ��BluetoothChatService���������͵���Ϣ����
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

    // ��BluetoothChatService����������յ�����Կ��
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // ������־
    private static boolean isCopy;
    // ά����־
    private static boolean isMaintenance = false;
    private static int p;// ƫ����
    private static String point;// Ƶ��
    private static int maintenMode;// ά������

    private static String firstString = "";
    private static String ImgString = "";
    private static int pageConut = 0;// ��ͼƬ����
    private static int pageCurrent = 0;// ��ǰͼƬ����
    private static int gettimes = 0; // ͼƬ�����մ���
    // private static boolean isHead = false; //�жϰ���ʶ
    private static int upCount = 0; // �������ϴ�����
    private static ArrayList<Integer> tempPages;
    private static boolean isFill = false;// ��֡��ʶ
    private static String tempMsg = "";// ��ʱ�����

    // ����ģʽ
    private static int copyType;
    // ִ��ģʽ
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


    //�������
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
        // ��ֹ�Զ��������뷨
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        preferenceBiz = new PreferenceBiz(this);
        setBiz = new SetBiz(this);

        // ��ʼ����������
        meterNo = getIntent().getStringExtra("meterNo"); // ����
        meterTypeNo = getIntent().getStringExtra("meterTypeNo");// 7,8λ��
        tvLoadingPhotoComNum.setText(meterNo);

        //�õ��ܶ������
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


        copyType = getIntent().getIntExtra("copyType", 1); // ������ʽ��1������2Ⱥ����
        operationType = getIntent().getIntExtra("operationType", 1); // ��������
        if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) { // �Ƿ����ü�����
            isCol = getIntent().getBooleanExtra("isCol", false);
        }


        serverUrl = setBiz.getCopyPhotoUrl();
        ImgServerUrl = serverUrl + "Images/";

        // �������
        if (NetWorkManager.isConnect(this) == false) {
            new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog)
                    .setTitle("�������")
                    .setMessage("��������ʧ�ܣ�����������������ӵ����磬���顣")
                    .setCancelable(false)
                    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0,
                                            int arg1) {
                            finish();
                        }
                    }).show();
        } else { // ����ܷ�����ʶ�������
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
        tvTitlebar_name.setText("�������װ��������");

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

        // ����Ĭ�ϵ�ImageLoader���ò���
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        loader = ImageLoader.getInstance();


        tvTime = (TextView) findViewById(R.id.tvTime);
    }

    // ����
    private void wakeUp(String commNumber) {
        sendMessage("aadd50aaaaaa" + commNumber.substring(6));
        tvCopyPhotoBackMsg.setText("���ڻ��ѱ���");
        firstString = "";
        ImgString = "";
        pageConut = 0;// ��ͼƬ����
        pageCurrent = 0;// ��ǰͼƬ����
        gettimes = 0; // ͼƬ�����մ���
        tempMsg = "";
        // isHead = false; //�жϰ���ʶ
        isFill = false;
        upCount = 0;
        pgbCopyPhoto.setProgress(0);
        tvCopyPhotoDevPower.setText("");

        tvCopyPhotoDevState.setText("");
    }

    private void wakeUp2(String commNumber) {
        sendMessage("aadd50aaaaaa" + commNumber.substring(6));
        tvCopyPhotoBackMsg.setText("���ڻ��ѱ���");
        firstString = "";
        ImgString = "";
        pageConut = 0;// ��ͼƬ����
        pageCurrent = 0;// ��ǰͼƬ����
        gettimes = 0; // ͼƬ�����մ���
        tempMsg = "";
        // isHead = false; //�жϰ���ʶ
        isFill = false;
        upCount = 0;
        pgbCopyPhoto.setProgress(0);

    }


    // ����
    private void query() {
        String commNumber = meterNo;
        CqueueData data = new CqueueData();
        if ((commNumber.length() != 10)) {
            Toast.makeText(getApplicationContext(), "ͨѶ��Ŵ��󣬷�10λ!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "���ǲ�ѯ����!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // ����������Ϣ
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
            Toast.makeText(getApplicationContext(), "���ǲ�ѯ����!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // ����������Ϣ
        }
    }

    private void maintenance() { // ά��
        String databcd = "";
        CqueueData data = new CqueueData();
        if (maintenMode == 1) {// ͼƬλ�õ���
            databcd = Integer.toHexString(p);
            data.setCmdType("28");
            data.set();
            if (p > 0) {
                databcd = patchHexString(databcd, 2);
            } else if (p < 0) {
                databcd = databcd.substring(6);
            }
        } else if (maintenMode == 2) { // Ƶ������
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
            Toast.makeText(getApplicationContext(), "���ǲ�ѯ����!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage(message); // ����������Ϣ
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

    // ����������Ϣ
    private void sendMessage(String message) {
        // �������
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            return;
        }
        // ���ʵ���Ϸ��͵Ķ���
        if (message.length() > 0) {

            myMessage = myMessage + "\n\n\n" + currentTimeMillis() + "��������\n" + message;
            // �õ���Ϣ���ֽڣ�����bluetoothchatserviceд
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }


    private void addListener() {
        btnCopyPhotoScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("�����ѻ������ڳ���")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У�����ı��������ӣ�", Toast.LENGTH_SHORT).show();
                } else {
                    // ����DeviceListActivity�����豸����ɨ��
                    Intent intent = new Intent(HtCopyPhotoActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
            }
        });

        btnCopyPhotoRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("�����ѻ������ڳ���") || tvCopyPhotoBackMsg.getText().toString().equals("���ڻ��ѱ���")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("�����豸δ����") || tvPhotoBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��",
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
                if (tvCopyPhotoBackMsg.getText().toString().equals("�����ѻ������ڳ���")
                        || tvCopyPhotoBackMsg.getText().toString()
                        .equals("���ڻ��ѱ���")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������",
                            Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("�����豸δ����")
                        || tvPhotoBtInfo.getText().toString()
                        .equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��",
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
                        maintenMode = 1;// ����ͼƬΪֹ
                        wakeUp(meterNo);
                    } else {
                        Toast.makeText(getApplicationContext(), "�������������1-10��Χ��������", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnCopyPhotoImgDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCopyPhotoBackMsg.getText().toString().equals("�����ѻ������ڳ���") || tvCopyPhotoBackMsg.getText().toString().equals("���ڻ��ѱ���")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("�����豸δ����") || tvPhotoBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��", Toast.LENGTH_SHORT).show();
                } else {
                    String position = etCopyPhotoImgPosition.getText().toString();
                    position = "-" + position;
                    try {
                        p = Integer.parseInt(position);
                    } catch (NumberFormatException e) {
                    }
                    if (p >= -10 && p <= -1) {
                        isMaintenance = true;
                        maintenMode = 1;// ����ͼƬΪֹ
                        wakeUp(meterNo);
                    } else {
                        Toast.makeText(getApplicationContext(), "�������������1-10��Χ��������", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCopyPhotoSetPoint.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (tvCopyPhotoBackMsg.getText().toString().equals("�����ѻ������ڳ���") || tvCopyPhotoBackMsg.getText().toString().equals("���ڻ��ѱ���")) {
                    Toast.makeText(getApplicationContext(), "���ڳ����У������ظ�������", Toast.LENGTH_SHORT).show();
                } else if (tvPhotoBtInfo.getText().toString().equals("�����豸δ����") || tvPhotoBtInfo.getText().toString().equals("�������������豸")) {
                    Toast.makeText(getApplicationContext(), "�������������豸��", Toast.LENGTH_SHORT).show();
                } else {
                    String poi = etCopyPhotoSetPoint.getText().toString();
                    if (poi.length() != 6) {
                        Toast.makeText(getApplicationContext(), "������󣬱���6λ��", Toast.LENGTH_SHORT).show();
                    } else {
                        point = Cfun.ToHEXString(Integer.parseInt(poi + "000"));
                        isMaintenance = true;
                        maintenMode = 2;// ����Ƶ��
                        wakeUp(meterNo);
                    }

                }
            }
        });
    }

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
        if (timer != null) {
            timer.cancel();
        }
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

    private String lastMeterNo;

    private void getCopyDataPhoto(String postData, String meterTypeNo) {
        GetCopyDataPhoto(postData, meterTypeNo);//��������
    }

    private void doNext() {

        ToastUtils.showToast(HtCopyPhotoActivity.this, "ִ����һ��");
        lastMeterNo = meterNo;
        //����һ����˵�������ɹ���
        nowNum++;
        if (meterNos != null && meterNos.size() > nowNum) {
            meterNo = meterNos.get(nowNum);
            meterTypeNo = meterTypeNos.get(nowNum);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wakeUp2(meterNo);
                    tvLoadingPhotoComNum.setText(meterNo + "(" + (nowNum + 1) + "/" + (meterNos.size()) + ")");//���õ�ǰ��������
                }
            }, 3000);
        } else if (meterNos != null && meterNos.size() <= nowNum) {
            LogUtil.i("������");
            ToastUtils.showToast(HtCopyPhotoActivity.this, "������");
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
        LogUtil.i("����GetCopyDataPhoto" + postData + "meterTypeNo" + meterTypeNo);
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
                        LogUtil.i("�����" + e.toString());
                        Message msg = Message.obtain(mHandler, MESSAGE_GETCOPYDATAPHOTO_FAILURT);
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtil.i("�����" + response);
                        LogUtil.i("�����2" + Xml2Json.xml2JSON(response));
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
            if (gettimes == 0) { // ��ȡ��һ������
                if (getMsg.substring(0, 2).equals("68") && getMsg.substring(getMsg.length() - 2).equals("16")) { // ��һ�����ݡ�
                    firstString = getMsg;
                    gettimes++;
                }
            } else {
                if (gettimes == 1) { // ��ʼ���ܰ�����Ϣ
                    pageConut = Integer.parseInt(getMsg.substring(4, 6), 16); // ��ȡ�ܰ���
                    pgbCopyPhoto.setMax(pageConut - 1);
                    tempPages = new ArrayList<>();
                    tempMsg = "";
                }
                if (checkImgPage(getMsg)) { // ��׼һ��ͼƬ
                    doGetCopy(getMsg);
                } else { // �жϰ��Ĵ���
                    tempMsg = tempMsg + getMsg;
                    if (checkImgPage(tempMsg)) { // �жϰ�ƴ����֤ͨ��
                        doGetCopy(tempMsg);
                        tempMsg = "";
                    }
                }
            }
        } catch (Exception e) {
            tvCopyPhotoBackMsg.setText("�������ݽ��մ��� ������");
        }
    }

    private void doGetCopy(String getMsg) {
        pageCurrent = Integer.parseInt(getMsg.substring(6, 8), 16); // ��ȡ��ǰ����
        tempPages.add(pageCurrent);// �洢
        // Log.i("pageCurrent", tempPages.toString());
        if (pageCurrent < pageConut) { // �������һ��
            ImgString += getMsg;
            ImgString += ";";
            gettimes++;
            pgbCopyPhoto.incrementProgressBy(1);
        } else if (pageCurrent == pageConut) {// ���һ��
            ImgString += getMsg;
            ImgString += ";";
            if (gettimes == pageConut) {// ��������
                pgbCopyPhoto.incrementProgressBy(1);
                tvCopyPhotoBackMsg.setText("�ɹ� ��ʼ�ϴ�������ʶ��");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            } else {
                tvCopyPhotoBackMsg.setText("���ݲ����� ��֡��");
                checkFill();
            }
        }
    }

    private void fillCopy(String getMsg) {

        LogUtil.i("fillCopy", getMsg);

        if (getMsg.length() == 116) {
            int page = Integer.parseInt(getMsg.substring(6, 8), 16); // ��ȡ��ǰ����
            page = page - 1;
            ImgString = ImgString.substring(0, 117 * page) + getMsg + ";" + ImgString.substring(117 * page);
            isFill = false;
            pgbCopyPhoto.incrementProgressBy(1);
            // �ٴμ��
            if (!checkFill()) {
                // Log.i("filled", ImgString);
                tvCopyPhotoBackMsg.setText("�ɹ� ��ʼ�ϴ�������ʶ��");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            }
        } else if (getMsg.length() == 484) {
            int page = Integer.parseInt(getMsg.substring(6, 8), 16); // ��ȡ��ǰ����
            page = page - 1;
            ImgString = ImgString.substring(0, 485 * page) + getMsg + ";" + ImgString.substring(485 * page);
            isFill = false;
            pgbCopyPhoto.incrementProgressBy(1);
            // �ٴμ��
            if (!checkFill()) {
                // Log.i("filled", ImgString);
                tvCopyPhotoBackMsg.setText("�ɹ� ��ʼ�ϴ�������ʶ��");
                String postData = firstString + ";" + ImgString;
                getCopyDataPhoto(postData, meterTypeNo);
            }
        }
    }

    private boolean checkFill() {
        for (int i = 1; i <= pageConut; i++) {
            if (!tempPages.contains(i)) { // �����������еģ��貹֡
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
    // ��BluetoothChatService������������Ϣ����
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
                            // �������Ӻ��Զ���ʼ����
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
                    // ����һ���ַ�����Ч�ֽڵĻ�����
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    myMessage = myMessage + "\n\n\n\n" + currentTimeMillis() + "�յ�����\n" + readMessage;
                    lastGetMessageTime = System.currentTimeMillis();
                    LogUtil.i("�յ�����" + readMessage);

                    if (timer != null) {
                        timer.cancel();
                    }
                    //��ʱ15��
                    timer = new CountDownTimer(15000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvTime.setText("�ȴ���ʱʱ��:" + (int) (millisUntilFinished / 1000) + "s");

                        }

                        @Override
                        public void onFinish() {
                            LogUtil.i("������ʱ");
                            tvTime.setText("������ȡ��һֻ��");
                            //ʱ�䵽
                            doNext();
                        }
                    };
                    timer.start();
                    if (readMessage.equals("16161616")) {
                        if (!isMaintenance) {
                            if (operationType == GlobalConsts.COPY_OPERATION_COPY) {
                                tvCopyPhotoBackMsg.setText("�����ѻ������ڳ���");
                                query();
                            } else if (operationType == GlobalConsts.COPY_OPERATION_PHOTOCOMNUMBER) {
                                tvCopyPhotoBackMsg.setText("�����ѻ�����������");
                                isMaintenance = true;
                                maintenance();
                            }
                            break;
                        } else {
                            tvCopyPhotoBackMsg.setText("�����ѻ��ѿ�ʼ����");
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
                                tvCopyPhotoBackMsg.setText("ͼƬλ�õ����ɹ�");
                            } else {
                                tvCopyPhotoBackMsg.setText("ͼƬλ�õ���ʧ��");
                            }
                        } else if (readMessage.length() == 38) {
                            tvCopyPhotoBackMsg.setText("����Ƶ�����óɹ�");
                        } else {
                            tvCopyPhotoBackMsg.setText("����ʧ��");
                        }
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // ���������豸������
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_SUCCESS:
                    tvCopyPhotoBackMsg.setText("������� ��˶�ʶ����");
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_FAILURT:
                    if (upCount < 1) {
                        tvCopyPhotoBackMsg.setText("�����ϴ�ʧ�� ������");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String postData = firstString + ";" + ImgString;
                        getCopyDataPhoto(postData, meterTypeNo);
                        upCount++;
                    } else {
                        tvCopyPhotoBackMsg.setText("�����ϴ�ʧ�� ��������");
                    }
                    break;
                case MESSAGE_GETCOPYDATAPHOTO_ERROR:
                    tvCopyPhotoBackMsg.setText("���ݽ���ʧ�� �볢���س�");
                    break;

                //ֱ����һ��
                case MESSAGE_COPYCANT:
                    tvCopyPhotoBackMsg.setText("����ʧ�ܱ�������Ӧ");
//                    doNext();
                    break;
                case MESSAGE_SERVER_CONNECT_SUCCESS:
                    // ����Ƿ����Զ���������
                    String address = preferenceBiz.getDeviceAddress();
                    if (address != null) {
                        connectDevice(address, true);
                    }
                    break;
                case MESSAGE_SERVER_CONNECT_FAILURT:
                    new AlertDialog.Builder(HtCopyPhotoActivity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog)
                            .setTitle("�������")
                            .setMessage("����ʶ�������ʧ�ܣ����顣")
                            .setCancelable(false)
                            .setPositiveButton("ȷ��",
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