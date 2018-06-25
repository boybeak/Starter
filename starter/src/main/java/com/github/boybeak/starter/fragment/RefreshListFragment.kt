package com.github.boybeak.starter.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.boybeak.starter.R
import com.github.boybeak.starter.widget.EmptyView
import org.jetbrains.annotations.NotNull

/**
 * Created by gaoyunfei on 2018/1/28.
 */
abstract class RefreshListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var emptyView: EmptyView? = null

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_refresh_list, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView!!.adapter = getAdapter()
        if (recyclerView!!.layoutManager == null) {
            recyclerView!!.layoutManager = getLayoutManager()
        }

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        swipeRefreshLayout!!.setOnRefreshListener(this@RefreshListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun recyclerView(): RecyclerView {
        return recyclerView!!
    }

    fun swipeRefreshLayout (): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    @NotNull
    abstract fun getAdapter(): RecyclerView.Adapter<*>

    @NotNull
    abstract fun getLayoutManager():RecyclerView.LayoutManager


    private fun showEmptyViewIn (parent: ViewGroup, params: ViewGroup.LayoutParams,
                                 icon: Drawable?, title: CharSequence, message: CharSequence, button: CharSequence,
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

    fun showEmptyView (title: String, message: String, button: String, listener: EmptyView.ActionListener?) {
        showEmptyView(context!!.resources.getDrawable(R.drawable.bg_empty_default), title, message, button, listener)
    }

    fun showEmptyView(@DrawableRes icon: Int, @StringRes title: Int, @StringRes message: Int, @StringRes button: Int,
                      listener: EmptyView.ActionListener?) {
        var iconDrawable: Drawable? = null
        if (icon > 0) {
            iconDrawable = resources.getDrawable(icon)
        }
        showEmptyView(iconDrawable, getString(title), getString(message), getString(button), listener)
    }

    open fun showEmptyView(icon: Drawable?, title: CharSequence, message: CharSequence, button: CharSequence,
                           listener: EmptyView.ActionListener?) {
        val contentLayout: FrameLayout = view!!.findViewById(R.id.content)
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

    fun scrollToTop () {
        recyclerView().scrollToPosition(0)
    }

}