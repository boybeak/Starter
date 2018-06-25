package com.github.boybeak.starter.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.github.boybeak.starter.R

/**
 * Created by gaoyunfei on 2018/1/29.
 */

class RatioCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    private var widthRadio: Int = 0
    private var heightRadio: Int = 0

    private val isRadioWork: Boolean
        get() = widthRadio != 0 && heightRadio != 0

    init {
        initThis(context, attrs)
    }

    private fun initThis(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            var ta: TypedArray? = null
            try {
                ta = context.obtainStyledAttributes(attrs, R.styleable.RatioCardView)
                widthRadio = ta!!.getInteger(R.styleable.RatioCardView_widthRatio, 0)
                heightRadio = ta.getInteger(R.styleable.RatioCardView_heightRatio, 0)
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
            val height = width * heightRadio / widthRadio
            heightMeasureSpecNew = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.getMode(widthMeasureSpec))
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
    }
}
