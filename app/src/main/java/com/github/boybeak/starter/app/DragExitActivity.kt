package com.github.boybeak.starter.app

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.github.boybeak.de.DragExitLayout
import com.github.boybeak.starter.activity.DragExitToolbarActivity
import kotlinx.android.synthetic.main.activity_drag_exit.*

class DragExitActivity : DragExitToolbarActivity() {

    private val dragListener = object : DragExitLayout.OnExitListener {
        override fun onExit() {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_exit)

        web_view.webViewClient = WebViewClient()
        web_view.webChromeClient = WebChromeClient()

        web_view.loadUrl("https://github.com/boybeak/Starter")

    }

    override fun onDragExitLayoutPrepared(del: DragExitLayout) {

    }

}
