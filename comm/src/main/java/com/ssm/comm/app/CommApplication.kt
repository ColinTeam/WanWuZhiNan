package com.ssm.comm.app

import android.content.res.Resources
import android.text.TextUtils
import androidx.multidex.MultiDexApplication
import com.hjq.toast.ToastUtils
import com.ssm.comm.BuildConfig
import com.ssm.comm.ext.getCurrentVersionName
import com.ssm.comm.ext.getUID
import com.ssm.comm.global.AppActivityManager
import com.ssm.comm.utils.LogUtils
import com.tencent.bugly.crashreport.CrashReport
//import com.tencent.bugly.Bugly
//import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener
import kotlin.properties.Delegates

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.app
 * ClassName: BaseApplication
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 18:08
 * Email:shiming024@163.com
 * Description:
 */
//全局上下文
val appContext: CommApplication by lazy {
    CommApplication.instance
}

open class CommApplication : MultiDexApplication() {

    var api: IWXAPI? = null

    companion object {
        // 单例不会是null   所以使用notNull委托
        var instance: CommApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppActivityManager.getInstance().init(appContext)
        ToastUtils.init(appContext)
        initMMKV()
        initX5Environment()
        //MMKVUtils.clearAll()
    }


    /**
     * 初始化腾讯bug管理平台
     */
    fun initBugly() {
        CrashReport.initCrashReport(applicationContext, "7531451148", false);
//        val strategy = CrashReport.UserStrategy(appContext)
//        strategy.appVersion = getCurrentVersionName()
//        strategy.appPackageName = packageName
//        if(TextUtils.isEmpty(getUID())){
//            strategy.deviceID=""
//        }else{
//            strategy.deviceID= getUID()
//        }
//        //Bugly会在启动10s后联网同步数据
//        strategy.appReportDelay = 10000
//
//        CrashReport.initCrashReport(
//            appContext,
//            BuildConfig.BUGLY_APP_ID,
//            com.comm.net_work.BuildConfig.IS_ENABLE_LOG,
//            strategy
//        )
//        Bugly.init(appContext, BuildConfig.BUGLY_APP_ID, com.comm.net_work.BuildConfig.IS_ENABLE_LOG)
    }

    fun registerToWX() {
        // 通过 WXAPIFactory 工厂，获取 IWXAPI 的实例
        this.api = WXAPIFactory.createWXAPI(this, BuildConfig.WE_CHAT_APP_ID)
        // 将应用的 appId 注册到微信
        this.api?.registerApp(BuildConfig.WE_CHAT_APP_ID)
    }

    private fun initMMKV() {
        //自定义根目录
        val dir = filesDir.absolutePath + "/mmkv"
        val rootDir = MMKV.initialize(dir)
        LogUtils.d("mmkv root:$rootDir")
    }

    private fun initX5Environment() {
        /* SDK内核初始化周期回调，包括 下载、安装、加载 */
        QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param stateCode 110: 表示当前服务器认为该环境下不需要下载
             */
            override fun onDownloadFinish(stateCode: Int) {
                LogUtils.d("initX5Environment-onDownloadFinish:stateCode=$stateCode")
            }

            /**
             * @param stateCode 200、232安装成功
             */
            override fun onInstallFinish(stateCode: Int) {
                LogUtils.d("initX5Environment-onInstallFinish:stateCode=$stateCode")
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(progress: Int) {
                LogUtils.d("initX5Environment-onDownloadProgress:progress=$progress")
            }
        })
        QbSdk.initX5Environment(appContext, object : PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
                LogUtils.d("initX5Environment-内核初始化完毕")
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            override fun onViewInitFinished(isX5: Boolean) {
                LogUtils.d("initX5Environment-X5内核是否成功加载=$isX5")
            }
        })
        // 设置非wifi下也进行下载X5WebView
        QbSdk.setDownloadWithoutWifi(true)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (res != null) {
            val config = res.configuration
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f
                res.updateConfiguration(config, res.displayMetrics)
            }
        }
        return res
    }
}