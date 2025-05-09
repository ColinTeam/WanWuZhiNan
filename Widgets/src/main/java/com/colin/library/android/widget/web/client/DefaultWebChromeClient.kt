package com.colin.library.android.widget.web.client

import com.colin.library.android.utils.Log
import com.colin.library.android.widget.web.IWebViewCallback
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

/**
 * @author : dly on 2023/4/26
 *
 * desc :
 */
class DefaultWebChromeClient(private val webViewCallBack: IWebViewCallback) : WebChromeClient() {


    override fun onReceivedTitle(view: WebView, title: String?) {
        webViewCallBack.title(title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        webViewCallBack.progress(newProgress)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d("onConsoleMessage: ${consoleMessage?.message()}")
        return super.onConsoleMessage(consoleMessage)
    }


    override fun onJsAlert(
        view: WebView?, url: String?, message: String?, result: JsResult?
    ): Boolean {
        return super.onJsAlert(view, url, message, result)
    }
}