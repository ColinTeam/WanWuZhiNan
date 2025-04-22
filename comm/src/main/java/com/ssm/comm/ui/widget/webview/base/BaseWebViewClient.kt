package com.ssm.comm.ui.widget.webview.base

import android.graphics.Bitmap
import android.view.View
import android.widget.ProgressBar
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.webview.base
 * ClassName: BaseWebViewClient
 * Author:ShiMing Shi
 * CreateDate: 2022/9/15 12:14
 * Email:shiming024@163.com
 * Description:
 */
abstract class BaseWebViewClient constructor(progressBar: ProgressBar?): WebViewClient(){

    private var mProgressBar: ProgressBar? = null

    init{
        this.mProgressBar = progressBar
    }


    //页面开始加载
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        //获取Cookie
        val instance = CookieManager.getInstance()
        val cookie = instance.getCookie(url)
        mProgressBar?.visibility = View.VISIBLE
    }

    //WebView 加载页面资源时会回调，每一个资源产生的一次网络加载，
    // 除非本地有当前 url 对应有缓存，否则就会加载。
    override fun onLoadResource(webView: WebView?, url: String?) {
        super.onLoadResource(webView, url)
    }

    //页面加载完成
    override fun onPageFinished(view: WebView?, url: String?) {
        mProgressBar?.visibility = View.GONE
    }


    //页面加载错误
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?,
    ) {
        super.onReceivedError(view, request, error)
    }

    //访问证书出错，handler.cancel()取消加载，handler.proceed()对错误也继续加载。
    override fun onReceivedSslError(
        webView: WebView?,
        sslErrorHandler: SslErrorHandler,
        sslError: SslError?,
    ) {
        //https忽略证书问题
        sslErrorHandler.proceed()
    }

    //以拦截某一次的 request 来返回我们自己加载的数据
    override fun shouldInterceptRequest(
        webView: WebView?,
        webResourceRequest: WebResourceRequest?,
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(webView, webResourceRequest)
    }

    //回调拦截 url
    //该方法会在webview加载一个url之前，拦截请求
    //如果未使用自定义的WebViewClient，系统会要求Activity Manager去处理当前URL，通常使用系统浏览器加载
    //如果使用了自定义的WebViewClient，返回true表示当前webview中止加载url（效果就像比如点击一个a标签链接没反应），
    // 返回false表示webview正常继续加载url
    //
    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        return onInterceptUrl(webView, url)
    }


    protected abstract fun onInterceptUrl(webView: WebView?, url: String?): Boolean


    //以拦截某一次的 request 来返回我们自己加载的数据
    override fun shouldInterceptRequest(webView: WebView?, url: String?): WebResourceResponse? {
        return super.shouldInterceptRequest(webView, url)
    }

}