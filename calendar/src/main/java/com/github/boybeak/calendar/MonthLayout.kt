package com.github.boybeak.calendar

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.Toast
import java.util.*

class MonthLayout : ViewGroup {

    companion object {
        private val TAG = MonthLayout::class.java.simpleName!!
        private const val ROW_COUNT = 6
    }

    private var year: Int = 0
    private var month: Int = 0

    private var selectedRowIndex = 0

    private val weekBarTitleHeight = context.resources.getDimensionPixelSize(R.dimen.week_bar_title_height)
    private val weekBarHeight = context.resources.getDimensionPixelSize(R.dimen.week_bar_height)

    private val minHeight = weekBarTitleHeight + weekBarHeight
    private val maxHeight = weekBarTitleHeight + weekBarHeight * ROW_COUNT

    private val distance = maxHeight - minHeight

    private var isAnimating = false

    val isCollapsed: Boolean
        get() {
            return weekBarHeight + weekBarTitleHeight == height
        }

    val isExpanded: Boolean
        get() {
            return weekBarHeight * 6 + weekBarTitleHeight == height
        }

    private val weekBarTitle = LayoutInflater.from(context).inflate(R.layout.layout_month_week_title, this, false)
    private val weekBars = List(ROW_COUNT) {
        WeekBar(context)
    }

    private val sundays = Array<Calendar>(ROW_COUNT) {
        Calendar.getInstance()
    }

    private var touchSlop = 0
    private val scroller: Scroller = Scroller(context)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {

        val config = ViewConfiguration.get(context)
        touchSlop = config.scaledPagingTouchSlop

        val titleP = LayoutParams(LayoutParams.MATCH_PARENT, weekBarTitleHeight)
        addView(weekBarTitle, titleP)
        val p = LayoutParams(LayoutParams.MATCH_PARENT, weekBarHeight)
        weekBars.forEach {
            addView(it, p)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val newHeight = Math.min(maxHeight, Math.max(height, minHeight))
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        measureChildren(widthMeasureSpec, newHeightMeasureSpec)
        /*for(i in 0 until childCount) {
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, newHeightMeasureSpec, 0)
        }*/
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        weekBarTitle.layout(0, 0, width, t + weekBarTitleHeight)
        if (isExpanded) {
            weekBars.forEachIndexed { index, weekBar ->
                val left = 0
                val right = /*width - paddingEnd*/left + weekBar.measuredWidth
                val top = weekBarTopWhenExpanded(index)
                val bottom = top + weekBarHeight
                weekBar.layout(left, top, right, bottom)
            }
        } else {
            val currentHeight = b - t - minHeight * 1f
            weekBars.forEachIndexed { index, weekBar ->
                if(index != selectedRowIndex) {
                    if (index != selectedRowIndex) {
                        weekBar.alpha = currentHeight*1f / distance
                    }

                    val deltaX = (distance - currentHeight) * 1f / distance * weekBarLeftWhenCollapsed(index)

                    val left = paddingStart + deltaX.toInt()
                    val right = left + weekBar.measuredWidth
                    val top = weekBarTitleHeight + (index * 1f / (ROW_COUNT - 1) * currentHeight).toInt()
                    val bottom = top + weekBarHeight
                    weekBar.layout(left, top, right, bottom)
                }
            }
            val selectedWeekBar = weekBars[selectedRowIndex]
            val left = 0
            val right = left + selectedWeekBar.measuredWidth
            val top = weekBarTitleHeight + (selectedRowIndex * 1f / (ROW_COUNT - 1) * currentHeight).toInt()
            val bottom = top + weekBarHeight
            selectedWeekBar.layout(left, top, right, bottom)
        }



    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    private var dragY = 0f
    /*override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                dragY = ev.rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val y = ev.rawY
                val delta = Math.abs(y - dragY)
                if (delta > touchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }*/

    private var lastY: Float = 0f
    private var deltaY: Float = 0f
    private var directionY: Float = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isAnimating) {
            return false
        }
        when(event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.rawY
                deltaY = y - lastY
                this.height = height + deltaY.toInt()

                lastY = y
                if (deltaY != 0f) {
                    directionY = deltaY
                }
            }
            MotionEvent.ACTION_UP -> {
                if (directionY < 0) {
                    if (!isCollapsed) {
                        animateCollapsed()
                    }
                } else if (directionY > 0) {
                    if (!isExpanded) {
                        animateExpanded()
                    }
                }
                if (isCollapsed) {
                    Toast.makeText(context, "up", Toast.LENGTH_SHORT).show()
                    val targetIndex = (scrollX + width / 2) / width
                    val dx = targetIndex * width - scrollX
                    scroller.startScroll(scrollX, 0, dx, 0)
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    override fun computeHorizontalScrollExtent(): Int {
        return 0
    }

    override fun computeHorizontalScrollOffset(): Int {
        return 0
    }

    override fun computeHorizontalScrollRange(): Int {
        return Int.MAX_VALUE
    }

    private fun weekBarTopWhenExpanded(index: Int): Int {
        return weekBarTitleHeight + weekBarHeight * index
    }

    private fun weekBarLeftWhenCollapsed(index: Int): Int {
        return (index - selectedRowIndex) * width
    }

    fun setYearAndMonth(year: Int, month: Int) {
        this.year = year
        this.month = month

        onYearMonthChanged()
    }

    fun setYear(year: Int) {
        this.year = year

        onYearMonthChanged()
    }

    fun setMonth(month: Int) {
        this.month = month

        onYearMonthChanged()
    }

    private fun onYearMonthChanged() {
        val day1 = Calendar.getInstance()
        day1.set(year, month, 1)
        val day1OfWeek = day1.get(Calendar.DAY_OF_WEEK)
        if (day1OfWeek != Calendar.SUNDAY) {
            day1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        sundays.forEachIndexed { index, calendar ->
            calendar.set(Calendar.DAY_OF_MONTH, day1.get(Calendar.DAY_OF_MONTH) + index * 7)
            weekBars[index].setSunday(calendar)
        }
    }

    fun setHeight(height: Int) {
        val params = layoutParams
        params.height = height
        layoutParams = params
    }

    private fun animateCollapsed() {
        val anim = ObjectAnimator.ofInt(this, "height", height, minHeight)
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimating = false
                animation!!.removeAllListeners()
            }

            override fun onAnimationCancel(animation: Animator?) {
                isAnimating = false
                animation!!.removeAllListeners()
            }

            override fun onAnimationStart(animation: Animator?) {
                isAnimating = true
            }

        })
        anim.start()
    }

    private fun animateExpanded() {
        val anim = ObjectAnimator.ofInt(this, "height", height, maxHeight)
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                isAnimating = false
                animation!!.removeAllListeners()
            }

            override fun onAnimationCancel(animation: Animator?) {
                isAnimating = false
                animation!!.removeAllListeners()
            }

            override fun onAnimationStart(animation: Animator?) {
                isAnimating = true
            }

        })
        anim.start()
    }

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

}
