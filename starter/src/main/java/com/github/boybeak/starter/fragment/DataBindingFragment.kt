package com.github.boybeak.starter.fragment

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by gaoyunfei on 2018/2/6.
 */
abstract class DataBindingFragment<out B: ViewDataBinding>: BaseFragment() {

    private var b: B? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = DataBindingUtil.inflate(inflater, getLayoutResource(), null, false)
        return b!!.root
    }

    @LayoutRes
    abstract fun getLayoutResource (): Int

    fun getBinding (): B? {
        return b
    }
}