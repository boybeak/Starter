package com.github.boybeak.starter.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.boybeak.de.DragExitLayout
import kotlinx.android.synthetic.main.activity_drag_exit.*

class DragExitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_exit)

        drag_exit.setOnExitListener(object : DragExitLayout.OnExitListener {
            override fun onExit() {
//                finish()
            }
        })

    }

    fun showToast(view: View) {
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show()
    }

}
