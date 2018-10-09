package com.github.boybeak.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by gaoyunfei on 2018/3/7.
 */

open class BaseDataBindingHolder<out B : ViewDataBinding>(binding: B) : RecyclerView.ViewHolder(binding.root) {

    private var mBinding: B? = null

    init {
        mBinding = binding
    }

    fun binding(): B {
        return mBinding!!
    }
}
