package com.github.boybeak.starter.adapter.footer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View

import com.github.boybeak.adapter.extension.Footer
import com.github.boybeak.starter.databinding.LayoutFooterBinding

/**
 * Created by gaoyunfei on 2018/3/9.
 */

class FooterHolder(binding: LayoutFooterBinding) : com.github.boybeak.adapter.AbsDataBindingHolder<FooterLayout, LayoutFooterBinding>(binding) {

    override fun onBindData(context: Context, layout: FooterLayout, position: Int, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        val footer = layout.source
        val msg = footer.message

        when(footer.state) {
            Footer.LOADING -> {
                binding().footerNotNone.visibility = View.VISIBLE
                binding().footerNone.visibility = View.GONE
                binding().footerMsg.visibility = View.GONE
                binding().footerPb.visibility = View.VISIBLE
            }
            Footer.SUCCESS -> {
                binding().footerNotNone.visibility = View.VISIBLE
                binding().footerNone.visibility = View.GONE
                binding().footerMsg.visibility = View.VISIBLE
                binding().footerPb.visibility = View.GONE
                binding().footerMsg.text = msg
            }
            Footer.FAILED -> {
                binding().footerNotNone.visibility = View.VISIBLE
                binding().footerNone.visibility = View.GONE
                binding().footerMsg.visibility = View.VISIBLE
                binding().footerPb.visibility = View.GONE
                binding().footerMsg.text = msg
            }
            Footer.NO_ONE -> {
                binding().footerNotNone.visibility = View.GONE
                binding().footerNone.visibility = View.VISIBLE
                binding().footerMsg.visibility = View.GONE
                binding().footerPb.visibility = View.GONE
                binding().footerNoneActionBtn.visibility = visiblityOfAction(layout)
                binding().footerNoneMsg.text = msg
            }
            Footer.NO_MORE -> {
                binding().footerNotNone.visibility = View.VISIBLE
                binding().footerNone.visibility = View.GONE
                binding().footerMsg.visibility = View.VISIBLE
                binding().footerPb.visibility = View.GONE
                binding().footerMsg.text = msg
            }
        }
    }

    private fun visiblityOfAction(layout: FooterLayout): Int {
        return if (!TextUtils.isEmpty(layout.actionText) && layout.actionListener != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}