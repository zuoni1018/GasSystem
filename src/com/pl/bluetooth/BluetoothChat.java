/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pl.bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.common.MyActivityManager;
import com.pl.gassystem.DeviceListActivity;
import com.pl.gassystem.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
    // �����õ���־��־TAG���Ƿ��ӡ��־�ı�־D
    private static final String TAG = "BluetoothChat";
    private static boolean D = true;

    // ��BluetoothChatServie���͸�Handler�������Ϣ����
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // ��BluetoothChatService���յ�Handler�ļ�ֵ
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent�������
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    public static final String BluetoothData = "fullscreen";
    public String filename = ""; // ��������洢���ļ���
    private String newCode = "";
    private String newCode2 = "";
    private String fmsg = ""; // ���������ݻ���

    // ������ͼ
    // private TextView mTitle;
    private EditText mInputEditText;
    private EditText mOutEditText;
    private EditText mOutEditText2;
    private Button mSendButton;
    private Button breakButton;
    private CheckBox checkBox_sixteen;
    private CheckBox HEXCheckBox;
    // private ImageView ImageLogoView;

    // �����豸������
    private String mConnectedDeviceName = null;
    // �����������Ի����߳�
    private ArrayAdapter<String> mConversationArrayAdapter;
    // ������Ϣ���ַ���������
    private StringBuffer mOutStringBuffer;
    // ��������������
    private BluetoothAdapter mBluetoothAdapter = null;
    // ��Ա������������
    private BluetoothChatService mChatService = null;

    // ���ñ�ʶ����ѡ���û����ܵ����ݸ�ʽ
    private boolean dialogs;

    // ��һ���������-->����
    private int sum = 1;
    private int UTF = 1;

    //
    String mmsg = "";
    String mmsg2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager.getInstance().pushActivity(this);

        D = false;
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");
        // Log.i(info, "" + dialogs);

        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // ���ô��ڲ���
        setContentView(R.layout.activity_bluetooth_communication);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
        // R.layout.custom_title);
        mInputEditText = (EditText) findViewById(R.id.editText1);
        mInputEditText.setGravity(Gravity.TOP);
        mInputEditText.setSelection(mInputEditText.getText().length(),
                mInputEditText.getText().length());
        mInputEditText.clearFocus();
        mInputEditText.setFocusable(false);
        // ����ImageView
        // ImageLogoView = (ImageView) findViewById(R.id.imagelogo); //hpf ɾ��
        // --20141221
        // ImageLogoView.setImageResource(R.drawable.logo); //hpf ɾ�� --20141221
        // �����ı��ı���
        // mTitle = (TextView) findViewById(R.id.title_left_text);
        // mTitle.setText(R.string.app_name);
        // mTitle = (TextView) findViewById(R.id.title_right_text);

        breakButton = (Button) findViewById(R.id.button_connect);

        // ��ȡ��������������
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // ��ʼ��CheckBox
        checkBox_sixteen = (CheckBox) findViewById(R.id.checkBox_sixteen);
        HEXCheckBox = (CheckBox) findViewById(R.id.HexOut);

        // ���ͼƬ��ת����˾ҳ��
        // hpf ɾ�� --20141221
		/*
		 * ImageLogoView.setOnClickListener(new View.OnClickListener() {
		 *
		 * public void onClick(View v) {
		 *
		 * Intent intent = new Intent();
		 *
		 * intent.setAction(Intent.ACTION_VIEW);
		 *
		 * intent.addCategory(Intent.CATEGORY_BROWSABLE);
		 *
		 * intent.setData(Uri.parse("http://www.hh-ic.com/"));
		 *
		 * startActivity(intent);
		 *
		 * }
		 *
		 * });
		 */

        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // ���������
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        // �����������null,��ô��֧������
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checkBox_sixteen
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // String getValue =
                        // mInputEditText.getText().toString();
                        // if (isChecked) {
                        // mInputEditText.setText(CodeFormat.stringToHex(getValue));
                        //
                        // } else {
                        // mInputEditText.setText(fmsg);
                        //
                        // }
                    }
                });
        HEXCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOutEditText.setText("");
                    mOutEditText.setVisibility(View.GONE);
                    mOutEditText2.setVisibility(View.VISIBLE);
                } else {
                    mOutEditText.setVisibility(View.VISIBLE);
                    mOutEditText2.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (D)
            Log.e(TAG, "++ ON START ++");

        // ���BT���ڵĻ���Ҫ���伤�
        // setupChat��onActivityResult()��������
        if (!mBluetoothAdapter.isEnabled()) {
            // ��Ϊ����������ʾ�������Ч��fu'c'k
            // mBluetoothAdapter.enable();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // �������ûỰ
        } else {
            if (mChatService == null)
                setupChat();
        }
    }

    // ���Ӱ�����Ӧ����
    public void onConnectButtonClicked(View v) {

        if (breakButton.getText().equals("����") || breakButton.getText().equals("connect")) {
            Intent serverIntent = new Intent(this, DeviceListActivity.class); // ��ת��������
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // ���÷��غ궨��
            breakButton.setText(R.string.button_disconnect);

        } else {
            // �ر�����socket
            try {
                // �ر�����
                breakButton.setText(R.string.button_connect);
                mChatService.stop();

            } catch (Exception e) {
            }
        }
        return;
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D)
            Log.e(TAG, "+ ON RESUME +");

        // ִ�����ּ����onResume()���ǵ������û������BT��onStart(),��������ͣ������������
        // onresume()��������ʱ��action_request_enable����ء�
        if (mChatService != null) {
            // ֻ��״̬��STATE_NONE,�����Ѿ�֪��,���ǻ�û��ʼ��
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // ��������Chat Service
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // �����ʼ���������Ի����߳�
        // mConversationArrayAdapter = new ArrayAdapter<String>(this,
        // R.layout.message);
        // ��ʼ������ֶ���һ�����������ؼ�
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
        mOutEditText2 = (EditText) findViewById(R.id.edit_text_out2);
        mOutEditText2.setOnEditorActionListener(mWriteListener);

        // ��ʼ�����Ͱ�ť�������¼���������
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ʹ���ı��༭�ؼ������ݷ�����Ϣ
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                TextView view2 = (TextView) findViewById(R.id.edit_text_out2);
                String message = view.getText().toString();
                String message2 = view2.getText().toString();

                try {
                    message.getBytes("ISO_8859_1");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (HEXCheckBox.isChecked()) {
                    sendMessage(message2);
                } else {
                    sendMessage(message);
                }
            }
        });

        // ��ʼ��bluetoothchatservice������������
        mChatService = new BluetoothChatService(this, mHandler);

        // ��ʼ��������Ϣ������
        mOutStringBuffer = new StringBuffer("");
    }

    public void onMyButtonClick(View view) {
        if (view.getId() == R.id.button_clean) {
            mInputEditText.setText("");
            fmsg = "";
            sum = 0;
        }
        if (view.getId() == R.id.button_connect) {

            onConnectButtonClicked(breakButton);
        }
        if (view.getId() == R.id.button_full_screen) {
            String Data = mInputEditText.getText().toString();
            if (Data.length() > 0) {
                Intent intent = new Intent();
                intent.putExtra(BluetoothData, Data);
                intent.setClass(BluetoothChat.this, FullScreen.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.prompt_message, Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D)

            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ֹͣ�����������
        if (mChatService != null)
            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    // ʹ���������ɼ�
    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// ��ɼ�ʱ��Ϊ300��
            startActivity(discoverableIntent);
        }
    }

    /**
     * ����һ����Ϣ
     *
     * @param message
     *            һ���ı��ַ�������.
     */
    private void sendMessage(String message) {
        // �������ʵ�������κ�����
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // ���ʵ���Ϸ��͵Ķ���
        if (message.length() > 0) {
            // �õ���Ϣ���ֽڣ�����bluetoothchatserviceд
            byte[] send = message.getBytes();
            mChatService.write(send);
            // �������ַ���������Ϊ�����ȷ�ı༭�ı��ֶ�
            // mOutStringBuffer.setLength(0);
            // mOutEditText.setText(mOutStringBuffer);
            // mOutEditText2.setText(mOutStringBuffer);
        }
        // }else if(message.length()<=0){
        // Toast.makeText(BluetoothChat.this, "�����ѶϿ�",
        // Toast.LENGTH_LONG).show();
        // // �û�δ����������������
        // mChatService = new BluetoothChatService(this, mHandler);
        // Intent serverIntent = new Intent(BluetoothChat.this,
        // DeviceListActivity.class);
        // startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        // }
    }

    // EditText��������������,�����س���
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

            // ���������һ���ؼ��¼��ϵķ��ؼ���������Ϣ
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                if (view.getId() == R.id.edit_text_out2) {
                    String tmp = view.getText().toString();
                    String d;
                    for (int i = 0; i < tmp.length(); i++) {
                        d = tmp.charAt(i) + "";
                        if (i % 2 != 0) {
                            d += " ";
                        }
                        sendMessage("\n" + d);
                    }
                }
            }
            if (D)
                Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subTitle);
    }

    // ��BluetoothChatService�����������Ϣ����
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    // if (D)
                    // Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            // mTitle.setText(R.string.title_connected_to);
                            // mTitle.append(mConnectedDeviceName);
                            setStatus(getString(R.string.title_connected_to)
                                    + mConnectedDeviceName);
                            mInputEditText.setText("");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            // mTitle.setText(R.string.title_connecting);
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            // mTitle.setText(R.string.title_not_connected);
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // ����һ���ַ���������
                    String writeMessage = new String(writeBuf);
                    sum = 1;
                    UTF = 1;
                    mmsg += writeMessage;
                    if (checkBox_sixteen.isChecked()) {
                        newCode = CodeFormat.Stringspace("\n<--" + writeMessage
                                + "\n");
                        mInputEditText.getText().append(newCode);
                        fmsg += "\n<--" + newCode + "\n";
                    } else {
                        mInputEditText.getText().append(
                                "\n<--" + writeMessage + "\n");
                        fmsg += "\n<--" + writeMessage + "\n";
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // ����һ���ַ�����Ч�ֽڵĻ�����
                    if (sum == 1) {
                        mInputEditText.getText().append(
                                Html.fromHtml("<font color=\"#00bfff\">"
                                        + "\n-->\n" + "</font>"));
                        fmsg += "\n-->\n";
                        sum++;
                    } else {
                        sum++;
                    }
                    String readMessage = new String(readBuf, 0, msg.arg1);// �Է����������ݽ���String�ٹ��촦��
                    if (checkBox_sixteen.isChecked()) {
                        if (UTF == 1) {
                            newCode2 = CodeFormat.bytesToHexStringTwo(readBuf, 7);
                            mInputEditText.getText().append(
                                    Html.fromHtml("<font color=\"#00bfff\">"
                                            + CodeFormat.Stringspace(newCode2)
                                            + "</font>"));
                            fmsg += Html.fromHtml("<font color=\"#00bfff\">"
                                    + CodeFormat.bytesToHexStringTwo(readBuf, 7)
                                    + "</font>");
                            UTF++;
                        } else {
                            UTF++;
                        }
                    } else {
                        mInputEditText.getText().append(
                                Html.fromHtml("<font color=\"#00bfff\">"
                                        + readMessage + "</font>"));
                        fmsg += Html.fromHtml("<font color=\"#00bfff\">"
                                + readMessage + "</font>");
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // ���������豸������
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "������ " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };

    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // ��Ĭ���ַ���������ַ�����
            byte[] bs = str.getBytes();
            // ���µ��ַ����������ַ���
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * ���ַ�����ת����UTF-8��
     */
    public String toUTF_8(String str) throws UnsupportedEncodingException {
        return this.changeCharset(str, "UTF_8");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (D)
        // Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // ��devicelistactivity�������豸����
                if (resultCode == Activity.RESULT_OK) {
                    // ����豸��ַ
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // ���BluetoothDevice����
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // �������ӵ��豸
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // ������������������
                if (resultCode == Activity.RESULT_OK) {
                    // ���������ã����Խ���һ���Ự
                    setupChat();
                } else {
                    // �û�δ����������������
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                // ����DeviceListActivity�����豸����ɨ��
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.discoverable:
                // ȷ�����豸�ǿɷ��ֵ�
                ensureDiscoverable();
                return true;

            case R.id.setup:
                new AlertDialog.Builder(this)
                        .setTitle("���ÿ�ѡ����")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] { "ʮ������", "�ַ���" }, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        if (dialog.equals("ʮ������")) {
                                            Log.d(TAG, "ʮ������");
                                            dialogs = true;
                                        } else {
                                            dialogs = false;
                                            Log.d(TAG, "�ַ���");
                                        }
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("ȡ��", null).show();
                return true;

            case R.id.clenr:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
            localBuilder.setTitle("����ͨѶ����") // ���logo��.setIcon(R.drawable.logo_hh)
                    .setMessage("��ȷ��Ҫ�˳���");
            localBuilder.setPositiveButton("ȷ��",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            BluetoothChat.this.finish(); // ���˳���������
                        }
                    });
            localBuilder.setNegativeButton("ȡ��",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            paramDialogInterface.cancel();
                        }
                    }).create();
            localBuilder.show();

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }
        return true;
    }

    // ���水����Ӧ����
    public void onSaveButtonClicked(View v) {
        Save();
    }

    // ���湦��ʵ��
    private void Save() {
        // ��ʾ�Ի��������ļ���
        LayoutInflater factory = LayoutInflater.from(this); // ͼ��ģ�����������
        final View DialogView = factory.inflate(R.layout.sname, null); // ��sname.xmlģ��������ͼģ��
        new AlertDialog.Builder(this).setTitle("�ļ���").setView(DialogView) // ������ͼģ��
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() // ȷ��������Ӧ����
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        EditText text1 = (EditText) DialogView
                                .findViewById(R.id.sname); // �õ��ļ����������
                        filename = text1.getText().toString(); // �õ��ļ���

                        try {
                            if (Environment.getExternalStorageState()
                                    .equals(Environment.MEDIA_MOUNTED)) { // ���SD����׼����

                                filename = filename + ".txt"; // ���ļ���ĩβ����.txt
                                File sdCardDir = Environment
                                        .getExternalStorageDirectory(); // �õ�SD����Ŀ¼
                                File BuildDir = new File(sdCardDir,
                                        "/data"); // ��dataĿ¼���粻����������
                                if (BuildDir.exists() == false)
                                    BuildDir.mkdirs();
                                File saveFile = new File(BuildDir,
                                        filename); // �½��ļ���������Ѵ������½��ĵ�
                                FileOutputStream stream = new FileOutputStream(
                                        saveFile); // ���ļ�������
                                stream.write(fmsg.getBytes());
                                stream.close();
                                Toast.makeText(BluetoothChat.this,
                                        "�洢�ɹ���", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(BluetoothChat.this,
                                        "û�д洢����", Toast.LENGTH_LONG)
                                        .show();
                            }

                        } catch (IOException e) {
                            return;
                        }

                    }
                }).setNegativeButton("ȡ��", // ȡ��������Ӧ����,ֱ���˳��Ի������κδ���
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                }).show(); // ��ʾ�Ի���
    }
}