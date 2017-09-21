package com.zuoni.zuoni_common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import java.lang.reflect.Field;

/**
 * Created by zangyi_shuai_ge on 2017/9/12
 * 状态栏View
 */

public class StatusView extends View {
    private static boolean isInitialize = false;
    private static int statusBarHeight = 0;
    private static int screenWidth = 0;

    private static void initialize(View view) {
        if(!isInitialize) {
            isInitialize = true;
            Context context = view.getContext();
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;

            try {
                Class e = Class.forName("com.android.internal.R$dimen");
                Field outRect1 = e.getField("status_bar_height");
                int decorView1 = Integer.parseInt(outRect1.get(e.newInstance()).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(decorView1);
            } catch (Throwable var5) {
                Rect outRect = new Rect();
                View decorView = getParentView(view);
                decorView.getWindowVisibleDisplayFrame(outRect);
                statusBarHeight = outRect.top;
            }

        }
    }

    private static View getParentView(View view) {
        ViewParent viewParent = view.getParent();
        return viewParent != null?getParentView((View)viewParent):view;
    }

    public StatusView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(this);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable.StatusView);
//        int fitsViewId = typedArray.getInt(styleable.StatusView_fitsView, -1);
//        typedArray.recycle();
//        if(fitsViewId != -1) {
//            View parent = getParentView(this);
//            View fitsView = parent.findViewById(fitsViewId);
//            if(fitsView != null && Build.VERSION.SDK_INT < 21 && Build.VERSION.SDK_INT > 14) {
//                fitsView.setFitsSystemWindows(true);
//            }
//        }

    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(Build.VERSION.SDK_INT >= 21) {
            this.setMeasuredDimension(screenWidth, statusBarHeight);
        } else {
            this.setMeasuredDimension(0, 0);
            this.setVisibility(GONE);
        }
    }
}