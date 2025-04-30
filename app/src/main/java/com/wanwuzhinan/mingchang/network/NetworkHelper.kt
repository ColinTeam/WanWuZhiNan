package com.wanwuzhinan.mingchang.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


object NetworkHelper {
    val retrofit by lazy {
        Retrofit.Builder().baseUrl(NetworkConfig.baseUrl).client(NetworkConfig.httpClient)
            .addConverterFactory(GsonConverterFactory.create(NetworkConfig.gson))
            .callbackExecutor(Executors.newSingleThreadExecutor()).build()
    }

    /**
     * 获取 apiService
     */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}

