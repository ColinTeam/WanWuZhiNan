package com.wanwuzhinan.mingchang.ui.publics

import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityWebViewBinding
import com.wanwuzhinan.mingchang.vm.SplashViewModel
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.BaseActivity
import com.ssm.comm.ui.widget.webview.DefWebViewClient
import com.ssm.comm.ui.widget.webview.SafeWebChromeClient
import com.tencent.smtt.sdk.WebView
import com.wanwuzhinan.mingchang.ui.pop.EditAddressDialog
import com.wanwuzhinan.mingchang.vm.UserViewModel

class WebGoodsViewActivity : BaseActivity<ActivityWebViewBinding, UserViewModel>(UserViewModel()) {

    override fun getLayoutId(): Int {
        return R.layout.activity_web_view
    }

    override fun initView() {
        val bundle = getBundle()
        if (bundle != null) {
            //val appHost = com.comm.net_work.BuildConfig.APP_HOST
            val goodsID = bundle.getString(Constant.GOODS_ID,"")
            mDataBinding.tvSure.visibility = View.VISIBLE

            Log.e("TAG", "initView: "+goodsID )

            mViewModel.goodsInfo(goodsID)

            mViewModel.goodsInfoLiveData.observeState(this){
                onSuccess = { data, msg ->
                    mDataBinding.toolbar.setTitleText(data!!.info!!.title)
                    val mWebChromeClient =
                        SafeWebChromeClient(mDataBinding.toolbar, mDataBinding.progressbar)
                    val mWebViewClient =
                        DefWebViewClient(mDataBinding.progressbar, object : DefWebViewClient.OnCallback {
                            override fun onInterceptUrl(webView: WebView?, url: String?): Boolean { return false
                            }

                            override fun onFinished(webView: WebView?, url: String?) {

                            }
                        })
                    mDataBinding.webView.getSettings().javaScriptEnabled=true
                    mDataBinding.webView.webChromeClient = mWebChromeClient
                    mDataBinding.webView.webViewClient = mWebViewClient
                    mDataBinding.webView.loadData(data!!.info!!.content,"text/html", "UTF-8")
                }
            }

        }
    }

    override fun initClick() {

        setOnClickNoRepeat(mDataBinding.tvSure){
            when(it){
                mDataBinding.tvSure -> {
                    EditAddressDialog().show(
                        getCurrentActivity().supportFragmentManager,
                        "EditAddressDialog"
                    )
                }
            }
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