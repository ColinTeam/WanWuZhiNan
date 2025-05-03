package com.colin.library.android.network.callback

import com.colin.library.android.utils.Log

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-29 17:13
 *
 * Des   :NetworkCallback
 */
interface NetworkCallback<T> {
    fun start(time: Long = System.currentTimeMillis())
    fun success(data: T)
    fun failure(code: Int, msg: String)
    fun finish(time: Long = System.currentTimeMillis())
}


val CALLBACK = object : NetworkCallback<Any> {
    override fun start(time: Long) {
        Log.i("start:$time")
    }

    override fun success(data: Any) {
        Log.i("data:$data")
    }

    override fun failure(code: Int, msg: String) {
        Log.i("code:$code\tmsg:$msg ")
    }

    override fun finish(time: Long) {
        Log.i("start:$time")
    }
}

