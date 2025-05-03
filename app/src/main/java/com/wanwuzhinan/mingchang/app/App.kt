package com.wanwuzhinan.mingchang.app

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.config.UtilConfig
import com.colin.library.android.utils.helper.UtilHelper
import com.kongzue.dialogx.DialogX
import com.ssm.comm.global.AppActivityManager
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener
import com.wanwuzhinan.mingchang.BuildConfig

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 19:39
 *
 * Des   :App
 */
class App : MultiDexApplication() {
    companion object {
        private lateinit var instance: App
        fun getInstance(): App = instance
    }

    override fun onCreate() {
        super.onCreate()
        UtilHelper.init(UtilConfig.newBuilder(this, BuildConfig.DEBUG).build())
        AppActivityManager.getInstance().init(this)
        initMMKV(this)
        initX5(this)
        DialogX.init(this);

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeObserver())
    }

    private fun initMMKV(context: Context) {
        val path = context.externalCacheDir?.path ?: filesDir.path
        Log.d("initMMKV path:$path")
        MMKV.initialize(context, path, MMKVLogLevel.LevelInfo)
    }

    private fun initX5(context: Context) {
        QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param stateCode 110: 表示当前服务器认为该环境下不需要下载
             */
            override fun onDownloadFinish(stateCode: Int) {
                Log.d("initX5Environment-onDownloadFinish:stateCode=$stateCode")
            }

            /**
             * @param stateCode 200、232安装成功
             */
            override fun onInstallFinish(stateCode: Int) {
                Log.d("initX5Environment-onInstallFinish:stateCode=$stateCode")
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(progress: Int) {
                Log.d("initX5Environment-onDownloadProgress:progress=$progress")
            }
        })
        QbSdk.initX5Environment(context, object : PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
                Log.d("initX5Environment-内核初始化完毕")
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(isX5: Boolean) {
                Log.d("initX5Environment-X5内核是否成功加载=$isX5")
            }
        })
        // 设置非wifi下也进行下载X5WebView
        QbSdk.setDownloadWithoutWifi(true)
    }

    var api: IWXAPI? = null
    fun registerWXPay() {
        // 通过 WXAPIFactory 工厂，获取 IWXAPI 的实例
        this.api = WXAPIFactory.createWXAPI(this, BuildConfig.WE_CHAT_APP_ID)
        // 将应用的 appId 注册到微信
        this.api?.registerApp(BuildConfig.WE_CHAT_APP_ID)
    }


    private inner class AppLifeObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (Lifecycle.Event.ON_START == event) {
                Log.e("foreground")
            } else if (Lifecycle.Event.ON_STOP == event) {
                Log.e("background")
            }
        }
    }
}