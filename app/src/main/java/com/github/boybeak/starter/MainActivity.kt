package com.github.boybeak.starter

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.safr.Callback
import com.github.boybeak.safr.SAFR

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SAFR.newInstance().byAction(Intent.ACTION_GET_CONTENT).type("image/*").startActivityForResult(this, 100, object : Callback {
            override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {
                var s = "s"
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        s = "OK"
                    }
                    Activity.RESULT_CANCELED -> {
                        s = "CANCELED"
                    }
                    Activity.RESULT_FIRST_USER -> {
                        s = "FIRST_USER"
                    }
                }
                Toast.makeText(this@MainActivity, s, Toast.LENGTH_SHORT).show()
            }

        })
    }
}
