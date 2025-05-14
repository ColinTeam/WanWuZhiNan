package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.getCurrentVersionCode
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityHomeBinding
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import com.wanwuzhinan.mingchang.vm.HomeViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 21:57
 *
 * Des   :HomeActivity
 */
class HomeActivity : AppActivity<ActivityHomeBinding, HomeViewModel>() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    var needUpdate = false

    /**
     * 重新启动 一般需要跳转到制定界面如Login
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent:$intent")
        loadData(true)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.nav_host)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        if (bundle == null) navController.navigate(R.id.action_toSplash)
        viewBinding.apply {
            onClick(ivAd, ivAdclose) {
                when (it) {
                    ivAd -> {
                        val url = viewModel.getConfigValue()?.info?.home_ad_link ?: return@onClick
                        WebViewActivity.start(
                            this@HomeActivity, url = url, title = getString(R.string.home_detail)
                        )
                    }

                    ivAdclose -> {
                        viewModel.updateAD(true)
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            closeAD.observe {
                Log.i("closeAD:$it")
                changeADState(!it, getConfigValue())
            }
            configData.observe {
                Log.i("configData:$it")
                updateTips(it.info)
            }
            userInfo.observe {
                Log.i("userInfo:$it")
            }
            showToast.observe {
                Log.i("showToast:$it")
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        viewModel.getConfig()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun changeADState(visible: Boolean, config: Config? = viewModel.getConfigValue()) {
        val isEmpty =
            config?.info?.home_ad.isNullOrEmpty() or config?.info?.home_ad_link.isNullOrEmpty()
        val isVisible = visible && !isEmpty
        viewBinding.ivAd.isVisible = isVisible
        viewBinding.ivAdclose.isVisible = isVisible
    }

    fun updateTips(data: ConfigData) {
        if (data.android_code > getCurrentVersionCode() && !needUpdate) {
            window.decorView.post {
                needUpdate = true
                NetErrorPop(this@HomeActivity).showUpdate(
                    getConfigData().android_update,
                    onSure = {},
                    onCancel = {

                    })
            }
        }
    }

    companion object {

        @JvmStatic
        fun start(context: Context, id: Int = R.id.action_toHome) {
            val starter = Intent(
                context, HomeActivity::class.java
            ).putExtra(ConfigApp.EXTRAS_POSITION, id)
            context.startActivity(starter)
        }
    }
}