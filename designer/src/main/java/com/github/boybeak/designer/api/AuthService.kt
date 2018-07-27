package com.github.boybeak.designer.api

import com.github.boybeak.designer.api.model.AuthInfo
import com.github.boybeak.designer.api.model.Shot

import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    @FormUrlEncoded
    @POST("oauth/token")
    fun getAuthInfo(@Field("client_id") id: String,
                    @Field("client_secret") secret: String,
                    @Field("code") code: String,
                    @Field("redirect_uri") redirectUri: String): Call<AuthInfo>

}
