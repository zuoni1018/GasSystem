package com.pl.bll;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceBiz {
	private static final String PREF_KEY_DEVICE_ADDRESS = "device_address";
	private static final String PREF_KEY_USERNAME = "username";
	private SharedPreferences pref;

	public PreferenceBiz(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	// 保存蓝牙连接设备地址
	public void saveDeviceAddress(String address) {
		pref.edit().putString(PREF_KEY_DEVICE_ADDRESS, address).commit();
	}

	// 获取保存的蓝牙连接设备地址
	public String getDeviceAddress() {
		return pref.getString(PREF_KEY_DEVICE_ADDRESS, null);
	}

	// 保存用户名
	public void save(String name) {
		pref.edit().putString(PREF_KEY_USERNAME, name).commit();
	}

	// 移除用户名
	public void remove() {
		pref.edit().remove(PREF_KEY_USERNAME).commit();
	}

	// 获取保存的用户名
	public String getUserName() {
		return pref.getString(PREF_KEY_USERNAME, null);
	}
}
