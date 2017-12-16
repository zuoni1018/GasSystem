package com.pl.gassystem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pl.dal.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/7
 * 抄表日志
 */

public class HtLogDao {

    private DBOpenHelper helper;

    public HtLogDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    //插入一条日志
    public void putSXLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//时间戳
        values.put("type", type);//类型 0为发送  1位接收
        values.put("message", message);//命令
        //判断数据库原来是否存在该条数据
        db.insert("SXLOG", null, values);
        db.close();
    }

    public void putKPLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//时间戳
        values.put("type", type);//类型 0为发送  1位接收
        values.put("message", message);//命令
        //判断数据库原来是否存在该条数据
        db.insert("KPLOG", null, values);
        db.close();
    }

    public void putWXLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//时间戳
        values.put("type", type);//类型 0为发送  1位接收
        values.put("message", message);//命令
        //判断数据库原来是否存在该条数据
        db.insert("WXLOG", null, values);
        db.close();
    }

    /**
     * 通过集中器编号去查询所有的表
     */
    public List<LogMessage> getLogList(String LogType) {

        List<LogMessage> mLogList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;

        if (LogType.equals("WX")) {
            cursor = db.rawQuery("select * from WXLOG ", null);
        } else if (LogType.equals("KP")) {
            cursor = db.rawQuery("select * from KPLOG ", null);
        } else {
            //SX
            cursor = db.rawQuery("select * from SXLOG ", null);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LogMessage logMessage=new LogMessage();
                logMessage.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                logMessage.setType(cursor.getString(cursor.getColumnIndex("type")));
                logMessage.setTime(cursor.getString(cursor.getColumnIndex("time")));
                mLogList.add(logMessage);
            }
            cursor.close();
        }
        db.close();
        return mLogList;
    }


    public void deleteLogs(String LogType) {
        SQLiteDatabase db = helper.getReadableDatabase();
        switch (LogType) {
            case "WX":
                db.delete("WXLOG",null,null);
                break;
            case "KP":
                db.delete("KPLOG",null,null);
                break;
            default:
                //SX
                db.delete("SXLOG",null,null);
                break;
        }
        db.close();
    }
}
