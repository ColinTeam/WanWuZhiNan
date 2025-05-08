package com.colin.library.android.widget.web

import com.tencent.smtt.sdk.WebView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 12:39
 *
 * Des   :WebViewCallback
 */
interface IWebViewCallback {
    fun start(url: String?)

    fun progress(progress: Int)

    fun finished(url: String?)

    fun error(url: String, error: String)

    fun title(title: String?)

    fun intercept(view: WebView, url: String?): Boolean
}