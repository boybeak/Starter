package com.github.boybeak.starter.widget

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.boybeak.starter.R

/**
 * Created by gaoyunfei on 2018/3/6.
 */

class EmptyView : LinearLayout {

    companion object {
        private val TAG = EmptyView::class.java.simpleName
    }

    private val iconIv: AppCompatImageView
    private val titleTv: AppCompatTextView
    private val msgTv: AppCompatTextView
    private val actionBtn: AppCompatButton

    private var actionListener: ActionListener? = null

    private constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)

        iconIv = findViewById(R.id.empty_view_icon)
        titleTv = findViewById(R.id.empty_view_title)
        msgTv = findViewById(R.id.empty_view_message)
        actionBtn = findViewById(R.id.empty_view_action)
    }

    private fun setIcon (icon: Drawable) {
        iconIv.setImageDrawable(icon)
        iconIv.visibility = View.VISIBLE
    }

    private fun setTitle (title: CharSequence) {
        titleTv.text = title
        titleTv.visibility = View.VISIBLE
    }

    private fun setMessage (message: CharSequence) {
        msgTv.text = message
        msgTv.visibility = View.VISIBLE
    }

    private fun setAction (actionText: CharSequence, autoDismiss: Boolean, listener: ActionListener?) {

        actionBtn.text = actionText
        actionListener = listener
        actionBtn.setOnClickListener{

            actionListener?.onClick(it, this@EmptyView)
            if (autoDismiss) {
                dismiss()
            }
        }
        actionBtn.visibility = View.VISIBLE
    }

    fun isShowing (): Boolean {
        val parent = parent
        if (parent != null && parent is ViewGroup) {
            return parent.indexOfChild(this) >= 0
        }
        return false
    }

    fun dismiss() {
        val parent = parent
        if (parent != null && parent is ViewGroup) {
            if (parent.indexOfChild(this) >= 0) {
                parent.removeView(this)
            }
        }
        actionListener = null
    }

    interface ActionListener {
        fun onClick (btn: View, emptyView: EmptyView)
    }

    class Builder private constructor(private val parent: ViewGroup) {

        private var icon: Drawable? = null
        private var title: CharSequence? = null
        private var message: CharSequence? = null
        private var actionText: CharSequence? = null
        private var actionListener: ActionListener? = null
        private var autoDismiss: Boolean = false

        private fun context (): Context {
            return parent.context
        }

        fun icon (iconBmp: Bitmap): Builder {
            return icon(BitmapDrawable(parent.context.resources, iconBmp))
        }

        fun icon (@DrawableRes iconRes: Int): Builder {
            return icon(context().resources.getDrawable(iconRes))
        }

        fun icon(iconDrawable: Drawable?): Builder {
            icon = iconDrawable
            return this
        }

        fun title (title: CharSequence?): Builder {
            this.title = title
            return this
        }

        fun title (@StringRes titleRes: Int): Builder {
            return title(context().getString(titleRes))
        }

        fun title (@StringRes titleRes: Int, vararg formatArgs: Any): Builder {
            return title(context().getString(titleRes, formatArgs))
        }

        fun message (text: CharSequence?): Builder {
            this.message = text
            return this
        }

        fun message (@StringRes textRes: Int): Builder {
            return message(context().getString(textRes))
        }

        fun message (@StringRes textRes: Int, vararg formatArgs: Any): Builder {
            return message(context().getString(textRes, formatArgs))
        }

        fun action (actionText: CharSequence?, autoDismiss: Boolean, listener: ActionListener?): Builder {
            this.actionText = actionText
            this.autoDismiss = autoDismiss
            this.actionListener = listener
            return this
        }

        fun action (@StringRes actionTextRes: Int, autoDismiss: Boolean, listener: ActionListener?): Builder {
            return action(context().getString(actionTextRes), autoDismiss, listener)
        }

        fun action (actionText: CharSequence, listener: ActionListener?): Builder {
            return action(actionText, autoDismiss, listener)
        }

        fun action (@StringRes actionTextRes: Int, listener: ActionListener?): Builder {
            return action(context().getString(actionTextRes), listener)
        }

        fun show(params: ViewGroup.LayoutParams): EmptyView {
            val emptyView = EmptyView(context())
            if (icon != null) {
                emptyView.setIcon(icon!!)
            }
            if (!TextUtils.isEmpty(title)) {
                emptyView.setTitle(title!!)
            }
            if (!TextUtils.isEmpty(message)) {
                emptyView.setMessage(message!!)
            }
            if (!TextUtils.isEmpty(actionText) && this@Builder.actionListener != null) {
                emptyView.setAction(actionText!!, autoDismiss, this@Builder.actionListener)
            }
            parent.addView(emptyView, params)
            actionListener = null
            return emptyView
        }

        companion object {

            fun with(activity: Activity): Builder {
                return with(activity.findViewById<View>(R.id.content) as ViewGroup)
            }

            fun with(activity: Activity, @IdRes parentId: Int): Builder {
                return with(activity.findViewById<View>(parentId) as ViewGroup)
            }

            fun with(fragment: Fragment, @IdRes parentId: Int): Builder {
                return with(fragment.view!!.findViewById<View>(parentId) as ViewGroup)
            }

            fun with(parent: ViewGroup): Builder {

                return Builder(parent)
            }
        }
    }

}
