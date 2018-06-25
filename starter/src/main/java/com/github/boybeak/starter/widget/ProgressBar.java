package com.github.boybeak.starter.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 16/7/23.
 */
public class ProgressBar extends View {

    private static final String TAG = ProgressBar.class.getSimpleName();

    private Paint mPaint = null;

    private int mProgress = 0, mMax = 100;

    public ProgressBar(Context context) {
        super(context);
        initThis(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThis(context, attrs);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis(context, attrs);
    }

    private void initThis(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
            try {
                final int color = a.getColor(R.styleable.ProgressBar_progressColor, Color.BLACK);
                mPaint.setColor(color);
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setStrokeWidth(6);
                Log.v(TAG, "Paint inited");
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.v(TAG, "onMeasure width=" + width + " height=" + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int length = (int) ((float) getWidth() * mProgress / mMax);
        final int left = getPaddingStart();
        final int top = getPaddingTop();
        final int right = left + length;
        final int bottom = top + getHeight();

        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        Log.v(TAG, "setProgress " + progress + " color=" + Integer.toHexString(mPaint.getColor()));
        invalidate();
    }

    public void setProgressColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}
