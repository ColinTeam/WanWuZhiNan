package com.colin.library.android.network.callback

import com.colin.library.android.network.data.HttpResult

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-29 17:13
 *
 * Des   :NetworkCallback
 */
interface INetworkFeedback {
    fun onStart(show: Boolean = false, start: HttpResult.Start)
    fun onToast(show: Boolean, toast: HttpResult.Toast)
    fun onAction(action: HttpResult.Action)
    fun onFinish(show: Boolean = false, finish: HttpResult.Finish)
}

