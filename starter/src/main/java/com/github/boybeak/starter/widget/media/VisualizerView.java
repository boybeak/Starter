package com.github.boybeak.starter.widget.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2017/5/14.
 */

public abstract class VisualizerView extends View {

    private Visualizer mVisualizer;

    private Visualizer.OnDataCaptureListener mCaptureListener = new Visualizer.OnDataCaptureListener() {
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            if (isWaveformEnable) {
                mWaveform = waveform;
                mSamplingRate = samplingRate;
                invalidate();
            }
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            if (isFftEnable) {
                mFft = fft;
                mSamplingRate = samplingRate;
                invalidate();
            }
        }
    };

    private byte[] mWaveform, mFft;
    private int mSamplingRate;

    private boolean isWaveformEnable, isFftEnable;

    private int mPeriod;

    private int mCaptureSize = 128;

    private int mMediaSession = -1;

    public VisualizerView(Context context) {
        this(context, null);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVisualizerView(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVisualizerView(context,attrs);
    }

    private void initVisualizerView (Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VisualizerView);
        mPeriod = array.getInt(R.styleable.VisualizerView_period,
                getContext().getResources().getInteger(R.integer.config_period_default));
        mCaptureSize = array.getInt(R.styleable.VisualizerView_captureSize, 128);
        isWaveformEnable = array.getBoolean(R.styleable.VisualizerView_waveformEnable, true);
        isFftEnable = array.getBoolean(R.styleable.VisualizerView_fftEnable, true);
        array.recycle();
    }

    public void attachMediaPlayer (MediaPlayer player) {
        attachMediaSession(player.getAudioSessionId());
    }

    public void attachMediaPlayer (MediaPlayer player, int period, boolean waveformEnable, boolean fftEnable) {
        attachMediaSession(player.getAudioSessionId(), mCaptureSize, period, waveformEnable, fftEnable);
    }

    public void attachMediaSession (int mediaSession) {
        attachMediaSession(mediaSession, mCaptureSize, mPeriod, isWaveformEnable, isFftEnable);
    }

    public void attachMediaSession (int mediaSession, int captureSize, int period, boolean waveformEnable, boolean fftEnable) {
        mMediaSession = mediaSession;
        this.mPeriod = period;
        this.isWaveformEnable = waveformEnable;
        this.isFftEnable = fftEnable;
        mVisualizer = new Visualizer(mediaSession);
        mVisualizer.setCaptureSize(captureSize);
        mVisualizer.setDataCaptureListener(mCaptureListener, (int)((1f * 1000 / mPeriod) * 1000),
                isWaveformEnable, isFftEnable);
        mVisualizer.setEnabled(true);
    }

    public void detachMediaSession() {
        mVisualizer.setEnabled(false);
        mVisualizer.release();
        mVisualizer = null;
        mMediaSession = -1;
    }

    public boolean isAttachedWithMediaSession () {
        return mVisualizer != null;
    }

    public boolean isAttachedWithMediaSession (int mediaSession) {
        return isAttachedWithMediaSession() && mMediaSession == mediaSession;
    }

    public boolean isWaveformEnable() {
        return isWaveformEnable;
    }

    public boolean isFftEnable() {
        return isFftEnable;
    }

    public int getPeriod() {
        return mPeriod;
    }

    /**
     * This method will not work after attached with media session.
     * @param waveformEnable
     */
    public void setWaveformEnable(boolean waveformEnable) {
        if (isAttachedWithMediaSession()) {
            return;
        }
        isWaveformEnable = waveformEnable;
    }

    /**
     * This method will not work after attached with media session.
     * @param fftEnable
     */
    public void setFftEnable(boolean fftEnable) {
        if (isAttachedWithMediaSession()) {
            return;
        }
        isFftEnable = fftEnable;
    }

    /**
     * This method will not work after attached with media session.
     * @param period
     */
    public void setPeriod(int period) {
        if (isAttachedWithMediaSession()) {
            return;
        }
        this.mPeriod = period;
    }

    public int getCaptureSize() {
        return mCaptureSize;
    }

    /**
     *
     * @param captureSize must be pow of 2, and between {@link Visualizer @getCaptureSizeRange}
     */
    public void setCaptureSize(int captureSize) {
        this.mCaptureSize = captureSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWaveform != null) {
            onDrawWaveform(canvas, mWaveform, mSamplingRate);
        }
        if (mFft != null) {
            onDrawFft(canvas, mFft, mSamplingRate);
        }
    }

    public abstract void onDrawWaveform (Canvas canvas, byte[] waveform, int samplingRate);
    public abstract void onDrawFft (Canvas canvas, byte[] fft, int samplingRate);
}
