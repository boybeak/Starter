package com.github.boybeak.wa

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class AuthWebView : WebView {

    companion object {
        private val TAG = AuthWebView::class.java.simpleName
    }

    private var authParams: AuthParams? = null

    private val webClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            Log.v(TAG, "onPageFinished url=$url")
            if (url!!.contains(authParams!!.redirectUri)) {

            }
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, privateBrowsing: Boolean) : super(context, attrs, defStyleAttr, privateBrowsing)


    init {

        webViewClient = webClient
        webChromeClient = WebChromeClient()

        settings.javaScriptEnabled = true
    }

    fun loadAuthParams(url: String, params: AuthParams) {
        authParams = params
        loadUrl("$url?$params")
    }

    fun loadAuthParams(url: String, params: AuthParams.Builder) {
        loadAuthParams(url, params.build())
    }

}