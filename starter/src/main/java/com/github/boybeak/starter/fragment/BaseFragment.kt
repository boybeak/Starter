package com.github.boybeak.starter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.github.boybeak.starter.ILife
import com.github.boybeak.starter.activity.BaseActivity

/**
 * Created by gaoyunfei on 2018/1/28.
 */
open class BaseFragment : Fragment(), ILife {

    override var isAlive: Boolean = false

    open fun onBackPress(): Boolean {
        return false
    }

    open fun getTitle(context: Context): CharSequence? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAlive = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isAlive = false
    }

}