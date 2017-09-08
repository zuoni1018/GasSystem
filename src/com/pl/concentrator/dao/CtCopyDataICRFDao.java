package com.pl.concentrator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.utils.LogUtil;
import com.pl.concentrator.bean.model.CtBookInfo;
import com.pl.concentrator.bean.model.CtCopyDataICRF;
import com.pl.dal.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/7
 * 插入集中器纯无线抄表记录
 */

public class CtCopyDataICRFDao {

    private DBOpenHelper helper;

    public CtCopyDataICRFDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * 向数据库中插入一条抄表数据
     * 存在则更新 不存在则插入
     */
    public void putCtCopyData(CtCopyDataICRF copyDataICRF) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meterNo", copyDataICRF.getMeterNo());
        values.put("Cumulant", copyDataICRF.getCumulant());
        values.put("SurplusMoney", copyDataICRF.getSurplusMoney());
        values.put("OverZeroMoney", copyDataICRF.getOverZeroMoney());
        values.put("BuyTimes", copyDataICRF.getBuyTimes());
        values.put("OverFlowTimes", copyDataICRF.getOverFlowTimes());
        values.put("MagAttTimes", copyDataICRF.getMagAttTimes());
        values.put("CardAttTimes", copyDataICRF.getCardAttTimes());
        values.put("MeterState", copyDataICRF.getMeterState());
        values.put("StateMessage", copyDataICRF.getStateMessage());
        values.put("CurrMonthTotal", copyDataICRF.getCurrMonthTotal());
        values.put("Last1MonthTotal", copyDataICRF.getLast1MonthTotal());
        values.put("Last2MonthTotal", copyDataICRF.getLast2MonthTotal());
        values.put("Last3MonthTotal", copyDataICRF.getLast3MonthTotal());
        values.put("copyWay", copyDataICRF.getCopyWay());
        values.put("copyTime", copyDataICRF.getCopyTime());
        values.put("copyMan", copyDataICRF.getCopyMan());
        values.put("copyState", copyDataICRF.getCopyState());
        values.put("meterName", copyDataICRF.getMeterName());
        values.put("dBm", copyDataICRF.getdBm());
        values.put("elec", copyDataICRF.getElec());
        values.put("unitPrice", copyDataICRF.getUnitPrice());
        values.put("accMoney", copyDataICRF.getAccMoney());
        values.put("accBuyMoney", copyDataICRF.getAccBuyMoney());
        values.put("currentShow", copyDataICRF.getCurrentShow());
        values.put("No01", copyDataICRF.getNo01());//向数据库中多存取这2个数据
        values.put("No02", copyDataICRF.getNo02());
        values.put("name", copyDataICRF.getName());

        values.put("CommunicateNo", copyDataICRF.getCommunicateNo());//表编号
        values.put("CollectorNo", copyDataICRF.getCollectorNo());//所在集中器编号

