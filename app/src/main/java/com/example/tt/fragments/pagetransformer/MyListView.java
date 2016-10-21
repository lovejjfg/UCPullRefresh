package com.example.tt.fragments.pagetransformer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Joe on 2016/10/18.
 * Email lovejjfg@gmail.com
 */

public class MyListView extends ListView {
    public static final String TAG = "MyListView";
    private int startX;
    private int startY;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: 拦截了！！");
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
                if (Math.abs(endX - startX) < (Math.abs(endY - startY))) {
                    Log.e(TAG, "onTouchEvent: 取消拦截了！！total" + getCount() + ";;最后可见" + getLastVisiblePosition());
                    Log.e(TAG, "onTouchEvent: " + getScrollY());
                    if (endY >= startY && getFirstVisiblePosition() == 0 && getChildAt(0).getTop() == 0) {//下拉  顶点不处理
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (endY < startY && getFirstVisiblePosition() + getChildCount() == getCount() && getChildAt(getChildCount() - 1).getBottom() == getMeasuredHeight()) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

                }
                break;
        }
        return super.onTouchEvent(ev);
    }


}
