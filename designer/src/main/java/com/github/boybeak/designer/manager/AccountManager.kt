package com.github.boybeak.designer.manager

import android.content.Context
import android.text.TextUtils
import com.github.boybeak.designer.MetaData
import com.github.boybeak.designer.api.Api
import com.github.boybeak.designer.api.model.AuthInfo
import com.github.boybeak.designer.api.model.User
import com.github.boybeak.starter.retrofit.SafeCallback
import com.github.boybeak.starter.retrofit.SimpleCallback

object AccountManager {

//    private val KEY_ = "com.github.boybeak.designer."
    private const val AUTH_INFO_SP = "auth_info.sp"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_SCOPE = "scope"
    private const val KEY_TOKEN_TYPE = "token_type"

    private var authInfo: AuthInfo? = null
    private var me: User? = null

    fun startUp(context: Context) {
        val authInfoSp = context.getSharedPreferences(AUTH_INFO_SP, Context.MODE_PRIVATE)
        val accessToken = authInfoSp.getString(KEY_ACCESS_TOKEN, "")
        if (!TextUtils.isEmpty(accessToken)) {
            val scope = authInfoSp.getString(KEY_SCOPE, "")
            val tokenType = authInfoSp.getString(KEY_TOKEN_TYPE, "")
            authInfo = AuthInfo(accessToken, tokenType, scope)
        }
    }

    fun getAuthInfo(context: Context, code: String, callback: SafeCallback<AuthInfo>) {

        val authInfoSp = context.getSharedPreferences(AUTH_INFO_SP, Context.MODE_PRIVATE)

        Api.authService().getAuthInfo(MetaData.getClientId(context), MetaData.getClientSecret(context),
                code, MetaData.getRedirectUri(context)).enqueue(object : SimpleCallback<AuthInfo>(callback) {
            override fun onResult(t: AuthInfo) {
                authInfo = t
                val editor = authInfoSp.edit()
                editor.putString(KEY_ACCESS_TOKEN, t.access_token)
                editor.putString(KEY_SCOPE, t.scope)
                editor.putString(KEY_TOKEN_TYPE, t.token_type)
                editor.apply()
            }

            override fun onError(t: Throwable) {

            }

        })
    }

    fun getMe() {
        Api.service().getMe().enqueue(object : SimpleCallback<User>() {
            override fun onResult(t: User) {
                me = t
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun authorized(): Boolean {
        return authInfo != null
    }

    fun accessToken(): String {
        if (authInfo == null) {
            return ""
        }
        return authInfo!!.access_token
    }


}