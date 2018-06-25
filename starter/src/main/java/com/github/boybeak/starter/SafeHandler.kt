package com.github.boybeak.starter

import android.os.Handler
import android.os.Message

import java.lang.ref.WeakReference

class SafeHandler<out T : SafeHandler.HandlerContainer>(obj: T) : Handler() {

    private val mRef: WeakReference<T> = WeakReference(obj)

    private val container: T?
        get() = mRef.get()

    override fun handleMessage(msg: android.os.Message) {
        super.handleMessage(msg)
        val container = container
        container?.handleMessage(msg)
    }

    // interface.

    // handler container.

    interface HandlerContainer {
        fun handleMessage(message: Message)
    }
}