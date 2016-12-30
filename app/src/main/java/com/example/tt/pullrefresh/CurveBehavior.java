package com.example.tt.pullrefresh;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tt.pullrefresh.widget.CurveView;

/**
 * Created by Joe on 2016/12/29.
 * Email lovejjfg@gmail.com
 */
public class CurveBehavior extends CoordinatorLayout.Behavior<CurveView> {


    public CurveBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CurveView child, View dependency) {
        Log.e("TAG1", "layoutDependsOn:" + dependency);
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CurveView child, View dependency) {
        Log.e("TAG1", "onDependentViewChanged:dependency:: " + dependency.getY() + "height:" + dependency.getHeight());
        Log.e("TAG1", "onDependentViewChanged: child::" + child.toString());
        int height = child.getHeight();
        float v = dependency.getY() / (dependency.getHeight() - height);
        if (v < -1.0) {
            return false;
        }
        Log.e("TAG1", "onDependentViewChanged: 比列::" + v);
        if (v == 0) {
            child.setY(-height);
        } else {
            float y = -height - v * height;
            child.setY(y > 0 ? 0 : y);
        }
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, CurveView child, View target, int dx, int dy, int[] consumed) {
        Log.e("TAG1", "onNestedScroll: dy" + dy);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, CurveView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("TAG1", "onNestedScroll: dyConsumed" + dyConsumed + ";dyUnconsumed:" + dyUnconsumed);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, CurveView child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }
}
