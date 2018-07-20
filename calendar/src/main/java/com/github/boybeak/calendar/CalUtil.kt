package com.github.boybeak.calendar

import java.util.*

object CalUtil {

    fun isLeapYear(year: Int): Boolean {
        return ((year % 4 == 0) and (year % 100 != 0)) or (year%400 == 0)
    }

    fun getDayCountOfMonth(year: Int, month: Int): Int {
        return when(month) {
            Calendar.JANUARY, Calendar.MARCH,Calendar.MAY,Calendar.JULY,Calendar.AUGUST,
            Calendar.OCTOBER,Calendar.DECEMBER -> {
                31
            }
            Calendar.APRIL,Calendar.JUNE,Calendar.SEPTEMBER,Calendar.NOVEMBER -> {
                30
            }
            Calendar.FEBRUARY -> {
                if (isLeapYear(year)) 29 else 28
            }
            else -> {
                throw IllegalArgumentException("Illegal year or month")
            }
        }
    }

}