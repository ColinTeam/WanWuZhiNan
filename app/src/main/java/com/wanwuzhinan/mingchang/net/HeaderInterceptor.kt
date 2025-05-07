package com.wanwuzhinan.mingchang.net

import com.colin.library.android.utils.encrypt.EncryptUtil
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.getCurrentVersionName
import com.wanwuzhinan.mingchang.utils.getToken
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-07 13:24
 *
 * Des   :HeaderInterceptor
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.method(request.method, request.body)
        val token = getToken()
        if (token.isNotEmpty()) {
            val time = System.currentTimeMillis() / 1000
            builder.header(Constant.TOKEN,token)
            builder.header("token_time", "$time")
            builder.header("token_verify", EncryptUtil.md5("$token${time}wwzn"))
        }
        builder.header(Constant.VERSION, getCurrentVersionName())
        return chain.proceed(builder.build())
    }
}