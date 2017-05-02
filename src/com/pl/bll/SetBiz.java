package com.pl.bll;

import com.pl.dal.SetDao;

import android.content.Context;

public class SetBiz {

	private SetDao dao;

	public SetBiz(Context context) {
		dao = new SetDao(context);
	}

	public String getBookInfoUrl() {
		return dao.getBookInfoUrl();
	}

	public String getCopyPhotoUrl() {
		return dao.getCopyPhotoUrl();
	}

	public String getRunMode() {
		return dao.getRunMode();
	}

	public int getIntervalTime() {
		return dao.getIntervalTime();
	}

	public int getRepeatCount() {
		return dao.getRepeatCount();
	}

	public String getWakeupTime() {
		return dao.getWakeupTime();
	}

	public int getCopyWait() {
		return dao.getCopyWait();
	}

	public int updateBookInfoUrl(String bookInfoUrl) {
		return dao.updateBookInfoUrl(bookInfoUrl);
	}

	public int updateCopyPhotoUrl(String copyPhotoUrl) {
		return dao.updateCopyPhotoUrl(copyPhotoUrl);
	}

	public int updateRunMode(String runMode) {
		return dao.updateRunMode(runMode);
	}

	public int updateIntervalTime(int intervalTime) {
		return dao.updateIntervalTime(intervalTime);
	}

	public int updateWakeupTime(String wakeupTime) {
		return dao.updateWakeupTime(wakeupTime);
	}

	public int updateCopyWait(int copyWait) {
		return dao.updateCopyWait(copyWait);
	}

	public int updateCopyTimeSet(int intervalTime, String wakeupTime,
			int copyWait, int repeatCount) {
		return dao.updateCopyTimeSet(intervalTime, wakeupTime, copyWait,
				repeatCount);
	}

}
