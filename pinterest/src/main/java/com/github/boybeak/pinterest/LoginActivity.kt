package com.github.boybeak.pinterest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.github.boybeak.starter.activity.ToolbarActivity
import com.pinterest.android.pdk.PDKCallback
import com.pinterest.android.pdk.PDKClient
import com.pinterest.android.pdk.PDKException
import com.pinterest.android.pdk.PDKResponse

class LoginActivity : ToolbarActivity() {

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        PDKClient.getInstance().login(this,
                listOf(
                    PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC
                ),
                object : PDKCallback() {
                    override fun onSuccess(response: PDKResponse?) {
                        Log.v(TAG, "onSuccess user=${response!!.user.username}")
                    }

                    override fun onFailure(exception: PDKException?) {
                        Log.v(TAG, "onFailure ${exception!!.detailMessage} ${exception!!.stausCode}")
                        exception!!.printStackTrace()

                    }

                    override fun onErrorResponse(error: VolleyError?) {
                        super.onErrorResponse(error)
                        error!!.printStackTrace()
                        Log.v(TAG, "onErrorResponse ")
                    }
                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data)
    }

}
