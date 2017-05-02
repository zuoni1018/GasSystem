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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 这个类并设置和管理的所有工作蓝牙连接其他设备。 监听传入连接的线程,一个线程与设备连接,连接时一个线程来执行数据传输。
 * 
 */
public class BluetoothChatService {
	// 调试
	private static final String TAG = "BluetoothChatService";
	private static final boolean D = true;

	// 创建服务器套接字时的SDP记录名称
	private static final String NAME = "BluetoothChat";

	// 独特的UUID这个应用程序
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static StringBuffer hexString = new StringBuffer();

	// 适配器成员
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

	// 常量，用于指示当前连接状态
	public static final int STATE_NONE = 0; // 当前没有可用的连接
	public static final int STATE_LISTEN = 1; // 现在侦听传入的连接
	public static final int STATE_CONNECTING = 2; // 现在开始出站连接
	public static final int STATE_CONNECTED = 3; // 现在连接到一个远程设备
	public static boolean bRun = true;

	/**
	 * 构造函数。准备一个新的bluetoothchat会话。
	 * 
	 * @param context
	 *            用户界面活动上下文
	 * @param handler
	 *            一个处理程序发送消息到用户界面活动
	 */
	public BluetoothChatService(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mState = STATE_NONE;
		mHandler = handler;

	}

