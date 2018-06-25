package com.github.boybeak.starter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.github.boybeak.starter.ILife
import com.github.boybeak.starter.activity.BaseActivity

/**
 * Created by gaoyunfei on 2018/3/20.
 */
open class BaseDialogFragment : DialogFragment(), ILife {

    companion object {
        private val TAG = BaseDialogFragment::class.java.simpleName
    }

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

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAlive = true
    }

    final override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder (context!!)
        onApplyDialogOptions(builder)
        return builder.create()
    }

    open fun onApplyDialogOptions(builder: AlertDialog.Builder) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.setView(view)
    }

    override fun getDialog(): AlertDialog {
        return super.getDialog() as AlertDialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isAlive = false
    }
}