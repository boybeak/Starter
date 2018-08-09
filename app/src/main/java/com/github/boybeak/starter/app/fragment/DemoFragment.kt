package com.github.boybeak.starter.app.fragment

import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.boybeak.starter.app.R
import com.github.boybeak.starter.fragment.BaseFragment

class DemoFragment : BaseFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val iv = AppCompatImageView(context)
        iv.setImageResource(R.mipmap.ic_launcher)
        return iv
    }
}