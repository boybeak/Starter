package com.github.boybeak.starter.widget.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2017/5/7.
 */

public abstract class AmplitudeView extends View {

    private static final String TAG = AmplitudeView.class.getSimpleName();

    private int mPeriod;

    private MediaRecorder mRecorder;

    private List<Integer> mAmpArray;

    private Runnable mAmpRun = new Runnable() {
        @Override
        public void run() {
            if (mRecorder != null) {
                final int amp = mRecorder.getMaxAmplitude();
                mAmpArray.add(amp);
                onNewAmplitude(amp);
                postDelayed(this, getPeriod());
            }
        }
    };

    private int mLastAmplitude;

    private Amplitude mAmp;

    private boolean isPlayStarted = false, isRecorderPaused = false, isPlayPaused = false;

    private long mLastNewAmpTime = 0, mPauseAt;

    public AmplitudeView(Context context) {
        this(context, null);
    }

    public AmplitudeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmplitudeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAmplitudeView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AmplitudeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAmplitudeView(context, attrs);
    }

    private void initAmplitudeView (Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AmplitudeView);
        setPeriod(array.getInt(R.styleable.AmplitudeView_period,
                getContext().getResources().getInteger(R.integer.config_period_default)));
        array.recycle();
        mAmpArray = new ArrayList<>();
    }

    public void onNewAmplitude (int amplitude) {
        mLastAmplitude = amplitude;
        mLastNewAmpTime = SystemClock.elapsedRealtime();
    }

    public int getLastAmplitude () {
        return mLastAmplitude;
    }

    public int getPeriod() {
        return mPeriod;
    }

    public void setPeriod(int period) {
        this.mPeriod = period;
    }

    public boolean isAttachedWithRecorder () {
        return mRecorder != null;
    }

    public void attachMediaRecorder(MediaRecorder recorder) {
        mAmp = null;
        stopPlay();
        mAmpArray.clear();
        mRecorder = recorder;
        post(mAmpRun);
    }

    public void onMediaRecorderPaused () {
        isRecorderPaused = true;
        removeCallbacks(mAmpRun);
        mPauseAt = SystemClock.elapsedRealtime();
    }

    public void onMediaRecorderResumed () {
        isRecorderPaused = false;
        if (mLastNewAmpTime > 0) {
            postDelayed(mAmpRun, getPeriod() - (mPauseAt - mLastNewAmpTime));
        } else {
            post(mAmpRun);
        }
    }

    public boolean isRecorderPaused () {
        return isRecorderPaused;
    }

    public Amplitude detachedMediaRecorder () {
        Amplitude amplitude = new Amplitude(mPeriod, mAmpArray);

        mAmpArray.clear();
        removeCallbacks(mAmpRun);
        mRecorder = null;
        setAmplitude(amplitude);
        return amplitude;
    }

    public int getAmplitudeSize () {
        return mAmpArray.size();
    }

    public int getAmplitude (int index) {
        return mAmpArray.get(index);
    }

    public void setAmplitude (Amplitude amplitude) {
        if (isAttachedWithRecorder()) {
            detachedMediaRecorder();
        }
        mAmp = amplitude;
        mAmpArray.clear();
        mAmpArray.addAll(getAmplitude().getAmplitudeList());
        invalidate();
    }

    public Amplitude getAmplitude () {
        return mAmp;
    }

    public boolean hasAmplitude () {
        return mAmp != null;
    }

    public void startPlay () {
        isPlayStarted = true;
        invalidate();
    }

    public void pausePlay () {
        isPlayPaused = true;
    }

    public void resumePlay () {
        isPlayPaused = false;
        invalidate();
    }

    public void stopPlay () {
        //mAmp = null;
        isPlayStarted = false;
    }

    public boolean isPlayStarted () {
        return  mAmp != null && isPlayStarted;
    }

    public boolean isPlayPaused () {
        return isPlayPaused;
    }

}
