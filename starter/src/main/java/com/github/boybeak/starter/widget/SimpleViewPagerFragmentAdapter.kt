package com.github.boybeak.starter.widget

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.github.boybeak.starter.fragment.BaseFragment

import java.util.ArrayList
import java.util.Arrays

class SimpleViewPagerFragmentAdapter : FragmentPagerAdapter {

    private var context: Context? = null
    private var fragments: List<BaseFragment>? = null

    constructor(context: Context, fm: FragmentManager, fragments: List<BaseFragment>) : super(fm) {
        this.context = context
        this.fragments = fragments
    }

    constructor(context: Context, fm: FragmentManager, fragments: Array<BaseFragment>) : this(context, fm, Arrays.asList(*fragments))

    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments!![position].getTitle(context!!)
    }
}
