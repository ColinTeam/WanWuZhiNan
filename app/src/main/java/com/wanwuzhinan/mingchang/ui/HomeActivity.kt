package com.wanwuzhinan.mingchang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityHomeBinding
import com.wanwuzhinan.mingchang.utils.MMKVUtils
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
            }
            showError.observe {
                Log.i("showError:$it")
                ToastUtil.show(it.msg)
                if (it.code == 2 || it.code == 4) {
                    navController.navigate(R.id.action_toLogin)
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        MMKVUtils.decodeString(ConfigApp.MMKV_SPLASH_TIME)
        viewModel.getConfig()
        viewModel.getUserInfo()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}