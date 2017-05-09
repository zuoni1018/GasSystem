package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pl.entity.GroupInfo;

import java.util.ArrayList;

public class GroupInfoDao {

	private DBOpenHelper helper;

	public GroupInfoDao(Context context) {
		helper = new DBOpenHelper(context);
	}

	// 查询分组
	public ArrayList<GroupInfo> getGroupInfos(String BookNo) {
		ArrayList<GroupInfo> groupInfos = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from GroupInfo where bookNo = ? order by groupNo desc",
						new String[] { BookNo });
		if (cursor != null) {
			groupInfos = new ArrayList<GroupInfo>();
			while (cursor.moveToNext()) {
				GroupInfo gpInfo = new GroupInfo();
				gpInfo.setGroupNo(cursor.getString(cursor
						.getColumnIndex("groupNo")));
				gpInfo.setEstateNo(cursor.getString(cursor
						.getColumnIndex("estateNo")));
				gpInfo.setGroupName(cursor.getString(cursor
						.getColumnIndex("groupName")));
				gpInfo.setRemark(cursor.getString(cursor
						.getColumnIndex("Remark")));
				gpInfo.setMeterTypeNo(cursor.getString(cursor
						.getColumnIndex("meterTypeNo")));
				gpInfo.setBookNo(BookNo);
				groupInfos.add(gpInfo);
			}
			cursor.close();
		}
		db.close();
		return groupInfos;
	}

	// 查询最后一分组的编号
	public String getLastGroupNo() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT groupNo FROM GroupInfo ORDER BY groupNo DESC LIMIT 1",
				null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "0000000001";
		}
	}

	// 根据分组号查询表具类型
	public String getMeterTypeNoByGroupNo(String groupNo) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select meterTypeNo from GroupInfo where groupNo = ?",
				new String[] { groupNo });
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(0);
		} else {
			return "";
		}
	}

	// 添加分组信息
	public long addGroupInfo(GroupInfo gpInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("groupNo", gpInfo.getGroupNo());
		values.put("estateNo", gpInfo.getEstateNo());
		values.put("groupName", gpInfo.getGroupName());
		values.put("bookNo", gpInfo.getBookNo());
		values.put("Remark", gpInfo.getRemark());
		values.put("meterTypeNo", gpInfo.getMeterTypeNo());
		long rowId = db.insert("GroupInfo", null, values);
		db.close();
		return rowId;
	}

	// 更新分组信息
	public int updateGroupInfo(GroupInfo gpInfo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("estateNo", gpInfo.getEstateNo());
		values.put("groupName", gpInfo.getGroupName());
		values.put("Remark", gpInfo.getRemark());
		values.put("meterTypeNo", gpInfo.getMeterTypeNo());
		int count = db.update("GroupInfo", values, "groupNo=?",
				new String[] { gpInfo.getGroupNo() });
		db.close();
		return count;
	}

	// 删除分组信息
	public int removeGroupInfo(String groupNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", "groupNo=?",
				new String[] { groupNo });
		db.close();
		return count;
	}

	// 根据账册编号删除分组信息
	public int removeGroupInfoByBookNo(String bookNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", "bookNo=?", new String[] { bookNo });
		db.close();
		return count;
	}

	// 删除所有分组信息
	public int removeGroupInfoAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", null, null);
		db.close();
		return count;
	}
}
