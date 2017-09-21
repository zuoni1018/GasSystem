package com.zuoni.zuoni_common.gallery;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author Eicky
 * @Description: 滑动效果
 * @date: 16/10/20 下午4:44
 * @version: V1.0
 */
public class GalleryTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.70f;
    private static final float MIN_SCALE2 = 0.85f;
    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float scaleFactor2 = Math.max(MIN_SCALE2, 1 - Math.abs(position));

        float rotate = 8 * Math.abs(position);
        if (position < -1) {

        } else if (position < 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor2);
            page.setRotationY(rotate);
        } else if (position >= 0 && position < 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor2);
            page.setRotationY(-rotate);
        } else if (position >= 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor2);
            page.setRotationY(-rotate);
        }
    }
}
