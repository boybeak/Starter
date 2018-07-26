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

    private var activity: BaseActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw IllegalStateException("BaseFragment must work with BaseActivity")
        }
        activity = context
    }

    fun activity(): BaseActivity? {
        return activity
    }

    open fun getTitle(context: Context): CharSequence? {
        return null
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
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