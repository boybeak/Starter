package com.github.boybeak.starter.mvc

import android.os.Bundle

/**
 * Created by gaoyunfei on 2018/2/26.
 */
abstract class AbsPresenter<out T: IView>(private val view: T): IPresenter<T> {

    override fun getView(): T {
        return view
    }

    abstract fun saveState (outState: Bundle?)
    abstract fun restoreState (outState: Bundle?)

    override fun start() {

    }

    override fun stop() {
    }
}