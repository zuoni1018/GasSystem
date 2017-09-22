package com.pl.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.CopyDataPhoto;
import com.pl.gassystem.utils.LogUtil;

import java.util.ArrayList;

public class CopyDao {

    private DBOpenHelper helper;

    public CopyDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    // 根据分组号查询抄表总数
    public int getCopyCounts(String groupNo) {
        String sql = "SELECT count(*) from GroupBind WHERE groupNo = ?";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{groupNo});
        if (cursor != null && cursor.moveToNext()) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    /**
     * 通过分组编号去查询该分组下的所有表的编号
     */
    public ArrayList<String> GetCopyMeterNo(String groupNo) {
        ArrayList<String> meterNos = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select meterNo from GroupBind where groupNo = ?", new String[]{groupNo});
        if (cursor != null) {
            meterNos = new ArrayList<>();
            while (cursor.moveToNext()) {
                meterNos.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return meterNos;
    }

    // 根据分组号查询未抄表号
    public ArrayList<String> GetCopyUnReadMeterNo(String groupNo, String meterTypeNo) {
        ArrayList<String> meterNos = GetCopyMeterNo(groupNo);
        if (meterTypeNo.equals("04")) {// IC卡无线
            ArrayList<CopyDataICRF> copyDataICRFs = getCopyDataICRFByMeterNos(meterNos, 0);
            if (copyDataICRFs != null && copyDataICRFs.size() > 0) {
                meterNos.clear();
                for (int i = 0; i < copyDataICRFs.size(); i++) {
                    meterNos.add(copyDataICRFs.get(i).getMeterNo());
                }
                return meterNos;
            } else {
                return null;
            }
        } else if (meterTypeNo.equals("05")) {// 纯无线
            ArrayList<CopyData> copyDatas = getCopyDataByMeterNos(meterNos, 0);
            if (copyDatas != null && copyDatas.size() > 0) {
                meterNos.clear();
                for (int i = 0; i < copyDatas.size(); i++) {
                    meterNos.add(copyDatas.get(i).getMeterNo());
                }
                return meterNos;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 根据分组号查询已抄表号
    public ArrayList<String> GetCopyReadMeterNo(String groupNo,
                                                String meterTypeNo) {
        ArrayList<String> meterNos = GetCopyMeterNo(groupNo);
        if (meterTypeNo.equals("04")) {// IC卡无线
            ArrayList<CopyDataICRF> copyDataICRFs = getCopyDataICRFByMeterNos(
                    meterNos, 1);
            if (copyDataICRFs != null && copyDataICRFs.size() > 0) {
                meterNos.clear();
                for (int i = 0; i < copyDataICRFs.size(); i++) {
                    meterNos.add(copyDataICRFs.get(i).getMeterNo());
                }
                return meterNos;
            } else {
                return null;
            }
        } else if (meterTypeNo.equals("05")) {// 纯无线
            ArrayList<CopyData> copyDatas = getCopyDataByMeterNos(meterNos, 1);
            if (copyDatas != null && copyDatas.size() > 0) {
                meterNos.clear();
                for (int i = 0; i < copyDatas.size(); i++) {
                    meterNos.add(copyDatas.get(i).getMeterNo());
                }
                return meterNos;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 根据表号修改抄表状态
    public int ChangeCopyState(String meterNo, int copyState, String meterTypeNo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (meterTypeNo.equals("04")) {
            values.put("copyState", copyState);
            int count = db.update("CopyDataICRF", values, "meterNo=?", new String[]{meterNo});
            db.close();
            return count;
        } else if (meterTypeNo.equals("05")) {
            values.put("copyState", copyState);
            int count = db.update("CopyData", values, "meterNo=?", new String[]{meterNo});
            db.close();
            return count;
        } else {
            return 0;
        }

    }

    // 根据id号获取无线表抄表数据（单表）
    public CopyData getCopyDataById(String Id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from CopyData where _id = ?",
                new String[]{Id});
        if (cursor != null && cursor.moveToNext()) {
            CopyData copyData = new CopyData();
            copyData.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            copyData.setMeterNo(cursor.getString(cursor
                    .getColumnIndex("meterNo")));
            copyData.setLastShow(cursor.getString(cursor
                    .getColumnIndex("lastShow")));
            copyData.setLastDosage(cursor.getString(cursor
                    .getColumnIndex("lastDosage")));
            copyData.setCurrentShow(cursor.getString(cursor
                    .getColumnIndex("currentShow")));
            copyData.setCurrentDosage(cursor.getString(cursor
                    .getColumnIndex("currentDosage")));
            copyData.setUnitPrice(cursor.getString(cursor
                    .getColumnIndex("unitPrice")));
            copyData.setPrintFlag(cursor.getInt(cursor
                    .getColumnIndex("printFlag")));
            copyData.setMeterState(cursor.getInt(cursor
                    .getColumnIndex("meterState")));
            copyData.setCopyState(cursor.getInt(cursor
                    .getColumnIndex("copyState")));
            copyData.setCopyWay(cursor.getString(cursor
                    .getColumnIndex("copyWay")));
            copyData.setCopyTime(cursor.getString(cursor
                    .getColumnIndex("copyTime")));
            copyData.setCopyMan(cursor.getString(cursor
                    .getColumnIndex("copyMan")));
            copyData.setOperator(cursor.getString(cursor
                    .getColumnIndex("Operator")));
            copyData.setOperateTime(cursor.getString(cursor
                    .getColumnIndex("operateTime")));
            copyData.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
            copyData.setIsBalance(cursor.getInt(cursor
                    .getColumnIndex("isBalance")));
            copyData.setMeterName(cursor.getString(cursor
                    .getColumnIndex("meterName")));
            copyData.setdBm(cursor.getString(cursor.getColumnIndex("dBm")));
            copyData.setElec(cursor.getString(cursor.getColumnIndex("elec")));
            db.close();
            return copyData;
        } else {
            return null;
        }
    }

    // 根据表号获取无线表最近一条抄表记录（单表）
    public CopyData getLastCopyDataByMeterNo(String MeterNo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "select * from CopyData where meterNo = ? order by _id desc  LIMIT 0,1",
                        new String[]{MeterNo});
        if (cursor != null && cursor.moveToNext()) {
            CopyData copyData = new CopyData();
            copyData.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            copyData.setMeterNo(cursor.getString(cursor
                    .getColumnIndex("meterNo")));
            copyData.setLastShow(cursor.getString(cursor
                    .getColumnIndex("lastShow")));
            copyData.setLastDosage(cursor.getString(cursor
                    .getColumnIndex("lastDosage")));
            copyData.setCurrentShow(cursor.getString(cursor
                    .getColumnIndex("currentShow")));
            copyData.setCurrentDosage(cursor.getString(cursor
                    .getColumnIndex("currentDosage")));
            copyData.setUnitPrice(cursor.getString(cursor
                    .getColumnIndex("unitPrice")));
            copyData.setPrintFlag(cursor.getInt(cursor
                    .getColumnIndex("printFlag")));
            copyData.setMeterState(cursor.getInt(cursor
                    .getColumnIndex("meterState")));
            copyData.setCopyState(cursor.getInt(cursor
                    .getColumnIndex("copyState")));
            copyData.setCopyWay(cursor.getString(cursor
                    .getColumnIndex("copyWay")));
            copyData.setCopyTime(cursor.getString(cursor
                    .getColumnIndex("copyTime")));
            copyData.setCopyMan(cursor.getString(cursor
                    .getColumnIndex("copyMan")));
            copyData.setOperator(cursor.getString(cursor
                    .getColumnIndex("Operator")));
            copyData.setOperateTime(cursor.getString(cursor
                    .getColumnIndex("operateTime")));
            copyData.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
            copyData.setIsBalance(cursor.getInt(cursor
                    .getColumnIndex("isBalance")));
            copyData.setMeterName(cursor.getString(cursor
                    .getColumnIndex("meterName")));
            copyData.setdBm(cursor.getString(cursor.getColumnIndex("dBm")));
            copyData.setElec(cursor.getString(cursor.getColumnIndex("elec")));
            db.close();
            return copyData;
        } else {
            return null;
        }
    }

    // 根据表号获取摄像表最近一条抄表记录（单表）
    public CopyDataPhoto getLastCopyDataPhotoByCommunicateNo(String CommunicateNo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from CopyDataPhoto where CommunicateNo = ? order by _id desc  LIMIT 0,1", new String[]{CommunicateNo});
        if (cursor != null && cursor.moveToNext()) {
            CopyDataPhoto copyDataPhoto = new CopyDataPhoto();
            copyDataPhoto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            copyDataPhoto.setCommunicateNo(cursor.getString(cursor.getColumnIndex("CommunicateNo")));
            copyDataPhoto.setReadState(cursor.getInt(cursor.getColumnIndex("ReadState")));
            copyDataPhoto.setImageName(cursor.getString(cursor.getColumnIndex("ImageName")));
            copyDataPhoto.setCollectorNo(cursor.getString(cursor.getColumnIndex("CollectorNo")));
            copyDataPhoto.setOperater(cursor.getString(cursor.getColumnIndex("Operater")));
            copyDataPhoto.setReadTime(cursor.getString(cursor.getColumnIndex("ReadTime")));
            copyDataPhoto.setDevState(cursor.getString(cursor.getColumnIndex("DevState")));
            copyDataPhoto.setDevPower(cursor.getString(cursor.getColumnIndex("DevPower")));
            copyDataPhoto.setOcrState(cursor.getInt(cursor.getColumnIndex("OcrState")));
            copyDataPhoto.setOcrRead(cursor.getString(cursor.getColumnIndex("OcrRead")));
            copyDataPhoto.setThisRead(cursor.getString(cursor.getColumnIndex("ThisRead")));
            copyDataPhoto.setOcrResult(cursor.getString(cursor.getColumnIndex("OcrResult")));
            copyDataPhoto.setOcrTime(cursor.getString(cursor.getColumnIndex("OcrTime")));
            copyDataPhoto.setCreateTime(cursor.getString(cursor.getColumnIndex("CreateTime")));
            copyDataPhoto.setMeterName(cursor.getString(cursor.getColumnIndex("meterName")));
            db.close();
            return copyDataPhoto;
        } else {
            return null;
        }
    }



    /**
     * 根据表号们 获得该表号对应的抄表数据
     * CopyData 为抄表记录表
     * sql 语句注释
     * 把所有meterNo相同的放到一个分组里再将该分组里的数据按时间倒序排序取第一条数据
     * 也就是说这里查出来的都是最新的抄表记录集合
     */
    public ArrayList<CopyData> getCopyDataByMeterNos(ArrayList<String> meterNos, int copyState) {

        ArrayList<CopyData> copyDatas = new ArrayList<>();
        if (meterNos.size() > 0) {

            int sizeNo = meterNos.size();
            int sizeCount = sizeNo / 500;

            SQLiteDatabase db = helper.getReadableDatabase();
            for (int j = 0; j < sizeCount + 1; j++) {
                String sql = "SELECT * FROM CopyData where (";
                sizeNo = sizeNo - 500;
                if (sizeNo < 0) {
                    for (int i = 500 * j; i < 500 * j + sizeNo + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                } else {
                    for (int i = 500 * j; i < 500 * j + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                }
                sql += " 1=2) ";
                if (copyState == 2) { // 全部
                    sql += "GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 1) {// 已抄
                    sql += "and copyState = 1 GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 0) {// 未抄
                    sql += "and copyState = 0 GROUP BY meterNo ORDER BY copyTime DESC";
                }

                LogUtil.i("Sql", "表号查询抄表内容sql=" + sql);

                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        CopyData copyData = new CopyData();
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
                        copyDatas.add(copyData);
                    }
                    cursor.close();
                }
            }
            db.close();
            return copyDatas;
        } else {
            return copyDatas;
        }
    }


    public ArrayList<CopyData> getCopyDataByMeterNos_N(ArrayList<String> meterNos, int copyState, String name) {
        if (meterNos.size() > 0) {
            // if(meterNos.size()<500){
            // ArrayList<CopyData> copyDatas = null;
            // String sql = "SELECT * FROM CopyData where (";
            // for (int i = 0; i < meterNos.size(); i++) {
            // sql += "meterNo = '" + meterNos.get(i) + "' or ";
            // }
            // sql += " 1=2) ";
            // if(copyState == 2){ //全部
            // sql += "GROUP BY meterNo ORDER BY copyTime DESC";
            // }else if(copyState ==1){//已抄
            // sql +=
            // "and copyState = 1 GROUP BY meterNo ORDER BY copyTime DESC";
            // }else if (copyState ==0) {//未抄
            // sql +=
            // "and copyState = 0 GROUP BY meterNo ORDER BY copyTime DESC";
            // }
            // SQLiteDatabase db = helper.getReadableDatabase();
            // Cursor cursor = db.rawQuery(sql, null);
            // if(cursor!=null){
            // copyDatas = new ArrayList<CopyData>();
            // while (cursor.moveToNext()) {
            // CopyData copyData = new CopyData();
            // copyData.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            // copyData.setMeterNo(cursor.getString(cursor.getColumnIndex("meterNo")));
            // copyData.setLastShow(cursor.getString(cursor.getColumnIndex("lastShow")));
            // copyData.setLastDosage(cursor.getString(cursor.getColumnIndex("lastDosage")));
            // copyData.setCurrentShow(cursor.getString(cursor.getColumnIndex("currentShow")));
            // copyData.setCurrentDosage(cursor.getString(cursor.getColumnIndex("currentDosage")));
            // copyData.setUnitPrice(cursor.getString(cursor.getColumnIndex("unitPrice")));
            // copyData.setPrintFlag(cursor.getInt(cursor.getColumnIndex("printFlag")));
            // copyData.setMeterState(cursor.getInt(cursor.getColumnIndex("meterState")));
            // copyData.setCopyState(cursor.getInt(cursor.getColumnIndex("copyState")));
            // copyData.setCopyWay(cursor.getString(cursor.getColumnIndex("copyWay")));
            // copyData.setCopyTime(cursor.getString(cursor.getColumnIndex("copyTime")));
            // copyData.setCopyMan(cursor.getString(cursor.getColumnIndex("copyMan")));
            // copyData.setOperator(cursor.getString(cursor.getColumnIndex("Operator")));
            // copyData.setOperateTime(cursor.getString(cursor.getColumnIndex("operateTime")));
            // copyData.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
            // copyData.setIsBalance(cursor.getInt(cursor.getColumnIndex("isBalance")));
            // copyData.setMeterName(cursor.getString(cursor.getColumnIndex("meterName")));
            // copyData.setdBm(cursor.getString(cursor.getColumnIndex("dBm")));
            // copyData.setElec(cursor.getString(cursor.getColumnIndex("elec")));
            // copyDatas.add(copyData);
            // }
            // cursor.close();
            // }
            // db.close();
            // return copyDatas;
            // }else {
            int sizeNo = meterNos.size();
            int sizeCount = sizeNo / 500;
            ArrayList<CopyData> copyDatas = new ArrayList<CopyData>();
            SQLiteDatabase db = helper.getReadableDatabase();
            for (int j = 0; j < sizeCount + 1; j++) {
                String sql = "SELECT * FROM CopyData where (";
                sizeNo = sizeNo - 500;
                if (sizeNo < 0) {
                    for (int i = 500 * j; i < 500 * j + sizeNo + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                } else {
                    for (int i = 500 * j; i < 500 * j + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                }
                sql += " 1=2) ";
                sql += " and meterName like " + name + " ";
                if (copyState == 2) { // 全部
                    sql += "GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 1) {// 已抄
                    sql += "and copyState = 1 GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 0) {// 未抄
                    sql += "and copyState = 0 GROUP BY meterNo ORDER BY copyTime DESC";
                }
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        CopyData copyData = new CopyData();
                        copyData.setId(cursor.getInt(cursor
                                .getColumnIndex("_id")));
                        copyData.setMeterNo(cursor.getString(cursor
                                .getColumnIndex("meterNo")));
                        copyData.setLastShow(cursor.getString(cursor
                                .getColumnIndex("lastShow")));
                        copyData.setLastDosage(cursor.getString(cursor
                                .getColumnIndex("lastDosage")));
                        copyData.setCurrentShow(cursor.getString(cursor
                                .getColumnIndex("currentShow")));
                        copyData.setCurrentDosage(cursor.getString(cursor
                                .getColumnIndex("currentDosage")));
                        copyData.setUnitPrice(cursor.getString(cursor
                                .getColumnIndex("unitPrice")));
                        copyData.setPrintFlag(cursor.getInt(cursor
                                .getColumnIndex("printFlag")));
                        copyData.setMeterState(cursor.getInt(cursor
                                .getColumnIndex("meterState")));
                        copyData.setCopyState(cursor.getInt(cursor
                                .getColumnIndex("copyState")));
                        copyData.setCopyWay(cursor.getString(cursor
                                .getColumnIndex("copyWay")));
                        copyData.setCopyTime(cursor.getString(cursor
                                .getColumnIndex("copyTime")));
                        copyData.setCopyMan(cursor.getString(cursor
                                .getColumnIndex("copyMan")));
                        copyData.setOperator(cursor.getString(cursor
                                .getColumnIndex("Operator")));
                        copyData.setOperateTime(cursor.getString(cursor
                                .getColumnIndex("operateTime")));
                        copyData.setRemark(cursor.getString(cursor
                                .getColumnIndex("Remark")));
                        copyData.setIsBalance(cursor.getInt(cursor
                                .getColumnIndex("isBalance")));
                        copyData.setMeterName(cursor.getString(cursor
                                .getColumnIndex("meterName")));
                        copyData.setdBm(cursor.getString(cursor
                                .getColumnIndex("dBm")));
                        copyData.setElec(cursor.getString(cursor
                                .getColumnIndex("elec")));
                        copyDatas.add(copyData);
                    }
                    cursor.close();
                }
            }
            db.close();
            return copyDatas;
            // }

        } else {
            return null;
        }
    }

    // 根据id号获取IC无线表抄表数据（单表）
    public CopyDataICRF getCopyDataICRFById(String Id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from CopyDataICRF where _id = ?", new String[]{Id});
        if (cursor != null && cursor.moveToNext()) {
            CopyDataICRF copyIcrf = new CopyDataICRF();
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
            db.close();
            return copyIcrf;
        } else {
            return null;
        }
    }

    // 根据表号组获取IC无线表抄表数据
    public ArrayList<CopyDataICRF> getCopyDataICRFByMeterNos(ArrayList<String> meterNos, int copyState) {
        ArrayList<CopyDataICRF> copyDataICRFs = new ArrayList<CopyDataICRF>();
        if (meterNos.size() > 0) {
            int sizeNo = meterNos.size();
            int sizeCount = sizeNo / 500;

            SQLiteDatabase db = helper.getReadableDatabase();
            for (int j = 0; j < sizeCount + 1; j++) {
                String sql = "SELECT * FROM CopyDataICRF where (";
                sizeNo = sizeNo - 500;
                if (sizeNo < 0) {
                    for (int i = 500 * j; i < 500 * j + sizeNo + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                } else {
                    for (int i = 500 * j; i < 500 * j + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                }
                sql += " 1=2) ";
                if (copyState == 2) { // 全部
                    sql += "GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 1) {// 已抄
                    sql += "and copyState = 1 GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 0) {// 未抄
                    sql += "and copyState = 0 GROUP BY meterNo ORDER BY copyTime DESC";
                }
//                System.out.println(sql);
                Log.i("sql2", "" + sql);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        CopyDataICRF copyIcrf = new CopyDataICRF();
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
                        copyIcrf.setNo01(cursor.getString(cursor.getColumnIndex("No01")));
                        copyIcrf.setNo02(cursor.getString(cursor.getColumnIndex("No02")));
                        copyIcrf.setName(cursor.getString(cursor.getColumnIndex("name")));
                        copyDataICRFs.add(copyIcrf);
                    }
                    cursor.close();
                }
            }
            db.close();
            return copyDataICRFs;
        } else {
            return copyDataICRFs;
        }
    }

    public ArrayList<CopyDataICRF> getCopyDataICRFByMeterNos_N(
            ArrayList<String> meterNos, int copyState, String name) {
        if (meterNos.size() > 0) {
            int sizeNo = meterNos.size();
            int sizeCount = sizeNo / 500;
            ArrayList<CopyDataICRF> copyDataICRFs = new ArrayList<CopyDataICRF>();
            SQLiteDatabase db = helper.getReadableDatabase();
            for (int j = 0; j < sizeCount + 1; j++) {
                String sql = "SELECT * FROM CopyDataICRF where (";
                sizeNo = sizeNo - 500;
                if (sizeNo < 0) {
                    for (int i = 500 * j; i < 500 * j + sizeNo + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                } else {
                    for (int i = 500 * j; i < 500 * j + 500; i++) {
                        sql += "meterNo = '" + meterNos.get(i) + "' or ";
                    }
                }
                sql += " 1=2)";
                sql += " and meterName like " + name + " ";
                if (copyState == 2) { // 全部
                    sql += "GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 1) {// 已抄
                    sql += "and copyState = 1 GROUP BY meterNo ORDER BY copyTime DESC";
                } else if (copyState == 0) {// 未抄
                    sql += "and copyState = 0 GROUP BY meterNo ORDER BY copyTime DESC";
                }

                Cursor cursor = db.rawQuery(sql, null);
                System.out.println(sql + "-->" + cursor.getCount());
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        CopyDataICRF copyIcrf = new CopyDataICRF();
                        copyIcrf.setId(cursor.getInt(cursor
                                .getColumnIndex("_id")));
                        copyIcrf.setMeterNo(cursor.getString(cursor
                                .getColumnIndex("meterNo")));
                        copyIcrf.setCumulant(cursor.getString(cursor
                                .getColumnIndex("Cumulant")));
                        copyIcrf.setSurplusMoney(cursor.getString(cursor
                                .getColumnIndex("SurplusMoney")));
                        copyIcrf.setOverZeroMoney(cursor.getString(cursor
                                .getColumnIndex("OverZeroMoney")));
                        copyIcrf.setBuyTimes(cursor.getInt(cursor
                                .getColumnIndex("BuyTimes")));
                        copyIcrf.setOverFlowTimes(cursor.getInt(cursor
                                .getColumnIndex("OverFlowTimes")));
                        copyIcrf.setMagAttTimes(cursor.getInt(cursor
                                .getColumnIndex("MagAttTimes")));
                        copyIcrf.setCardAttTimes(cursor.getInt(cursor
                                .getColumnIndex("CardAttTimes")));
                        copyIcrf.setMeterState(cursor.getInt(cursor
                                .getColumnIndex("MeterState")));
                        copyIcrf.setCopyState(cursor.getInt(cursor
                                .getColumnIndex("copyState")));
                        copyIcrf.setCopyWay(cursor.getString(cursor
                                .getColumnIndex("copyWay")));
                        copyIcrf.setCopyTime(cursor.getString(cursor
                                .getColumnIndex("copyTime")));
                        copyIcrf.setCopyMan(cursor.getString(cursor
                                .getColumnIndex("copyMan")));
                        copyIcrf.setStateMessage(cursor.getString(cursor
                                .getColumnIndex("StateMessage")));
                        copyIcrf.setCurrMonthTotal(cursor.getString(cursor
                                .getColumnIndex("CurrMonthTotal")));
                        copyIcrf.setLast1MonthTotal(cursor.getString(cursor
                                .getColumnIndex("Last1MonthTotal")));
                        copyIcrf.setLast2MonthTotal(cursor.getString(cursor
                                .getColumnIndex("Last2MonthTotal")));
                        copyIcrf.setLast3MonthTotal(cursor.getString(cursor
                                .getColumnIndex("Last3MonthTotal")));
                        copyIcrf.setMeterName(cursor.getString(cursor
                                .getColumnIndex("meterName")));
                        copyIcrf.setdBm(cursor.getString(cursor
                                .getColumnIndex("dBm")));
                        copyIcrf.setElec(cursor.getString(cursor
                                .getColumnIndex("elec")));
                        copyIcrf.setUnitPrice(cursor.getString(cursor
                                .getColumnIndex("unitPrice")));
                        copyIcrf.setAccMoney(cursor.getString(cursor
                                .getColumnIndex("accMoney")));
                        copyIcrf.setAccBuyMoney(cursor.getString(cursor
                                .getColumnIndex("accBuyMoney")));
                        copyIcrf.setCurrentShow(cursor.getString(cursor
                                .getColumnIndex("currentShow")));
                        copyDataICRFs.add(copyIcrf);
                    }
                    cursor.close();
                }
            }
            db.close();
            return copyDataICRFs;
        } else {
            return null;
        }
    }

    // 插入无线表抄表数据
    public long addCopyData(CopyData copyData) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meterNo", copyData.getMeterNo());
        values.put("lastShow", copyData.getLastDosage());
        values.put("lastDosage", copyData.getLastDosage());
        values.put("currentShow", copyData.getCurrentShow());
        values.put("currentDosage", copyData.getCurrentDosage());
        values.put("unitPrice", copyData.getUnitPrice());
        values.put("printFlag", copyData.getPrintFlag());
        values.put("meterState", copyData.getMeterState());
        values.put("copyWay", copyData.getCopyWay());
        values.put("copyState", copyData.getCopyState());
        values.put("copyTime", copyData.getCopyTime());
        values.put("copyMan", copyData.getCopyMan());
        values.put("Operator", copyData.getOperator());
        values.put("operateTime", copyData.getOperateTime());
        values.put("isBalance", copyData.getIsBalance());
        values.put("Remark", copyData.getRemark());
        values.put("meterName", copyData.getMeterName());
        values.put("dBm", copyData.getdBm());
        values.put("elec", copyData.getElec());
        long rowId = db.insert("CopyData", null, values);
        db.close();
        return rowId;
    }

    // 插入IC无线表抄表数据
    public long addCopyDataICRF(CopyDataICRF copyDataICRF) {
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
        long rowId = db.insert("CopyDataICRF", null, values);
        db.close();
        return rowId;
    }

    // 插入摄像表抄表数据
    public long addCopyDataPhoto(CopyDataPhoto copyDataPhoto) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CommunicateNo", copyDataPhoto.getCommunicateNo());
        values.put("ReadState", copyDataPhoto.getReadState());
        values.put("ImageName", copyDataPhoto.getImageName());
        values.put("CollectorNo", copyDataPhoto.getCollectorNo());
        values.put("Operater", copyDataPhoto.getOperater());
        values.put("ReadTime", copyDataPhoto.getReadTime());
        values.put("DevState", copyDataPhoto.getDevState());
        values.put("DevPower", copyDataPhoto.getDevPower());
        values.put("OcrRead", copyDataPhoto.getOcrRead());
        values.put("OcrState", copyDataPhoto.getOcrState());
        values.put("ThisRead", copyDataPhoto.getThisRead());
        values.put("OcrResult", copyDataPhoto.getOcrResult());
        values.put("OcrTime", copyDataPhoto.getOcrTime());
        values.put("CreateTime", copyDataPhoto.getCreateTime());
        values.put("meterName", copyDataPhoto.getMeterName());
        long rowId = db.insert("CopyDataPhoto", null, values);
        db.close();
        return rowId;
    }

    // 根据表号获得表名称
    public String getMeterNameByNo(String meterNo) {
        String sql = "SELECT meterName from GroupBind WHERE meterNo = ?";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{meterNo});
        if (cursor != null && cursor.moveToNext()) {
            return cursor.getString(0);
        } else {
            return "";
        }
    }

    // 根据表号获得表芯片类型
    public String getMeterWITypeByNo(String meterNo) {
        String sql = "SELECT MeterType from GroupBind WHERE meterNo = ?";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{meterNo});
        if (cursor != null && cursor.moveToNext()) {
            return cursor.getString(0);
        } else {
            return "";
        }
    }

    // 删除所有无线表记录
    public int removeCopyDataAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("CopyData", null, null);
        db.close();
        return count;
    }

    // 删除所有无线表记录
    public int removeCopyDataICRFAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("CopyDataICRF", null, null);
        db.close();
        return count;
    }

    // 删除所有无线表记录
    public int removeCopyDataPhotoAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("CopyDataPhoto", null, null);
        db.close();
        return count;
    }

}
