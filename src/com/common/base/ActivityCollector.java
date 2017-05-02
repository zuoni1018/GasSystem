package com.common.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 这个类专门用来收集Activity
 */

public class ActivityCollector {

    private static List<Activity> mActivities = new ArrayList<>();

    //向集合里添加一个活动
    public static void addActivity(Activity pActivity) {
        mActivities.add(pActivity);
    }

    //从数组里移除一个活动
    public static void removeActivity(Activity pActivity) {
        mActivities.remove(pActivity);
    }

    //销毁所有活动
    public static void finishAll() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
