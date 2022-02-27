package com.ishuinzu.childside.transformer;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class PagerTransformer implements ViewPager.PageTransformer {
    private int maxTranslateOffsetX;
    private ViewPager viewPager;

    public PagerTransformer(Context context) {
        this.maxTranslateOffsetX = dp2px(context);
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) page.getParent();
        }

        int leftInScreen = page.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + page.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
    }

    private int dp2px(Context context) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) ((float) 180 * m + 0.5f);
    }
}
