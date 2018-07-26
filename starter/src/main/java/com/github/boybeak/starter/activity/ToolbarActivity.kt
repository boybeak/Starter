package com.github.boybeak.starter.activity

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.FrameLayout
import com.github.boybeak.starter.R
import com.github.boybeak.starter.widget.EmptyView
import kotlinx.android.synthetic.main.activity_toolbar.*

open class ToolbarActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_toolbar)
        setSupportActionBar(toolbar, actionBarAsUpEnable())
    }

    override fun actionBarAsUpEnable(): Boolean {
        return true
    }

    fun toolbar(): Toolbar {
        return toolbar
    }

    fun contentLayout (): FrameLayout {
        return content_layout
    }

    override fun showEmptyView(icon: Drawable?, title: CharSequence?, message: CharSequence?, button: CharSequence?, listener: EmptyView.ActionListener?) {
        val p: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        p.gravity = Gravity.CENTER
        showEmptyViewIn(content_layout, p, icon, title, message, button, true, listener)
    }

    override fun isEmptyViewShowing(): Boolean {
        return isEmptyViewShowing(content_layout)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(LayoutInflater.from(this)
                .inflate(layoutResID, null, false))
    }

    override fun setContentView(view: View?) {
        setContentView(view, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        content_layout.addView(view, params)
    }
}
