package com.pl.gassystem.activity.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 抄表App 重构Activity收集器
 */

public class ActivityCollector {

    private static List<Activity> mActivities = new ArrayList<>();


    public static void addActivity(Activity pActivity) {
        mActivities.add(pActivity);
    }

    public static void removeActivity(Activity pActivity) {
        mActivities.remove(pActivity);
    }

    public static void finishAll() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
