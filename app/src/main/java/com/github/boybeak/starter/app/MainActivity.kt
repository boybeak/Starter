package com.github.boybeak.starter.app

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.github.boybeak.starter.Router
import com.github.boybeak.starter.activity.BaseActivity
import com.github.boybeak.starter.app.activity.DownloaderActivity



class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun goToPicker(view: View) {
//        val uri = Uri.parse("content://com.android.providers.media.documents/document/image%3A365661")
//        UriParser.isDocumentContentUri(uri)
//        Toast.makeText(this, DocumentsContract.getDocumentId(uri), Toast.LENGTH_SHORT).show()
        Router.with(view).goTo(PickerActivity::class.java)
//        get(uri)
    }

    private fun get(uri: Uri) {

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
