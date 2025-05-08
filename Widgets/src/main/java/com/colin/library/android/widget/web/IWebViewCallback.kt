package com.colin.library.android.widget.web

import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 12:39
 *
 * Des   :WebViewCallback
 */
interface IWebViewCallback {
    fun pageStarted(url: String?)

    fun pageProgress(progress: Int)

    fun pageFinished(url: String?)

    fun pageError(url: String, error: String)

    fun updateTitle(title: String?)

    fun overrideUrlLoading(view: WebView, url: WebResourceRequest?): Boolean
}