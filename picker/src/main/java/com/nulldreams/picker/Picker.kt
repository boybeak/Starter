package com.nulldreams.picker

import android.content.Context
import android.content.Intent
import java.io.File

abstract class Picker {
    companion object {
        fun singleChoice (context: Context): SinglePicker {
            return SinglePicker(context)
        }

        fun multipleChoice (context: Context): MultiplePicker {
            return MultiplePicker(context)
        }
    }

    private val intent = Intent()
    private val context: Context

    protected constructor(context: Context) {
        this@Picker.context = context
    }

    fun bySystemGallery (bySystem: Boolean): Picker {
        intent.putExtra("bySystem", bySystem)
        return this
    }

    fun go () {
        intent.setClass(context, AgentActivity::class.java)
        context.startActivity(intent)
    }

    interface Callback {
        fun onCancel ()
    }

    interface SingleCallback: Callback {
        fun onGet(file: File)
    }

    interface MultipleCallback: Callback {
        fun onGet (files: List<File>)
    }

}