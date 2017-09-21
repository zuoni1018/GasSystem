package com.zuoni.zuoni_common.utils.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by zangyi_shuai_ge on 2017/5/16
 */

public class FileUtils {

    //获得SD卡位置
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;

    }

    //生成路径
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
