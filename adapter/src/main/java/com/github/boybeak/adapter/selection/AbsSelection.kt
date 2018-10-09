package com.github.boybeak.adapter.selection

import com.github.boybeak.adapter.AbsAdapter
import java.util.*

abstract class AbsSelection(private var adapter: AbsAdapter?) : Selection {

    private var id = UUID.randomUUID().toString()

    private var isStarted = false;

    override fun id(): String {
        return id
    }

    internal fun setId(id: String) {
        this.id = id
    }

    fun adapter(): AbsAdapter {
        if (adapter == null) {
            throw IllegalStateException("you can not use this Selection after release")
        }
        return adapter!!
    }

    override fun start(): Selection {
        for(i in 0 until adapter().itemCount) {
            val layout = adapter().getItem(i)
            if (layout.supportSelect()) {
                layout.isSelectable = true
            }
        }
        adapter().notifyDataSetChanged()
        isStarted = true
        return this
    }

    override fun end(): Selection {
        isStarted = false
        for(i in 0 until adapter().itemCount) {
            val layout = adapter().getItem(i)
            if (layout.supportSelect()) {
                layout.isSelectable = false
            }
        }
        adapter().notifyDataSetChanged()
        return this
    }

    fun isStarted(): Boolean {
        return isStarted
    }

    override fun release() {
        adapter = null
    }

    override fun isReleased(): Boolean {
        return adapter == null
    }
}