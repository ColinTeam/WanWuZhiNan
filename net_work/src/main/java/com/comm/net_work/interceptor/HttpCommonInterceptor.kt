package com.comm.net_work.interceptor

import android.util.Log
import com.comm.net_work.sign.Md5Utils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.interceptor
 * ClassName: HttpCommonInterceptor
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 13:36
 * Email:shiming024@163.com
 * Description:
 */
class HttpCommonInterceptor : Interceptor {

    private var mHeaderParamsMap = HashMap<String, String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val builder = oldRequest.newBuilder()
        // builder.removeHeader("Cache-Control");
        builder.method(oldRequest.method, oldRequest.body)
        //添加公共参数到header中
        if (mHeaderParamsMap.size > 0) {
            mHeaderParamsMap.forEach { (key, value) ->
                if (key == "token"){
                    val time = System.currentTimeMillis() / 1000
                    builder.header("token_time","${time}")
                    val md5 = Md5Utils.md5("${value}${time}wwzn")
                    builder.header("token_verify",md5)
                }
                builder.header(key, value)
            }
        }
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }

    data class Builder(
        var mInterceptor: HttpCommonInterceptor,
    ) {

        fun addHeaderParams(key: String, value: String) = apply {
            mInterceptor.mHeaderParamsMap[key] = value
        }

        fun addHeaderParams(key: String, value: Int) = apply {
            mInterceptor.mHeaderParamsMap[key] = value.toString()
        }

        fun addHeaderParams(key: String, value: Float) = apply {
            mInterceptor.mHeaderParamsMap[key] = value.toString()
        }

        fun addHeaderParams(key: String, value: Double) = apply {
            mInterceptor.mHeaderParamsMap[key] = value.toString()
        }

        fun addHeaderParams(key: String, value: Long) = apply {
            mInterceptor.mHeaderParamsMap[key] = value.toString()
        }

        fun build() = HttpCommonInterceptor()
    }

}