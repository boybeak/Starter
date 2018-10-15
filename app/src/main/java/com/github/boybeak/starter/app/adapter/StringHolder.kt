package com.github.boybeak.starter.app.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.starter.app.databinding.LayoutStringBinding

class StringHolder(b: LayoutStringBinding) : AbsDataBindingHolder<StringImpl, LayoutStringBinding>(b) {
    override fun onBindData(context: Context, layout: StringImpl, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().text = layout.source
    }
}