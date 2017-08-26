package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cache.UserInfo;

/**
 * ������Ϣ���ѯ������
 */
public class UserInfoDao {
    private DBOpenHelper helper;

    public UserInfoDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    //����һ������
    public void putUserInfo(UserInfo userInfo) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //ͨ��
        values.put("tableNumber", userInfo.getTableNumber());
        values.put("userName", userInfo.getUserName());
        values.put("address", userInfo.getAddress());
        values.put("userNum", userInfo.getUserNum());
        values.put("userPhone", userInfo.getUserPhone());
        values.put("tableName", userInfo.getTableName());
        //����
        values.put("xiNingTableNumber", userInfo.getXiNingTableNumber());

        //�������ݿ�
        //��ԭ����ɾ��
        db.delete("UserInfo2", "tableNumber = ?", new String[]{userInfo.getTableNumber()});
        //��������
        db.insert("UserInfo2", null, values);
        db.close();
    }

    private String tableNumber = "";//��߱��
    private String userName = "";//�û���
    private String address = "";//��ַ
    private String userNum = "";//�û����
    private String userPhone = "";//�û��ֻ�����
    private String tableName = "";//�������
    //����
    private String xiNingTableNumber;//������߱�ţ������ı�߸ֺ�Ϊͨ�ð�ı�߱�ţ�

    //��ѯһ���û���Ϣ
    public UserInfo getUserInfo(String tableNumber) {
        UserInfo userInfo = new UserInfo();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from UserInfo2 where tableNumber = ?", new String[]{tableNumber});
        if (cursor != null && cursor.moveToFirst()) {
            //ֻ��һ����������ֻҪ����һ��
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
