package com.github.boybeak.starter.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.github.boybeak.starter.widget.OnScrollBottomListener

/**
 * Created by gaoyunfei on 2018/1/29.
 */

abstract class PaginationFragment : RefreshListFragment() {

    private val keyPage = "page"

    private var page: Int = 0
    private var loading: Boolean = false

    private val scrollBottomListener = object: OnScrollBottomListener(){

        override fun onScrollBottom(recyclerView: RecyclerView?, newState: Int) {
            this@PaginationFragment.onScrollBottom()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(keyPage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView()!!.addOnScrollListener(scrollBottomListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(keyPage, page)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView()!!.removeOnScrollListener(scrollBottomListener)
    }

    fun page(): Int {
        return page
    }

    fun pageIncrease() {
        page++
    }

    fun pageReset() {
        page = 0
    }

    fun isLoading(): Boolean {
        return loading
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
    }

    abstract fun onScrollBottom ()
}
