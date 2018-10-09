package com.github.boybeak.starter.activity.de

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.github.boybeak.starter.R
import com.github.boybeak.starter.widget.OnScrollBottomListener
import kotlinx.android.synthetic.main.activity_srl_rv.*

open class DragExitSrlRvActivity : DragExitToolbarActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var bottomListener: OnScrollBottomListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srl_rv)

        recycler_view.layoutManager = layoutManager()
        swipe_refresh_layout.setOnRefreshListener(this)

        if (paginationEnable()) {
            bottomListener = object : OnScrollBottomListener() {
                override fun onScrollBottom(recyclerView: RecyclerView?, newState: Int) {
                    onNextPage()
                }

            }
            recycler_view.addOnScrollListener(bottomListener!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bottomListener != null) {
            recyclerView().removeOnScrollListener(bottomListener!!)
        }
    }

    open fun paginationEnable(): Boolean {
        return false
    }

    open fun onNextPage() {

    }

    fun recyclerView (): RecyclerView {
        return recycler_view
    }

    fun swipeRefreshLayout (): SwipeRefreshLayout {
        return swipe_refresh_layout
    }

    override fun onRefresh() {

    }

    open fun layoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }
}
