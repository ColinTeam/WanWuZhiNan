package com.wanwuzhinan.mingchang.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.databinding.ActivityMainBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 21:57
 *
 * Des   :HomeActivity
 */
class HomeActivity : AppActivity<ActivityMainBinding, HomeViewModel>() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var listPopupWindow: ListPopupWindow? = null


    private val callback = object : IAidlRemoteCallback.Stub() {
        override fun aidlChanged(data: String?) {
            data?.let { Log.i("data:$it") }
        }

        override fun itemChanged(itembean: ItemBean?) {
            itembean?.let { Log.i("itemBean:$it") }
        }

    }
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            aidlService = IDemoAidlInterface.Stub.asInterface(service)
            aidlService!!.register(callback)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    private var aidlService: IDemoAidlInterface? = null
    override fun onResume() {
        super.onResume()
        viewModel.update(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.update(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        aidlService?.let {
            it.unregister(callback)
            unbindService(connection)
        }
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
                R.id.fragment_home,
                R.id.fragment_gallery,
                R.id.fragment_method,
                R.id.fragment_slideshow
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
        viewModel.status.observe {
            Log.i(TAG, "status:$it")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language) {
            val popupWindow = listPopupWindow ?: createPopupWindow().also {
                listPopupWindow = it
            }
            popupWindow.show()
            return true
        }
        return super.onOptionsItemSelected(item)
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