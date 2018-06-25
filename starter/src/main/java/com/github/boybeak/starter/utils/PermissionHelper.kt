package com.github.boybeak.starter.utils

import android.content.Context
import android.content.Intent
import com.github.boybeak.starter.activity.PermissionAgentActivity
import java.lang.ref.WeakReference

object PermissionHelper {

    private var callbackRef: WeakReference<PermissionCallback>? = null

    fun askForPermission (context: Context,
                          vararg permissions: String,
                          callback: PermissionCallback) {
        callbackRef = WeakReference(callback)
        val it = Intent(context, PermissionAgentActivity::class.java)
        it.putExtra("permissions", permissions)
        context.startActivity(it)
    }

    internal fun grant (permissions: Array<out String?>) {
        if (callbackRef != null) {
            callbackRef!!.get()?.onGranted(permissions)
        }
        release()
    }

    internal fun deny(permission: String?): Boolean {
        if (callbackRef != null) {
            return callbackRef!!.get()?.onDenied(permission)!!
        }
        release()
        return false
    }

    internal fun shouldShowRationale (permission: String?): Boolean {
        var result = false
        if (callbackRef != null) {
            val callback = callbackRef!!.get()
            if (callback != null) {
                result = callback.onShouldShowRationale(permission)
            }
        }
        release()
        return result
    }

    internal fun release () {
        if (callbackRef != null) {
            callbackRef!!.clear()
        }
    }

    interface PermissionCallback {
        fun onGranted (vararg permissions: Array<out String?>)
        fun onDenied (permission: String?): Boolean
        fun onShouldShowRationale (permission: String?): Boolean
    }

    abstract class AbsPermissionCallback : PermissionCallback {
        override fun onDenied(permission: String?): Boolean {
            return false
        }

        override fun onShouldShowRationale(permission: String?): Boolean {
            return false
        }
    }

}