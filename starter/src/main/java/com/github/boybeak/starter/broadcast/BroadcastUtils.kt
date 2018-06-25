package com.github.boybeak.starter.broadcast

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager

object BroadcastUtils {
    fun registerReceiver (context: Context, receiver: AbsReceiver, vararg actions: String) {
        if (receiver.isRegistered()) {
            return
        }
        val lbm = LocalBroadcastManager.getInstance(context)
        actions.forEach {
            lbm.registerReceiver(receiver, IntentFilter(it))
            receiver.addAction(it)
        }
    }

    fun unregisterReceiver (context: Context, receiver: AbsReceiver) {
        if (receiver.isRegistered()) {
            val lbm = LocalBroadcastManager.getInstance(context)
            lbm.unregisterReceiver(receiver)
            receiver.onUnregistered()
        }
    }

    fun sendBroadcast (context: Context, intent: Intent) {
        val lbm = LocalBroadcastManager.getInstance(context)
        lbm.sendBroadcast(intent)
    }

    fun sendBroadcastSync (context: Context, intent: Intent) {
        val lbm = LocalBroadcastManager.getInstance(context)
        lbm.sendBroadcastSync(intent)
    }
}