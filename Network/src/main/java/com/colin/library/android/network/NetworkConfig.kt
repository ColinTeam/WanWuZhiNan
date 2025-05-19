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
    private const val DELAY: Long = 0L
    private const val RETRY: Int = 3
    private const val TIMEOUT: Long = 10000L
    private const val URL_DEBUG: String = "https://www.mxwsl.cn"
    private const val URL_RELEASE: String = "https://app.wanwuzhinan.top"

    var gson: Gson = GsonBuilder().setLenient()
        .registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
        .registerTypeAdapter(String::class.java, StringTypeAdapter()).create()


    @Volatile
    var baseUrl: String = URL_DEBUG

    @Volatile
    var retry: Int = RETRY

    @Volatile
    var delay: Long = DELAY

    @Volatile
    var timeout: Long = TIMEOUT

    val interceptors = mutableListOf<Interceptor>()
    val networkInterceptors = mutableListOf<Interceptor>(createLoggingInterceptor())

    private fun createLoggingInterceptor() = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
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
