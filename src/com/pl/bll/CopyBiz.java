package com.pl.bll;

import android.content.Context;

import com.pl.dal.CopyDao;
import com.pl.entity.CopyData;
import com.pl.entity.CopyDataICRF;
import com.pl.entity.CopyDataPhoto;

import java.util.ArrayList;

public class CopyBiz {

    private CopyDao copyDao;

    public CopyBiz(Context context) {
        copyDao = new CopyDao(context);
    }

    public int getCopyCounts(String groupNo) {
        return copyDao.getCopyCounts(groupNo);
    }

    public long addCopyData(CopyData copyData) {
        return copyDao.addCopyData(copyData);
    }

    public long addCopyDataICRF(CopyDataICRF copyDataICRF) {
        return copyDao.addCopyDataICRF(copyDataICRF);
    }

    public long addCopyDataPhoto(CopyDataPhoto copyDataPhoto) {
        return copyDao.addCopyDataPhoto(copyDataPhoto);
    }

    public ArrayList<String> GetCopyMeterNo(String groupNo) {
        return copyDao.GetCopyMeterNo(groupNo);
    }

    public ArrayList<CopyData> getCopyDataByMeterNos(
            ArrayList<String> meterNos, int copyState) {
        return copyDao.getCopyDataByMeterNos(meterNos, copyState);
    }

    public ArrayList<CopyData> getCopyDataByMeterNos_N(
            ArrayList<String> meterNos, int copyState, String name) {
        return copyDao.getCopyDataByMeterNos_N(meterNos, copyState, name);
    }

    public ArrayList<CopyDataICRF> getCopyDataICRFByMeterNos(ArrayList<String> meterNos, int copyState) {
        return copyDao.getCopyDataICRFByMeterNos(meterNos, copyState);
    }

    public ArrayList<CopyDataICRF> getCopyDataICRFByMeterNos_N(
            ArrayList<String> meterNos, int copyState, String name) {
        return copyDao.getCopyDataICRFByMeterNos_N(meterNos, copyState, name);
    }

    public ArrayList<String> GetCopyUnReadMeterNo(String groupNo,
                                                  String meterTypeNo) {
        return copyDao.GetCopyUnReadMeterNo(groupNo, meterTypeNo);
    }

    public ArrayList<String> GetCopyReadMeterNo(String groupNo,
                                                String meterTypeNo) {
        return copyDao.GetCopyReadMeterNo(groupNo, meterTypeNo);
    }

    public int ChangeCopyState(String meterNo, int copyState, String meterTypeNo) {
        return copyDao.ChangeCopyState(meterNo, copyState, meterTypeNo);
    }

    public CopyData getCopyDataById(String Id) {
        return copyDao.getCopyDataById(Id);
    }

    public CopyDataICRF getCopyDataICRFById(String Id) {
        return copyDao.getCopyDataICRFById(Id);
    }

    public String getMeterNameByNo(String meterNo) {
        return copyDao.getMeterNameByNo(meterNo);
    }

    public CopyData getLastCopyDataByMeterNo(String MeterNo) {
        return copyDao.getLastCopyDataByMeterNo(MeterNo);
    }

    public CopyDataPhoto getLastCopyDataPhotoByCommunicateNo(
            String CommunicateNo) {
        return copyDao.getLastCopyDataPhotoByCommunicateNo(CommunicateNo);
    }

    public int removeCopyDataAll() {
        return copyDao.removeCopyDataAll();
    }

    public int removeCopyDataICRFAll() {
        return copyDao.removeCopyDataICRFAll();
    }

    public int removeCopyDataPhotoAll() {
        return copyDao.removeCopyDataPhotoAll();
    }

    // 根据表号获得表芯片类型
    public String getMeterWITypeByNo(String meterNo) {
        return copyDao.getMeterWITypeByNo(meterNo);
    }

}
