package com.pl.concentrator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.common.utils.LogUtil;
import com.pl.concentrator.bean.model.CtBookInfo;
import com.pl.concentrator.bean.model.CtCopyData;
import com.pl.dal.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/9/7
 * ���뼯���������߳����¼
 */

public class CtCopyDataDao {

    private DBOpenHelper helper;

    public CtCopyDataDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * �����ݿ��в���һ����������
     * ��������� �����������
     */
    public void putCtCopyData(CtCopyData ctCopyData) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meterNo", ctCopyData.getMeterNo());
        values.put("lastShow", ctCopyData.getLastDosage());
        values.put("lastDosage", ctCopyData.getLastDosage());
        values.put("currentShow", ctCopyData.getCurrentShow());
        values.put("currentDosage", ctCopyData.getCurrentDosage());
        values.put("unitPrice", ctCopyData.getUnitPrice());
        values.put("printFlag", ctCopyData.getPrintFlag());
        values.put("meterState", ctCopyData.getMeterState());
        values.put("copyWay", ctCopyData.getCopyWay());
        values.put("copyState", ctCopyData.getCopyState());
        values.put("copyTime", ctCopyData.getCopyTime());
        values.put("copyMan", ctCopyData.getCopyMan());
        values.put("Operator", ctCopyData.getOperator());
        values.put("operateTime", ctCopyData.getOperateTime());
        values.put("isBalance", ctCopyData.getIsBalance());
        values.put("Remark", ctCopyData.getRemark());
        values.put("meterName", ctCopyData.getMeterName());
        values.put("dBm", ctCopyData.getdBm());
        values.put("elec", ctCopyData.getElec());

        values.put("CommunicateNo", ctCopyData.getCommunicateNo());//����
        values.put("CollectorNo", ctCopyData.getCollectorNo());//���ڼ��������

        //�ж����ݿ�ԭ���Ƿ���ڸ�������
        Cursor cursor = db.rawQuery("select * from CtCopyData where CommunicateNo = ?", new String[]{ctCopyData.getCommunicateNo()});
        if (cursor != null && cursor.moveToNext()) {
            db.update("CtCopyData", values, "CommunicateNo = ?", new String[]{ctCopyData.getCommunicateNo()});
            cursor.close();
        } else {
            db.insert("CtCopyData", null, values);
        }
        db.close();
    }

    /**
     * �����ݿ��в���һ�������ݲ����ں���
     * ��������� �����������
     */
    public void putCtCopyDataOtherInfo(CtBookInfo ctBookInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //����ӵ�
        values.put("CommunicateNo", ctBookInfo.getCommunicateNo());//����
        values.put("CollectorNo", ctBookInfo.getCollectorNo());//���ڼ��������
        values.put("meterNo", ctBookInfo.getCommunicateNo());
        values.put("meterName",ctBookInfo.getAddress());
        //�������ݿ�
        //�ж����ݿ�ԭ���Ƿ���ڸ�������
        Cursor cursor = db.rawQuery("select * from CtCopyData where CommunicateNo = ?", new String[]{ctBookInfo.getCommunicateNo()});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                //���ƶ���ȥ˵��������ִ�и��²���
                db.update("CtCopyData", values, "CommunicateNo = ?", new String[]{ctBookInfo.getCommunicateNo()});
                cursor.close();
            } else {
                db.insert("CtCopyData", null, values);
                cursor.close();
            }
        }
        db.close();
    }


    /**
     * ͨ�����������ȥ��ѯ���еı�
     */
    public List<CtCopyData> getCtCopyDataListByCollectorNo(String CollectorNo) {
        LogUtil.i("��ѯ���������" + CollectorNo);
        //���������б�
        List<CtCopyData> mCtCopyDataList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from CtCopyData where CollectorNo = ?", new String[]{CollectorNo});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CtCopyData copyData = new CtCopyData();
                copyData.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                copyData.setMeterNo(cursor.getString(cursor.getColumnIndex("meterNo")));
                copyData.setLastShow(cursor.getString(cursor.getColumnIndex("lastShow")));
                copyData.setLastDosage(cursor.getString(cursor.getColumnIndex("lastDosage")));
                copyData.setCurrentShow(cursor.getString(cursor.getColumnIndex("currentShow")));
                copyData.setCurrentDosage(cursor.getString(cursor.getColumnIndex("currentDosage")));
                copyData.setUnitPrice(cursor.getString(cursor.getColumnIndex("unitPrice")));
                copyData.setPrintFlag(cursor.getInt(cursor.getColumnIndex("printFlag")));
                copyData.setMeterState(cursor.getInt(cursor.getColumnIndex("meterState")));
                copyData.setCopyState(cursor.getInt(cursor.getColumnIndex("copyState")));
                copyData.setCopyWay(cursor.getString(cursor.getColumnIndex("copyWay")));
                copyData.setCopyTime(cursor.getString(cursor.getColumnIndex("copyTime")));
                copyData.setCopyMan(cursor.getString(cursor.getColumnIndex("copyMan")));
                copyData.setOperator(cursor.getString(cursor.getColumnIndex("Operator")));
                copyData.setOperateTime(cursor.getString(cursor.getColumnIndex("operateTime")));
                copyData.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
                copyData.setIsBalance(cursor.getInt(cursor.getColumnIndex("isBalance")));
                copyData.setMeterName(cursor.getString(cursor.getColumnIndex("meterName")));
                copyData.setdBm(cursor.getString(cursor.getColumnIndex("dBm")));
                copyData.setElec(cursor.getString(cursor.getColumnIndex("elec")));
                copyData.setCommunicateNo(cursor.getString(cursor.getColumnIndex("CommunicateNo")));
                copyData.setCollectorNo(cursor.getString(cursor.getColumnIndex("CollectorNo")));
                mCtCopyDataList.add(copyData);
            }
            cursor.close();
        }
        return mCtCopyDataList;
    }
}
