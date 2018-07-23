package com.github.boybeak.starter.app

import android.content.Context
import android.graphics.Color
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.OverScroller
import android.widget.Toast

class CVP : ViewGroup {

    companion object {
        private val TAG = CVP::class.java.simpleName!!
    }

    private val scroller: OverScroller = OverScroller(context)

    private val gestures = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            Log.v(TAG, "onDown ")
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.v(TAG, "onScroll $distanceX")
            for(i in 0 until childCount) {
                val child = getChildAt(i)
                child.offsetLeftAndRight(-distanceX.toInt())

            }
//            ViewCompat.postInvalidateOnAnimation(this@CVP)
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.v(TAG, "onFling $velocityX")
            scroller.forceFinished(true)
            if(velocityX > 0) {
                scroller.fling(0, 0, -velocityX.toInt(), 0, -width, 0, 0, 0)
            } else if (velocityX < 0) {
                scroller.fling(0, 0, -velocityX.toInt(), 0, 0, width, 0, 0)
            }

            ViewCompat.postInvalidateOnAnimation(this@CVP)
            return true
        }

    }

    private var gestureDetectorCompat: GestureDetectorCompat? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {

        val p = LayoutParams(LayoutParams.MATCH_PARENT, 120)

        for (i in 0 until 10) {
            val tv = AppCompatTextView(context)
            tv.text = "$i----------------------------$i"
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.LTGRAY)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
            addView(tv, p)
        }

        gestureDetectorCompat = GestureDetectorCompat(context, gestures)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gestureDetectorCompat!!.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val hm = MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY)
        measureChildren(widthMeasureSpec, hm)
        super.onMeasure(widthMeasureSpec, hm)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.v(TAG, "onLayout ")
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val left = i * child.measuredWidth
            val right = left + child.measuredWidth
            child.layout(left, 0, right, 120)
        }
    }

    private var lastCurrX = 0
    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            val currX = scroller.currX
            if (lastCurrX != 0) {
                val move = currX - lastCurrX
                for (i in 0 until childCount) {
                    getChildAt(i).offsetLeftAndRight(-move)
                }
            }
            lastCurrX = currX
            Log.v(TAG, "computeScroll $currX")
            invalidate()
        }
    }
}
