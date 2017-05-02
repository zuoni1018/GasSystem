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
 * ��������һ���Ի������г���������Ե�װ�ú��豸�ڸõ��������֡� ��һ���豸�����û�ѡ���豸��MAC��ַ���ͻؽ����ͼ�ĸ����
 */
public class DeviceListActivity extends Activity {

	// ����
	private static final String TAG = "DeviceListActivity";
	private static final boolean D = true;

	// ����ʱ���ݱ�ǩ
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// ������
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��������ʾ����
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);// ���ô�����ʾģʽΪ���ڷ�ʽ
		setContentView(R.layout.device_list);

		// ����Ĭ�Ϸ���ֵΪȡ��
		setResult(Activity.RESULT_CANCELED);

		// ��ʼ��ɨ�谴ť�����豸����
		Button scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doDiscovery();
				v.setVisibility(View.GONE);
			}
		});

		// ��ʼ��������������һ�������װ�ú�һ���·��ֵ��豸
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

		// Ѱ�Һͽ���������豸�б�
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Ѱ�Һͽ����·��ֵ��豸�б�
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// ע��ʱ���͹㲥���豸
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// �㲥ʱ���������ע��
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// ��ȡ��������������
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// �õ�����������豸�б�
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// �������Ե��豸�����������豸���б���ʾ
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

		// �رշ������
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// ע���㲥������
		this.unregisterReceiver(mReceiver);
	}

	/**
	 * ��ʼ������豸����
	 */
	private void doDiscovery() {
		if (D) Log.d(TAG, "doDiscovery()");

		// �ڴ�����ʾ��������Ϣ
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.scanning);

		// ��ʾ�����豸��δ����豸���б�
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// �ر��ٽ��еķ������
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// �����¿�ʼ
		mBtAdapter.startDiscovery();
	}

	// ѡ���豸��Ӧ����
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// ׼�������豸���رշ������
			mBtAdapter.cancelDiscovery();

			// ��ȡ�豸��MAC��ַ�������17���ַ�
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			// ��������MAC��ַ��Intent
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			// ���ý�����رյ�ǰActivity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};

	// �������ֵ��豸����������ɺ���ı����BroadcastReceiver
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// ���ҵ�һ���豸
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// ��Intent�����ȡһ��BluetoothDevice����
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// ������Ѿ���ԣ�����������Ϊ���Ѿ�ƥ������б��Ѿ�����ʾ
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				// ��ɨ����ɺ󣬸ı����Activity��Title�����Activity��һ����Բ�ν�������Activity
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
