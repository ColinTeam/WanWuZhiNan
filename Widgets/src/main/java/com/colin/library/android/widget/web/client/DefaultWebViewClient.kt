package com.colin.library.android.widget.web.client

import android.graphics.Bitmap
import com.colin.library.android.widget.web.IWebViewCallback
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * @author : dly on 2023/4/26
 *
 * desc :
 */
class DefaultWebViewClient(private val callback: IWebViewCallback) : WebViewClient() {


    override fun onPageFinished(view: WebView, url: String?) {
        callback.finished(url)
    }

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        callback.start(url)
    }

    override fun onReceivedError(
        view: WebView, request: WebResourceRequest?, error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        callback.error("${request?.url}", "$error")
    }

    /**
     * url重定向会执行此方法以及点击页面某些链接也会执行此方法
     *
     * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载
     *          false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     */
    override fun shouldOverrideUrlLoading(
        view: WebView, request: WebResourceRequest?
    ): Boolean {
        return callback.intercept(view, request?.url?.toString()) ||
                super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        return callback.intercept(view, url) || super.shouldOverrideUrlLoading(view, url)
    }

    // WebView发生改变时调用
    override fun onScaleChanged(view: WebView, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
    }


}