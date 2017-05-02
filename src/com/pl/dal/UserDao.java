package com.pl.dal;

import com.pl.entity.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	private DBOpenHelper helper;

	public UserDao(Context context) {
		// TODO 自动生成的构造函数存根
		helper = new DBOpenHelper(context);

	}

	public boolean exists(User user) {
		boolean isExists = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from UserInfo where name =? and password = ?",
				new String[] { user.getName(), user.getPassword() });
		if (cursor != null && cursor.moveToNext()) {
			isExists = true;
			cursor.close();
		}
		db.close();

		return isExists;
	}
}
