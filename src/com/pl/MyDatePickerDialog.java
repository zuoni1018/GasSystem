package com.pl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * Created by zangyi_shuai_ge on 2017/5/12
 */

public class MyDatePickerDialog extends DatePickerDialog {
    public MyDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public MyDatePickerDialog(@NonNull Context context, @StyleRes int theme, @Nullable OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);
    }

    protected void onStop() {
/**
 * ×¢ÊÍ
 */
// super.onStop();
    }
}
