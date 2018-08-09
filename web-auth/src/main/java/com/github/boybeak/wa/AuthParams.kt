package com.github.boybeak.wa

import android.text.TextUtils
import java.net.URLEncoder

data class AuthParams private constructor(val clientId: String,
                                          val scope: String,
                                          val state: String,
                                          val responseType: String,
                                          val redirectUri: String) {

    override fun toString(): String {
        return StringBuilder()
                .append("client_id=").append(clientId).append('&')
                .append("scope=").append(scope).append('&')
                .append("state=").append(state).append('&')
                .append("response_type=").append(responseType).append('&')
                .append("redirect_uri=").append(URLEncoder.encode(redirectUri))
                .toString()
    }

    class Builder {

        private var clientId: String? = null
        private var scope: String? = null
        private var state: String? = null
        private var responseType: String? = null
        private var redirectUri: String? = null

        fun cliendId(clientId: String): Builder {
            this.clientId = clientId
            return this
        }

        fun scopes(vararg scopes: String): Builder {
            val ssb = StringBuilder()
            scopes.forEachIndexed { index, s ->
                if (index > 1) {
                    ssb.append(',')
                }
                ssb.append(s)
            }
            scope = ssb.toString()
            return this
        }

        fun state(state: String): Builder {
            this.state = state
            return this
        }

        fun responseType(type: String): Builder {
            this.responseType = type
            return this
        }

        fun redirectUri(uri: String): Builder {
            this.redirectUri = uri
            return this
        }

        fun build(): AuthParams {
            if (TextUtils.isEmpty(clientId)) {
                throw IllegalStateException("clientId can not be null")
            }
            if (TextUtils.isEmpty(redirectUri)) {
                throw IllegalStateException("redirectUri can not be null")
            }
            return AuthParams(clientId!!, scope!!, state!!, responseType!!, redirectUri!!)
        }

    }

}