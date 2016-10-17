package com.example.tt.fragments.pagetransformer;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by HanHailong on 15/9/27.
 */
public class MyScalePageTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 1.2f;
    public static final float MIN_SCALE = 0.6f;

    @Override
    public void transformPage(View page, float position) {
        int p = (Integer)page.getTag();

        Log.e(TAG, "transformPage: " + position);
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
