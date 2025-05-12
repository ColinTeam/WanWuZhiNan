package com.comm.net_work.base

import com.comm.net_work.BuildConfig
import com.comm.net_work.interceptor.HttpCommonInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.platform.Platform
import java.util.concurrent.TimeUnit

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net
 * ClassName: BaseRetrofitBuilder
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 16:33
 * Email:shiming024@163.com
 * Description:Retrofit构建器-基类做了一些基本的配置，子类继承后可以添加新的配置，并配置自己喜欢的日志拦截器
 *      buildConfigField("String", "IMG_HOST", "\"http://images.smart0006.cn/\"")
 *         buildConfigField("String", "APP_AES_KEY", "\"forceGFpT66AjkTM\"")
 *         buildConfigField("String", "APP_AES_IV", "\"force2022501luck\"")
 *         buildConfigField("String", "APP_AES_MODEL", "\"AES/CBC/PKCS5Padding\"")
 *         buildConfigField("String", "APP_API_SIGNATURE", "\"G5D*Y2\$FDdf4gf1576SD\"")
 */
abstract class BaseRetrofitBuilder {

    private var builder: OkHttpClient.Builder? = null

    companion object {
        private const val TIME_OUT_LENGTH = 90L
        const val LOG_TAG_HTTP_REQUEST = "okhttp_request"
        const val LOG_TAG_HTTP_RESULT = "okhttp_result"
    }

    private var mInterceptorBuilder: HttpCommonInterceptor.Builder? = null

    private val okHttpClient: OkHttpClient by lazy {
        builder = OkHttpClient.Builder()
            .callTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        initLoggingInterceptor().also {
            builder!!.addInterceptor(it)
        }

//        this.mInterceptorBuilder = initHttpComBuilder()
        addBuilderInterceptor(mInterceptorBuilder?.mInterceptor!!)
        handleOkHttpClientBuilder(builder!!)
        builder!!.build()
    }




    /**
     * 子类自定义 OKHttpClient 的配置
     */
    abstract fun handleOkHttpClientBuilder(builder: OkHttpClient.Builder)


    /**
     * 子类添加公共参数
     */
    abstract fun handleOkHttpCommBuilder(builder: HttpCommonInterceptor.Builder)

    /**
     * 添加公共参数
     */
    fun addOkHttpCommBuilder(key: String = "", value: String = "") {
        if (mInterceptorBuilder != null) {
            mInterceptorBuilder!!.addHeaderParams(key, value)
        }
    }

    fun removeOkHttpCommBuilder(key: String = "") {
        if (mInterceptorBuilder != null) {
            mInterceptorBuilder!!.addHeaderParams(key, "")
        }
    }

    /**
     * 异步GET请求
     */
    fun performSyncRequestGET(url: String = "", callBack: Callback) {
        val request = Request.Builder().url(url).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(callBack)
    }

    /**
     * 配置日志拦截器
     */
    private fun initLoggingInterceptor(): Interceptor {
        val builder = LoggingInterceptor.Builder()
        if (BuildConfig.DEBUG) {
            builder.setLevel(Level.BASIC)
        } else {
            builder.setLevel(Level.BASIC)
        }
        builder.log(Platform.INFO)
        builder.request(LOG_TAG_HTTP_REQUEST)
        builder.response(LOG_TAG_HTTP_RESULT)
        return builder.build()
    }

    /**
     * 配置header参数添加拦截器
     */
    private fun initHttpComBuilder(): HttpCommonInterceptor.Builder {
        val mInterceptor = HttpCommonInterceptor()
        val builder = HttpCommonInterceptor.Builder(mInterceptor)
        handleOkHttpCommBuilder(builder)
        return builder
    }

    open fun addBuilderInterceptor(interceptor: Interceptor) {
        builder?.addInterceptor(interceptor)
    }
}