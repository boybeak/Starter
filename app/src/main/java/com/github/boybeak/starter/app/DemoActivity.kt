package com.github.boybeak.starter.app

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import com.github.boybeak.starter.activity.BaseActivity
import com.github.boybeak.starter.app.fragment.DemoFragment
import com.github.boybeak.starter.widget.SimpleFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : BaseActivity() {

    private val list = listOf<DemoFragment>(DemoFragment(), DemoFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        view_pager.adapter = SimpleFragmentStatePagerAdapter(this, supportFragmentManager, list)

        tab_layout.setupWithViewPager(view_pager)

    }

    private fun getBg(theme: Resources.Theme): Drawable {
        val a = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, a, true)
        return if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            val color = a.data
            ColorDrawable(color)
        } else {
            // windowBackground is not a color, probably a drawable
            val d = getResources().getDrawable(a.resourceId)
            d
        }
    }

}
