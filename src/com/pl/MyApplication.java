package com.pl;

import android.app.Application;

/**
 * Created by zangyi_shuai_ge on 2017/8/24
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LitePal.initialize(this);
//        Connector.getDatabase();//创建数据
//        LitePalDB litePalDB = LitePalDB.fromDefault("gasSystem2");
//        LitePal.use(litePalDB); // 使用 litepal.xml 创建一个新库
    }
}
