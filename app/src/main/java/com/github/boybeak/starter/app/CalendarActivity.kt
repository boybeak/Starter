package com.github.boybeak.starter.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        month_view.setYearAndMonth(2018, Calendar.JULY)

        size_et.setOnEditorActionListener { v, actionId, event ->
            val size: Int = size_et.text.toString().toInt()
            val params = month_view.layoutParams
            params.height = size
            month_view.layoutParams = params
            Toast.makeText(this@CalendarActivity, size_et.text, Toast.LENGTH_SHORT).show()
            true
        }

//        val today = Calendar.getInstance()
//        today.set(2018, Calendar.JULY, 22)
//        Toast.makeText(this, "${today.get(Calendar.DAY_OF_WEEK)}", Toast.LENGTH_SHORT).show()
//        cal.setSunday(today.time)
    }
}
