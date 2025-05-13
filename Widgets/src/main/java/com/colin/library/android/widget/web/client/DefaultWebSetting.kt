package com.colin.library.android.widget.web.client

import android.annotation.SuppressLint
import android.content.Context
import com.colin.library.android.utils.Log
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

/**
 * @author : dly on 2023/4/26
 *
 * desc :
 */
object DefaultWebSetting {

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun updateSetting(view: WebView, agent: String = view.settings.userAgentString) {
        view.settings.apply {
            var userAgent = agent.replace("MQQBrowser/6.2", "")
            Log.d("webView old==============>$agent")
            Log.d("webView new==============>$userAgent")
            this.userAgentString = userAgent
            // 设置WebView是否允许执行JavaScript脚本，默认false，不允许。
            this.javaScriptEnabled = true
            //支持通过JS打开新窗口
            this.javaScriptCanOpenWindowsAutomatically = true
            //将图片调整到适合webView的大小 useWideViewPort
            this.useWideViewPort = true
            this.loadWithOverviewMode = true
            //不显示webView缩放按钮
            this.displayZoomControls = false
            // 设置setBuiltInZoomControls(boolean)可以使用特殊的缩放机制
            this.builtInZoomControls = false
            // 是否支持使用屏幕上的缩放控件和手势进行缩放
            this.setSupportZoom(true)
            // 设置页面文本的尺寸,百分比，默认NORMAL
            this.textZoom = 100
            //支持自动加载图片
            this.loadsImagesAutomatically = true
            //设置可以访问文件
            this.allowFileAccess = true
            //设置缓存模式
            this.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            //是否开启DOM缓存
            this.domStorageEnabled = true
            // 数据库存储API是否可用
            this.databaseEnabled = true
            // 缓存路径
            this.databasePath = view.context.getDir("web_database", Context.MODE_PRIVATE).path
            // 应用缓存API是否可用,结合setAppCachePath(String)使用
            this.setAppCacheEnabled(true)
            this.setAppCachePath(view.context.getDir("web", Context.MODE_PRIVATE).path)
            // 设置应用缓存内容的最大值
            this.setAppCacheMaxSize(1024 * 1024 * 80.toLong())
            // WebView是否下载图片资源，默认为true
            this.loadsImagesAutomatically = true
            // 设置WebView是否支持多窗口
            this.setSupportMultipleWindows(false)
            //是否阻塞加载网络图片  协议http or https
            this.blockNetworkImage = false
            //允许加载本地文件html  file协议
            this.allowFileAccess = true
            //将图片调整到适合webView的大小 useWideViewPort
            this.useWideViewPort = true
            // 是否允许WebView度超出以概览的方式载入页面
            this.loadWithOverviewMode = true
            this.setNeedInitialFocus(true)
            //设置编码格式
            this.defaultTextEncodingName = "utf-8"
            this.defaultFontSize = 16
            //设置 WebView 支持的最小字体大小，默认为 8
            this.minimumFontSize = 10
            // 定位是否可用
            this.setGeolocationEnabled(true)
            //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            this.setAllowFileAccessFromFileURLs(false)
            //允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            this.setAllowUniversalAccessFromFileURLs(false)
            this.pluginState = WebSettings.PluginState.ON_DEMAND
            this.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        }
    }

}