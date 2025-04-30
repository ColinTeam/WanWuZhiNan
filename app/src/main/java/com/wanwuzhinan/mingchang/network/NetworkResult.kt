package com.wanwuzhinan.mingchang.network

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-28 13:16
 *
 * Des   :实际开发中UI层通常需要根据网络和服务器返回值处理各种异常情况，为此总结了三种状态。
 *
 * 成功：一切都如我们期待的状态
 * 失败：因为业务逻辑错误+网络和服务器原因造成的 404、502 或者 DNS 请求失败等错误状态
 */
const val HTTP_EMPTY = -1
const val HTTP_TIMEOUT = -3
const val HTTP_ERROR = -2


sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Failure(val code: Int, val msg: String) : NetworkResult<Nothing>()
    companion object {
        @JvmStatic
        fun empty() = Failure(HTTP_EMPTY, "empty")

        @JvmStatic
        fun timeout() = Failure(HTTP_TIMEOUT, "timeout")

        @JvmStatic
        fun failure(code: Int = HTTP_ERROR, msg: String) = Failure(code, msg)
    }

    override fun toString(): String {
        return when (this) {
            is Success<T> -> "Success[data=$data]"
            is Failure -> "Failure[code=$code, msg=$msg]"
        }
    }
}



