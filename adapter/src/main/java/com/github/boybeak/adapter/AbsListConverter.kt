package com.github.boybeak.adapter

abstract class AbsListConverter<Data> : ListConverter<Data> {

    override fun convert(data: Data, adapter: DataBindingAdapter): Collection<LayoutImpl<*, *>> {
        val list = ArrayList<LayoutImpl<*, *>>()
        fillDataList(data, adapter, list)
        return list
    }

    abstract fun fillDataList(data: Data, adapter: DataBindingAdapter, list: ArrayList<LayoutImpl<*, *>>)

}