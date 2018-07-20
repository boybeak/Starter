package com.github.boybeak.calendar

import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.widget.ImageView
import java.util.*

class MonthLayout : LinearLayoutCompat {

    companion object {
        private val TAG = MonthLayout::class.java.simpleName!!
        private const val ROW_COUNT = 6
    }

    private var year: Int = 0
    private var month: Int = 0

//    private var weekBarCount = 5

    private var weekBarHeight = context.resources.getDimensionPixelSize(R.dimen.week_bar_height)

    private val minHeight = weekBarHeight
    private val maxHeight = weekBarHeight * ROW_COUNT

    val isCollapsed: Boolean
        get() {
            return weekBarHeight == height
        }

    val isExpanded: Boolean
        get() {
            return weekBarHeight * 6 == height
        }

    private val weekBars = List(ROW_COUNT) {
        WeekBar(context)
    }

    private val sundays = Array<Calendar>(ROW_COUNT) {
        Calendar.getInstance()
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    init {
        orientation = VERTICAL
        val p = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        weekBars.forEach {
            addView(it, p)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val newHeight = Math.min(maxHeight, Math.max(height, minHeight))
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)

        /*for(i in 0 until childCount) {
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, newHeightMeasureSpec, 0)
        }*/
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /*override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val left = paddingStart
            val right = width - paddingEnd
            val top = paddingTop + child.height * i
            val bottom = top + child.height
            Log.v(TAG, "onLayout left=$left right=$right top=$top bottom=$bottom")
            child.layout(left, top, right, bottom)
        }
    }*/

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

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }
}
