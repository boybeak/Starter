package com.github.boybeak.permission

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View

class PermissionAgentActivity : AppCompatActivity() {

    private var id: String? = null
    private var permissions: ArrayList<String>? = null

    private var requestCode: Int = 0
    private var grantResults: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))

        id = intent.getStringExtra(PH.KEY_ID)
        permissions = intent.getStringArrayListExtra(PH.KEY_PERMISSION_LIST)

        val permissionArray = Array<String>(permissions!!.size) {
            permissions!![it]
        }

        ActivityCompat.requestPermissions(this, permissionArray, 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        this.requestCode = requestCode
        this.grantResults = grantResults

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        var result = true
        grantResults!!.forEachIndexed { index, i ->
            result = result and (i == PackageManager.PERMISSION_GRANTED)
            if (!result) {
                PH.actionDenied(id, permissions!![index])
                return
            }
        }
        if (result) {
            PH.actionGranted(id, permissions)
        }
    }

}
