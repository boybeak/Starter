package com.github.boybeak.designer

import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.github.boybeak.designer.api.model.AuthInfo
import com.github.boybeak.designer.manager.AccountManager
import com.github.boybeak.starter.Router
import com.github.boybeak.starter.activity.ToolbarActivity
import com.github.boybeak.starter.retrofit.SafeCallback
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.*

class AuthActivity : ToolbarActivity() {

    companion object {
        val TAG = AuthActivity::class.java.simpleName!!
    }

    private var redirectUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        redirectUri = MetaData.getRedirectUri(this)

        auth_web_view.webViewClient = object : WebViewClient() {
            // https://github.com/boybeak?code=5b642d1cbd0935c3ad01058f439578e804f63c706d519a95d1ae935503311e20&state=10f0f324-38a4-465f-b69a-c947bd254e0e
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.startsWith(redirectUri!!)) {
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
                    AccountManager.getAuthInfo(this@AuthActivity, code, object : SafeCallback<AuthInfo>(this@AuthActivity) {
                        override fun onResult(t: AuthInfo) {
                            Router.with(this@AuthActivity).goTo(MainActivity::class.java)
                            finish()
                        }

                        override fun onError(t: Throwable) {
                            Toast.makeText(this@AuthActivity, t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }

        auth_web_view.webChromeClient = object : WebChromeClient() {
        }

        val randomState = UUID.randomUUID().toString()

        val builder = StringBuilder("https://dribbble.com/oauth/authorize?")
                .append("client_id=").append(MetaData.getClientId(this))
                .append("&redirect_uri=").append(redirectUri)
                .append("&scope=").append("public+upload")
                .append("&state=").append(randomState)
        auth_web_view.loadUrl(builder.toString())

    }
}
