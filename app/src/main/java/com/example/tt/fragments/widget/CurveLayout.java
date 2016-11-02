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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tt.fragments.utils.AnimUtils;
import com.example.tt.fragments.utils.ViewOffsetHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 */
public class CurveLayout extends FrameLayout {
    private static final String TAG = "CurveLayout";
    private static final String KEY_DEFAULT = "key_default";
    private static final String KEY_EXPAND = "key_expand";
    private static final String KEY_DISMISS_OFFSET = "key_dismissoffset";
    private static final String KEY_TOP = "key_top";
    // constants
    private static final int MIN_SETTLE_VELOCITY = 6000; // px/s

    private final int MIN_FLING_VELOCITY;
    private final int MIN_DRAG_DISTANCE = 200;

    // child views & helpers
    private View sheet;
    private ViewDragHelper sheetDragHelper;
    private ViewOffsetHelper sheetOffsetHelper;

    // state
    private List<Callbacks> callbacks;
    private int sheetExpandedTop;
    private int sheetBottom;
    private int dismissOffset;
    private int nestedScrollInitialTop;
    private boolean settling = false;
    private boolean isNestedScrolling = false;
    private boolean initialHeightChecked = false;
    private boolean hasInteractedWithSheet = false;
    private float currentX;
    private boolean canUp;
    private boolean reverse;
    private int tabOffset;
    private int currentTop;

    public CurveLayout(Context context) {
        this(context, null, 0);
    }

    public CurveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurveLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        MIN_FLING_VELOCITY = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    /**
     * Callbacks for responding to interactions with the bottom sheet.
     */
    public static abstract class Callbacks {

        public void onSheetNarrowed() {

        }

        public void onSheetExpanded() {
        }

        public void onSheetPositionChanged(int sheetTop, float currentX, int dy, boolean userInteracted) {
        }
    }

    public void registerCallback(Callbacks callback) {
        if (callbacks == null) {
            callbacks = new CopyOnWriteArrayList<>();
        }
        callbacks.add(callback);
    }

    public void unregisterCallback(Callbacks callback) {
        if (callbacks != null && !callbacks.isEmpty()) {
            callbacks.remove(callback);
        }
    }

    public void dismiss() {
        reverse = true;
        animateSettle(dismissOffset, 0);
    }

    public void expand() {
        animateSettle(0, 0);
    }

