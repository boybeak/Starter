package com.github.boybeak.starter.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by boybe on 2017/4/10.
 */

public class BottomHideBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final String TAG = BottomHideBehavior.class.getSimpleName();

    private int mLastDyConsumed = 0;
    private ObjectAnimator mAnimator;

    public BottomHideBehavior() {
    }

    public BottomHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int nestedScrollAxes) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        float translationY = child.getTranslationY() + dyConsumed;

        if (translationY > child.getHeight()) {
            translationY = child.getHeight();
        } else if (translationY < 0) {
            translationY = 0;
        }
        child.setTranslationY(translationY);
        mLastDyConsumed = dyConsumed;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target) {
        if (child.getTranslationY() == 0 || child.getTranslationY() == child.getHeight()) {
            return;
        }
        if (mLastDyConsumed > 0) {
            mAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getTranslationY(), child.getHeight());
        } else if (mLastDyConsumed < 0) {
            mAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getTranslationY(), 0);
        }
        if (mAnimator != null) {
            mAnimator.start();
        }
    }
}
