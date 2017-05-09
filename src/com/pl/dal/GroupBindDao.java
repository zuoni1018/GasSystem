package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pl.entity.GroupBind;

import java.util.ArrayList;

public class GroupBindDao {

	private DBOpenHelper helper;

	public GroupBindDao(Context context) {
		helper = new DBOpenHelper(context);
	}

	// 根据分组编号查询表具
	public ArrayList<GroupBind> getGroupBindByGroupNo(String groupNo) {
		ArrayList<GroupBind> groupBinds = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from GroupBind where groupNo = ?",
				new String[] { groupNo });
		if (cursor != null) {
			groupBinds = new ArrayList<GroupBind>();
			while (cursor.moveToNext()) {
				GroupBind gpBind = new GroupBind();
				gpBind.setGroupNo(cursor.getString(cursor
						.getColumnIndex("groupNo")));
				gpBind.setMeterNo(cursor.getString(cursor
						.getColumnIndex("meterNo")));
				gpBind.setMeterName(cursor.getString(cursor
						.getColumnIndex("meterName")));
				gpBind.setMeterType(cursor.getString(cursor
						.getColumnIndex("MeterType")));
				groupBinds.add(gpBind);
			}
			cursor.close();
		}
		db.close();
		return groupBinds;
	}

	public ArrayList<GroupBind> getGroupBindByGroupName(String id, String name) {
		ArrayList<GroupBind> groupBinds = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from GroupBind where groupNo=? and meterName like ?",
				new String[] { id, name });
		// select * from GroupBind where groupNo=? and meterName like %?%
		System.out.println("cursor" + cursor.getCount());
		if (cursor != null) {
			groupBinds = new ArrayList<GroupBind>();
			while (cursor.moveToNext()) {
				GroupBind gpBind = new GroupBind();
				gpBind.setGroupNo(cursor.getString(cursor
						.getColumnIndex("groupNo")));
				gpBind.setMeterNo(cursor.getString(cursor
						.getColumnIndex("meterNo")));
				gpBind.setMeterName(cursor.getString(cursor
						.getColumnIndex("meterName")));
				gpBind.setMeterType(cursor.getString(cursor
						.getColumnIndex("MeterType")));
				groupBinds.add(gpBind);
			}
			cursor.close();
		}
		db.close();
		return groupBinds;
	}

	// 根据分组编号查询表号
	public ArrayList<String> getMeterNoByGroupNo(String groupNo) {
		ArrayList<String> meterNos = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select meterNo from GroupBind where groupNo = ?",
				new String[] { groupNo });
		if (cursor != null) {
			meterNos = new ArrayList<String>();
			while (cursor.moveToNext()) {
				meterNos.add(cursor.getString(cursor.getColumnIndex("meterNo")));
			}
			cursor.close();
		}
		db.close();
		return meterNos;
	}

	// 获得库内所有绑定表具
	public ArrayList<GroupBind> getGroupBindAll() {
		ArrayList<GroupBind> groupBinds = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from GroupBind order by groupNo",
				null);
		if (cursor != null) {
			groupBinds = new ArrayList<GroupBind>();
			while (cursor.moveToNext()) {
				GroupBind gpBind = new GroupBind();
				gpBind.setGroupNo(cursor.getString(cursor
						.getColumnIndex("groupNo")));
				gpBind.setMeterNo(cursor.getString(cursor
						.getColumnIndex("meterNo")));
				gpBind.setMeterName(cursor.getString(cursor
						.getColumnIndex("meterName")));
				groupBinds.add(gpBind);
			}
			cursor.close();
		}
		db.close();
		return groupBinds;
	}

	// 添加绑定信息
	public long addGroupBind(GroupBind gpBind) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("groupNo", gpBind.getGroupNo());
		values.put("meterNo", gpBind.getMeterNo());
		values.put("meterName", gpBind.getMeterName());
		values.put("MeterType", gpBind.getMeterType());
		long rowId = db.insert("GroupBind", null, values);
		db.close();
		return rowId;
	}

	// // 更新绑定信息
	// public int updateGroupBind(GroupBind gpBind) {
	// SQLiteDatabase db = helper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put("groupNo", gpBind.getGroupNo());
	// values.put("meterNo", gpBind.getMeterNo());
	// values.put("meterName", gpBind.getMeterName());
	// int count = db.update("GroupBind", values, "meterNo=?",
	// new String[] { gpBind.getMeterNo() });
	// db.close();
	// return count;
	// }

	// 删除绑定信息
	public int removeGroupBind(String meterNo, String groupNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupBind", "meterNo=? and groupNo=?",
				new String[] { meterNo, groupNo });
		db.close();
		return count;
	}

	// 根据分组号删除绑定信息
	public int removeGroupBindByGroupNo(String groupNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupBind", "groupNo=?",
				new String[] { groupNo });
		db.close();
		return count;
	}

	// 删除所有绑定信息
	public int removeGroupBindAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupBind", null, null);
		db.close();
		return count;
	}
}
