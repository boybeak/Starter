package com.github.boybeak.starter.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import com.github.boybeak.starter.R

class RatioViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    companion object {
        private val TAG = RatioViewPager::class.java.simpleName!!
    }

    private var widthRatio: Int = 0
    private var heightRatio: Int = 0

    init {
        if (attrs != null) {
            var ta: TypedArray? = null
            try {
                ta = context.obtainStyledAttributes(attrs, R.styleable.RatioViewPager)
                widthRatio = ta!!.getInteger(R.styleable.RatioCardView_widthRatio, 0)
                heightRatio = ta.getInteger(R.styleable.RatioCardView_heightRatio, 0)
            } finally {
                if (ta != null) {
                    ta.recycle()
                }
            }
        }
    }

    private val isRadioWork: Boolean
        get() = widthRatio != 0 && heightRatio != 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecNew = heightMeasureSpec
        if (isRadioWork) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = width * heightRatio / widthRatio
            heightMeasureSpecNew = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.getMode(widthMeasureSpec))
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
    }

    fun setRatios (widthRatio: Int, heightRatio: Int) {
        var changed = false
        if (this.widthRatio != widthRatio) {
            this.widthRatio = widthRatio
            changed = true
        }
        if (this.heightRatio != heightRatio) {
            this.heightRatio = heightRatio
            changed = true
        }
        if (changed) {
            requestLayout()
        }
    }

}
