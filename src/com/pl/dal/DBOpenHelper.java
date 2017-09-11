package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pl.utils.LogUtil;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "gasSystem.db", null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建登录用户表，并初始化登录账户
        db.execSQL("create table UserInfo ("
                + "_id integer primary key autoincrement ,"
                + "name text not null," + "password text )");
        ContentValues values = new ContentValues();
        values.put("name", "admin");
        values.put("password", "123456");
        db.insert("UserInfo", null, values);
        // 创建账册表
        String sql = "CREATE TABLE BookInfo (" + "bookNo  TEXT NOT NULL,"
                + "estateNo  TEXT, bookName  TEXT NOT NULL,"
                + "staffNo  TEXT, Remark  TEXT, meterTypeNo  TEXT NOT NULL,"
                + " PRIMARY KEY (bookNo) )";
        db.execSQL(sql);
        // 创建分组表
        sql = "CREATE TABLE GroupInfo (" + "groupNo  TEXT NOT NULL,"
                + "estateNo  TEXT, groupName TEXT NOT NULL,"
                + "Remark  TEXT, meterTypeNo  TEXT NOT NULL,"
                + "bookNo  TEXT NOT NULL, PRIMARY KEY (groupNo) )";
        db.execSQL(sql);
        // 创建分组表计绑定表
        sql = "CREATE TABLE  GroupBind ("
                + "groupNo  TEXT NOT NULL, meterNo TEXT NOT NULL, meterName TEXT,MeterType TEXT)";
        db.execSQL(sql);
        // 创建无线数据表
        sql = "CREATE TABLE CopyData ("
                + "_id integer primary key autoincrement ,"
                + "meterNo TEXT NOT NULL, lastShow TEXT,"
                + "lastDosage TEXT, currentShow TEXT,"
                + "currentDosage TEXT,unitPrice TEXT,"
                + "printFlag integer,meterState integer,"
                + "copyWay TEXT, copyState integer,"
                + "copyTime TEXT, copyMan TEXT,"
                + "Operator TEXT, operateTime TEXT,"
                + "isBalance integer, Remark TEXT, meterName TEXT,"
                + "dBm TEXT, elec TEXT )";
        db.execSQL(sql);
        // 创建IC无线数据表
        sql = "CREATE TABLE CopyDataICRF ("
                + "_id integer primary key autoincrement ,"
                + "meterNo TEXT NOT NULL, Cumulant TEXT,"
                + "No01 TEXT, No02 TEXT, name TEXT,"
                + "SurplusMoney TEXT, OverZeroMoney TEXT,"
                + "BuyTimes integer, OverFlowTimes integer,"
                + "MagAttTimes integer,CardAttTimes integer,"
                + "MeterState integer, StateMessage TEXT,"
                + "CurrMonthTotal TEXT, Last1MonthTotal TEXT,"
                + "Last2MonthTotal TEXT, Last3MonthTotal TEXT,"
                + "copyWay TEXT, copyTime TEXT,"
                + "copyMan TEXT,copyState integer,meterName TEXT,"
                + "dBm TEXT, elec TEXT, unitPrice TEXT, accMoney TEXT, accBuyMoney TEXT, currentShow TEXT )";
        db.execSQL(sql);
        // 创建摄像表数据表
        sql = "CREATE TABLE CopyDataPhoto ("
                + "_id integer primary key autoincrement ,"
                + "CommunicateNo TEXT NOT NULL, ReadState integer,"
                + "ImageName TEXT, CollectorNo TEXT,"
                + "Operater TEXT, ReadTime TEXT,"
                + "DevState TEXT, DevPower TEXT,"
                + "OcrRead TEXT, OcrState integer,"
                + "ThisRead TEXT, OcrResult TEXT,"
                + "OcrTime TEXT, CreateTime TEXT, meterName TEXT )";
        db.execSQL(sql);
        // 创建系统设置表
        sql = "CREATE TABLE SetUp ("
                + "_id integer primary key autoincrement ,"
                + "BookInfoUrl TEXT, CopyPhotoUrl TEXT, "
                + "RunMode TEXT, intervalTime integer, wakeupTime TEXT, copyWait integer, repeatCount integer)";
        db.execSQL(sql);
        //新建用户信息表
        sql = "CREATE TABLE UserInfo2 ("
                + "_id integer primary key autoincrement ,"
                + "tableNumber TEXT, userName TEXT, "
                + "address TEXT, userNum TEXT, userPhone TEXT, tableName TEXT, xiNingTableNumber TEXT, xiNingUnitPrice TEXT)";
        db.execSQL(sql);

        //新建集中器抄表  表号绑定表信息
        sql = "CREATE TABLE CtBookInfo ("
                + "_id integer primary key autoincrement ,"
                + "MeterNo TEXT, CommunicateNo TEXT, "
                + "meterTypeNo TEXT, address TEXT, CollectorNo TEXT)";
        db.execSQL(sql);

        // 创建无线数据表
        sql = "CREATE TABLE CtCopyData ("
                + "_id integer primary key autoincrement ,"
                + "meterNo TEXT NOT NULL, lastShow TEXT,"
                + "lastDosage TEXT, currentShow TEXT,"
                + "currentDosage TEXT,unitPrice TEXT,"
                + "printFlag integer,meterState integer,"
                + "copyWay TEXT, copyState integer,"
                + "copyTime TEXT, copyMan TEXT,"
                + "Operator TEXT, operateTime TEXT,"
                + "isBalance integer, Remark TEXT, meterName TEXT,"
                + "dBm TEXT, elec TEXT ,CommunicateNo TEXT, CollectorNo TEXT)";
        db.execSQL(sql);

        // 创建IC无线数据表
        sql = "CREATE TABLE CtCopyDataICRF ("
                + "_id integer primary key autoincrement ,"
                + "meterNo TEXT NOT NULL, Cumulant TEXT,"
                + "No01 TEXT, No02 TEXT, name TEXT,"
                + "SurplusMoney TEXT, OverZeroMoney TEXT,"
                + "BuyTimes integer, OverFlowTimes integer,"
                + "MagAttTimes integer,CardAttTimes integer,"
                + "MeterState integer, StateMessage TEXT,"
                + "CurrMonthTotal TEXT, Last1MonthTotal TEXT,"
                + "Last2MonthTotal TEXT, Last3MonthTotal TEXT,"
                + "copyWay TEXT, copyTime TEXT,"
                + "copyMan TEXT,copyState integer,meterName TEXT,"
                + "dBm TEXT, elec TEXT, unitPrice TEXT, accMoney TEXT, accBuyMoney TEXT, currentShow TEXT ,CommunicateNo TEXT, CollectorNo TEXT)";
        db.execSQL(sql);

        values.clear();
        values.put("BookInfoUrl", "http://app.hh-ic.com/");
        values.put("CopyPhotoUrl", "http://app.hh-ic.com/");
        values.put("RunMode", "1");
        values.put("intervalTime", 200);
        values.put("wakeupTime", "30");
        values.put("copyWait", 2000);
        values.put("repeatCount", 0);
        db.insert("SetUp", null, values);
        System.out.println("数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 自动生成的方法存根
        LogUtil.i("数据库zzz更新");

        if (oldVersion < 2) {
            String sql = "ALTER TABLE SetUp ADD COLUMN repeatCount integer";
            db.execSQL(sql);
            sql = "update SetUp set repeatCount = 0";
            db.execSQL(sql);
            System.out.println("数据库更新成功");
        }

        if (oldVersion < 3) {
            String sql = "ALTER TABLE CopyDataICRF ADD COLUMN No01 TEXT , No02 TEXT,name TEXT";
            db.execSQL(sql);
            System.out.println("数据库更新成功");
        }

        //5的时候添加这张表
        if (oldVersion < 5) {
            //创建用户信息表
            db.execSQL("drop table if exists UserInfo2");//删除原来的表
            //新建这张表
            String sql = "CREATE TABLE UserInfo2 ("
                    + "_id integer primary key autoincrement ,"
                    + "tableNumber TEXT, userName TEXT, "
                    + "address TEXT, userNum TEXT, userPhone TEXT, tableName TEXT, xiNingTableNumber TEXT)";
            db.execSQL(sql);
        }
        if (oldVersion < 6) {
            String sql = "ALTER TABLE UserInfo2 ADD COLUMN xiNingUnitPrice TEXT";
            db.execSQL(sql);
            System.out.println("数据库更新成功");
        }

        if (oldVersion < 7) {
            //创建用户信息表
            db.execSQL("drop table if exists CtBookInfo");//删除原来的表
            //新建这张表
            String sql = "CREATE TABLE CtBookInfo ("
                    + "_id integer primary key autoincrement ,"
                    + "MeterNo TEXT, CommunicateNo TEXT, "
                    + "meterTypeNo TEXT, address TEXT, CollectorNo TEXT)";
            db.execSQL(sql);

            db.execSQL("drop table if exists CtCopyData");//删除原来的表
            //新建这张表
            sql = "CREATE TABLE CtCopyData ("
                    + "_id integer primary key autoincrement ,"
                    + "meterNo TEXT NOT NULL, lastShow TEXT,"
                    + "lastDosage TEXT, currentShow TEXT,"
                    + "currentDosage TEXT,unitPrice TEXT,"
                    + "printFlag integer,meterState integer,"
                    + "copyWay TEXT, copyState integer,"
                    + "copyTime TEXT, copyMan TEXT,"
                    + "Operator TEXT, operateTime TEXT,"
                    + "isBalance integer, Remark TEXT, meterName TEXT,"
                    + "dBm TEXT, elec TEXT ,CommunicateNo TEXT, CollectorNo TEXT)";
            db.execSQL(sql);

            db.execSQL("drop table if exists CtCopyDataICRF");//删除原来的表
            //新建这张表
            sql = "CREATE TABLE CtCopyDataICRF ("
                    + "_id integer primary key autoincrement ,"
                    + "meterNo TEXT NOT NULL, Cumulant TEXT,"
                    + "No01 TEXT, No02 TEXT, name TEXT,"
                    + "SurplusMoney TEXT, OverZeroMoney TEXT,"
                    + "BuyTimes integer, OverFlowTimes integer,"
                    + "MagAttTimes integer,CardAttTimes integer,"
                    + "MeterState integer, StateMessage TEXT,"
                    + "CurrMonthTotal TEXT, Last1MonthTotal TEXT,"
                    + "Last2MonthTotal TEXT, Last3MonthTotal TEXT,"
                    + "copyWay TEXT, copyTime TEXT,"
                    + "copyMan TEXT,copyState integer,meterName TEXT,"
                    + "dBm TEXT, elec TEXT, unitPrice TEXT, accMoney TEXT, accBuyMoney TEXT, currentShow TEXT ,CommunicateNo TEXT, CollectorNo TEXT)";
            db.execSQL(sql);
        }
    }

}
