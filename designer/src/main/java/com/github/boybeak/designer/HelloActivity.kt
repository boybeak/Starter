package com.github.boybeak.designer

import android.os.Bundle
import com.github.boybeak.designer.manager.AccountManager
import com.github.boybeak.starter.Router
import com.github.boybeak.starter.activity.BaseActivity

class HelloActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        runOnUiThreadDelayed(Runnable {
            if (AccountManager.authorized()) {
                Router.with(this@HelloActivity).goTo(MainActivity::class.java)
            } else {
                Router.with(this@HelloActivity).goTo(AuthActivity::class.java)
            }
            finish()
        }, 2000)
    }
}
