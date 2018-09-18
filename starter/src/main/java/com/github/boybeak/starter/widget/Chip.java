package com.github.boybeak.starter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.boybeak.starter.R;

/**
 * Created by gaoyunfei on 2017/6/27.
 */

public class Chip extends LinearLayout {

    private AppCompatImageView mImageView;
    private AppCompatTextView mTextView;
    private AppCompatImageView mActionImageView;

    private boolean showImage, showAction;

    private int mHeightPx = 0;

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        CharSequence text = null;
        Drawable actionDrawable = null;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Chip);
        try {
            text = ta.getText(R.styleable.Chip_text);
            actionDrawable = ta.getDrawable(R.styleable.Chip_actionIcon);

            showImage = ta.getBoolean(R.styleable.Chip_showImage, false);
            showAction = ta.getBoolean(R.styleable.Chip_showAction, false);
        } finally {
            ta.recycle();
        }

        mHeightPx = getResources().getDimensionPixelSize(R.dimen.chip_height);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.drawable.bg_chip);

        mTextView = new AppCompatTextView(context, attrs, defStyleAttr);
        mTextView.setDuplicateParentStateEnabled(true);
        mTextView.setMaxLines(1);
        mTextView.setMinWidth(context.getResources().getDimensionPixelSize(R.dimen.chip_min_label_width));
        mTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTextView.setTextColor(getResources().getColorStateList(R.color.text_color_chip));
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        mTextView.setClickable(false);

        LayoutParams labelParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        labelParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_12);
        labelParams.rightMargin = labelParams.leftMargin;

        this.addView(mTextView, labelParams);

        if (showImage) {

            mImageView = new AppCompatImageView(context, attrs, defStyleAttr);
            mImageView.setId(R.id.chip_icon);

            LayoutParams params = new LayoutParams(mHeightPx, mHeightPx);
            this.addView(mImageView, 0, params);

            labelParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_8);
            mTextView.setLayoutParams(labelParams);
        }

        if (showAction) {

            mActionImageView = new AppCompatImageView(context, attrs, defStyleAttr);
            mActionImageView.setId(R.id.chip_action_btn);
            mActionImageView.setImageDrawable(actionDrawable);
            int actionSize = getResources().getDimensionPixelSize(R.dimen.chip_action_size);
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_4);

            mActionImageView.setPadding(margin, margin, margin, margin);

            LayoutParams params = new LayoutParams(actionSize, actionSize);
            params.setMargins(margin, margin, margin, margin);
            addView(mActionImageView, params);

            labelParams.rightMargin = 0;
            mTextView.setLayoutParams(labelParams);
        }

        setText(text);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeightPx, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setText (CharSequence text) {
        mTextView.setText(text);
    }

    public void setText (@StringRes int textRes) {
        mTextView.setText(textRes);
    }

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        if (this.showImage == showImage) {
            return;
        }
        this.showImage = showImage;
        LayoutParams labelParams = (LayoutParams) mTextView.getLayoutParams();
        if (showImage) {
            labelParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_8);

            if (mImageView == null) {
                mImageView = new AppCompatImageView(getContext());
                int size = getResources().getDimensionPixelSize(R.dimen.chip_height);
                LayoutParams imgParams = new LayoutParams(size, size);
                addView(mImageView, 0, imgParams);
            } else {
                mImageView.setVisibility(VISIBLE);
            }
        } else {
            if (mImageView == null) {
                return;
            }
            mImageView.setVisibility(GONE);
            labelParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_12);
        }
        mTextView.setLayoutParams(labelParams);
    }

    public boolean isShowAction() {
        return showAction;
    }

    public void setShowAction(boolean showAction) {
        if (this.showAction == showAction) {
            return;
        }
        this.showAction = showAction;
        LayoutParams labelParams = (LayoutParams) mTextView.getLayoutParams();
        if (showAction) {
            labelParams.rightMargin = 0;

            if (mActionImageView == null) {
                mActionImageView = new AppCompatImageView(getContext());
                mActionImageView.setId(R.id.chip_action_btn);
                int size = getResources().getDimensionPixelSize(R.dimen.chip_action_size);
                int margin = getResources().getDimensionPixelSize(R.dimen.margin_4);
                LayoutParams actionParams = new LayoutParams(size, size);
                actionParams.setMargins(margin, margin, margin, margin);
                this.addView(mActionImageView, actionParams);
            } else {
                mActionImageView.setVisibility(VISIBLE);
            }
        } else {
            if (mImageView == null) {
                return;
            }
            mActionImageView.setVisibility(GONE);
            labelParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_12);
        }
        mTextView.setLayoutParams(labelParams);
    }

    public @Nullable ImageView getImageView () {
        return mImageView;
    }

    public void setActionClickListener (OnClickListener actionClickListener) {
        if (mActionImageView != null) {
            mActionImageView.setOnClickListener(actionClickListener);
        }
    }
}