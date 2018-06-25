package com.github.boybeak.starter.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.github.boybeak.starter.R

class ViewPagerLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        private val TAG = ViewPagerLayout::class.java.simpleName!!
    }

    private var isDragging = false

    private val pagerListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
            isDragging = state == ViewPager.SCROLL_STATE_DRAGGING
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            select(position)
        }

    }

    private val run: Runnable = object : Runnable {
        override fun run() {
            if (!isDragging) {
                vp.setCurrentItem((vp.currentItem + 1) % vp.adapter!!.count, true)
            }
            if (isAlive) {
                postDelayed(this, duration)
            }
        }
    }

    private var vp: RatioViewPager
    private var indexLayout: LinearLayoutCompat

    private var duration = 5000L

    private var isAlive = false

    init {

        LayoutInflater.from(context).inflate(R.layout.layout_view_pager, this, true)
        vp = findViewById(R.id.vp)
        indexLayout = findViewById(R.id.index_layout)

        if (attrs != null) {
            var ta: TypedArray? = null
            try {
                ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerLayout)
                val widthRatio = ta!!.getInteger(R.styleable.RatioCardView_widthRatio, 0)
                val heightRatio = ta.getInteger(R.styleable.RatioCardView_heightRatio, 0)
                vp.setRatios(widthRatio, heightRatio)
            } finally {
                if (ta != null) {
                    ta.recycle()
                }
            }
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isAlive = true
        viewPager().addOnPageChangeListener(pagerListener)
        if (vp.adapter != null) {
            indexLayout.removeAllViews()
            val dotSize = context.resources.getDimensionPixelSize(R.dimen.margin_4)
            val dotMargin = context.resources.getDimensionPixelSize(R.dimen.margin_4)
            for (i in 0 until vp.adapter!!.count) {
                val v = View(context)
                v.setBackgroundResource(R.drawable.bg_vp_dot)
                val p = LayoutParams(dotSize, dotSize)
                p.setMargins(dotMargin, dotMargin, dotMargin, dotMargin)
                indexLayout.addView(v, p)
            }
            select(0)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isAlive = false
        stop()
        viewPager().removeOnPageChangeListener(pagerListener)
    }

    fun viewPager(): RatioViewPager {
        return vp
    }

    private fun select (position: Int) {
        if (position < indexLayout.childCount) {
            for (i in 0 until indexLayout.childCount) {
                indexLayout.getChildAt(i).isSelected = i == position
            }
        }
    }

    fun start() {
        postDelayed(run, duration)
    }

    fun stop() {
        removeCallbacks(run)
    }

}
