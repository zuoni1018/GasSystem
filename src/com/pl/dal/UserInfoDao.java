package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cache.UserInfo;

/**
 * 个人信息表查询辅助类
 */
public class UserInfoDao {
    private DBOpenHelper helper;

    public UserInfoDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    //插入一条数据
    public void putUserInfo(UserInfo userInfo) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //通用
        values.put("tableNumber", userInfo.getTableNumber());
        values.put("userName", userInfo.getUserName());
        values.put("address", userInfo.getAddress());
        values.put("userNum", userInfo.getUserNum());
        values.put("userPhone", userInfo.getUserPhone());
        values.put("tableName", userInfo.getTableName());
        //西宁
        values.put("xiNingTableNumber", userInfo.getXiNingTableNumber());

        //插入数据库
        //把原来的删掉
        db.delete("UserInfo2", "tableNumber = ?", new String[]{userInfo.getTableNumber()});
        //插入数据
        db.insert("UserInfo2", null, values);
        db.close();
    }

    private String tableNumber = "";//表具编号
    private String userName = "";//用户名
    private String address = "";//地址
    private String userNum = "";//用户编号
    private String userPhone = "";//用户手机号码
    private String tableName = "";//表具名称
    //西宁
    private String xiNingTableNumber;//西宁表具编号（西宁的表具钢号为通用版的表具编号）

    //查询一条用户信息
    public UserInfo getUserInfo(String tableNumber) {
        UserInfo userInfo = new UserInfo();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from UserInfo2 where tableNumber = ?", new String[]{tableNumber});
        if (cursor != null && cursor.moveToFirst()) {
            //只有一行数据所以只要遍历一次
            userInfo.setTableNumber(cursor.getString(cursor.getColumnIndex("tableNumber")));
            userInfo.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            userInfo.setAddress(cursor.getString(cursor.getColumnIndex("address")));

            userInfo.setUserNum(cursor.getString(cursor.getColumnIndex("userNum")));
            userInfo.setUserPhone(cursor.getString(cursor.getColumnIndex("userPhone")));
            userInfo.setTableName(cursor.getString(cursor.getColumnIndex("tableName")));
            userInfo.setXiNingTableNumber(cursor.getString(cursor.getColumnIndex("xiNingTableNumber")));
        }
        return userInfo;
    }


}
