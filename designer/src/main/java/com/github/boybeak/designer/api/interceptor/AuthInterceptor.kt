package com.github.boybeak.designer.api.interceptor

import android.util.Log
import com.github.boybeak.designer.manager.AccountManager
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response

class AuthInterceptor : Interceptor{

    companion object {
        private val TAG = AuthInterceptor::class.java.simpleName
    }

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        Log.v(TAG, "url before=${request.url().url()}")
        val builder = request.newBuilder()
        if (AccountManager.authorized()) {
            val method = request.method().toUpperCase()
            when(method) {
                "GET" -> {
                    val url = request.url().newBuilder()
                            .addQueryParameter("access_token", AccountManager.accessToken())
                            .build()
                    builder.url(url)
                }
                "POST" -> {
                    val formBodyBuilder = FormBody.Builder()
                    val oldBody = request.body()
                    if (oldBody is FormBody) {
                        val body = oldBody
                        for (i in 0 until body.size()) {
                            formBodyBuilder.addEncoded(body.name(i), body.value(i))
                        }
                        formBodyBuilder.addEncoded("access_token", AccountManager.accessToken())
                        builder.post(formBodyBuilder.build())
                    } else if (oldBody is MultipartBody) {

                    }
                }
            }
        }
        val newReq = builder.build()
        Log.v(TAG, "after url=${newReq.url().url()}")
        return chain.proceed(newReq)
    }
}