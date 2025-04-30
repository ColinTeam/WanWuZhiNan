package com.wanwuzhinan.mingchang.network

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-30 09:33
 *
 * Des   :INetworkResponse
 */
interface INetworkResponse<T> {
    abstract fun isSuccess(): Boolean
    abstract fun getCode(): Int
    abstract fun getMsg(): String
    abstract fun getData(): T?
}