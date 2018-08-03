package com.github.boybeak.starter.adapter.selection

import com.github.boybeak.starter.adapter.DataBindingAdapter
import com.github.boybeak.starter.adapter.LayoutImpl

class MultipleSelection(adapter: DataBindingAdapter) : AbsSelection(adapter) {

    private val selectedList = ArrayList<LayoutImpl<*, *>>()

    override fun select(index: Int) {
        if (index < 0 || index >= adapter().itemCount) {
            return
        }
        select(adapter().getItem(index))
    }

    override fun select(layout: LayoutImpl<*, *>) {
        if (!isStarted()) {
            start()
        }
        val index = adapter().index(layout)
        if (index >= 0 && index < adapter().itemCount) {
            if (isSelected(layout)) {
                selectedList.remove(layout)
                layout.isSelected = false
            } else {
                selectedList.add(layout)
                layout.isSelected = true
            }
            adapter().notifyItemChanged(index)
        }
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
        return selectedList.contains(layout)
    }

    override fun <Data> isSelected(data: Data): Boolean {
        for (i in 0 until adapter().itemCount) {
            if (adapter().getItem(i).source == data) {
                return isSelected(i)
            }
        }
        return false
    }

    override fun start(): MultipleSelection {
        super.start()
        return this
    }

    override fun end(): MultipleSelection {
        unSelectAll()
        super.end()
        return this
    }

    override fun release() {
        super.release()
        Selection.releaseMultiple(id())
    }

    fun selectAll() {
        if (!isStarted()) {
            start()
        }
        for (i in 0 until adapter().itemCount) {
            val layout = adapter().getItem(i)
            layout.isSelected = true
            selectedList.add(layout)
        }
        adapter().notifyDataSetChanged()
    }

    fun unSelectAll() {
        selectedList.clear()
        for (i in 0 until adapter().itemCount) {
            adapter().getItem(i).isSelected = false
        }
    }

    fun getSelectedItems(): List<LayoutImpl<*, *>> {
        return selectedList
    }

    @Suppress("UNCHECKED_CAST")
    fun <Data> getSelectedData(): List<Data> {
        return List(selectedList.size) {
            selectedList[it].source as Data
        }
    }

    fun hasSelectedItems(): Boolean {
        return selectedList.isNotEmpty()
    }

    fun isAllSelected(): Boolean {
        for(i in 0 until adapter().itemCount) {
            if (!adapter().getItem(i).isSelected) {
                return false
            }
        }
        return true
    }

}