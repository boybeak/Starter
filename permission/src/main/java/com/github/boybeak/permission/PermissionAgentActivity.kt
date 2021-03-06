package com.github.boybeak.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View

class PermissionAgentActivity : Activity() {

    private var id: String? = null
    private var permissions: ArrayList<String>? = null

    private var requestCode: Int = 0
    private var grantResults: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))

        id = intent.getStringExtra(PH.KEY_ID)
        permissions = intent.getStringArrayListExtra(PH.KEY_PERMISSION_LIST)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            finish()
        } else {
            val permissionArray = Array<String>(permissions!!.size) {
                permissions!![it]
            }
            ActivityCompat.requestPermissions(this, permissionArray, 100)
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantResults!!.forEachIndexed { index, i ->
                result = result and (i == PackageManager.PERMISSION_GRANTED)
                if (!result) {
                    val permission = permissions!![index]
                    PH.actionDenied(id, permission,
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                    return
                }
            }
        }
        if (result) {
            PH.actionGranted(id, permissions)
        }
    }

}
