package com.ssm.comm.ui.widget.webview

import android.widget.ProgressBar
import com.ssm.comm.ui.widget.webview.base.BaseWebViewClient
import com.tencent.smtt.sdk.WebView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.webview
 * ClassName: DefWebViewClient
 * Author:ShiMing Shi
 * CreateDate: 2022/9/15 12:24
 * Email:shiming024@163.com
 * Description:
 */
open class DefWebViewClient constructor(
    progressBar: ProgressBar?,
    callBack: OnCallback
) : BaseWebViewClient(progressBar) {

    private var callBack: OnCallback

    init {
        this.callBack = callBack
    }

    override fun onInterceptUrl(webView: WebView?, url: String?): Boolean {
        return callBack.onInterceptUrl(webView, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        callBack.onFinished(view, url)
    }

    interface OnCallback{
        fun onInterceptUrl(webView: WebView?, url: String?): Boolean
        fun onFinished(webView: WebView?, url: String?)
    }

}