	/**
	 * 设置当前状态的聊天连接
	 * 
	 * @param state
	 *            定义当前连接状态的整数
	 */
	private synchronized void setState(int state) {
		if (D)
			// Log.d(TAG, "setState() " + mState + " -> " + state);
			mState = state;

		// 给新状态的Handler，使界面活动可以更新
		mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1)
				.sendToTarget();
	}

	/**
	 * 返回当前的连接状态。
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * 开始聊天服务。特别acceptthread线程启动 开始会话侦听（服务器）模式。所谓的活动onresume()
	 */
	public synchronized void start() {
		if (D)
			// Log.d(TAG, "start");

			// 取消任何线程尝试建立连接
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}

		// 取消任何线程当前正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动线程监听一个bluetoothserversocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
		setState(STATE_LISTEN);
	}

	// 连接按键响应函数
	/**
	 * 启动ConnectThread发起一个连接到远程设备。
	 * 
	 * @param device
	 *            连接的蓝牙设备
	 */
	public synchronized void connect(BluetoothDevice device) {
		if (D)
			// Log.d(TAG, "connect to: " + device);

			// 取消任何线程尝试建立连接
			if (mState == STATE_CONNECTING) {
				if (mConnectThread != null) {
					mConnectThread.cancel();
					mConnectThread = null;
				}
			}

		// 取消任何线程当前正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 启动线程与给定的设备连接
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * 启动ConnectedThread开始管理一个蓝牙连接
	 * 
	 * @param socket
	 *            在连接上的BluetoothSocket
	 * @param device
	 *            已连接的蓝牙设备
	 */
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		if (D)
			// Log.d(TAG, "connected");

			// 取消线程完成连接
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}

		// 取消任何线程当前正在运行的连接
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// 取消接受线程，因为我们只需要连接到一台设备
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		// 启动线程管理连接，并执行传输
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// 将连接设备的名称发送回到界面活动
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	/**
	 * 停止所有的线程
	 */
	public synchronized void stop() {
		if (D)
			// Log.d(TAG, "stop");
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * 
	 * 以非同步方式写入ConnectedThread
	 * 
	 * @param out
	 *            The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// 创建临时对象
		ConnectedThread r;
		// 同步副本的connectedthread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		// 执行写不同步
		r.write(out);
	}

	/**
	 * 
	 * 表明连接尝试失败，并通知用户界面活动。
	 */
	private void connectionFailed() {
		setState(STATE_LISTEN);

		// 发送失败的信息带回Activity
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChat.TOAST, "无法连接设备");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * 
	 * 表明连接丢失，并通知用户界面活动。
	 */
	private void connectionLost() {
		setState(STATE_LISTEN);

		// 发送失败的信息带回Activity
		Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(BluetoothChat.TOAST, "蓝牙设备已断开连接");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * 这个线程运行在侦听传入的连接。 它的行为就像一个服务器端的客户端。 它运行直到连接被接受（或直至取消）。
	 */
	// 蓝牙服务端socket监听线程
	private class AcceptThread extends Thread {
		// 本地服务端Socket
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {

			BluetoothServerSocket tmp = null;

			// 创建一个新的监听服务端Socket
			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				// Log.e(TAG, "listen() failed", e);
			}
			mmServerSocket = tmp;
		}

		@Override
		public void run() {
			if (D)
				// Log.d(TAG, "BEGIN mAcceptThread" + this);
				setName("AcceptThread");
			BluetoothSocket socket = null;

			// 不断的监听server socket，如果我们没有连接
			while (mState != STATE_CONNECTED) {
				try {
					// 这是一个阻塞的方法accept，只会返回一个成功的连接或异常的
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					// Log.e(TAG, "accept() failed", e);
					break;
				}

				// 如果连接成功
				if (socket != null) {
					synchronized (BluetoothChatService.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// 状态正常。开启通信线程ConnectedThread。
							connected(socket, socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// 要么没有准备好或已连接。关闭新建的socket。
							try {
								socket.close();
							} catch (IOException e) {
								// Log.e(TAG, "Could not close unwanted socket",
								// e);
							}
							break;
						}
					}
				}
			}
			// if (D)
			// Log.i(TAG, "END mAcceptThread");
		}

		public void cancel() {
			if (D)
				// Log.d(TAG, "cancel " + this);
				try {
					mmServerSocket.close();
				} catch (IOException e) {
					// Log.e(TAG, "close() of server failed", e);
				}
		}
	}

	/**
	 * 该线程试图让与设备的出站连接运行。 它运行直通;连接成功或失败。
	 */
	// 蓝牙socket连接线程
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// 得到的BluetoothSocket与给定BluetoothDevice类的连接
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				// Log.e(TAG, "create() failed", e);
			}
			mmSocket = tmp;
		}

		@Override
		public void run() {
			// Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// 一定要取消扫描，因为它会减慢连接速度
			mAdapter.cancelDiscovery();

			// 使连接到bluetoothsocket
			try {
				// connect方法也会造成阻塞，只会返回一个成功的连接或异常的。
				mmSocket.connect();
			} catch (IOException e) {
				connectionFailed();
				// 关闭这个socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					// Log.e(TAG,
					// "unable to close() socket during connection failure",
					// e2);
				}
				// 连接失败了，把软件变成监听模式，可以让别的设备来连接
				BluetoothChatService.this.start();
				return;
			}

			// 重置连接线程，因为我们已经完成了
			synchronized (BluetoothChatService.this) {
				mConnectThread = null;
			}

			// 启动通信线程ConnectedThread
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				// Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	/**
	 * 该线程与远程设备的连接过程中运行。 它可以处理所有传入和传出的传输。
	 */
	// 连接后的通信线程
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			// Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// 得到BluetoothSocket的输入和输出流
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				// Log.e(TAG, "没有创建临时sockets", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;

		}

		@Override
		public void run() {
			// Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[1024];
			int bytes;

			// 已经连接上以后持续从通道中监听输入流的情况
			while (true) {
				try {
					// 从通道的输入流InputStream中读取数据到buffer数组中
					bytes = mmInStream.read(buffer);

					// 将获取到数据的消息发送到UI界面，同时也把内容buffer发过去显示
					mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes,
							-1, buffer).sendToTarget();
				} catch (IOException e) {
					// Log.e(TAG, "disconnected", e);
					connectionLost();
					// 开始服务到重启监听modeStart服务重启监听模式
					// BluetoothChatService.this.start();
					break;
				}
			}
		}

		// int num = 0;
		// byte[] buffer = new byte[1024];
		// byte[] buffer_new = new byte[1024];
		// int i = 0;
		// int n = 0;
		// bRun = true;
		// //接收线程
		// while(true){
		// try{
		// while(mmInStream.available()==0){
		// while(bRun == false){}
		// }
		// while(true){
		// num = mmInStream.read(buffer); //读入数据
		// n=0;
		//
		// String s0 = new String(buffer,0,num);
		// fmsg+=s0; //保存收到数据
		// for(i=0;i<num;i++){
		// if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
		// buffer_new[n] = 0x0a;
		// i++;
		// }else{
		// buffer_new[n] = buffer[i];
		// }
		// n++;
		// }
		// String s = new String(buffer_new,0,n);
		//
		// smsg += s; //写入接收缓存

		// if(mmInStream.available()==0)break; //短时间没有数据才跳出进行显示
		// //发送显示消息，进行显示刷新
		//
		// }
		//
		// }catch(IOException e){
		// }
		//
		// }

		// }

		/**
		 * 写输出的连接。
		 * 
		 * @param buffer
		 *            将要写入的字节
		 */
		public void write(byte[] buffer) {
			try {

				mmOutStream.write(buffer);
				// 用于将自己发送给对方的内容也在UI界面显示
				mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1,
						buffer).sendToTarget();
			} catch (IOException e) {
				// Log.e(TAG, "Exception during write", e);
			}

		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				// Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}
}
