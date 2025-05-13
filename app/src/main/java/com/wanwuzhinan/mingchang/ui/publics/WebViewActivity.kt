package com.wanwuzhinan.mingchang.ui.publics

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.BaseActivity
import com.ssm.comm.ui.widget.webview.DefWebViewClient
import com.ssm.comm.ui.widget.webview.SafeWebChromeClient
import com.tencent.smtt.sdk.WebView
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityWebViewBinding
import com.wanwuzhinan.mingchang.vm.SplashViewModel

class WebViewActivity : BaseActivity<ActivityWebViewBinding, SplashViewModel>(SplashViewModel()) {

    override fun getLayoutId(): Int {
        return R.layout.activity_web_view
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        val bundle = getBundle()
        if (bundle != null) {
            //val appHost = com.comm.net_work.BuildConfig.APP_HOST
            val url = when (bundle.getInt(ConfigApp.EXTRAS_POSITION, Constant.OTHER_TYPE)) {
                //用户协议
                Constant.USER_AGREEMENT_TYPE -> {
                    ConfigApp.USER_AGREEMENT
                }
                //隐私政策
                Constant.PRIVACY_POLICY_TYPE -> {
                    ConfigApp.PRIVACY_POLICY
                }
                //其他
                else -> {
                    bundle.getString(ConfigApp.EXTRAS_URL, "")
                }
            }
            if (isEmpty(url)) {
                toastError("h5地址不能为空")
                this.finish()
                return
            }
            Log.e("TAG", "initView: " + url)
            val string = bundle.getString(ConfigApp.EXTRAS_TITLE, "")
            if (isEmpty(string)) {
                mDataBinding.toolbar.setTitleText("")
            } else {
                mDataBinding.toolbar.setTitleText(string)
            }
            val mWebChromeClient =
                SafeWebChromeClient(mDataBinding.toolbar, mDataBinding.progressbar)
            val mWebViewClient =
                DefWebViewClient(mDataBinding.progressbar, object : DefWebViewClient.OnCallback {
                    override fun onInterceptUrl(webView: WebView?, url: String?): Boolean {
//                    if (!isEmpty(url)){
//                        LogUtils.e("interceptUrl=============>${url}")
//                        val isWXPay = url!!.contains("weixin") || url.contains("weixin://wap/pay")
//                        val isAliPay = url.contains("alipay://alipayclient") || url.contains("alipays://platformapi")
//                        if (isWXPay){
//                            LogUtils.e("interceptUrl=============>微信支付")
//                            launchPay(url,true)
//                            return true
//                        }else if(isAliPay){
//                            launchPay(url,false)
//                            LogUtils.e("interceptUrl=============>支付宝支付")
//                            return true
//                        }
//                    }
                        return false
                    }

                    override fun onFinished(webView: WebView?, url: String?) {
                        mDataBinding.webView.loadUrl(
                            "javascript:(function(){" + "var objs = document.getElementsByTagName('img'); " + "for(var i=0;i<objs.length;i++) " + "{" + "var img = objs[i]; " + " img.style.maxWidth = '100%'; img.style.height = 'auto'; " + "}" + "})()"
                        );
                    }
                })
            mDataBinding.webView.settings.javaScriptEnabled = true
            mDataBinding.webView.webChromeClient = mWebChromeClient
            mDataBinding.webView.webViewClient = mWebViewClient
            mDataBinding.webView.loadUrl(url)

            // mDataBinding.webView.addJavascriptInterface(JsBridge(), "android")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDataBinding.webView.canGoBack()) {
            // 返回前一个页面
            mDataBinding.webView.goBack()
            //finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        mDataBinding.webView.webViewOnPause()
    }

    override fun onResume() {
        super.onResume()
        mDataBinding.webView.webViewOnResume()
    }

    override fun onDestroy() {
        mDataBinding.webView.webViewOnDestroy()
        super.onDestroy()
    }
}