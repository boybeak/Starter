@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("CollectionsKt")

package com.github.boybeak.starter.widget

import android.view.View
import android.view.ViewGroup

inline fun ViewGroup.foreachChild(action: (View) -> Unit) {
    val count = childCount
    for (i in 0 until count) {
        val child = getChildAt(i)
        action(child)
    }
}

inline fun ViewGroup.foreachChildIndexed(action: (index: Int, View) -> Unit) {
    val count = childCount
    for (i in 0 until count) {
        val child = getChildAt(i)
        action(i, child)
    }
}

inline operator fun ViewGroup.get(index: Int): View {
    return getChildAt(index)
}