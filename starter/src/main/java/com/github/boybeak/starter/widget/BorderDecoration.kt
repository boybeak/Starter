package com.github.boybeak.starter.widget

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

class BorderDecoration(val border: Int) : RecyclerView.ItemDecoration(){

    companion object {
        private val TAG = BorderDecoration::class.java.simpleName
    }

    constructor(context: Context, @DimenRes dimen: Int): this(context.resources.getDimensionPixelSize(dimen))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when(parent!!.layoutManager) {
            is LinearLayoutManager -> {
                getItemOffsetsLinearLayoutManager(parent.layoutManager as GridLayoutManager, outRect, view, parent, state)
            }
            is GridLayoutManager -> {
                getItemOffsetsGridLayoutManager(parent.layoutManager as LinearLayoutManager, outRect, view, parent, state)
            }
            else -> {
                throw IllegalStateException("BorderDecoration can only deal with LinearLayoutManager and GridLayoutManager")
            }
        }
    }

    private fun getItemOffsetsLinearLayoutManager(glm: GridLayoutManager, outRect: Rect?, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = glm.spanCount
        val lookup = glm.spanSizeLookup

        val adapterPosition = parent!!.getChildAdapterPosition(view)

//        val spanSize = lookup.getSpanSize(adapterPosition)
        val groupIndex = lookup.getSpanGroupIndex(adapterPosition, spanCount)

        Log.v(TAG, "getItemOffsetsLinearLayoutManager $adapterPosition $groupIndex")

    }

    private fun getItemOffsetsGridLayoutManager(llm: LinearLayoutManager, outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {

    }

}