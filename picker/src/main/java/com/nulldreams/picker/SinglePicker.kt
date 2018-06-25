package com.nulldreams.picker

import android.content.Context

class SinglePicker (context: Context) : Picker(context) {

    fun callback (callback: SingleCallback): SinglePicker {
        PickerManager.instance().setChoiceMode(PickerManager.MODE_SINGLE)
        PickerManager.instance().setCallback(callback)
        return this
    }

}