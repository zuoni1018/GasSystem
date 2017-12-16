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
 * ������־
 */

public class HtLogDao {

    private DBOpenHelper helper;

    public HtLogDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    //����һ����־
    public void putSXLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//ʱ���
        values.put("type", type);//���� 0Ϊ����  1λ����
        values.put("message", message);//����
        //�ж����ݿ�ԭ���Ƿ���ڸ�������
        db.insert("SXLOG", null, values);
        db.close();
    }

    public void putKPLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//ʱ���
        values.put("type", type);//���� 0Ϊ����  1λ����
        values.put("message", message);//����
        //�ж����ݿ�ԭ���Ƿ���ڸ�������
        db.insert("KPLOG", null, values);
        db.close();
    }

    public void putWXLog(String time, String type, String message) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);//ʱ���
        values.put("type", type);//���� 0Ϊ����  1λ����
        values.put("message", message);//����
        //�ж����ݿ�ԭ���Ƿ���ڸ�������
        db.insert("WXLOG", null, values);
        db.close();
    }

    /**
     * ͨ�����������ȥ��ѯ���еı�
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
