package com.github.boybeak.adapter

inline fun <T : LayoutImpl<*, *>> AbsAdapter.forEach (clz: Class<T>, action: (T) -> Unit) {
    for(i in 0 until itemCount) {
        val item = getItem(i)
        if (clz.isInstance(item)) {
            action(item as T)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> AbsAdapter.forEachIndexed (clz: Class<T>, action: (T, index: Int) -> Unit) {
    for(i in 0 until itemCount) {
        val item = getItem(i)
        if (clz.isInstance(item)) {
            action(item as T, i)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> AbsAdapter.forEachSource (clz: Class<T>, action: (T) -> Unit) {
    for(i in 0 until itemCount) {
        val source = getItem(i).source
        if (clz.isInstance(source)) {
            action(source as T)
        }
    }
}

inline fun <T : LayoutImpl<*, *>> AbsAdapter.forEachSourceIndexed (clz: Class<T>, action: (T, index: Int) -> Unit) {
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