        //判断数据库原来是否存在该条数据
        Cursor cursor = db.rawQuery("select * from CtCopyData where CommunicateNo = ?", new String[]{copyDataICRF.getCommunicateNo()});
        if (cursor != null && cursor.moveToNext()) {
            db.update("CtCopyDataICRF", values, "CommunicateNo = ?", new String[]{copyDataICRF.getCommunicateNo()});
            cursor.close();
        } else {
            db.insert("CtCopyDataICRF", null, values);
        }
        db.close();
    }

    /**
     * 向数据库中插入一条表数据插入在后面
     * 存在则更新 不存在则插入
     */
    public void putCtCopyDataOtherInfo(CtBookInfo ctBookInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //后面加的
        values.put("CommunicateNo", ctBookInfo.getCommunicateNo());//表编号
        values.put("CollectorNo", ctBookInfo.getCollectorNo());//所在集中器编号
        values.put("meterNo", ctBookInfo.getCommunicateNo());
        values.put("meterName",ctBookInfo.getAddress());
        //插入数据库
        //判断数据库原来是否存在该条数据
        Cursor cursor = db.rawQuery("select * from CtCopyDataICRF where CommunicateNo = ?", new String[]{ctBookInfo.getCommunicateNo()});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                //能移动下去说明有数据执行更新操作
                db.update("CtCopyDataICRF", values, "CommunicateNo = ?", new String[]{ctBookInfo.getCommunicateNo()});
                cursor.close();
            } else {
                db.insert("CtCopyDataICRF", null, values);
                cursor.close();
            }
        }
        db.close();
    }


    /**
     * 通过集中器编号去查询所有的表
     */
    public List<CtCopyDataICRF> getCtCopyDataICRFListByCollectorNo(String CollectorNo) {
        LogUtil.i("查询集中器编号" + CollectorNo);
        //抄表数据列表
        List<CtCopyDataICRF> mCtCopyDataList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from CtCopyDataICRF where CollectorNo = ?", new String[]{CollectorNo});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CtCopyDataICRF copyIcrf = new CtCopyDataICRF();
                copyIcrf.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                copyIcrf.setMeterNo(cursor.getString(cursor.getColumnIndex("meterNo")));
                copyIcrf.setCumulant(cursor.getString(cursor.getColumnIndex("Cumulant")));
                copyIcrf.setSurplusMoney(cursor.getString(cursor.getColumnIndex("SurplusMoney")));
                copyIcrf.setOverZeroMoney(cursor.getString(cursor.getColumnIndex("OverZeroMoney")));
                copyIcrf.setBuyTimes(cursor.getInt(cursor.getColumnIndex("BuyTimes")));
                copyIcrf.setOverFlowTimes(cursor.getInt(cursor.getColumnIndex("OverFlowTimes")));
                copyIcrf.setMagAttTimes(cursor.getInt(cursor.getColumnIndex("MagAttTimes")));
                copyIcrf.setCardAttTimes(cursor.getInt(cursor.getColumnIndex("CardAttTimes")));
                copyIcrf.setMeterState(cursor.getInt(cursor.getColumnIndex("MeterState")));
                copyIcrf.setCopyState(cursor.getInt(cursor.getColumnIndex("copyState")));
                copyIcrf.setCopyWay(cursor.getString(cursor.getColumnIndex("copyWay")));
                copyIcrf.setCopyTime(cursor.getString(cursor.getColumnIndex("copyTime")));
                copyIcrf.setCopyMan(cursor.getString(cursor.getColumnIndex("copyMan")));
                copyIcrf.setStateMessage(cursor.getString(cursor.getColumnIndex("StateMessage")));
                copyIcrf.setCurrMonthTotal(cursor.getString(cursor.getColumnIndex("CurrMonthTotal")));
                copyIcrf.setLast1MonthTotal(cursor.getString(cursor.getColumnIndex("Last1MonthTotal")));
                copyIcrf.setLast2MonthTotal(cursor.getString(cursor.getColumnIndex("Last2MonthTotal")));
                copyIcrf.setLast3MonthTotal(cursor.getString(cursor.getColumnIndex("Last3MonthTotal")));
                copyIcrf.setMeterName(cursor.getString(cursor.getColumnIndex("meterName")));
                copyIcrf.setdBm(cursor.getString(cursor.getColumnIndex("dBm")));
                copyIcrf.setElec(cursor.getString(cursor.getColumnIndex("elec")));
                copyIcrf.setUnitPrice(cursor.getString(cursor.getColumnIndex("unitPrice")));
                copyIcrf.setAccMoney(cursor.getString(cursor.getColumnIndex("accMoney")));
                copyIcrf.setAccBuyMoney(cursor.getString(cursor.getColumnIndex("accBuyMoney")));
                copyIcrf.setCurrentShow(cursor.getString(cursor.getColumnIndex("currentShow")));

                copyIcrf.setCommunicateNo(cursor.getString(cursor.getColumnIndex("CommunicateNo")));
                copyIcrf.setCollectorNo(cursor.getString(cursor.getColumnIndex("CollectorNo")));
                mCtCopyDataList.add(copyIcrf);
            }
            cursor.close();
        }
        return mCtCopyDataList;
    }
}
