package com.github.boybeak.starter

import android.content.Context

inline fun Context.convertDpToPixel(dp: Float): Int {
    return (dp * resources.displayMetrics.density).toInt()
}