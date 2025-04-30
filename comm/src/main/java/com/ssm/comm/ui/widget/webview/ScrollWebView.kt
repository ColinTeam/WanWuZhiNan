package com.ssm.comm.ui.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.ssm.comm.utils.LogUtils
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import kotlin.math.abs

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.webview
 * ClassName: ScrollWebView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/15 11:58
 * Email:shiming024@163.com
 * Description:
 */
class ScrollWebView : WebView {

    private var listener: OnScrollChangeListener? = null

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context, attrs)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(context: Context, attrs: AttributeSet?) {
        // 清缓存和记录，缓存引起的白屏
        this.clearCache(true)
        this.clearHistory()
        this.requestFocus()
        this.overScrollMode = OVER_SCROLL_NEVER
        //添加js监听 这样html就能调用客户端
        this.addJavascriptInterface(this, "android")
        //是否可以后退
        this.canGoBack()
        //是否可以前进
        this.canGoForward()
        this.scrollBarStyle = SCROLLBARS_INSIDE_OVERLAY

        val webSettings = settings
        //允许使用js
        webSettings.javaScriptEnabled = true
        //支持通过JS打开新窗口
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        //将图片调整到适合webView的大小 useWideViewPort
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        //setLayoutAlgorithm
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        //设置WebView中加载页面字体变焦百分比，默认100
        webSettings.textZoom = 100
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        var userAgent = webSettings.userAgentString
        userAgent = userAgent.replace("MQQBrowser/6.2", "")
        LogUtils.e("webView userAgent==============>$userAgent")
        //webSettings.setUserAgent(userAgent)

        //缩放操作
        //支持缩放，默认为true。是下面那个的前提
        webSettings.setSupportZoom(true)
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.builtInZoomControls = true
        //不显示webView缩放按钮
        webSettings.displayZoomControls = false
        //设置编码格式
        webSettings.defaultTextEncodingName = "utf-8"
        //支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        //设置可以访问文件
        webSettings.allowFileAccess = true

        //缓存
        //是否启用缓存模式
        webSettings.setAppCacheEnabled(true)
        //设置缓存模式
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //设置最大缓存
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE)
        //是否开启数据库缓存
        webSettings.databaseEnabled = true
        //是否开启DOM缓存
        webSettings.domStorageEnabled = true

        webSettings.setGeolocationEnabled(true)
        webSettings.pluginState = WebSettings.PluginState.ON_DEMAND
    }


    //激活 js 调用，设置 webView 活跃状态
    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun webViewOnResume(){
        this.onResume()
        this.settings.javaScriptEnabled = true
    }

    //退出界面暂停 webView的活跃，并且关闭 JS 支持
    @Suppress("DEPRECATION")
    fun webViewOnPause(){
        this.onPause()
        this.settings.lightTouchEnabled = false
    }

    //关闭界面时，销毁webview
    fun webViewOnDestroy(){
        this.clearCache(true)
        this.clearHistory()
        this.clearFormData()
        this.destroy()
    }

    @Suppress("DEPRECATION")
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val webContent = contentHeight * scale
        val webNow = height + scaleY
        if (abs(webContent - webNow) < 1) {
            // 已经处于底端
            if (listener != null) {
                listener!!.onPageEnd(l, t, oldl, oldt)
            }
        } else if (scrollY == 0) {
            //已经处于顶端
            if (listener != null) {
                listener!!.onPageTop(l, t, oldl, oldt)
            }
        } else{
            //正在滑动
            if (listener != null) {
                listener!!.onScrollChanged(l, t, oldl, oldt)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            //按下
            if (listener != null) {
                listener!!.onTouchScreen()
            }
        }
        return super.onTouchEvent(event)
    }

    //滚动到底部
    fun scrollToBottom() {
        scrollTo(0, computeVerticalScrollRange())
    }

    //滚动到顶部
    fun scrollToTop() {
        scrollTo(0, 0)
    }

    fun setOnScrollChangeListener(listener: OnScrollChangeListener) {
        this.listener = listener
    }

    interface OnScrollChangeListener {
        fun onPageEnd(l: Int, t: Int, oldl: Int, oldt: Int)

        fun onPageTop(l: Int, t: Int, oldl: Int, oldt: Int)

        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)

        fun onTouchScreen()
    }

}