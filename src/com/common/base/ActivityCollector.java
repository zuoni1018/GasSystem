package com.common.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * �����ר�������ռ�Activity
 */

public class ActivityCollector {

    private static List<Activity> mActivities = new ArrayList<>();

    //�򼯺������һ���
    public static void addActivity(Activity pActivity) {
        mActivities.add(pActivity);
    }

    //���������Ƴ�һ���
    public static void removeActivity(Activity pActivity) {
        mActivities.remove(pActivity);
    }

    //�������л
    public static void finishAll() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
