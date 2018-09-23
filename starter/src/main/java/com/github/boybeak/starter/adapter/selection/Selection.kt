package com.github.boybeak.starter.adapter.selection

import com.github.boybeak.starter.adapter.AbsAdapter
import com.github.boybeak.starter.adapter.DataBindingAdapter
import com.github.boybeak.starter.adapter.LayoutImpl

interface Selection {

    companion object {

        private val singleSelectionMap = HashMap<String, SingleSelection>()
        private val multipleSelectionMap = HashMap<String, MultipleSelection>()

        fun obtainSingle(adapter: AbsAdapter): SingleSelection {
            val sel = SingleSelection(adapter)
            singleSelectionMap[sel.id()] = sel
            return sel
        }

        fun obtainSingle(id: String, adapter: AbsAdapter): SingleSelection {
            return if (singleSelectionMap.containsKey(id)) {
                singleSelectionMap[id]!!
            } else {
                val s = SingleSelection(adapter)
                s.setId(id)
                singleSelectionMap[id] = s
                s
            }
        }

        fun obtainMultiple(adapter: AbsAdapter): MultipleSelection {
            val sel = MultipleSelection(adapter)
            multipleSelectionMap[sel.id()] = sel
            return sel
        }

        fun obtainMultiple(id: String, adapter: AbsAdapter): MultipleSelection {
            return if (multipleSelectionMap.containsKey(id)) {
                multipleSelectionMap[id]!!
            } else {
                val s = MultipleSelection(adapter)
                s.setId(id)
                multipleSelectionMap[id] = s
                s
            }
        }

        internal fun releaseSingle(id: String) {
            singleSelectionMap.remove(id)
        }

        internal fun releaseMultiple(id: String) {
            multipleSelectionMap.remove(id)
        }

    }

    fun id(): String

    fun start(): Selection
    fun end(): Selection

    fun select(index: Int): Selection
    fun select(layout: LayoutImpl<*, *>): Selection
    fun <Data> select(data: Data): Selection

    fun isSelected(index: Int): Boolean
    fun isSelected(layout: LayoutImpl<*, *>): Boolean
    fun <Data> isSelected(data: Data): Boolean

    fun release()
    fun isReleased(): Boolean

}