    public boolean isExpanded() {
        Log.e(TAG, "isExpanded: top:" + sheet.getTop() + ";;;sheetExpandedTop:" + sheetExpandedTop);

        return sheet.getTop() == sheetExpandedTop;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (sheet != null) {
            throw new UnsupportedOperationException("CurveLayout must only have 1 child view");
        }
        sheet = child;
        sheetOffsetHelper = new ViewOffsetHelper(sheet);
        sheet.addOnLayoutChangeListener(sheetLayout);
        // force the sheet contents to be gravity bottom. This ain't a top sheet.
        ((LayoutParams) params).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        super.addView(child, index, params);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        currentX = ev.getRawX();
        Log.e(TAG, "BottomSheet onInterceptTouchEvent: " + currentX);
        if (isExpanded()) {
            Log.e(TAG, "已经展开了！！！: " + currentX);
            sheetDragHelper.cancel();
            return false;
        }
        Log.e(TAG, "已经展开了就不应该走到这里了！！！！: " + currentX);
        hasInteractedWithSheet = true;
        if (isNestedScrolling) return false;    /* prefer nested scrolling to dragging */

        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            sheetDragHelper.cancel();
            return false;
        }
        Log.e(TAG, "已经展开了就不应该走到这里了！！！！: " + currentX);
        return isDraggableViewUnder((int) ev.getX(), (int) ev.getY())
                && (sheetDragHelper.shouldInterceptTouchEvent(ev));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (isExpanded()) {
//            return false;
//        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "BottomSheet onTouchEvent DOWN: " + currentX);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.e(TAG, "BottomSheet onTouchEvent MOVE: " + currentX);
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG, "BottomSheet onTouchEvent UP: " + currentX);
        }
        if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            Log.e(TAG, "BottomSheet onTouchEvent CANCEL: " + currentX);
        }
        currentX = ev.getRawX();
        sheetDragHelper.processTouchEvent(ev);
        return sheetDragHelper.getCapturedView() != null || super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (sheetDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sheet != null) {
            sheetDragHelper = ViewDragHelper.create((ViewGroup) sheet.getParent(), dragHelperCallbacks);
            sheetDragHelper.captureChildView(sheet, 1);
        }
    }

    private boolean isDraggableViewUnder(int x, int y) {
        return getVisibility() == VISIBLE && sheetDragHelper.isViewUnder(this, x, y);
    }

    private void animateSettle(int targetOffset, float initialVelocity) {
        animateSettle(sheetOffsetHelper.getTopAndBottomOffset(), targetOffset, initialVelocity);
    }

    private void animateSettle(int initialOffset, final int targetOffset, float initialVelocity) {
        if (settling) return;
        Log.e(TAG, "animateSettle:TopAndBottom :::" + sheetOffsetHelper.getTopAndBottomOffset());
        if (sheetOffsetHelper.getTopAndBottomOffset() == targetOffset) {
            if (targetOffset >= dismissOffset) {
                dispatchDismissCallback();
            }
            return;
        }

        settling = true;
        final boolean dismissing = targetOffset == dismissOffset;
        final long duration = computeSettleDuration(initialVelocity, dismissing);
        final ObjectAnimator settleAnim = ObjectAnimator.ofInt(sheetOffsetHelper,
                ViewOffsetHelper.OFFSET_Y,
                initialOffset,
                targetOffset);
        settleAnim.setDuration(duration);
        settleAnim.setInterpolator(getSettleInterpolator(dismissing, initialVelocity));
        settleAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchPositionChangedCallback();
                if (dismissing) {
                    dispatchDismissCallback();
                }
                settling = false;
            }
        });
        if (callbacks != null && !callbacks.isEmpty()) {
            settleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedFraction() > 0f) {
                        dispatchPositionChangedCallback();
                    }
                }
            });
        }
        settleAnim.start();
    }

    /**
     * Provides the appropriate interpolator for the settle animation depending upon:
     * – If dismissing then exit at full speed i.e. linearly otherwise decelerate
     * – If have initial velocity then respect it (i.e. start linearly) otherwise accelerate into
     * the animation.
     */
    private TimeInterpolator getSettleInterpolator(boolean dismissing, float initialVelocity) {
        if (initialVelocity != 0) {
            if (dismissing) {
                return AnimUtils.getLinearInterpolator();
            } else {
                return AnimUtils.getLinearOutSlowInInterpolator(getContext());
            }
        } else {
            if (dismissing) {
                return AnimUtils.getFastOutLinearInInterpolator(getContext());
            } else {
                return AnimUtils.getFastOutSlowInInterpolator(getContext());
            }
        }
    }

    /**
     * Calculate the duration of the settle animation based on the gesture velocity
     * and how far it has to go.
     */
    private long computeSettleDuration(final float velocity, final boolean dismissing) {
        // enforce a min velocity to prevent too slow settling
        final float clampedVelocity = Math.max(MIN_SETTLE_VELOCITY, Math.abs(velocity));
        final int settleDistance = Math.abs(dismissing
                ? sheetBottom - sheet.getTop()
                : sheet.getTop() - sheetExpandedTop);
        // velocity is in px/s but we want duration in ms thus * 1000
        return (long) (settleDistance * 1000 / clampedVelocity);
    }

    private final ViewDragHelper.Callback dragHelperCallbacks = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == sheet && !isExpanded();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.min(Math.max(top, sheetExpandedTop), sheetBottom);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return sheet.getLeft();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return sheetBottom - sheetExpandedTop;
        }

        @Override
        public void onViewPositionChanged(View child, int left, int top, int dx, int dy) {
            // notify the offset helper that the sheets offsets have been changed externally
            reverse = false;
            sheetOffsetHelper.resyncOffsets();
            dispatchPositionChangedCallback();
            canUp = Math.abs(top - dismissOffset) > MIN_DRAG_DISTANCE;
        }

        @Override
        public void onViewReleased(View releasedChild, float velocityX, float velocityY) {
            // dismiss on downward fling, otherwise settle back to expanded position
            boolean expand = canUp || Math.abs(velocityY) > MIN_FLING_VELOCITY;
            reverse = false;
            animateSettle(expand ? tabOffset : dismissOffset, velocityY);
        }
    };

    private final OnLayoutChangeListener sheetLayout = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {

            sheetExpandedTop = top + tabOffset;
            sheetBottom = bottom;
            currentTop = top;
            sheetOffsetHelper.onViewLayout();

            // modal bottom sheet content should not initially be taller than the 16:9 keyline
            if (!initialHeightChecked) {
                applySheetInitialHeightOffset(false, -1);
                initialHeightChecked = true;
            } else if (!hasInteractedWithSheet
                    && (oldBottom - oldTop) != (bottom - top)) { /* sheet height changed */
                /* if the sheet content's height changes before the user has interacted with it
                   then consider this still in the 'initial' state and apply the height constraint,
                   but in this case, animate to it */
                applySheetInitialHeightOffset(true, oldTop - sheetExpandedTop);
            }
            Log.e(TAG, "onLayoutChange: 布局变化了！！" + sheet.getTop());
        }
    };

    private void applySheetInitialHeightOffset(boolean animateChange, int previousOffset) {
        final int minimumGap = sheet.getMeasuredWidth() / 16 * 9;
        if (sheet.getTop() < minimumGap) {
            final int offset = minimumGap - sheet.getTop();
            if (animateChange) {
                animateSettle(previousOffset, offset, 0);
            } else {
                sheetOffsetHelper.setTopAndBottomOffset(offset);
            }
        }
    }

    private void dispatchDismissCallback() {
        isNestedScrolling = false;
        if (callbacks != null && !callbacks.isEmpty()) {
            for (Callbacks callback : callbacks) {
                callback.onSheetNarrowed();
            }
        }
    }

    private void dispatchPositionChangedCallback() {
//        int dy = sheet.getTop() - currentTop;
//        currentTop = sheet.getTop();
        if (callbacks != null && !callbacks.isEmpty()) {
            for (Callbacks callback : callbacks) {
                callback.onSheetPositionChanged(sheet.getTop(), currentX, -1, reverse);
                if (isExpanded()) {
                    if (sheetDragHelper != null) {
                        sheetDragHelper.cancel();
                    }
                    callback.onSheetExpanded();
                }
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        // TODO: 2016/10/25 恢复的时候，状态乱了
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());
        bundle.putBoolean(KEY_EXPAND, isExpanded());
        bundle.putInt(KEY_DISMISS_OFFSET, dismissOffset);
        bundle.putInt(KEY_TOP, sheet.getTop());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            boolean b = bundle.getBoolean(KEY_EXPAND);
            dismissOffset = bundle.getInt(KEY_DISMISS_OFFSET);
            if (b) {
                Log.e(TAG, "onRestoreInstanceState: 读取缓存，展开了！");
                expand();
            } else {
                Log.e(TAG, "onRestoreInstanceState: 读取缓存，关闭了了！");
                dismiss();
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setDismissOffset(int dismissOffset) {
        this.dismissOffset = dismissOffset;
    }

    public void setTabOffset(int tabOffset) {
        if (this.tabOffset != tabOffset) {
            this.tabOffset = tabOffset;
            sheetExpandedTop = currentTop + tabOffset;
        }
    }
}
