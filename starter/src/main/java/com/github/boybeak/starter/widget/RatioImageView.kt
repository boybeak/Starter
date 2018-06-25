package com.github.boybeak.starter.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import com.github.boybeak.starter.R

/**
 * Created by gaoyunfei on 2018/1/29.
 */

class RatioImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var widthRatio: Int = 0
    private var heightRatio: Int = 0

    private val isRadioWork: Boolean
        get() = widthRatio != 0 && heightRatio != 0

    init {
        initThis(context, attrs)
    }

    private fun initThis(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            var ta: TypedArray? = null
            try {
                ta = context.obtainStyledAttributes(attrs, R.styleable.RatioCardView)
                widthRatio = ta!!.getInteger(R.styleable.RatioCardView_widthRatio, 0)
                heightRatio = ta.getInteger(R.styleable.RatioCardView_heightRatio, 0)
            } finally {
                if (ta != null) {
                    ta.recycle()
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecNew = heightMeasureSpec
        if (isRadioWork) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = width * heightRatio / widthRatio
            heightMeasureSpecNew = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.getMode(widthMeasureSpec))
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
    }
}
