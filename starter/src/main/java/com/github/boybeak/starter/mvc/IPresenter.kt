package com.github.boybeak.starter.mvc

/**
 * Created by gaoyunfei on 2018/2/26.
 */
interface IPresenter<out T: IView> {
    fun getView (): T
    fun start ()
    fun stop ()
}