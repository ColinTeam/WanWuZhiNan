package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.databinding.ActivityMainBinding
import com.wanwuzhinan.mingchang.utils.Log

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 21:57
 *
 * Des   :HomeActivity
 */
class HomeActivity : AppActivity<ActivityMainBinding, HomeViewModel>() {
    companion object{
        @JvmStatic
        fun start(context: Activity) {
            val starter = Intent(context, HomeActivity::class.java)
            context.startActivity(starter)
        }
    }
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.isLogin.observe {
            Log.i("isLogin:$it")
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}