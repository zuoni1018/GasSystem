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

import com.pl.gassystem.bean.ht.HtSendMessageSetNewKey;
import com.pl.gassystem.utils.LogUtil;
import com.pl.gassystem.utils.SPUtils;
import com.pl.bluetooth.BluetoothChatService;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.R;
import com.pl.gassystem.bean.ht.HtGetMessage;
import com.pl.gassystem.bean.ht.HtGetMessageChangeBookNoOrCumulant;
import com.pl.gassystem.bean.ht.HtGetMessageQueryParameter;
import com.pl.gassystem.bean.ht.HtSendMessage;
import com.pl.gassystem.bean.ht.HtSendMessageChange;
import com.pl.gassystem.bean.ht.HtSendMessageSetParameter;
import com.pl.gassystem.command.HtCommand;
import com.pl.gassystem.utils.CalendarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zangyi_shuai_ge on 2017/9/1
 * ����ȼ���������
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

    //����
    private BluetoothAdapter mBluetoothAdapter;// ��������������
    private BluetoothChatService mChatService; // ��������
    private String mConnectedDeviceName;// �����豸������
    //���� Intent�������
    private static final int REQUEST_CONNECT_DEVICE = 1;//�����豸������
    private static final int REQUEST_ENABLE_BT = 2;//������
    //��ǰ�����豸����״̬
    private static final int DEVICE_STATE_NONE = 0;//�豸δ����
    private static final int DEVICE_STATE_CONNECTING = 1;//�豸��������
    private static final int DEVICE_STATE_CONNECTED = 2;//�豸�Ѿ�����
    private int nowConnectState = 0;//��ǰ�豸����״̬


    // ��BluetoothChatService��������͵���Ϣ����
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // ��BluetoothChatService���������յ�����Կ��
    public static final String DEVICE_NAME = "device_name";


    private String commandType;//��������
    private String bookNo;//���� ���

    private String copyType = "";//����ʽΪȺ�����ǵ���
    private ArrayList<String> bookNos;//һ��ѱ��

    private int num = 0;//��ǰ���ص�����


    //���ñ����Ҫ�Ĳ���
    private String changeType;//�޸�����
    private String newBookNo;//�º�
    private String cumulant;//�ۼ���

    //�������ò�������Ҫ�Ĳ���
    private String setYinZi = "";
    private String setXinDao = "";
    private String setDongJieRi = "";
    private String setKaiChuangShiJian = "";
    private boolean isSetYinZi;

    //������Կ����Ҫ�Ĳ���
    private String newKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setTitle("��������");
        addOnTouchListener();


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// ��ȡ��������������

        if (mBluetoothAdapter == null) {
            showToast("��ǰ�豸��֧������");
            finish();//�˳���ǰ���沢��ִ���������
            return;
        }
        //��ȡIntent����������Ϣ
        commandType = getIntent().getStringExtra("commandType");
        bookNos = new ArrayList<>();

        //һ������������ ֻ����ȼ����� �� ������������ �Ϳ�����
        if (commandType.equals(HtSendMessage.COMMAND_TYPE_DOOR_STATE)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_OPEN_DOOR)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_CLOSE_DOOR)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {

            bookNo = getIntent().getStringExtra("bookNo");
            tvLoadingAll.setText("1");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_NORMAL)
                | commandType.equals(HtSendMessage.COMMAND_TYPE_COPY_FROZEN)) {
            //���� ����������
            copyType = getIntent().getStringExtra("copyType");
            if (copyType.equals(HtSendMessage.COPY_TYPE_SINGLE)) {
                //����
                bookNo = getIntent().getStringExtra("bookNo");
                tvLoadingAll.setText("1");
            } else {
                //Ⱥ��
                bookNos = getIntent().getStringArrayListExtra("bookNos");
                tvLoadingAll.setText(bookNos.size() + "");
            }
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)) {
            //�ı�Ź���
            bookNo = getIntent().getStringExtra("nowBookNo");
            newBookNo = getIntent().getStringExtra("newBookNo");
            cumulant = getIntent().getStringExtra("cumulant");
            changeType = getIntent().getStringExtra("changeType");//�޸�����
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)) {
            bookNos = getIntent().getStringArrayListExtra("bookNos");

            setYinZi = getIntent().getStringExtra("yinzi");
            setXinDao = getIntent().getStringExtra("xindao");
            setDongJieRi = getIntent().getStringExtra("dongjieri");
            setKaiChuangShiJian = getIntent().getStringExtra("kaichuangshijian");
            isSetYinZi = getIntent().getBooleanExtra("isSetYinZi", false);
            LogUtil.i("�������ò���" + setYinZi + "===" + setXinDao + "===" + setDongJieRi + "===" + setKaiChuangShiJian + "===" + isSetYinZi);
            tvLoadingAll.setText(bookNos.size() + "");
        } else if (commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)) {
            bookNos = getIntent().getStringArrayListExtra("bookNos");
            newKey = getIntent().getStringExtra("newKey");
        }

        mChatService = new BluetoothChatService(this, mHandler);//������������һ����������

        // ����Ƿ����Զ���������
        String address = (String) SPUtils.get(getContext(), "htDevice", "");
        assert address != null;
        if (!address.trim().equals("")) {
            connectDevice(address, true);
        }

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
        super.onDestroy();
    }

    @Override
    protected int setLayout() {
        return R.layout.ht_activity_copying;
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


    @OnClick({R.id.btnCopyScan, R.id.btnCopyingRead, R.id.btnCopyingStop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCopyScan:
                //�����豸
                if (nowConnectState == DEVICE_STATE_CONNECTED) {
                    showToast("�����豸������");
                } else {
                    Intent intent = new Intent(HtCopyingActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                }
                break;
            case R.id.btnCopyingRead:

                if (nowConnectState == DEVICE_STATE_CONNECTED) {

                    num = 0;
                    tvLoadingCount.setText(num + "");

                    //����������һ������
                    createCommand();
                } else {
                    showToast("������������");
                }

                break;
            case R.id.btnCopyingStop:

                break;
        }
    }


    /**
     * ����һ���޸ı�ŵ�����
     */
    private void createChangeBookNoOrCumulantCommand() {
        HtSendMessageChange htSendMessage = new HtSendMessageChange();

        htSendMessage.setCommandType(commandType);//������������
        htSendMessage.setBookNo(bookNo);
        htSendMessage.setNewBookNo(newBookNo);
        htSendMessage.setWakeUpTime(6000);
        htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessage.setKuoPinYinZi("09");//����ǲ�ѯ���� ��ô�ŵ��Ź̶�Ϊ00
        htSendMessage.setKuoPinXinDao("0E");
        htSendMessage.setCumulant(cumulant);
        htSendMessage.setChangeType(changeType);

        String message = HtCommand.encodeHangTianChangeBookNoOrCumulant(htSendMessage);
        sendMessage(message);

    }


    private void createCommand() {

        switch (commandType) {
            case HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT:
                createChangeBookNoOrCumulantCommand();
                break;
            case HtSendMessage.COMMAND_TYPE_SET_PARAMETER:
                createSetParameterCommand();
                break;
            case HtSendMessage.COMMAND_TYPE_SET_KEY:
                createSetKeyCommand();
                break;
            default:
                HtSendMessage htSendMessage = new HtSendMessage();
                htSendMessage.setCommandType(commandType);//������������

                if (copyType.equals(HtSendMessage.COPY_TYPE_GROUP)) {
                    //Ⱥ��
                    htSendMessage.setBookNo("FFFFFFFF");//���ñ��

                    htSendMessage.setBookNos(bookNos);//����һ��ѱ��

                    htSendMessage.setSetTime(CalendarUtils.getHtTime()); // 17 09 15 14 10 00

                    htSendMessage.setWakeUpTime(6000);

                    htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_GROUP);

                    htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
                    htSendMessage.setKuoPinYinZi("09");//����ǲ�ѯ���� ��ô�ŵ��Ź̶�Ϊ00
                    htSendMessage.setKuoPinXinDao("0E");

                } else {
                    // ���� �鿴����״̬ ���ط��� ��ֻ��ִ�����´��뼴��

                    htSendMessage.setBookNo(bookNo);//���ñ��

                    htSendMessage.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);

                    htSendMessage.setWakeUpTime(6000);

                    htSendMessage.setCopyType(HtSendMessage.COPY_TYPE_SINGLE);

                    //������Ƶ���Ӻ��ŵ�
                    htSendMessage.setKuoPinYinZi("09");
                    if (commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {
                        htSendMessage.setKuoPinXinDao("00");//����ǲ�ѯ���� ��ô�ŵ��Ź̶�Ϊ00
                    } else {
                        htSendMessage.setKuoPinXinDao("0E");
                    }
                }
                String message = HtCommand.encodeHangTian(htSendMessage);
                sendMessage(message);
                break;
        }
    }

    /**
     * ����һ�������޸���Կ������
     */
    private void createSetKeyCommand() {
        HtSendMessageSetNewKey htSendMessageSetNewKey = new HtSendMessageSetNewKey();
        htSendMessageSetNewKey.setCommandType(commandType);//������������
        htSendMessageSetNewKey.setBookNo("FFFFFFFF");
        htSendMessageSetNewKey.setBookNos(bookNos);
        htSendMessageSetNewKey.setWakeUpTime(6000);
        htSendMessageSetNewKey.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessageSetNewKey.setKuoPinYinZi("09");
        htSendMessageSetNewKey.setKuoPinXinDao("0E");

        //�����µ���Կ
        htSendMessageSetNewKey.setNewKey(newKey);

        String message = HtCommand.encodeHangTianSetKey(htSendMessageSetNewKey);
        sendMessage(message);
    }

    /**
     * ����һ�������޸ı���Ϣ������
     */
    private void createSetParameterCommand() {

//        List<String> bookNos=new ArrayList<>();
//        bookNos.add(bookNo);

        HtSendMessageSetParameter htSendMessageSetParameter = new HtSendMessageSetParameter();
        htSendMessageSetParameter.setCommandType(commandType);//������������
        htSendMessageSetParameter.setBookNo("FFFFFFFF");
        htSendMessageSetParameter.setBookNos(bookNos);
        htSendMessageSetParameter.setWakeUpTime(6000);
        htSendMessageSetParameter.setWakeUpMark(HtSendMessage.WAKE_UP_MARK_01);
        htSendMessageSetParameter.setKuoPinYinZi("09");
        htSendMessageSetParameter.setKuoPinXinDao("0E");

        //�޸ĵĲ���
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
                            LogUtil.i("�����豸", "������������");
                            setBluetoothState(DEVICE_STATE_CONNECTED);
                            createCommand();
                            break;

                        //��������
                        case BluetoothChatService.STATE_CONNECTING:
                            LogUtil.i("�����豸", "������������...");
                            setBluetoothState(DEVICE_STATE_CONNECTING);
                            break;

                        //�Ͽ����� ����ʧ��
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            LogUtil.i("�����豸", "����δ����...");
                            setBluetoothState(DEVICE_STATE_NONE);
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    //�յ��������
                    byte[] readBuf = (byte[]) msg.obj;
                    // ����һ���ַ�����Ч�ֽڵĻ�����
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    LogUtil.i("�����õ����", readMessage);
                    readMessage(readMessage);

                    break;

                case MESSAGE_DEVICE_NAME:
                    // ���������豸������
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    tvDeviceState.setText("������" + mConnectedDeviceName);

                    break;


            }
        }
    };

    private void readMessage(String readMessage) {

        if (commandType.equals(HtSendMessage.COMMAND_TYPE_QUERY_PARAMETER)) {
            //��ѯ�����
            HtGetMessageQueryParameter htGetMessageQueryParameter = HtCommand.readQueryParameterMessage(readMessage);
            LogUtil.i("������", htGetMessageQueryParameter.getResult());
            num++;
            tvLoadingCount.setText(num + "");
            tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessageQueryParameter.getResult());
        } else if (
                commandType.equals(HtSendMessage.COMMAND_TYPE_CHANGE_BOOK_NO_OR_CUMULANT)
                        | commandType.equals(HtSendMessage.COMMAND_TYPE_SET_PARAMETER)
                        | commandType.equals(HtSendMessage.COMMAND_TYPE_SET_KEY)
                ) {
            //�޸ı�� �����ۼ��� �޸���Կ
            HtGetMessageChangeBookNoOrCumulant htGetMessageQueryParameter = HtCommand.readChangeBookNoOrCumulantMessage(readMessage);
            assert htGetMessageQueryParameter != null;
            LogUtil.i("������", htGetMessageQueryParameter.getResult());
            num++;
            tvLoadingCount.setText(num + "");
            tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessageQueryParameter.getResult());
        } else {
            if (readMessage.length() > 25) {
                HtGetMessage htGetMessage = HtCommand.readMessage(readMessage);
                if (htGetMessage != null) {
                    LogUtil.i("������", htGetMessage.getResult());
                    num++;
                    tvLoadingCount.setText(num + "");
                    tvMessage.setText(tvMessage.getText().toString().trim() + "\n" + htGetMessage.getResult());
                }
            }
        }


    }

    /**
     * ������������״̬���ö�Ӧ��UI
     */
    private void setBluetoothState(int deviceState) {
        nowConnectState = deviceState;
        switch (deviceState) {
            case DEVICE_STATE_NONE:
                tvDeviceState.setText("�����豸δ����");
                break;
            case DEVICE_STATE_CONNECTING:
                tvDeviceState.setText("�����豸��������...");
                break;
            case DEVICE_STATE_CONNECTED:
                tvDeviceState.setText("������  :" + mConnectedDeviceName);
                break;
        }
    }

    /**
     * �����豸
     */
    private void connectDevice(String address, boolean secure) {
        SPUtils.put(getContext(), "htDevice", address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device);
    }

    /**
     * �豸ɨ�践��
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                //��ȡһ�������豸
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    connectDevice(address, true);//���������豸
                }
                break;
            case REQUEST_ENABLE_BT:
                // ������������������
                if (resultCode == Activity.RESULT_OK) {
                    showToast("���������ɹ���");
                } else {
                    showToast("����δ���á������˳�");
                    finish();
                }
        }
    }

    /**
     * �˳�����
     */
    private AlertDialog finishDialog;

    @Override
    protected void finishActivity() {
        if (finishDialog == null) {
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
                    finishDialog.dismiss();
                }
            });
            finishDialog = builder.create();
        }
        finishDialog.show();
    }

    /**
     * ���ô����¼�
     */
    private void addOnTouchListener() {
        setViewAnimation(btnCopyScan);
        setViewAnimation(btnCopyingRead);
        setViewAnimation(btnCopyingStop);
    }


}
