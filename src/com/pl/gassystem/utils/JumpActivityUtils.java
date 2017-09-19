package com.pl.gassystem.utils;

import android.content.Context;
import android.content.Intent;

import com.pl.bll.SetBiz;
import com.pl.gassystem.InputComNumActivity;
import com.pl.gassystem.MaintenanceActivity;
import com.pl.gassystem.activity.ht.HtMaintenanceActivity;
import com.pl.gassystem.activity.ht.HtSingleCopyTestActivity;
import com.pl.utils.GlobalConsts;

/**
 * Created by zangyi_shuai_ge on 2017/9/19
 * ���ڸ�App ���ڶ������ģ��
 * ��Щ����������� ������ת�Ľ���ȴ��һ��
 */

public class JumpActivityUtils {


    /**
     * ��ת�����ά������
     */
    public static void jumpToMaintenanceActivity(Context context) {

        SetBiz setBiz = new SetBiz(context);
        Intent mIntent;
        if (setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)) {
            mIntent = new Intent(context, HtMaintenanceActivity.class);
        } else {
            mIntent = new Intent(context, MaintenanceActivity.class);
        }
        context.startActivity(mIntent);
    }

    /**
     * ��ת���������Խ���
     */
    public static void jumpSingleCopyTestActivity(Context context) {
        SetBiz setBiz = new SetBiz(context);
        Intent mIntent;
        if (setBiz.getRunMode().equals(GlobalConsts.RUN_MODE_HANG_TIAN)) {
            mIntent = new Intent(context, HtSingleCopyTestActivity.class);
        } else {
            mIntent = new Intent(context, InputComNumActivity.class);
            mIntent.putExtra("operationType", 1);
        }
        context.startActivity(mIntent);
    }

}
