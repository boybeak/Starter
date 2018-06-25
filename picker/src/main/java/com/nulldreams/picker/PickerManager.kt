package com.nulldreams.picker

import android.content.Context
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import android.provider.MediaStore.Images.Thumbnails
import android.support.annotation.IntDef
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import java.lang.annotation.RetentionPolicy

class PickerManager {

    companion object {

        private var manager: PickerManager? = null

        fun instance (): PickerManager {
            if (manager == null) {
                synchronized(PickerManager::class.java) {
                    if (manager == null) {
                        manager = PickerManager()
                    }
                }
            }
            return manager!!
        }

        const val MODE_SINGLE = 1
        const val MODE_MULTIPLE = 2
    }

    private var callback: Picker.Callback? = null

    private var choiceMode = MODE_SINGLE

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(MODE_SINGLE, MODE_MULTIPLE)
    internal annotation class Mode

    fun setChoiceMode(@Mode mode: Int) {
        choiceMode = mode
    }

    fun getChoiceMode (): Int {
        return choiceMode
    }

    fun singleFinish (file: File) {
        if (callback != null && callback is Picker.SingleCallback) {
            (callback as Picker.SingleCallback).onGet(file)
        }
        release()
    }

    fun multipleFinish (fileList: List<File>) {
        if (callback != null && callback is Picker.MultipleCallback) {
            (callback as Picker.MultipleCallback).onGet(fileList)
        }
        release()
    }

    fun cancel () {
        if (callback != null) {
            callback!!.onCancel()
        }
        release()
    }

    private fun release () {
        callback = null
    }

    fun setCallback (callback: Picker.Callback) {
        this.callback = callback
    }

    fun getThumbnailsAsync (context: Context, callback: OnThumbnailsCallback) {
        callback.onQueryStart()
        Maybe.just(context.contentResolver).observeOn(Schedulers.io())
                .map {
                    it.query(Thumbnails.EXTERNAL_CONTENT_URI, arrayOf(Thumbnails.DATA),
                            null, null, null)
                }
                .map {
                    val thumbnails = mutableListOf<Thumb>()
                    it.moveToFirst()
                    while (!it.isAfterLast) {
                        val thumb = Thumb()
                        thumb.path = it.getString(it.getColumnIndex(Thumbnails.DATA))
                        thumbnails.add(thumb)
                        it.moveToNext()
                    }
                    thumbnails
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (it.isEmpty()) {
                        callback.onGetNothing()
                    } else {
                        callback.onPrepared(it)
                    }
                }
                .subscribe()
    }

    fun checkStatus (): Boolean {
        when(choiceMode) {
            MODE_SINGLE -> {
                return callback != null && callback is Picker.SingleCallback
            }
            MODE_MULTIPLE -> {
                return callback != null && callback is Picker.MultipleCallback
            }
        }
        return false
    }

    interface OnThumbnailsCallback {
        fun onQueryStart ()
        fun onPrepared (thumbnails: List<Thumb>)
        fun onGetNothing ()
    }

}