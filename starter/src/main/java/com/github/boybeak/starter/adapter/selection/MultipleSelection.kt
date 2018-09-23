package com.github.boybeak.starter.adapter.selection

import com.github.boybeak.starter.adapter.AbsAdapter
import com.github.boybeak.starter.adapter.DataBindingAdapter
import com.github.boybeak.starter.adapter.LayoutImpl

class MultipleSelection internal constructor(adapter: AbsAdapter) : AbsSelection(adapter) {

    private val selectedList = ArrayList<LayoutImpl<*, *>>()

    private var selectListener: OnSelectChangeListener? = null

    override fun select(index: Int): MultipleSelection {
        if (index < 0 || index >= adapter().itemCount) {
            return this
        }
        return select(adapter().getItem(index))
    }

    override fun select(layout: LayoutImpl<*, *>): MultipleSelection {
        if (!isStarted()) {
            start()
        }
        val index = adapter().index(layout)
        if (index >= 0 && index < adapter().itemCount) {
            if (isSelected(layout)) {
                selectedList.remove(layout)
                layout.isSelected = false
                selectListener?.onUnSelect(layout, isNothingSelected(), selectedCount())
            } else {
                selectedList.add(layout)
                layout.isSelected = true
                selectListener?.onSelect(layout, isAllSelected(), selectedCount())
            }
            adapter().notifyItemChanged(index)
        }
        return this
    }

    override fun <Data> select(data: Data): MultipleSelection {
        for (i in 0 until adapter().itemCount) {
            if (adapter().getItem(i).source == data) {
                select(i)
                return this
            }
        }
        return this
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
        selectedList.forEach {
            if (it.source == data) {
                return true
            }
        }
        return false
    }

    override fun start(): MultipleSelection {
        super.start()
        return this
    }

    fun start(translation: SelectionMapper): MultipleSelection {
        start()
        var changed = false
        for (i in 0 until adapter().itemCount) {
            val layout = adapter().getItem(i)
            if (layout.isSelectable) {
                layout.isSelected = translation.isAlreadySelected(i, adapter())
                if (layout.isSelected && !selectedList.contains(layout)) {
                    selectedList.add(layout)
                    changed = true
                }
            }
        }
        if (changed) {
            adapter().notifyDataSetChanged()
        }
        return this
    }

    override fun end(): MultipleSelection {
        unSelectAll()
        super.end()
        return this
    }

    fun listenBy(listener: OnSelectChangeListener): MultipleSelection {
        this.selectListener = listener
        return this
    }

    override fun release() {
        super.release()
        Selection.releaseMultiple(id())
        selectListener = null
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

    fun selectedItems(): List<LayoutImpl<*, *>> {
        return selectedList
    }

    @Suppress("UNCHECKED_CAST")
    fun <Data> selectedData(): List<Data> {
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

    fun isNothingSelected(): Boolean {
        return selectedList.isEmpty()
    }

    fun selectedCount(): Int {
        return selectedList.size
    }

    interface OnSelectChangeListener {
        fun onSelect(layout: LayoutImpl<*, *>, isAllSelected: Boolean, selectedCount: Int)
        fun onUnSelect(layout: LayoutImpl<*, *>, isNothingSelected: Boolean, selectedCount: Int)
    }

    interface SelectionMapper {
        fun isAlreadySelected(index: Int, adapter: AbsAdapter): Boolean
    }

}