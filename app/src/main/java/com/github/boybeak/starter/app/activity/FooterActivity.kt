package com.github.boybeak.starter.app.activity

import android.os.Bundle
import android.view.View
import com.github.boybeak.adapter.extension.FooterAdapter
import com.github.boybeak.starter.activity.BaseActivity
import com.github.boybeak.starter.adapter.OneAdapter
import com.github.boybeak.starter.app.R
import com.github.boybeak.starter.app.adapter.StringImpl
import kotlinx.android.synthetic.main.activity_footer.*

class FooterActivity : BaseActivity() {

    private lateinit var footerAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_footer)

        footerAdapter = OneAdapter(this)
        recycler_view.adapter = footerAdapter
    }

    fun add(view: View) {
        footerAdapter.notifyLoadingFooter()
        runOnUiThreadDelayed(Runnable {
            footerAdapter.add(StringImpl("${footerAdapter.itemCount}")).autoNotify()
            footerAdapter.notifySuccessFooter("Data load success")
        }, 3000)
    }

    fun remove(view: View) {
        footerAdapter.clearData().autoNotify()
        footerAdapter.notifyEmptyFooter("No data received")
    }

}
