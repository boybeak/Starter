package com.github.boybeak.starter.activity

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

    /*fun askForPermission (permission: String, callback: PermissionCallback) {
        askForPermissions(Array(1, {permission}), callback)
    }*/

    /*fun askForPermissions (permissions: Array<out String?>, callback: PermissionCallback) {
        mPermissionCallback = callback
        mPermissionRequestCode = Random().nextInt(128)
        var granted = true
        permissions.forEach { p ->
            if (ContextCompat.checkSelfPermission(this@BaseActivity, p!!) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                    val showed = mPermissionCallback!!.onShouldShowRationale(p)
                    if (!showed) {
                        showRequestPermissionRationale(p)
                    } else {
                        mPermissionCallback = null
                        mPermissionRequestCode = 0
                    }
                    return
                } else {
                }
                granted = false
            } else {
                granted = granted and true
            }
        }
        if (granted) {
            mPermissionCallback!!.onGranted(permissions)
            mPermissionCallback = null
            mPermissionRequestCode = 0
        } else {
            ActivityCompat.requestPermissions(this@BaseActivity, permissions, mPermissionRequestCode)
        }
    }*/

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (mPermissionCallback != null && requestCode == mPermissionRequestCode) {
            var granted = true
            grantResults.indices.forEach { i ->
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted = granted and true
                } else {
                    val showed = mPermissionCallback!!.onDenied(permissions[i])
                    mPermissionCallback = null
                    mPermissionRequestCode = 0
                    granted = false

                    if (!showed) {
                        showRequestPermissionDenied(permissions[i])
                    }
                    return
                }
            }
            if (granted) {
                mPermissionCallback!!.onGranted(permissions)
                mPermissionCallback = null
                mPermissionRequestCode = 0
            }
        }
    }*/

    /*private fun showRequestPermissionRationale (permission: String?) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.text_dialog_permission_need, permission))
                .setPositiveButton(R.string.text_dialog_positive) { _, _ ->
                    ActivityCompat.requestPermissions(this@BaseActivity, Array(1, {permission}), mPermissionRequestCode) }
                .setNegativeButton(R.string.text_dialog_negative, null)
                .setNeutralButton(R.string.text_dialog_neutral_go_to_permission_setting) { _, _ ->
                    Intents.settingsTo(this@BaseActivity) }
                .show()
    }

    private fun showRequestPermissionDenied (permission: String?) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.text_dialog_permission_denied, permission))
                .setPositiveButton(R.string.text_dialog_positive) { _, _ -> Intents.settingsTo(this@BaseActivity) }
                .setNegativeButton(R.string.text_dialog_negative, null)
                .setNeutralButton(R.string.text_dialog_neutral_go_to_permission_setting) { _, _ -> Intents.settingsTo(this@BaseActivity) }
                .show()
    }*/

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