package com.github.boybeak.starter.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.github.boybeak.starter.R
import kotlinx.android.synthetic.main.activity_srl_rv.*

open class SrlRvActivity : ToolbarActivity(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srl_rv)

        recycler_view.layoutManager = layoutManager()
        swipe_refresh_layout.setOnRefreshListener(this)
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
