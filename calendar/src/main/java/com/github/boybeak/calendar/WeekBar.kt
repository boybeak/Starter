package com.github.boybeak.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import java.util.*

class WeekBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val sundayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val mondayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val tuesdayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val wednesdayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val thursdayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val fridayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)
    private val saturdayView: AppCompatTextView = AppCompatTextView(context, attrs, defStyleAttr)

    private val dayViews = arrayOf(sundayView, mondayView, tuesdayView,
            wednesdayView, thursdayView, fridayView, saturdayView)

    private val weekBarHeight = context.resources.getDimensionPixelSize(R.dimen.week_bar_height)

    private var sundayCalendar = Calendar.getInstance()

    init {
        orientation = HORIZONTAL
        val p = LayoutParams(LayoutParams.MATCH_PARENT, weekBarHeight)
        p.weight = 1f
        addView(sundayView, p)
        addView(mondayView, p)
        addView(tuesdayView, p)
        addView(wednesdayView, p)
        addView(thursdayView, p)
        addView(fridayView, p)
        addView(saturdayView, p)

        setBackgroundColor(Color.CYAN)

        makeDayViewStyles()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(weekBarHeight, MeasureSpec.EXACTLY))
    }

    private fun makeDayViewStyles () {
        dayViews.forEach {
            it.gravity = Gravity.CENTER
            it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            it.setTypeface(it.typeface, Typeface.BOLD)
        }
    }

    private fun refreshDays() {
        dayViews.forEachIndexed { index, tv ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = sundayCalendar.timeInMillis
            cal.set(Calendar.DAY_OF_WEEK, sundayCalendar.get(Calendar.DAY_OF_WEEK) + index)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            tv.text = day.toString()
        }
    }

    fun setSunday(timeStamp: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeStamp
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalStateException("timeStamp is not sunday")
        }
        sundayCalendar = cal

        refreshDays()
    }

    fun setSunday(date: Date) {
        val cal = Calendar.getInstance()
        cal.time = date
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalStateException("date is not sunday")
        }
        sundayCalendar = cal

        refreshDays()
    }

    fun setSunday(cal: Calendar) {
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalStateException("date is not sunday")
        }
        sundayCalendar = cal

        refreshDays()
    }

}
