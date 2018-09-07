package com.github.boybeak.starter.widget.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2017/5/14.
 */

public class VisualizerBarView extends VisualizerView {

    private static final String TAG = VisualizerBarView.class.getSimpleName();

    private Paint mPaint;

    private int mSkipCount = 0, mOffset;

    private float mHeightAcceleration = 1, mHeightUnit = 1;

    public VisualizerBarView(Context context) {
        this(context, null);
    }

    public VisualizerBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVisualizerBarView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VisualizerBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVisualizerBarView(context, attrs);
    }

    private void initVisualizerBarView (Context context, @Nullable AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VisualizerBarView);
        setSkipCount(array.getInt(R.styleable.VisualizerBarView_skipCount, 0));
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final float heightSpace = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) * 1f;
        mHeightAcceleration = heightSpace / getPeriod();
        mHeightUnit = heightSpace / Byte.SIZE;

        Log.v(TAG, "onMeasure mHeightAcceleration=" + mHeightAcceleration + " mHeightUnit=" + mHeightUnit);
    }

    private byte[] mLastWaveform = null;
    private long mLastDrawTime;
    @Override
    public void onDrawWaveform(Canvas canvas, byte[] waveform, int samplingRate) {
        final long now = SystemClock.elapsedRealtime();
        final long delta = now - mLastDrawTime;

        float widthUnit = getWidth() * 1f / waveform.length;
        for (int i = 0; i < waveform.length; i += mOffset) {
            //canvas.drawText("w:" + waveform[i], 10 + widthUnit * i, 20, mPaint);
            final float l = i * widthUnit;
            final float r = l + 10;
            final float b = getHeight() - getPaddingBottom();
            float height = delta * mHeightAcceleration;
            float maxHeight = ((int)waveform[i] + Math.abs((int) Byte.MAX_VALUE)) * mHeightUnit;
            height = Math.min(height, maxHeight);
            height = Math.max(height, 1);
            float t = b - height;
            Log.v(TAG, "onDrawWaveform l=" + l + " r=" + r + " b=" + b + " t=" + t + " waveform[" + i + "]=" + waveform[i]);
            canvas.drawRect(l, t, r, b, mPaint);
        }
        if (delta > getPeriod()) {
            mLastDrawTime = now;
            mLastWaveform = waveform;
            Log.v(TAG, "onDrawWaveform period=" + getPeriod() + " isAttachedWithMediaSession=" + isAttachedWithMediaSession());
        }
        if (isAttachedWithMediaSession()) {
            invalidate();
        }
    }

    @Override
    public void onDrawFft(Canvas canvas, byte[] fft, int samplingRate) {
        StringBuilder builder = new StringBuilder("onDrawFft {");
        for (int i = 0; i < fft.length; i++) {
            builder.append(fft[i]);
            if (i < fft.length - 1) {
                builder.append(",");
            }
        }
        Log.v(TAG, builder.toString());
        /*Log.v(TAG, "onDrawFft data.length=" + fft.length);
        int widthUnit = getWidth() / fft.length;
        for (int i = 0; i < fft.length; i++) {
            canvas.drawText("f:" + fft[i], 10 + widthUnit * i, 40, mPaint);
        }*/
    }

    private void setSkipCount (int skipCount) {
        mSkipCount = skipCount;
        mOffset = mSkipCount + 1;
    }
}
