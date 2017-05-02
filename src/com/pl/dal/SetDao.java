package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SetDao {
	private DBOpenHelper helper;

	public SetDao(Context context) {
		// TODO 自动生成的构造函数存根
		helper = new DBOpenHelper(context);

	}

	public String getBookInfoUrl() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT BookInfoUrl FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "";
		}
	}

	public String getCopyPhotoUrl() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT CopyPhotoUrl FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "";
		}
	}

	public int getIntervalTime() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT intervalTime FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 300;
		}
	}

	public int getRepeatCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT repeatCount FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}

	public String getWakeupTime() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT wakeupTime FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "40";
		}
	}

	public int getCopyWait() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT copyWait FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getInt(0);
		} else {
			return 150;
		}
	}

	public String getRunMode() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT RunMode FROM SetUp", null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "";
		}
	}

	public int updateBookInfoUrl(String bookInfoUrl) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("BookInfoUrl", bookInfoUrl);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateCopyPhotoUrl(String copyPhotoUrl) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("CopyPhotoUrl", copyPhotoUrl);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateRunMode(String runMode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("RunMode", runMode);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateIntervalTime(int intervalTime) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("intervalTime", intervalTime);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateWakeupTime(String wakeupTime) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("wakeupTime", wakeupTime);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateCopyWait(int copyWait) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("copyWait", copyWait);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

	public int updateCopyTimeSet(int intervalTime, String wakeupTime,
			int copyWait, int repeatCount) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("intervalTime", intervalTime);
		values.put("wakeupTime", wakeupTime);
		values.put("copyWait", copyWait);
		values.put("repeatCount", repeatCount);
		int count = db.update("SetUp", values, null, null);
		db.close();
		return count;
	}

}
