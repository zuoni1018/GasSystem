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

	// ��ѯ����
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

	// ��ѯ���һ����ı��
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

	// ���ݷ���Ų�ѯ�������
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

	// ��ӷ�����Ϣ
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

	// ���·�����Ϣ
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

	// ɾ��������Ϣ
	public int removeGroupInfo(String groupNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", "groupNo=?",
				new String[] { groupNo });
		db.close();
		return count;
	}

	// �����˲���ɾ��������Ϣ
	public int removeGroupInfoByBookNo(String bookNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", "bookNo=?", new String[] { bookNo });
		db.close();
		return count;
	}

	// ɾ�����з�����Ϣ
	public int removeGroupInfoAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("GroupInfo", null, null);
		db.close();
		return count;
	}
}
