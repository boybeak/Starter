package com.github.boybeak.starter.app

import android.os.Bundle
import android.view.View
import com.github.boybeak.starter.Router
import com.github.boybeak.starter.activity.BaseActivity
import com.github.boybeak.starter.adapter.FooterAdapter
import com.github.boybeak.starter.adapter.selection.MultipleSelection
import com.github.boybeak.starter.app.activity.DownloaderActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun goToPicker(view: View) {
        Router.with(view).goTo(PickerActivity::class.java)
    }

    fun gotoPermission(view: View) {
        Router.with(view)
                .goTo(PermissionActivity::class.java)
    }

    fun gotoCalendar(view: View) {
        Router.with(view).goTo(CalendarActivity::class.java)
    }

    fun gotoDragExit(view: View) {
        Router.with(view).goTo(DragExitActivity::class.java)
    }

    fun gotoDemo(view: View) {
        Router.with(view).goTo(DemoActivity::class.java)
    }

    fun gotoDownload(view: View) {
        Router.with(view).goTo(DownloaderActivity::class.java)
    }

}
