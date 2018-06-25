package com.github.boybeak.starter

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View

/**
 * Created by gaoyunfei on 2018/1/30.
 */

class Router private constructor(private var context: Context?) {

    companion object {
        fun with (context: Context): Router {
            return Router(context)
        }

        fun with (view: View): Router {
            return with(view.context)
        }

        fun with (fragment: Fragment): Router {
            return with(fragment.activity!!)
        }
    }

    private val it: Intent = Intent()
    private val sharedElements = ArrayList<Pair<View, String>>()

    fun category (vararg categories: String): Router {
        categories.forEach {
            this@Router.it.addCategory(it)
        }
        return this
    }

    fun data (uri: Uri): Router {
        it.data = uri
        return this
    }

    fun withParcelable(key: String, parcelable: Parcelable): Router {
        it.putExtra(key, parcelable)
        return this
    }

    fun withParcelableList (key: String, parcelables: List<Parcelable>): Router {
        val list = ArrayList<Parcelable>()
        list.addAll(parcelables)
        it.putParcelableArrayListExtra(key, list)
        return this
    }

    fun withBoolean (key: String, boolean: Boolean): Router {
        it.putExtra(key, boolean)
        return this
    }

    fun withInt (key: String, value: Int): Router {
        it.putExtra(key, value)
        return this
    }

    fun withSharedElement (view: View, translationName: String): Router {
        sharedElements.add(Pair(view, translationName))
        return this
    }

    fun withSharedElement (view: View, @StringRes translationNameRes: Int): Router {
        sharedElements.add(Pair(view, view.context.getString(translationNameRes)))
        return this
    }

    fun goTo(clz: Class<out Activity>) {
        it.setClass(context, clz)
        this.context!!.startActivity(it, makeOptionBundle())

        if (sharedElements.isNotEmpty()) {
            sharedElements.clear()
        }
        context = null
    }

    fun goTo(action: String) {
        it.action = action
        this.context!!.startActivity(it, makeOptionBundle())

        if (sharedElements.isNotEmpty()) {
            sharedElements.clear()
        }
        context = null
    }

    private fun makeOptionBundle(): Bundle? {
        val elements = Array<Pair<View, String>>(sharedElements.size, {
            sharedElements[it]
        })
        return ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *elements).toBundle()
    }

}
