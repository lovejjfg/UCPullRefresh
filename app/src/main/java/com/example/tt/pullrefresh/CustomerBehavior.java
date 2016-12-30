package com.example.tt.pullrefresh;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.tt.pullrefresh.widget.CurveLayout;
import com.example.tt.pullrefresh.widget.CurveView;

/**
 * Created by Joe on 2016/12/29.
 * Email lovejjfg@gmail.com
 */
public class CustomerBehavior extends CoordinatorLayout.Behavior<View> {


    public CustomerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        Log.e("TAG", "layoutDependsOn:" + dependency);
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.e("TAG", "onDependentViewChanged:dependency:: " + dependency.getY() + "height:" + dependency.getHeight());
        Log.e("TAG", "onDependentViewChanged: child::" + child.toString());
        int height = child.getHeight();
        float v = dependency.getY() / (dependency.getHeight() - height);
        if (v < -1.0) {
            return false;
        }
        Log.e("TAG", "onDependentViewChanged: 比列::" + v);
        if (v == 0) {
            child.setY(-height);
        } else {
            float y = -height - v * height;
            child.setY(y > 0 ? 0 : y);
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }


}
