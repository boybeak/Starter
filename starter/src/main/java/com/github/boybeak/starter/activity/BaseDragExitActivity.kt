package com.github.boybeak.starter.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.boybeak.de.DragExitLayout
import com.github.boybeak.starter.R
import kotlinx.android.synthetic.main.activity_base_drag_exit.*

open class BaseDragExitActivity : BaseActivity(), DragExitLayout.OnExitListener {

    override fun onExit() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base_drag_exit)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onDragExitLayoutPrepared(drag_exit_layout)

        drag_exit_layout.setOnExitListener(this)
    }

    open fun onDragExitLayoutPrepared(del: DragExitLayout) {

    }

    fun dragExitLayout(): DragExitLayout {
        return drag_exit_layout
    }

    override fun setContentView(layoutResID: Int) {
        drag_exit_layout.addView(LayoutInflater.from(this)
                .inflate(layoutResID, null, false),
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?) {
        drag_exit_layout.addView(view, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        drag_exit_layout.addView(view, params)
    }

}