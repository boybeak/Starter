package com.github.boybeak.starter.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.github.boybeak.starter.R
import kotlinx.android.synthetic.main.activity_toolbar_view_pager.*

open class ToolbarViewPagerActivity : ToolbarActivity(){

    private var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_view_pager)

        if (showTabLayout()) {
            decorView().viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    decorView().viewTreeObserver.removeOnGlobalLayoutListener(this)
                    tabLayout = TabLayout(this@ToolbarViewPagerActivity)
                    buildTabLayout(tabLayout!!)

                    toolbar().addView(tabLayout, buildTabLayoutParams())

                    tabLayout!!.setupWithViewPager(viewPager(), true)
                }

            })
        }
    }

    fun viewPager(): ViewPager {
        return view_pager
    }

    fun tabLayout(): TabLayout? {
        return tabLayout
    }

    open fun showTabLayout(): Boolean {
        return true
    }

    open fun buildTabLayout(tabLayout: TabLayout) {
    }

    open fun buildTabLayoutParams(): Toolbar.LayoutParams {
        val p = Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER)
        return p
    }

}