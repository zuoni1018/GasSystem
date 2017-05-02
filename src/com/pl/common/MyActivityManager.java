package com.pl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

/**
 * Activity����������ֹactivity��ת����
 * 
 * @author��
 * @date��
 */
public class MyActivityManager {

	/**
	 * ����activity��Stack
	 */
	private static MyActivityManager activityManager = null;
	private static Stack<Activity> activityStack = null;

	private MyActivityManager() {
	}

	/**
	 * ��ʵ��
	 * 
	 * @return
	 */
	public static MyActivityManager getInstance() {
		if (activityManager == null) {
			activityManager = new MyActivityManager();
		}
		return activityManager;
	}

	/**
	 * �ѵ�ǰActivity����ջ��
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * �ѵ�ǰActivity�Ƴ�ջ
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * ����ָ��activity
	 * 
	 * @param activity
	 */
	public void endActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * ��õ�ǰ��activity(�����ϲ�)
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty())
			activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * ������cls�������activity
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class<? extends Activity> cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	/**
	 * ������cls֮�������activity,ִ�н���������Stack
	 * 
	 * @param cls
	 */
	public void finishAllActivityExceptOne(Class<? extends Activity> cls) {
		while (!activityStack.empty()) {
			Activity activity = currentActivity();
			if (activity.getClass().equals(cls)) {
				popActivity(activity);
			} else {
				endActivity(activity);
			}
		}
	}

	/**
	 * ��������activity
	 */
	public void finishAllActivity() {
		while (!activityStack.empty()) {
			Activity activity = currentActivity();
			endActivity(activity);
		}
	}

	/**
	 * �˳�ϵͳ
	 */
	public void exit() {
		while (!activityStack.isEmpty()) {
			endActivity(activityStack.pop());
		}
		System.exit(0);
	}

	/**
	 * ���ĳActivity�Ƿ��ڵ�ǰTask��ջ��
	 * 
	 * @param cmdName
	 * @param context
	 * @return
	 */
	public static boolean isTopActivy(String cmdName, Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager
				.getRunningTasks(Integer.MAX_VALUE);
		String cmpNameTemp = null;
		if (null != runningTaskInfos) {
			cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
		}
		if (null == cmpNameTemp) {
			return false;
		}
		return cmpNameTemp.equals(cmdName);
	}

	/**
	 * �ж�AndroidӦ���Ƿ���ǰ̨
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		List<RecentTaskInfo> appTask = activityManager.getRecentTasks(
				Integer.MAX_VALUE, 1);
		if (appTask == null) {
			return false;
		}
		if (appTask.get(0).baseIntent.toString().contains(packageName)) {
			return true;
		}
		return false;
	}

	/**
	 * ���ص�ǰ��Ӧ���Ƿ���ǰ̨��ʾ״̬
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean isTopActivity2(Context context, String packageName) {
		// context��һ�������������
		ActivityManager __am = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> __list = __am
				.getRunningAppProcesses();
		if (__list.size() == 0)
			return false;
		for (ActivityManager.RunningAppProcessInfo __process : __list) {
			Log.d("sms", Integer.toString(__process.importance));
			Log.d("sms", __process.processName);
			if (__process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& __process.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡAndroid�ֻ��ڰ�װ����������
	 * 
	 * @param context
	 * @return
	 */
	private static List<String> getAllTheLauncher(Context context) {
		List<String> names = null;
		PackageManager pkgMgt = context.getPackageManager();
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
		if (ra.size() != 0) {
			names = new ArrayList<String>();
		}
		for (int i = 0; i < ra.size(); i++) {
			String packageName = ra.get(i).activityInfo.packageName;
			names.add(packageName);
		}
		return names;
	}

	/**
	 * Android �жϳ���ǰ��̨״̬
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isLauncherRunnig(Context context) {
		boolean result = false;
		List<String> names = getAllTheLauncher(context);
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appList = mActivityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList) {
			if (running.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (int i = 0; i < names.size(); i++) {
					if (names.get(i).equals(running.processName)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
}
