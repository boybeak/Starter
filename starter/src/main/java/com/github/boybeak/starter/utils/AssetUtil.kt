package com.github.boybeak.starter.utils

import android.content.Context
import android.content.res.AssetManager

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object AssetUtil {
    @Throws(IOException::class)
    fun getString(context: Context, path: String): String {
        val am = context.assets
        val `is` = am.open(path)
        val isr = InputStreamReader(`is`)
        val br = BufferedReader(isr)
        var line: String?
        val sb = StringBuilder()
        do {
            line = br.readLine()
            if (line != null) {
                sb.append(line)
            }
        } while (line != null)
        br.close()
        return sb.toString()
    }
}
