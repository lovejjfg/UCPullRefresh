package com.example.tt.fragments.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Joe on 2016-07-05
 * Email: lovejjfg@163.com
 */
public class ScrollAbleViewPager extends ViewPager {
    private boolean scrollble;

    public ScrollAbleViewPager(Context context) {
        super(context);
    }

    public ScrollAbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //scrollble false 直接返回。如果scrollble 为true 那么还要判断 super.onInterceptTouchEvent(ev)的返回。
        Log.e("TAG", "onInterceptTouchEvent: ...." );
        return scrollble && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("TAG", "onTouchEvent: ScrollAbleViewPager有事件了！！！");
        //如果子View不消费相关的事件，那么又会走到`onTouchEvent`,那么是不可滑动的话，直接就返回 true(!scrollble) ,其他情况那么直接就返回 super.onTouchEvent(ev).
        return !scrollble || super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }
    public void setScrollable(boolean scrollble) {
        this.scrollble = scrollble;
    }
}

