package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pl.entity.BookInfo;

import java.util.ArrayList;

public class BookInfoDao {

    private DBOpenHelper helper;

    public BookInfoDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    // 查询所有账册
    public ArrayList<BookInfo> getBookInfos() {
        ArrayList<BookInfo> bookInfos = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from BookInfo", null);
        if (cursor != null) {
            bookInfos = new ArrayList<BookInfo>();
            while (cursor.moveToNext()) {
                BookInfo bkInfo = new BookInfo();
                bkInfo.setBookNo(cursor.getString(cursor.getColumnIndex("bookNo")));
                bkInfo.setEstateNo(cursor.getString(cursor.getColumnIndex("estateNo")));
                bkInfo.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
                bkInfo.setStaffNo(cursor.getString(cursor.getColumnIndex("staffNo")));
                bkInfo.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
                bkInfo.setMeterTypeNo(cursor.getString(cursor.getColumnIndex("meterTypeNo")));
                bookInfos.add(bkInfo);
            }
            cursor.close();
        }
        db.close();
        return bookInfos;
    }

    // 根据账册编号查询账册信息
    public BookInfo getBookInfoByBookNo(String bookNo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from BookInfo where bookNo = ?",
                new String[]{bookNo});
        BookInfo bkInfo = null;
        if (cursor != null && cursor.moveToFirst()) {
            bkInfo = new BookInfo();
            bkInfo.setBookNo(cursor.getString(cursor.getColumnIndex("bookNo")));
            bkInfo.setEstateNo(cursor.getString(cursor.getColumnIndex("estateNo")));
            bkInfo.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            bkInfo.setStaffNo(cursor.getString(cursor.getColumnIndex("staffNo")));
            bkInfo.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
            bkInfo.setMeterTypeNo(cursor.getString(cursor.getColumnIndex("meterTypeNo")));
            cursor.close();
        }
        db.close();
        return bkInfo;
    }

    // 查询最后一账册的编号
    public String getLastBookNo() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT bookNo FROM BookInfo ORDER BY bookNo DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        } else {
            return "0000000001";
        }
    }

    // 添加账册信息
    public long addBookInfo(BookInfo bkInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookNo", bkInfo.getBookNo());
        values.put("estateNo", bkInfo.getEstateNo());
        values.put("bookName", bkInfo.getBookName());
        values.put("staffNo", bkInfo.getStaffNo());
        values.put("Remark", bkInfo.getRemark());
        values.put("meterTypeNo", bkInfo.getMeterTypeNo());
        long rowId = db.insert("BookInfo", null, values);
        db.close();
        return rowId;
    }

    // 更新账册信息
    public int updateBookInfo(BookInfo bkInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estateNo", bkInfo.getEstateNo());
        values.put("bookName", bkInfo.getBookName());
        values.put("staffNo", bkInfo.getStaffNo());
        values.put("Remark", bkInfo.getRemark());
        values.put("meterTypeNo", bkInfo.getMeterTypeNo());
        int count = db.update("BookInfo", values, "bookNo=?", new String[]{bkInfo.getBookNo()});
        db.close();
        return count;
    }

    // 删除账册信息
    public int removeBookInfo(String bookNo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("BookInfo", "bookNo=?", new String[]{bookNo});
        db.close();
        return count;
    }

    // 删除所有账册信息
    public int removeBookInfoAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("BookInfo", null, null);
        db.close();
        return count;
    }
}
