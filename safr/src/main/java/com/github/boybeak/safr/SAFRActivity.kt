package com.github.boybeak.safr

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class SAFRActivity : Activity() {

    private var id: String? = null

    private var requestCode: Int = 0
    private var resultCode: Int = 0
    private var data: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = intent.getStringExtra(SAFR.KEY_ID)

        val it = Intent()
        val extras = intent.extras
        it.putExtras(extras)

        val action = intent.getStringExtra(SAFR.KEY_ACTION)
        if (action != null) {
            it.action = action
        } else {
            val clzName = intent.getStringExtra(SAFR.KEY_CLASS)
            val clz = Class.forName(clzName)
            it.setClass(this, clz)
        }
        val requestCode = intent.getIntExtra(SAFR.KEY_REQUEST_CODE, 0)
        startActivityForResult(it, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this.requestCode = requestCode
        this.resultCode = resultCode
        this.data = data

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        SAFR.onActivityResult(id, requestCode, resultCode, data)

        data = null
    }
}
