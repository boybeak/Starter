package com.github.boybeak.starter.adapter.selection

import com.github.boybeak.starter.adapter.DataBindingAdapter
import com.github.boybeak.starter.adapter.LayoutImpl

class SingleSelection(adapter: DataBindingAdapter) : AbsSelection(adapter) {

    private var selectedItem: LayoutImpl<*, *>? = null

    override fun select(index: Int) {
        if (!isStarted()) {
            start()
        }
        if (index < 0 || index >= adapter().itemCount) {
            return
        }
        val layout = adapter().getItem(index)
        if (layout == selectedItem) {
            return
        }
        if (selectedItem != null) {
            selectedItem!!.isSelected = false
        }
        selectedItem = layout
        selectedItem!!.isSelected = true
        adapter().notifyItemChanged(index)
    }

    override fun select(layout: LayoutImpl<*, *>) {
        select(adapter().index(layout))
    }

    override fun <Data> select(data: Data) {
        for (i in 0 until adapter().itemCount) {
            if (adapter().getItem(i).source == data) {
                select(i)
                return
            }
        }
    }

    override fun isSelected(index: Int): Boolean {
        if (index < 0 || index >= adapter().itemCount) {
            return false
        }
        return isSelected(adapter().getItem(index))
    }

    override fun isSelected(layout: LayoutImpl<*, *>): Boolean {
        return layout == selectedItem
    }

    override fun <Data> isSelected(data: Data): Boolean {
        for (i in 0 until adapter().itemCount) {
            if (adapter().getItem(i) == data) {
                return true
            }
        }
        return false
    }

    override fun start(): SingleSelection {
        super.start()
        return this
    }

    override fun end(): SingleSelection {
        selectedItem = null
        super.end()
        return this
    }

    override fun release() {
        super.release()
        Selection.releaseSingle(id())
    }

    fun getSelectedItem(): LayoutImpl<*, *>? {
        return selectedItem
    }

    @Suppress("UNCHECKED_CAST")
    fun <Data> getSelectedData(): Data? {
        return if (selectedItem != null) {
            selectedItem!!.source as Data
        } else {
            null
        }
    }

}