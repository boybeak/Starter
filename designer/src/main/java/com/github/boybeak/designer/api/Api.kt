package com.github.boybeak.designer.api

import com.github.boybeak.designer.api.interceptor.AuthInterceptor
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


object Api {

    private val service: ApiService
    private var authService: AuthService? = null

    init {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(interceptor)
                .build()

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.dribbble.com/v2/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        service = retrofit.create<ApiService>(ApiService::class.java)
    }

    fun service(): ApiService {
        return service
    }

    fun authService(): AuthService {
        if (authService == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .addInterceptor(interceptor)
                    .build()

            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .create()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl("https://dribbble.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            authService = retrofit.create<AuthService>(AuthService::class.java)
        }
        return authService!!
    }

}