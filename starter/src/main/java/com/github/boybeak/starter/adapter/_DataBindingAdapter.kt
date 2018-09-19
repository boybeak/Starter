package com.github.boybeak.starter.adapter

inline fun <T : LayoutImpl<*, *>> DataBindingAdapter.forEach (clz: Class<T>, action: (T) -> Unit) {
    for(i in 0 until itemCount) {
        val item = getItem(i)
        if (clz.isInstance(item)) {
            action(item as T)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> DataBindingAdapter.forEachIndexed (clz: Class<T>, action: (T, index: Int) -> Unit) {
    for(i in 0 until itemCount) {
        val item = getItem(i)
        if (clz.isInstance(item)) {
            action(item as T, i)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> DataBindingAdapter.forEachSource (clz: Class<T>, action: (T) -> Unit) {
    for(i in 0 until itemCount) {
        val source = getItem(i).source
        if (clz.isInstance(source)) {
            action(source as T)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> DataBindingAdapter.forEachSourceIndexed (clz: Class<T>, action: (T, index: Int) -> Unit) {
    for(i in 0 until itemCount) {
        val source = getItem(i).source
        if (clz.isInstance(source)) {
            action(source as T, i)
        }
    }
}

operator fun DataBindingAdapter.get(index: Int): LayoutImpl<*, *> {
    return getItem(index)
}