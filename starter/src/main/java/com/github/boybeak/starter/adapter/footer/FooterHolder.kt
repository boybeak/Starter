package com.github.boybeak.starter.adapter.footer

import android.content.Context
import android.support.v7.widget.RecyclerView

import com.github.boybeak.adapter.AbsDataBindingHolder
import com.github.boybeak.starter.databinding.LayoutFooterBinding

/**
 * Created by gaoyunfei on 2018/3/9.
 */

class FooterHolder(binding: LayoutFooterBinding) : com.github.boybeak.adapter.AbsDataBindingHolder<FooterLayout, LayoutFooterBinding>(binding) {

    override fun onBindData(context: Context, layout: FooterLayout, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        binding().footer = layout.source
    }

}
