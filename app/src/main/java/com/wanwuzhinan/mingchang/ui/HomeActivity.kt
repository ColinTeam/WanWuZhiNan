package com.wanwuzhinan.mingchang.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.annotation.IdRes
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        setSupportActionBar(viewBinding.appBarMain.toolbar)
        viewBinding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action") {
                    ToastUtil.show(R.string.app_name)
                }.setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = viewBinding.drawerLayout
        val navView: NavigationView = viewBinding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val intent = Intent().apply {
            action = "com.colin.android.demo.kotlin.service.DemoAidlService"
            setComponent(
                ComponentName.createRelative(
                    "com.colin.android.demo.kotlin", ".service.DemoAidlService"
                )
            )
        }
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.isLogin.observe {
            Log.i(TAG, "status:$it")
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setMenuVisible(@IdRes res: Int, visible: Boolean) {
        setMenuVisible(viewBinding.appBarMain.toolbar.menu, res, visible)
    }

    fun setMenuVisible(menu: Menu?, @IdRes res: Int, visible: Boolean) {
        val menuItem =  menu?.findItem(res)?:return
        menuItem.setVisible(visible)
    }


}