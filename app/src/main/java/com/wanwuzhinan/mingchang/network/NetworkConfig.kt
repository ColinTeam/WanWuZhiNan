package com.wanwuzhinan.mingchang.network

import com.comm.net_work.BuildConfig
import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import java.util.concurrent.TimeUnit

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-25 23:49
 *
 * Des   :NetworkConfig
 */
object NetworkConfig {
    var gson: Gson = Gson()

    var baseUrl = if (BuildConfig.DEBUG) BuildConfig.APP_HOST else BuildConfig.APP_HOST
        get() = field
        set(value) {
            field = value
        }
    var retry = 3
    var delay = 500L

    var timeout = 1000L

    var httpClient = OkHttpClient.Builder().callTimeout(timeout, TimeUnit.MILLISECONDS)
        .connectTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS)
        .writeTimeout(timeout, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build()


    /**
     * 配置日志拦截器
     */
    var loggingInterceptor: LoggingInterceptor =
        LoggingInterceptor.Builder()
            .setLevel(if (BuildConfig.DEBUG) Level.BASIC else Level.HEADERS)
            .log(Platform.INFO)
            .tag("Colin-->>")
            .request("Colin-->>Request")
            .response("Colin-->>Response")
            .build()

}