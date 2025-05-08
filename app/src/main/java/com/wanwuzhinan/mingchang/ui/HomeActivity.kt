package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.getCurrentVersionCode
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
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
    companion object {
        @JvmStatic
        fun start(context: Context, actionId: Int = Constants.INVALID) {
            val starter = Intent(
                context, HomeActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Constant.EXTRAS_ACTION_ID, actionId)
            context.startActivity(starter)
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("onNewIntent:$intent")
        intent?.let {
            toAction(it.getIntExtra(Constant.EXTRAS_ACTION_ID, Constants.INVALID))
        }

    }

    private fun toAction(action: Int) {
        findNavController(R.id.nav_host_fragment_content_main).navigate(action)
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
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
            showError.observe {
                Log.i("showError:$it")
                ToastUtil.show(it.msg)
                if (it.code == 2 || it.code == 4) {
                    navController.navigate(R.id.action_toLogin)
                }
            }
        }
        viewBinding.apply {
            onClick(ivAd, ivAdclose) {
                when (it) {
                    ivAd -> {
                        toWeb(
                            getString(R.string.home_detail),
                            viewModel.getConfigValue()?.info?.home_ad_link
                        )
                    }

                    ivAdclose -> {
                        viewModel.updateAD(true)
                    }
                }
            }
        }
    }

    private fun toWeb(title: String, url: String?) {
        if (url.isNullOrEmpty()) return
        findNavController(R.id.nav_host_fragment_content_main).navigate(
            R.id.action_toWeb, Bundle().apply {
                putInt(Constant.URL_TYPE, Constant.OTHER_TYPE)
                putString(Constant.WEB_TITLE, title)
                putString(Constant.H5_URL, url)
            }, NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true).build()
        )
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getConfig()
        viewModel.getUserInfo()
        Log.d("initData:$bundle")
        val action =
            bundle?.getInt(Constant.EXTRAS_ACTION_ID, R.id.action_toSplash) ?: R.id.action_toSplash
        toAction(action)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun changeADState(visible: Boolean, config: Config? = viewModel.getConfigValue()) {
        val isEmpty = config?.info?.home_ad.isNullOrEmpty()
        val isVisible = visible && !isEmpty
        viewBinding.ivAd.isVisible = isVisible
        viewBinding.ivAdclose.isVisible = isVisible
    }

    var needUpdate = false
    fun updateTips(data: ConfigData) {
        if (data.android_code > getCurrentVersionCode() && !needUpdate) {
            window.decorView.post {
                needUpdate = true
                NetErrorPop(this@HomeActivity).showUpdate(getConfigData().android_update, onSure = {

                }, onCancel = {

                })
            }
        }
    }

}