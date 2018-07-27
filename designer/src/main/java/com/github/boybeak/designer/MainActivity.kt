package com.github.boybeak.designer

import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.designer.api.Api
import com.github.boybeak.designer.api.model.User
import com.github.boybeak.designer.manager.AccountManager
import com.github.boybeak.starter.activity.ToolbarActivity
import com.github.boybeak.starter.retrofit.SafeCallback

class MainActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Api.service().getShots().enqueue(object : SafeListCallback<Shot>(this) {
            override fun onResultList(ts: MutableList<Shot>) {
                Toast.makeText(this@MainActivity, "onResultList ${ts.size}", Toast.LENGTH_SHORT).show()
            }

            override fun onEmpty() {
                Toast.makeText(this@MainActivity, "onEmpty", Toast.LENGTH_SHORT).show()
            }

            override fun onError(t: Throwable) {
                Toast.makeText(this@MainActivity, "onError", Toast.LENGTH_SHORT).show()
            }

        })*/
        AccountManager.getMe()
    }

    override fun actionBarAsUpEnable(): Boolean {
        return false
    }

}
