package com.github.boybeak.starter.broadcast

import android.content.BroadcastReceiver

import java.util.ArrayList

abstract class AbsReceiver : BroadcastReceiver() {

    private val mActions = ArrayList<String>()

    internal fun addAction(action: String) {
        if (!mActions.contains(action)) {
            mActions.add(action)
        }
    }

    internal fun onUnregistered() {
        mActions.clear()
    }

    fun isRegistered (): Boolean {
        return !mActions.isEmpty()
    }

}
