package com.github.boybeak.starter.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.boybeak.starter.ILife
import com.github.boybeak.starter.R
import com.github.boybeak.starter.widget.EmptyView
import kotlinx.android.synthetic.*

/**
 * Created by gaoyunfei on 2018/1/28.
 */
open class BaseActivity: AppCompatActivity(), ILife {

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
    }

    private var emptyView: EmptyView? = null

    override var isAlive: Boolean = false
    private var mPermissionRequestCode = 0

//    private var mPermissionCallback: PermissionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAlive = true
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar, actionBarAsUpEnable())
    }

    open fun actionBarAsUpEnable(): Boolean {
        return false
    }

    fun setSupportActionBar (toolbar: Toolbar?, backEnable: Boolean) {
        super.setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar!!.elevation = getToolbarElevation()
        }
        if (backEnable) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun getToolbarElevation(): Float {
        return resources.getDimension(R.dimen.toolbar_elevation)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
        isAlive = false
    }

    fun decorView (): View {
        return window.decorView
    }

    fun contentView (): View {
        return findViewById(android.R.id.content)
    }

    fun runOnUiThreadDelayed (action: Runnable, delayMillis: Long) {
        decorView().postDelayed(object : Runnable {
            override fun run() {
                decorView().removeCallbacks(this)
                action.run()
            }
        }, delayMillis)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun showEmptyViewIn (parent: ViewGroup, params: ViewGroup.LayoutParams,
                         icon: Drawable?, title: CharSequence?, message: CharSequence?, button: CharSequence?,
                         autoDismiss: Boolean, listener: EmptyView.ActionListener?) {
        if (isEmptyViewShowing(parent)) {
            dismissEmptyView()
        }
        emptyView = EmptyView.Builder.with(parent)
                .icon(icon).title(title).message(message)
                .action(button, autoDismiss, listener)
                .show(params)
    }

    fun showEmptyView (listener: EmptyView.ActionListener?) {
        showEmptyView(R.drawable.bg_empty_default, R.string.title_empty_default, R.string.text_empty_default,
                R.string.text_empty_button_default, listener)
    }

    fun showEmptyView (@StringRes title: Int, @StringRes message: Int, @StringRes button: Int,
                       listener: EmptyView.ActionListener?) {
        showEmptyView(R.drawable.bg_empty_default, title, message, button, listener)
    }

    fun showEmptyView (title: CharSequence, message: CharSequence, button: CharSequence, listener: EmptyView.ActionListener?) {
        showEmptyView(null, title, message, button, listener)
    }

    fun showEmptyView (@StringRes messageRes: Int) {
        showEmptyView(R.drawable.bg_empty_default, R.string.title_empty_default, messageRes, 0, null)
    }

    fun showEmptyView(@DrawableRes icon: Int, @StringRes title: Int, @StringRes message: Int, @StringRes button: Int,
                      listener: EmptyView.ActionListener?) {
        var titleStr: String? = null
        if (title != 0) {
            titleStr = getString(title)
        }
        var msgStr: String? = null
        if (message != 0) {
            msgStr = getString(message)
        }
        var btnStr: String? = null
        if (button != 0) {
            btnStr = getString(button)
        }
        var iconDrawable: Drawable? = null
        if (icon > 0) {
            iconDrawable = resources.getDrawable(icon)
        }
        showEmptyView(iconDrawable, titleStr, msgStr, btnStr, listener)
    }

    open fun showEmptyView(icon: Drawable?, title: CharSequence?, message: CharSequence?, button: CharSequence?,
                           listener: EmptyView.ActionListener?) {
        val contentLayout: FrameLayout = findViewById(android.R.id.content)
        val p: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        p.gravity = Gravity.CENTER
        showEmptyViewIn(contentLayout, p, icon, title, message, button, true, listener)
    }

    fun dismissEmptyView() {
        if (isEmptyViewShowing()) {
            emptyView!!.dismiss()
        }
    }

    open fun isEmptyViewShowing (): Boolean {
        return emptyView != null && emptyView!!.isShowing()
    }

    fun isEmptyViewShowing (parent: ViewGroup): Boolean {
        return emptyView != null && parent.indexOfChild(emptyView) >= 0
    }

}