package com.wanwuzhinan.mingchang.data

import com.google.gson.annotations.SerializedName
import com.wanwuzhinan.mingchang.network.INetworkResponse

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-29 14:00
 *
 * Des   :NetworkResponse
 */
open class AppResponse<T>(
    open val code: Int = -1,
    open val msg: String = "",
    @SerializedName("data") open val data: T? = null
) : INetworkResponse<T> {
    override fun isSuccess() = code == 0

    override fun getCode() = code

    override fun getMsg() = msg

    override fun getData() = data

}