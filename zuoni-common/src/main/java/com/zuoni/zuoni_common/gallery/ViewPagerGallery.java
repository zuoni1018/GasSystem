package com.zuoni.zuoni_common.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zuoni.zuoni_common.R;

import java.util.List;



/**
 * @author Eicky
 * @Description: 自定义Gallery
 * @date: 16/10/20 下午3:19
 * @version: V1.0
 */
public class ViewPagerGallery extends ViewPager {
    private boolean isShadow = true;
    private Context mContext;
    private ViewGroup.LayoutParams mLayoutParams;
    private GalleryOnClickListener mGalleryOnClickListener;

    public ViewPagerGallery(Context context) {
        this(context, null);
    }

    public ViewPagerGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.viewpagergallery);
        isShadow = typedArray.getBoolean(R.styleable.viewpagergallery_isShadow, true);
        typedArray.recycle();
    }

    private void init() {
        int pagerWidth = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        mLayoutParams = this.getLayoutParams();
        if (mLayoutParams == null) {
            mLayoutParams = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams.width = pagerWidth;
        }
        this.setLayoutParams(mLayoutParams);
        this.setPageMargin(-50);
        this.setPageTransformer(true, new GalleryTransformer());
        this.setClipChildren(false);
        ViewGroup view = (ViewGroup) this.getParent();
        if (view != null) {
            view.setClipChildren(false);
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return dispatchTouchEvent(event);
                }
            });
        }
    }

//    public void setImgUrls(List<String> imgUrls) {
//        if (imgUrls == null)
//            throw new RuntimeException("Fuck, imgurls is null");
//        List<View> views = new ArrayList<>();
//        for (int i = 0; i < imgUrls.size(); i++) {
//            String url = imgUrls.get(i);
//            final ImageView imageView = new ImageView(mContext);
//            //imageView.setLayoutParams(mLayoutParams);
//            if (isShadow)
//                with(mContext).load(url).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        imageView.setImageBitmap(ImgUtils.getReverseBitmapById(resource, mContext));
//                    }
//                });
//            else
//                with(mContext).load(url).centerCrop().into(imageView);
//            if (mGalleryOnClickListener != null) {
//                final int finalI = i;
//                imageView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mGalleryOnClickListener.onClick(finalI);
//                    }
//                });
//            }
//            views.add(imageView);
//        }
//        this.setAdapter(new ViewPagerGalleryAdapter(views));
//        init();
//        this.setOffscreenPageLimit(views.size());
//    }

    public void setImgResources(List<View> views) {

//        if (imgResources != null && imgResources.size() > 0) {
//            List<View> views = new ArrayList<>();
//            for (int i = 0; i < imgResources.size(); i++) {
//                final ImageView imageView = new ImageView(mContext);
////                SelectableRoundedImageView
////                SelectableRoundedImageView sriv = new SelectableRoundedImageView(context);
////                sriv.setScaleType(ScaleType.CENTER_CROP);
////                sriv.setCornerRadiusesDP(4, 4, 0, 0);
////                sriv.setBorderWidthDP(4);
////                sriv.setBorderColor(Color.BLUE);
////                sriv.setImageDrawable(drawable);
////                sriv.setOval(true);
//
//
//
//
//
//                Glide
//                        .with(mContext)
//                        .load(imgResources.get(i))
//                        .centerCrop()
//                        .into(imageView);
//
//                views.add(imageView);
//            }
            this.setAdapter(new ViewPagerGalleryAdapter(views));
            init();
            if (views.size() > 3) {
                this.setCurrentItem(1);
            }
            this.setOffscreenPageLimit(views.size());
//        }

    }

    public void setOnClickListener(GalleryOnClickListener galleryOnClickListener) {
        this.mGalleryOnClickListener = galleryOnClickListener;
    }

    public interface GalleryOnClickListener {
        void onClick(int position);
    }
}
