package com.colin.library.android.network

import com.colin.library.android.network.gson.IntegerTypeAdapter
import com.colin.library.android.network.gson.StringTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-25 23:49
 *
 * Des   :NetworkConfig
 */
object NetworkConfig {
    var gson: Gson =
        GsonBuilder().setLenient().registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
            .registerTypeAdapter(String::class.java, StringTypeAdapter())
            .create()

    var baseUrl: String = if (BuildConfig.DEBUG) BuildConfig.URL_DEBUG else BuildConfig.URL_RELEASE

    var retry: Int = BuildConfig.RETRY

    var delay: Long = BuildConfig.DELAY

    var timeout: Long = BuildConfig.TIMEOUT

    var interceptors = mutableListOf<Interceptor>()
    var networkInterceptors = mutableListOf<Interceptor>(createLoggingInterceptor())

    private fun createLoggingInterceptor() = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
    )

    fun addInterceptor(interceptor: Interceptor) = apply {
        if (!interceptors.contains(interceptor)) interceptors.add(interceptor)
    }

    fun addNetworkInterceptors(interceptor: Interceptor) = apply {
        if (!networkInterceptors.contains(interceptor)) networkInterceptors.add(interceptor)
    }

    fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().callTimeout(timeout, TimeUnit.MILLISECONDS)
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)
            .readTimeout(timeout, TimeUnit.MILLISECONDS)
            .writeTimeout(timeout, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true)
        interceptors.forEach {
            builder.addInterceptor(it)
        }
        networkInterceptors.forEach {
            builder.addNetworkInterceptor(it)
        }
        return builder.build()
    }
}