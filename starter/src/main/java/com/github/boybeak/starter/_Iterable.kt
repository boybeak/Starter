package com.github.boybeak.starter

inline fun <T> Array<T>.connect(separator: Char = ',', action:(t: T, index: Int) -> String): String {
    val builder = StringBuilder()
    forEachIndexed { index, t ->
        if (index > 0) {
            builder.append(separator)
        }
        builder.append(action(t, index))
    }
    return builder.toString()
}

inline fun IntArray.connect(separator: Char = ',', action:(t: Int, index: Int) -> String): String {
    val builder = StringBuilder()
    forEachIndexed { index, t ->
        if (index > 0) {
            builder.append(separator)
        }
        builder.append(action(t, index))
    }
    return builder.toString()
}

inline fun CharArray.connect(separator: Char = ',', action:(t: Char, index: Int) -> String): String {
    val builder = StringBuilder()
    forEachIndexed { index, t ->
        if (index > 0) {
            builder.append(separator)
        }
        builder.append(action(t, index))
    }
    return builder.toString()
}

inline fun <T> Iterable<T>.connect(separator: Char = ',', action:(t: T, index: Int) -> String): String {
    val builder = StringBuilder()
    forEachIndexed { index, t ->
        if (index > 0) {
            builder.append(separator)
        }
        builder.append(action(t, index))
    }
    return builder.toString()
}