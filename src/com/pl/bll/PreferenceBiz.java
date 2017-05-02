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

	// �������������豸��ַ
	public void saveDeviceAddress(String address) {
		pref.edit().putString(PREF_KEY_DEVICE_ADDRESS, address).commit();
	}

	// ��ȡ��������������豸��ַ
	public String getDeviceAddress() {
		return pref.getString(PREF_KEY_DEVICE_ADDRESS, null);
	}

	// �����û���
	public void save(String name) {
		pref.edit().putString(PREF_KEY_USERNAME, name).commit();
	}

	// �Ƴ��û���
	public void remove() {
		pref.edit().remove(PREF_KEY_USERNAME).commit();
	}

	// ��ȡ������û���
	public String getUserName() {
		return pref.getString(PREF_KEY_USERNAME, null);
	}
}
