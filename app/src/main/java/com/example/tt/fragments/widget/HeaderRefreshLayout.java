/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tt.fragments.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


@SuppressWarnings("unused")
public class HeaderRefreshLayout extends FrameLayout implements NestedScrollingParent, NestedScrollingChild, TouchCircleView.OnLoadingListener {
    private static String TAG = "HeaderRefresh";
    // configurable attribs
    // state
    private float totalDrag;
    private TouchCircleView header;
    private float defaulTranslationY;
    FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    @Nullable
    private View targetView;
    private boolean mRefresh;
    private boolean scrollble = true;


    public HeaderRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public HeaderRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderRefreshLayout(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        header = new TouchCircleView(getContext());
        header.addLoadingListener(this);
        float density = context.getResources().getDisplayMetrics().density;
        addView(header, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (density * 120)));
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }


    // NestedScrollingChild

    @Override
    public void setEnabled(boolean enabled) {
        setNestedScrollingEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }


    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // if we're in a drag gesture and the user reverses up the we should take those events
        if (!header.ismRunning() && dy > 0 && totalDrag > defaulTranslationY) {
            Log.e(TAG, "onNestedPreScroll:消费 " + dy);
            updateOffset(dy);
            consumed[1] = dy;
        }
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        if (Math.abs(dyUnconsumed) > 150) {
            return;
        }
        if (!header.ismRunning() && dyUnconsumed < 0) {
            Log.e(TAG, "onNestedScroll:未消费：： " + dyUnconsumed);
            updateOffset(dyUnconsumed);
        }

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!header.ismRunning() && !consumed) {
            float v = velocityY * 0.3f;
            updateOffset((int) (v));
            return true;
        }
        return false;
    }


    private void updateOffset(int dyUnconsumed) {

        totalDrag -= dyUnconsumed * 0.5;
        Log.e(TAG, "updateOffset: " + totalDrag);
        if (totalDrag < 0) {
            totalDrag = 0;
        }
        if (totalDrag > header.getHeight() * 1.6f) {
            totalDrag = header.getHeight() * 1.6f;
        }
        if (targetView != null) {
            targetView.setTranslationY(totalDrag);
        }
        if (!header.ismRunning()) {
            header.handleOffset((int) (totalDrag));
        }
//        if (header.getTranslationY() == 0 && !isStart) {
//            isStart = true;
//            header.start();
//        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        mNestedScrollingParentHelper.onStopNestedScroll(child);
        Log.e(TAG, "onStopNestedScroll: ");
        header.resetTouch();
    }

    private void resetDrag(int offset) {
        if (targetView != null) {
            targetView.animate()
                    .translationY(offset)
                    .setDuration(200)
                    .setInterpolator(fastOutLinearInInterpolator)
                    .start();
            totalDrag = defaulTranslationY;
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
        if (targetView != null) {
            defaulTranslationY = targetView.getTranslationY();
        }
        totalDrag = defaulTranslationY;
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (targetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(header)) {
                    targetView = child;
                    break;
                }
            }
        }
    }

    @Override
    public void onProgressStateChange(int state, boolean isHide) {
        if (isHide && !header.ismRunning()) {
            resetDrag(0);
        }
    }


    @Override
    public void onProgressLoading() {
        resetDrag((int) (header.getHeight() * 0.6f));
    }

    @Override
    public void onGoBackHome() {

    }


    public void setRefresh(boolean refresh) {
        if (mRefresh == refresh) {
            return;
        }
        mRefresh = refresh;
        header.setRefresh(mRefresh);
    }

    public void setRefreshError() {
        header.setRefreshError();
    }

    public void setRefreshSuccess() {
        header.setRefreshSuccess();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: ....");
        if (!scrollble) {
            return header.ismRunning() || super.onInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return header.ismRunning() || super.onTouchEvent(ev);
        } else {
            return super.onTouchEvent(ev);
        }
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setLoadingScrollable(boolean scrollble) {
        this.scrollble = scrollble;
    }

    public void addLoadingListener(@NonNull TouchCircleView.OnLoadingListener listener) {
        header.addLoadingListener(listener);

    }

    public boolean removeLoadingListener(@NonNull TouchCircleView.OnLoadingListener listener) {
        return header.removeLoadingListener(listener);
    }
}
