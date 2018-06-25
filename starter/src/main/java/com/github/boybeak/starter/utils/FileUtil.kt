package com.github.boybeak.starter.utils

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {
    /**
     * Make sure you get the WRITE_EXTERNAL_STORAGE permission before calling this.
     */
    fun move (from: File, to: File, callback: ActionCallback) {
        copy(from, to, true, callback)
    }

    fun copy (from: File, to: File, callback: ActionCallback) {
        copy(from, to, false, callback)
    }

    private fun copy (from: File, to: File, removeFrom: Boolean, callback: ActionCallback) {
        if (!from.exists()) {
            callback.onError(from, to, IllegalArgumentException("the from file doesn't exist"))
            return
        }
        Single.just(from).subscribeOn(Schedulers.io())
                .map {
                    var inStream = FileInputStream(from)
                    var outStream = FileOutputStream(to)

                    val buffer = ByteArray(1024)

                    var length: Int
                    //copy the file content in bytes
                    do {
                        length = inStream.read(buffer)
                        if (length > 0) {
                            outStream.write(buffer, 0, length)
                        }
                    } while (length > 0)


                    inStream.close()
                    outStream.close()

                    if (removeFrom) {
                        from.delete()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    callback.onSuccess(from, to)
                }
                .doOnError {
                    callback.onError(from, to, it)
                }
                .subscribe()
    }

    interface ActionCallback {
        fun onSuccess (from: File, to: File)
        fun onError (from: File, to: File, t: Throwable)
    }
}