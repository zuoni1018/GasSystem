package com.pl.gassystem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * 这项活动出现一个对话框。它列出了所有配对的装置和设备在该地区检测后发现。 当一个设备是由用户选择，设备的MAC地址发送回结果意图的父活动。
 */
public class DeviceListActivity extends Activity {

	// 调试
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	// 返回时数据标签
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// 适配器
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 创建并显示窗口
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);// 设置窗口显示模式为窗口方式
		setContentView(R.layout.device_list);

		// 设置默认返回值为取消
		setResult(Activity.RESULT_CANCELED);

		// 初始化扫描按钮进行设备发现
		Button scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		// 初始化数组适配器。一个已配对装置和一个新发现的设备
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

		// 寻找和建立的配对设备列表
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// 寻找和建立新发现的设备列表
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// 注册时发送广播给设备
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// 广播时发现已完成注册
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// 获取本地蓝牙适配器
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// 得到已配对蓝牙设备列表
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// 如果有配对的设备，添加已配对设备到列表并显示
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.none_paired).toString();
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 关闭服务查找
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// 注销广播接收器
		this.unregisterReceiver(mReceiver);
	}

	/**
	 * 开始服务和设备查找
	 */
	private void doDiscovery() {
		if (D) Log.d(TAG, "doDiscovery()");

		// 在窗口显示查找中信息
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		// 显示其它设备（未配对设备）列表
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// 关闭再进行的服务查找
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// 并重新开始
		mBtAdapter.startDiscovery();
	}

	// 选择设备响应函数
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// 准备连接设备，关闭服务查找
			mBtAdapter.cancelDiscovery();

			// 获取设备的MAC地址，以最后17个字符
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// 创建包含MAC地址的Intent
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			// 设置结果并关闭当前Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};

	// 侦听发现的设备，当发现完成后更改标题的BroadcastReceiver
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// 当找到一个设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 从Intent里面获取一个BluetoothDevice对象
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果它已经配对，跳过它，因为在已经匹配过的列表已经有显示
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				// 当扫描完成后，改变这个Activity的Title，这个Activity是一个带圆形进度条的Activity
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}
	};
}
