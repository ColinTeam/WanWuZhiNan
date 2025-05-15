package com.colin.library.android.network.callback

import com.colin.library.android.network.data.NetworkResult

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-29 17:13
 *
 * Des   :NetworkCallback
 */
interface NetworkCallback {
    fun showLoading(loading: Boolean)
    fun showToast(failure: NetworkResult.Failure)
    fun postError(type: Int, error: String)
}

