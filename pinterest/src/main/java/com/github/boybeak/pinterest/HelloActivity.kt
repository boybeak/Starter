package com.github.boybeak.pinterest

import android.os.Bundle
import android.util.Log
import com.github.boybeak.starter.Router
import com.github.boybeak.starter.activity.BaseActivity
import com.github.boybeak.starter.utils.ManifestUtil
import com.pinterest.android.pdk.PDKClient

class HelloActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

//        val appId = ManifestUtil.getString(this, "APP_ID")
//        Log.v("ABC", "appId=$appId")
        PDKClient.configureInstance(this, "4982513543896120667")
        PDKClient.getInstance().onConnect(this)
        runOnUiThreadDelayed(Runnable {
            Router.with(this@HelloActivity).goTo(LoginActivity::class.java)
            finish()
        }, 2000)
    }
}
