package com.colin.library.android.widget.web

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colin.library.android.widget.web.client.DefaultWebChromeClient
import com.colin.library.android.widget.web.client.DefaultWebViewClient
import com.colin.library.android.widget.web.service.JsParam
import com.colin.library.android.widget.web.service.WebServiceConnection
import com.google.gson.Gson

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 12:46
 *
 * Des   :ScrollWebView
 */
class CustomWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : com.tencent.smtt.sdk.WebView(context, attrs, defStyleAttr), LifecycleEventObserver {

    fun bind(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun initWebClient(webViewCallBack: IWebViewCallback) {
        webViewClient = DefaultWebViewClient(webViewCallBack)
        webChromeClient = DefaultWebChromeClient(webViewCallBack)
    }

    override fun onStateChanged(
        source: LifecycleOwner, event: Lifecycle.Event
    ) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }

            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {
                onResume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                onPause()
            }

            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_DESTROY -> {
                clearCache(true)
                clearHistory()
                clearFormData()
                destroy()
            }

            Lifecycle.Event.ON_ANY -> {}
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

    }

    @JavascriptInterface
    fun takeNativeAction(json: String?) {
        if (!TextUtils.isEmpty(json)) {
            val jsParamObject: JsParam = Gson().fromJson(json, JsParam::class.java)
            WebServiceConnection.instance.executeCommand(
                jsParamObject.name, Gson().toJson(jsParamObject.json), this
            )
        }
    }


    fun handleCallback(callbackname: String, response: String?) {
        if (!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)) {
            post {
                val jscode = "javascript:myjs.callback('$callbackname',$response)"
                evaluateJavascript(jscode, null)
            }
        }
    }

    interface OnScrollChangedListener {
        fun onPageEnd(l: Int, t: Int, oldL: Int, oldT: Int)

        fun onPageTop(l: Int, t: Int, oldL: Int, oldT: Int)

        fun onScrollChanged(l: Int, t: Int, oldL: Int, oldT: Int)

        fun onTouchScreen()
    }


}