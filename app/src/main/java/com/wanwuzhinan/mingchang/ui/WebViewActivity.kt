package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.OVER_SCROLL_NEVER
import android.view.View.SCROLLBARS_INSIDE_OVERLAY
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.widget.web.IWebViewCallback
import com.colin.library.android.widget.web.client.DefaultWebSetting
import com.tencent.smtt.sdk.WebView
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentWebBinding
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 21:57
 *
 * Des   :WebViewActivity
 */
class WebViewActivity : AppActivity<FragmentWebBinding, HomeViewModel>() {
    var url: String? = null
    var title: String? = null
    var html: String? = null
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) parse(savedInstanceState) else parse(bundle)
        viewBinding.web.apply {
            this.bind(lifecycle)
            this.scrollBarStyle = SCROLLBARS_INSIDE_OVERLAY
            this.overScrollMode = OVER_SCROLL_NEVER
            DefaultWebSetting.updateSetting(this)
            //添加js监听 这样html就能调用客户端
            this.addJavascriptInterface(this,"android")
            this.initWebClient(callback)
        }
    }

    //重新打开
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            parse(it.extras)
            loadData(title, url, html)
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        loadData(title, url, html)
    }


    override fun goBack(): Boolean {
        return if (viewBinding.web.canGoBack()) {
            viewBinding.web.goBack()
            true
        } else super.goBack()
    }


    val callback = object : IWebViewCallback {
        override fun start(url: String?) {
            viewBinding.progressbar.visible(true)
        }

        override fun progress(progress: Int) {
            viewBinding.progressbar.progress = progress
        }

        override fun finished(url: String?) {
            viewBinding.progressbar.visible(false)
            viewBinding.web.loadUrl(
                "javascript:(function(){" + "var objs = document.getElementsByTagName('img'); " + "for(var i=0;i<objs.length;i++) " + "{" + "var img = objs[i]; " + " img.style.maxWidth = '100%'; img.style.height = 'auto'; " + "}" + "})()"
            );
        }

        override fun error(url: String, error: String) {
            Log.e("url:$url error:$error")
            ToastUtil.show(error)
        }


        override fun title(title: String?) {
            viewBinding.tvTitle.text = title ?: ""
        }

        override fun intercept(view: WebView, url: String?): Boolean {
            Log.i("url:$url")
            return false
        }

    }


    private fun loadData(title: String?, url: String?, html: String?) {
        Log.i("title:$title url:$url html:$html")
        viewBinding.tvTitle.text = title ?: ""
        if (!url.isNullOrEmpty()) {
            viewBinding.web.loadUrl(url)
            return
        }
        if (!html.isNullOrEmpty()) {
            viewBinding.web.loadUrl(html)
            return
        }
        ToastUtil.show("h5地址不能为空")
        goBack()
    }

    private fun parse(bundle: Bundle?) {
        url = bundle?.getString(ConfigApp.EXTRAS_URL)
        title = bundle?.getString(ConfigApp.EXTRAS_TITLE)
        html = bundle?.getString(ConfigApp.EXTRAS_HTML)
        Log.i("url:$url title:$title html:$html")
    }


    companion object {
        @JvmStatic
        fun start(
            activity: Activity?, url: String? = null, title: String? = null, html: String? = null
        ) {
            activity ?: return
            val bundle = Bundle().apply {
                url?.let { putString(ConfigApp.EXTRAS_URL, it) }
                title?.let { putString(ConfigApp.EXTRAS_TITLE, it) }
                html?.let { putString(ConfigApp.EXTRAS_HTML, it) }
            }
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtras(bundle))
        }
    }

}