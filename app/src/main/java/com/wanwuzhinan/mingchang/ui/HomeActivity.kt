package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityHomeBinding
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.utils.setData
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
        fun start(context: Context) {
            val starter = Intent(
                context, HomeActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(starter)
        }
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        return super.getOnBackInvokedDispatcher()

    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        viewModel.apply {
            configData.observe {
                Log.i("configData:$it")
            }
            userInfo.observe {
                Log.i("userInfo:$it")
                ConfigApp.question_count_error = it.question_count_error.toInt()
                ConfigApp.question_compass = it.question_compass.toInt()
                setData(Constant.USER_INFO, NetworkConfig.gson.toJson(it))
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
                        viewBinding.ivAd.visible(false)
                        viewBinding.ivAdclose.visible(false)
                    }
                }
            }
        }
    }

    private fun toWeb(title: String, url: String?) {
        Log.d("toWeb title$title url:$url")
        if (url.isNullOrEmpty()) return
        findNavController(R.id.nav_host_fragment_content_main).navigate(
            R.id.action_toWeb, Bundle().apply {
                putInt(Constant.URL_TYPE, Constant.OTHER_TYPE)
                putString(Constant.H5_URL, url)
            }, NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true).build()
        )
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_toLogin)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}