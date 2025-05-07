package com.colin.library.android.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


object NetworkHelper {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(NetworkConfig.baseUrl).client(NetworkConfig.getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(NetworkConfig.gson))
            .callbackExecutor(Executors.newSingleThreadExecutor()).build()
    }

    /**
     * 获取 apiService
     */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}