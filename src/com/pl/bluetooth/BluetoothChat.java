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
    // 调试用的日志标志TAG与是否打印日志的标志D
    private static final String TAG = "BluetoothChat";
    private static boolean D = true;

    // 从BluetoothChatServie发送给Handler处理的消息类型
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // 从BluetoothChatService接收到Handler的键值
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent请求代码
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    public static final String BluetoothData = "fullscreen";
    public String filename = ""; // 用来保存存储的文件名
    private String newCode = "";
    private String newCode2 = "";
    private String fmsg = ""; // 保存用数据缓存

    // 布局视图
    // private TextView mTitle;
    private EditText mInputEditText;
    private EditText mOutEditText;
    private EditText mOutEditText2;
    private Button mSendButton;
    private Button breakButton;
    private CheckBox checkBox_sixteen;
    private CheckBox HEXCheckBox;
    // private ImageView ImageLogoView;

    // 连接设备的名称
    private String mConnectedDeviceName = null;
    // 数组适配器对话的线程
    private ArrayAdapter<String> mConversationArrayAdapter;
    // 传出消息的字符串缓冲区
    private StringBuffer mOutStringBuffer;
    // 本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = null;
    // 成员对象的聊天服务
    private BluetoothChatService mChatService = null;

    // 设置标识符，选择用户接受的数据格式
    private boolean dialogs;

    // 第一次输入加入-->变量
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
        // 设置窗口布局
        setContentView(R.layout.activity_bluetooth_communication);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
        // R.layout.custom_title);
        mInputEditText = (EditText) findViewById(R.id.editText1);
        mInputEditText.setGravity(Gravity.TOP);
        mInputEditText.setSelection(mInputEditText.getText().length(),
                mInputEditText.getText().length());
        mInputEditText.clearFocus();
        mInputEditText.setFocusable(false);
        // 设置ImageView
        // ImageLogoView = (ImageView) findViewById(R.id.imagelogo); //hpf 删除
        // --20141221
        // ImageLogoView.setImageResource(R.drawable.logo); //hpf 删除 --20141221
        // 设置文本的标题
        // mTitle = (TextView) findViewById(R.id.title_left_text);
        // mTitle.setText(R.string.app_name);
        // mTitle = (TextView) findViewById(R.id.title_right_text);

        breakButton = (Button) findViewById(R.id.button_connect);

        // 获取本地蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 初始化CheckBox
        checkBox_sixteen = (CheckBox) findViewById(R.id.checkBox_sixteen);
        HEXCheckBox = (CheckBox) findViewById(R.id.HexOut);

        // 点击图片跳转到公司页面
        // hpf 删除 --20141221
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
            // 隐藏软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        // 如果适配器是null,那么不支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
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

        // 如果BT不在的话，要求将其激活。
        // setupChat在onActivityResult()将被调用
        if (!mBluetoothAdapter.isEnabled()) {
            // 以为这样会无提示，结果无效，fu'c'k
            // mBluetoothAdapter.enable();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // 否则，设置会话
        } else {
            if (mChatService == null)
                setupChat();
        }
    }

    // 连接按键响应函数
    public void onConnectButtonClicked(View v) {

        if (breakButton.getText().equals("连接") || breakButton.getText().equals("connect")) {
            Intent serverIntent = new Intent(this, DeviceListActivity.class); // 跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // 设置返回宏定义
            breakButton.setText(R.string.button_disconnect);

        } else {
            // 关闭连接socket
            try {
                // 关闭蓝牙
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

        // 执行这种检查在onResume()覆盖的情况下没有启用BT在onStart(),所以我们停下来启用它…
        // onresume()将被调用时，action_request_enable活动返回。
        if (mChatService != null) {
            // 只有状态是STATE_NONE,我们已经知道,我们还没开始做
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 启动蓝牙Chat Service
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // 数组初始化适配器对话的线程
        // mConversationArrayAdapter = new ArrayAdapter<String>(this,
        // R.layout.message);
        // 初始化组合字段与一个侦听器返回键
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
        mOutEditText2 = (EditText) findViewById(R.id.edit_text_out2);
        mOutEditText2.setOnEditorActionListener(mWriteListener);

        // 初始化发送按钮，单击事件的侦听器
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用文本编辑控件的内容发送消息
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

        // 初始化bluetoothchatservice进行蓝牙连接
        mChatService = new BluetoothChatService(this, mHandler);

        // 初始化传出消息缓冲区
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
        // 停止蓝牙聊天服务
        if (mChatService != null)
            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    // 使本机蓝牙可见
    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// 求可见时间为300秒
            startActivity(discoverableIntent);
        }
    }

    /**
     * 发送一个消息
     *
     * @param message
     *            一个文本字符串发送.
     */
    private void sendMessage(String message) {
        // 检查我们实际上在任何连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查实际上发送的东西
        if (message.length() > 0) {
            // 得到消息的字节，告诉bluetoothchatservice写
            byte[] send = message.getBytes();
            mChatService.write(send);
            // 重置了字符串缓冲区为零和明确的编辑文本字段
            // mOutStringBuffer.setLength(0);
            // mOutEditText.setText(mOutStringBuffer);
            // mOutEditText2.setText(mOutStringBuffer);
        }
        // }else if(message.length()<=0){
        // Toast.makeText(BluetoothChat.this, "连接已断开",
        // Toast.LENGTH_LONG).show();
        // // 用户未启用蓝牙或发生错误
        // mChatService = new BluetoothChatService(this, mHandler);
        // Intent serverIntent = new Intent(BluetoothChat.this,
        // DeviceListActivity.class);
        // startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        // }
    }

    // EditText部件动作监听器,监听回车键
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {

            // 如果动作是一个关键事件上的返回键，发送消息
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

    // 从BluetoothChatService处理程序获得信息返回
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
                    // 构建一个字符串缓冲区
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
                    // 构建一个字符串有效字节的缓冲区
                    if (sum == 1) {
                        mInputEditText.getText().append(
                                Html.fromHtml("<font color=\"#00bfff\">"
                                        + "\n-->\n" + "</font>"));
                        fmsg += "\n-->\n";
                        sum++;
                    } else {
                        sum++;
                    }
                    String readMessage = new String(readBuf, 0, msg.arg1);// 对发过来的数据进行String再构造处理
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
                    // 保存连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "已连接 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
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
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 将字符编码转换成UTF-8码
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
                // 当devicelistactivity返回与设备连接
                if (resultCode == Activity.RESULT_OK) {
                    // 获得设备地址
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 获得BluetoothDevice对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 尝试连接到设备
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // 当请求启用蓝牙返回
                if (resultCode == Activity.RESULT_OK) {
                    // 蓝牙已启用，所以建立一个会话
                    setupChat();
                } else {
                    // 用户未启用蓝牙或发生错误
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
                // 启动DeviceListActivity看到设备和做扫描
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.discoverable:
                // 确保该设备是可发现的
                ensureDiscoverable();
                return true;

            case R.id.setup:
                new AlertDialog.Builder(this)
                        .setTitle("设置可选参数")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] { "十六进制", "字符串" }, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        if (dialog.equals("十六进制")) {
                                            Log.d(TAG, "十六进制");
                                            dialogs = true;
                                        } else {
                                            dialogs = false;
                                            Log.d(TAG, "字符串");
                                        }
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", null).show();
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
            localBuilder.setTitle("蓝牙通讯测试") // 添加logo：.setIcon(R.drawable.logo_hh)
                    .setMessage("你确定要退出吗？");
            localBuilder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            BluetoothChat.this.finish(); // 不退出蓝牙连接
                        }
                    });
            localBuilder.setNegativeButton("取消",
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

    // 保存按键响应函数
    public void onSaveButtonClicked(View v) {
        Save();
    }

    // 保存功能实现
    private void Save() {
        // 显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(this); // 图层模板生成器句柄
        final View DialogView = factory.inflate(R.layout.sname, null); // 用sname.xml模板生成视图模板
        new AlertDialog.Builder(this).setTitle("文件名").setView(DialogView) // 设置视图模板
                .setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        EditText text1 = (EditText) DialogView
                                .findViewById(R.id.sname); // 得到文件名输入框句柄
                        filename = text1.getText().toString(); // 得到文件名

                        try {
                            if (Environment.getExternalStorageState()
                                    .equals(Environment.MEDIA_MOUNTED)) { // 如果SD卡已准备好

                                filename = filename + ".txt"; // 在文件名末尾加上.txt
                                File sdCardDir = Environment
                                        .getExternalStorageDirectory(); // 得到SD卡根目录
                                File BuildDir = new File(sdCardDir,
                                        "/data"); // 打开data目录，如不存在则生成
                                if (BuildDir.exists() == false)
                                    BuildDir.mkdirs();
                                File saveFile = new File(BuildDir,
                                        filename); // 新建文件句柄，如已存在仍新建文档
                                FileOutputStream stream = new FileOutputStream(
                                        saveFile); // 打开文件输入流
                                stream.write(fmsg.getBytes());
                                stream.close();
                                Toast.makeText(BluetoothChat.this,
                                        "存储成功！", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(BluetoothChat.this,
                                        "没有存储卡！", Toast.LENGTH_LONG)
                                        .show();
                            }

                        } catch (IOException e) {
                            return;
                        }

                    }
                }).setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                }).show(); // 显示对话框
    }
}