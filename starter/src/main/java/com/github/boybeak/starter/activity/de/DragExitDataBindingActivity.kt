package com.github.boybeak.starter.activity.de

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by gaoyunfei on 2018/3/8.
 */

open class DragExitDataBindingActivity<out B : ViewDataBinding> : BaseDragExitActivity() {

    private var binding: B? = null

    fun binding(): B {
        return binding!!
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, null, false)
    }

    override fun setContentView(view: View?) {
        throw UnsupportedOperationException("Do not use setContentView(View view), " + "use setContentView(int layoutResID)")
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        throw UnsupportedOperationException("Do not use setContentView(View view), " + "use setContentView(int layoutResID)")
    }
}
