package com.github.boybeak.starter.utils

import android.content.Context
import android.content.pm.PackageManager

object ManifestUtil {

    fun <T> getDataAs(context: Context, key: String, defaultValue: T): T {
        try {
            val ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            return bundle.get(key) as T
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return defaultValue
    }

    fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        return getDataAs(context, key, defaultValue)
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return getDataAs(context, key, defaultValue)
    }
    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        return getDataAs(context, key, defaultValue)
    }

    fun getVersionCode(context: Context): Int {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }
    fun getVersionName(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}