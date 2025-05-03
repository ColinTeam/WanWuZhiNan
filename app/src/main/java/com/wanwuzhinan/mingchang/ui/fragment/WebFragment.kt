package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.Log
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.widget.webview.DefWebViewClient
import com.ssm.comm.ui.widget.webview.SafeWebChromeClient
import com.tencent.smtt.sdk.WebView
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentWebBinding
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-24 19:05
 *
 * Des   :WebFragment
 */
class WebFragment() : AppFragment<FragmentWebBinding, HomeViewModel>() {
    var webType: Int = -1
    var url: String? = null
    var title: String? = null
    var content: String? = null
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        webType = bundle?.getInt(Constant.URL_TYPE) ?: -1
        url = bundle?.getString(Constant.H5_URL)
        title = bundle?.getString(Constant.WEB_TITLE)
        content = bundle?.getString(Constant.WEB_CONTENT)
        Log.i("webType:$webType url:$url title:$title content:$content")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        val url = getWebUrl()
        Log.i("url:$url")
        if (isEmpty(url)) {
            toastError("h5地址不能为空")
            findNavController().popBackStack()
            return
        }
        viewBinding.webView.apply {
            settings.javaScriptEnabled = true
            webChromeClient = SafeWebChromeClient(viewBinding.toolbar, viewBinding.progressbar)
            webViewClient =
                DefWebViewClient(viewBinding.progressbar, object : DefWebViewClient.OnCallback {
                    override fun onInterceptUrl(
                        webView: WebView?, url: String?
                    ): Boolean {
                        return false
                    }

                    override fun onFinished(
                        webView: WebView?, url: String?
                    ) {
                        this@apply.loadUrl(
                            "javascript:(function(){" + "var objs = document.getElementsByTagName('img'); " + "for(var i=0;i<objs.length;i++) " + "{" + "var img = objs[i]; " + " img.style.maxWidth = '100%'; img.style.height = 'auto'; " + "}" + "})()"
                        );
                    }

                })
            loadUrl(url)
        }
    }

    override fun onPause() {
        viewBinding.webView.webViewOnPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.webView.webViewOnResume()
    }

    override fun onDestroyView() {
        viewBinding.webView.webViewOnDestroy()
        super.onDestroyView()
    }

    private fun getWebUrl(): String? {
        return if (url.isNullOrEmpty()) {
            return when (webType) {
                Constant.USER_AGREEMENT_TYPE -> ConfigApp.USER_AGREEMENT
                Constant.PRIVACY_POLICY_TYPE -> ConfigApp.PRIVACY_POLICY
                else -> null
            }
        } else url
    }

    companion object {
        fun navigate(
            fragment: Fragment,
            type: Int = Constant.OTHER_TYPE,
            url: String? = null,
            title: String? = null,
            content: String? = null
        ) {
            fragment.findNavController().apply {
                navigate(
                    R.id.action_toWeb, Bundle().apply {
                        putInt(Constant.URL_TYPE, type)
                        url?.let { putString(Constant.H5_URL, it) }
                        title?.let { putString(Constant.WEB_TITLE, it) }
                        content?.let { putString(Constant.WEB_CONTENT, it) }
                    }, NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true).build()
                )
            }
        }
    }

}