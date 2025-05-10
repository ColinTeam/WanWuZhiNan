package com.wanwuzhinan.mingchang.net

import android.util.Log
import com.comm.net_work.base.BaseRetrofitBuilder
import com.comm.net_work.interceptor.HttpCommonInterceptor
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.getCurrentVersionName
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.utils.getToken
import okhttp3.OkHttpClient

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net
 * ClassName: RetrofitClient
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 18:50
 * Email:shiming024@163.com
 * Description:
 */
object RetrofitClient : BaseRetrofitBuilder() {

    val service by lazy {
        create(ApiService::class.java)
    }

    override fun handleOkHttpClientBuilder(builder: OkHttpClient.Builder) {

    }

    override fun handleOkHttpCommBuilder(builder: HttpCommonInterceptor.Builder) {
        val token = ConfigApp.token
        if (token.isNotEmpty()) {
            builder.addHeaderParams(Constant.TOKEN, token)
        }
        builder.addHeaderParams("version", getCurrentVersionName())
        Log.e("okhttp_request_token", getToken())
    }
}