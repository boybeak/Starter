package com.github.boybeak.starter.adapter.selection

import com.github.boybeak.starter.adapter.AbsAdapter
import com.github.boybeak.starter.adapter.LayoutImpl
import com.github.boybeak.starter.adapter.forEach
import com.github.boybeak.starter.adapter.forEachIndexed

class SingleSelection internal constructor(adapter: AbsAdapter) : AbsSelection(adapter) {

    private var selectedItem: LayoutImpl<*, *>? = null

    private var selectListener: OnSelectChangeListener? = null

    override fun select(index: Int): Selection {
        if (!isStarted()) {
            start()
        }
        if (index < 0 || index >= adapter().itemCount) {
            return this
        }
        val layout = adapter().getItem(index)
        if (layout == selectedItem) {
            return this
        }
        if (selectedItem != null) {
            selectedItem!!.isSelected = false
            adapter().notifyItemChanged(adapter().index(selectedItem))
            selectListener?.onUnSelect(selectedItem!!)
        } else {
            adapter().forEachIndexed(LayoutImpl::class.java) {layoutImpl, index ->
                if (layoutImpl.isSelected) {
                    layoutImpl.isSelected = false
                    adapter().notifyItemChanged(index)
                }
            }
        }
        selectedItem = layout
        selectedItem!!.isSelected = true
        adapter().notifyItemChanged(index)
        selectListener?.onSelect(layout)

        return this
    }

    override fun select(layout: LayoutImpl<*, *>): Selection {
        return select(adapter().index(layout))
    }

    override fun <Data> select(data: Data): Selection {
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

    fun listenBy(listener: OnSelectChangeListener): SingleSelection {
        selectListener = listener
        return this
    }

    override fun release() {
        super.release()
        Selection.releaseSingle(id())
        selectListener = null
    }

    fun getSelectedItem(): LayoutImpl<*, *>? {
        return if (selectedItem != null) {
            selectedItem
        } else {
            var item: LayoutImpl<*, *>? = null
            adapter().forEach(LayoutImpl::class.java) {
                if (it.isSelected) {
                    item = it
                    return@forEach
                }
            }
            item
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <Data> getSelectedData(): Data? {
        return if (selectedItem != null) {
            selectedItem!!.source as Data
        } else {
            var t: Data? = null
            adapter().forEach(LayoutImpl::class.java) {
                if (it.isSelected) {
                    t = it.source as Data
                    return@forEach
                }
            }
            t
        }
    }

    interface OnSelectChangeListener {
        fun onSelect (layout: LayoutImpl<*, *>)
        fun onUnSelect (layout: LayoutImpl<*, *>)
    }

}