package com.github.boybeak.starter.widget.media;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2017/5/10.
 */

public class AmplitudeBezierView extends AmplitudeView {

    private static final String TAG = AmplitudeBezierView.class.getSimpleName();

    private static final float[] P = {0f, 1f, 0f, -1f};

    private Path mPath = null;

    private Paint mPaint;

    private float mStartX, mStartY;
    private float mOffset;
    private int mKeyPointCount = 1;
    private float mUnitX = 0;

    private PointF[] mPoints = null;

    private int mAmplitudeValue;

    private int mMoveSpeed = 8;

    private AnimatorSet mAnimSet;
    private ObjectAnimator mAmpAnim, mColorAnim;

    private float mLineWidth, mHintLineWidth;

    private int mLineColor = Color.DKGRAY, mHintLineColor = Color.LTGRAY;

    private boolean showHintLine = false;

    private int mPlayingCursor;
    private Runnable mPlayingRun = new Runnable() {
        @Override
        public void run() {
            Amplitude amp = getAmplitude();
            if (isPlayStarted() && amp != null) {
                int[] ampArray = amp.getAmplitudeArray();
                if (mPlayingCursor < ampArray.length) {
                    onNewAmplitude(amp.getAmplitudeArray()[mPlayingCursor++]);
                    postDelayed(this, getPeriod());
                }
            }
        }
    };

    public AmplitudeBezierView(Context context) {
        this(context, null);
    }

    public AmplitudeBezierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmplitudeBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAmplitudeBezierView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AmplitudeBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAmplitudeBezierView(context, attrs);
    }

    private void initAmplitudeBezierView (Context context, AttributeSet attrs) {

        mPaint = new Paint();

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AmplitudeBezierView);
            setWaveCount(array.getInt(R.styleable.AmplitudeBezierView_waveCount, 1));
            setMoveSpeed(array.getInt(R.styleable.AmplitudeBezierView_moveSpeed, 8));
            setLineWidth(array.getDimensionPixelSize(R.styleable.AmplitudeBezierView_lineWidth, 4));
            setLineColor(array.getColor(R.styleable.AmplitudeBezierView_lineColor, Color.DKGRAY));
            setShowHintLine(array.getBoolean(R.styleable.AmplitudeBezierView_showHintLine, false));
            setHintLineColor(array.getColor(R.styleable.AmplitudeBezierView_hintLineColor, Color.LTGRAY));
            setHintLineWidth(array.getDimensionPixelSize(R.styleable.AmplitudeBezierView_hintLineWidth, 4));
            array.recycle();
        }

        mPath = new Path();

        mPaint.setAntiAlias(true);
        if (showHintLine) {
            mPaint.setColor(mHintLineColor);
        } else {
            mPaint.setColor(mLineColor);
        }
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStartX = -getMeasuredWidth();
        mStartY = getMeasuredHeight() / 2;
        mUnitX = getMeasuredWidth() * 1.0f / (mKeyPointCount * 2);

        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i].x = mStartX + i * mUnitX;
            mPoints[i].y = mStartY + P[i % P.length] * 0;
            //Log.v(TAG, "onMeasure [" + i + "]" + " x=" + mPoints[i].x + " y=" + mPoints[i].y);
        }
    }

    @Override
    public Amplitude detachedMediaRecorder() {
        animateBezierLine(0);
        return super.detachedMediaRecorder();
    }

    @Override
    public void startPlay() {
        super.startPlay();
        post(mPlayingRun);
    }

    @Override
    public void pausePlay() {
        super.pausePlay();
        removeCallbacks(mPlayingRun);
        animateBezierLine(0);
    }

    @Override
    public void resumePlay() {
        super.resumePlay();
        post(mPlayingRun);
    }

    @Override
    public void stopPlay() {
        super.stopPlay();
        removeCallbacks(mPlayingRun);
        animateBezierLine(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOneBezier(canvas);
        /*if (isAttachedWithRecorder() || isPlaying()) {

        } else if (showHintLine) {
            drawOneBezier(canvas);
            //animateBezierLine(0);
        }*/
        //invalidate();
    }
    private void drawOneBezier (Canvas canvas) {
        if (mOffset > getWidth()) {
            mOffset = 0;
        }
        mPath.reset();
        mPath.moveTo(mPoints[0].x + mOffset, mPoints[0].y);
        for (int i = 1; i < mPoints.length; i += 2) {
            mPath.quadTo(mPoints[i].x + mOffset, mPoints[i].y + computeOffsetY(i), mPoints[i + 1].x + mOffset, mPoints[i + 1].y + computeOffsetY(i + 1));
        }
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawPath(mPath, mPaint);
        mOffset += mMoveSpeed;
    }

    @Override
    public void onNewAmplitude(int amplitude) {
        animateBezierLine(amplitude);
        super.onNewAmplitude(amplitude);
    }

    private void animateBezierLine (int amplitude) {
        if (mAnimSet != null && mAnimSet.isRunning()) {
            mAnimSet.cancel();
        }
        mAnimSet = new AnimatorSet();
        if (mAmpAnim == null) {
            mAmpAnim = ObjectAnimator.ofInt(this, "amplitudeValue", mAmplitudeValue, amplitude);
        } else {
            mAmpAnim.setIntValues(mAmplitudeValue, amplitude);
        }
        if (amplitude != getLastAmplitude() && showHintLine && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (amplitude > 0) {
                mColorAnim = ObjectAnimator.ofArgb(mPaint, "color", mPaint.getColor(), mLineColor);
            } else {
                mColorAnim = ObjectAnimator.ofArgb(mPaint, "color", mPaint.getColor(), mHintLineColor);
            }
        }
        AnimatorSet.Builder builder = mAnimSet.play(mAmpAnim);
        if (mColorAnim != null) {
            builder.with(mColorAnim);
        }
        mAnimSet.setDuration(getPeriod());
        mAnimSet.start();
    }

    public void setAmplitudeValue (int amplitudeValue) {
        mAmplitudeValue = amplitudeValue;
        invalidate();
    }

    public void setMoveSpeed (int pixel) {
        mMoveSpeed = pixel;
    }

    private float computeOffsetY(int index) {
        float offset = (float) Math.sqrt(mAmplitudeValue);
        offset = Math.min(offset, (getHeight() - getPaddingBottom() - getPaddingTop()) / 2);
        return offset * P[index % P.length];
    }

    public void setLineWidth (float width) {
        mLineWidth = width;
        if (mPaint != null) {
            mPaint.setStrokeWidth(mLineWidth);
        }
    }

    public void setLineColor (int color) {
        mLineColor = color;
    }

    public void setShowHintLine (boolean show) {
        showHintLine = show;
        if (!isAttachedWithRecorder() && !isPlayStarted() && !isPlayPaused() && show) {
            mPaint.setColor(mHintLineColor);
            invalidate();
        }
    }

    public void setHintLineColor (int hintLineColor) {
        mHintLineColor = hintLineColor;
        if (!isAttachedWithRecorder() && !isPlayStarted() && !isPlayPaused() && showHintLine) {
            mPaint.setColor(mHintLineColor);
            invalidate();
        }
    }

    public void setHintLineWidth (float hintLineWidth) {
        mHintLineWidth = hintLineWidth;
    }

    public void setWaveCount (int count) {
        mKeyPointCount = count << 1;
        mPoints = new PointF[mKeyPointCount * 4 + 1];
        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new PointF();
        }
    }

}
