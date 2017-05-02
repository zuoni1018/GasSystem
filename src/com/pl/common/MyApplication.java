package com.pl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {

	private List<Activity> activities = new ArrayList<Activity>();
	private MyActivityManager activityManager = null; // activity������
	private Executor excutor = null; // �̳߳�ִ����
	public static String APPCONFIG = ""; // ��������������Ӧ�ó��������ֶ�
	private static MyApplication instance;

	private String devXml;

	private boolean mHasPwd;

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public boolean ismHasPwd() {
		return mHasPwd;
	}

	public void setmHasPwd(boolean mHasPwd) {
		this.mHasPwd = mHasPwd;
	}

	public String getDevXml() {
		return devXml;
	}

	public void setDevXml(String str) {
		this.devXml = str;
	}

	public synchronized static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		activityManager = MyActivityManager.getInstance(); // ���ʵ��
		excutor = Executors.newFixedThreadPool(5); // ӵ�й̶�����5���̳߳�
		super.onCreate();
		mHasPwd = true;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		for (Activity activity : activities) {
			activity.finish();
		}
		System.exit(0);
	}

	public MyActivityManager getActivityManager() {
		return activityManager;
	}

	public Executor getExcutor() {
		return excutor;
	}

	// �ر�ÿһ��list�ڵ�activity
	public void exit() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
