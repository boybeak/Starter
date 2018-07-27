package com.github.boybeak.designer.api

import com.github.boybeak.designer.api.model.AuthInfo
import com.github.boybeak.designer.api.model.Shot
import com.github.boybeak.designer.api.model.User

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("popular_shots")
    fun getPopularShots(): Call<List<Shot>>

    /*@Headers(
            "Content-Type: application/json; charset=utf-8"
    )*/
    @GET("user/shots")
    fun getShots(): Call<List<Shot>>

    @GET("user")
    fun getMe(): Call<User>
}
