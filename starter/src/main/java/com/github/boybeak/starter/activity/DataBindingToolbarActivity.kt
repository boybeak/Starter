package com.github.boybeak.starter.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * Created by gaoyunfei on 2018/2/6.
 */

open class DataBindingToolbarActivity<out B : ViewDataBinding> : ToolbarActivity() {

    private var binding: B? = null

    override fun setContentView(layoutResID: Int) {
        val inflater = LayoutInflater.from(this)
        binding = DataBindingUtil.inflate(inflater, layoutResID, null, false)

        contentLayout().addView(binding!!.root,
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT))
    }

    fun binding(): B {
        return binding!!
    }

    override fun setContentView(view: View?) {
        throw UnsupportedOperationException("Do not use setContentView(View view), "
                + "use setContentView(int layoutResID)")
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        throw UnsupportedOperationException("Do not use setContentView(View view, ViewGroup.LayoutParams params), "
                + "use setContentView(int layoutResID)")
    }

}
