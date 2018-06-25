package com.github.boybeak.starter.activity

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.github.boybeak.starter.Intents
import com.github.boybeak.starter.R
import com.github.boybeak.starter.utils.PermissionHelper

class PermissionAgentActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 12306
    }

    private var grantedPermissions: Array<out String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_agent)

        val permissions = intent.getStringArrayExtra("permissions")

        askPermissions(permissions)
    }

    private fun askPermissions (permissions: Array<out String?>) {
        var granted = true
        permissions.forEach { p ->
            granted = if (ContextCompat.checkSelfPermission(this, p!!) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, p!!)) {
                    if (PermissionHelper.shouldShowRationale(p)) {
                        finish()
                    } else {
                        showRequestPermissionRationale(p)
                    }
                    return
                } else {
                }
                false
            } else {
                granted and true
            }
        }
        if (granted) {
            delayTriggerGrant(permissions)
            finish()
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            var granted = true
            grantResults.indices.forEach { i ->
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted = granted and true
                } else {
                    if (PermissionHelper.deny(permissions[i])) {
                        finish()
                    } else {
                        showRequestPermissionDenied(permissions[i])
                    }
                    granted = false
                    return
                }
            }
            if (granted) {
                delayTriggerGrant(permissions)
            }
        }

        finish()
    }

    private fun showRequestPermissionRationale (permission: String?) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.text_dialog_permission_need, permission))
                .setPositiveButton(R.string.text_dialog_positive) { _, _ ->
                    ActivityCompat.requestPermissions(this, Array(1, {permission}), PERMISSION_REQUEST_CODE) }
                .setNegativeButton(R.string.text_dialog_negative, null)
                .setNeutralButton(R.string.text_dialog_neutral_go_to_permission_setting) { _, _ ->
                    Intents.settingsTo(this) }
                .setOnDismissListener { finish() }
                .show()
    }

    private fun showRequestPermissionDenied (permission: String?) {
        AlertDialog.Builder(this)
                .setMessage(getString(R.string.text_dialog_permission_denied, permission))
                .setPositiveButton(R.string.text_dialog_positive) { _, _ -> Intents.settingsTo(this) }
                .setNegativeButton(R.string.text_dialog_negative, null)
                .setNeutralButton(R.string.text_dialog_neutral_go_to_permission_setting) { _, _ -> Intents.settingsTo(this) }
                .setOnDismissListener { finish() }
                .show()
    }

    private fun delayTriggerGrant (permissions: Array<out String?>) {
        grantedPermissions = permissions

    }

    override fun onDestroy() {
        super.onDestroy()
        if (grantedPermissions != null) {
            PermissionHelper.grant(grantedPermissions!!)
        }
        PermissionHelper.release()      //Just make sure
    }

}
