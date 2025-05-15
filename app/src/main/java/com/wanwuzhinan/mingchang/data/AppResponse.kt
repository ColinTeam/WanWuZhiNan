package com.wanwuzhinan.mingchang.data

import com.colin.library.android.network.data.INetworkResponse
import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 15:38
 *
 * Des   :UserInfoResponse
 */

open class AppResponse<T>(
    private val data: T? = null,
    private val code: Int = Constants.INVALID,
    private val msg: String = "",
) : INetworkResponse<T> {
    override fun isSuccess() = getCode() == Constants.ZERO

    override fun getCode() = code

    override fun getMsg() = msg

    override fun getData() = data

    override fun toString(): String {
        return "AppResponse(isSuccess:${isSuccess()}:code=$code, msg='$msg\ndata=$data')"
    }


}

