package com.github.boybeak.starter.app

import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.starter.activity.ToolbarActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : ToolbarActivity() {

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

    }
}
