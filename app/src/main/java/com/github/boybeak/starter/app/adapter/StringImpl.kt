package com.github.boybeak.starter.app.adapter

import com.github.boybeak.adapter.AbsLayoutImpl
import com.github.boybeak.starter.app.R

class StringImpl(s: String) : AbsLayoutImpl<String>(s) {
    override fun getLayout(): Int {
        return R.layout.layout_string
    }

    override fun getHolderClass(): Class<StringHolder> {
        return StringHolder::class.java
    }
}