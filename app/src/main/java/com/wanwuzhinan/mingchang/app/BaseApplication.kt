package com.wanwuzhinan.mingchang.app

import android.content.Context
import android.util.Log
import com.colin.library.android.image.glide.GlideImageLoader
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.network.data.AppResponse
import com.colin.library.android.network.gson.IntegerTypeAdapter
import com.colin.library.android.network.gson.ObjectTypeAdapter
import com.colin.library.android.network.gson.StringTypeAdapter
import com.colin.library.android.utils.config.UtilConfig
import com.colin.library.android.utils.helper.UtilHelper
import com.comm.net_work.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kongzue.dialogx.DialogX
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import com.ssm.comm.app.CommApplication
import com.ssm.comm.app.appContext
import com.ssm.comm.global.AppActivityManager
import com.tencent.rtmp.TXLiveBase
import com.tencent.rtmp.TXLiveBaseListener
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.RegisterData
import com.wanwuzhinan.mingchang.net.HeaderInterceptor
import com.wanwuzhinan.mingchang.utils.setData
import com.zjh.download.SimpleDownload


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
class BaseApplication : CommApplication() {

    override fun onCreate() {
        super.onCreate()
        UtilHelper.init(UtilConfig.newBuilder(this, true).build())
        AppActivityManager.getInstance().init(appContext)
        initMMKV(this)
//        MediaHolder.initialize(this)
        initImageLoader()
        initDownload()
        DialogX.init(this);
        initX5Environment()
        enterInApp()
        initNetwork()
        Log.e("BaseApplication", "onCreate: ")

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {
            override fun createRefreshHeader(
                context: Context, layout: RefreshLayout
            ): RefreshHeader {
                layout.setPrimaryColorsId(R.color.transparents, R.color.color_333333) //全局设置主题颜色
                return ClassicsHeader(context)
            }
        })

        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(
                context: Context, layout: RefreshLayout
            ): RefreshFooter {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(R.color.transparents, R.color.color_333333) //全局设置主题颜色
                return ClassicsFooter(context).setDrawableSize(20f)
            }
        })
        PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
    }

    private fun initNetwork() {
        val gson =
            GsonBuilder().setLenient().registerTypeAdapter(Int::class.java, IntegerTypeAdapter())
                .registerTypeAdapter(String::class.java, StringTypeAdapter())
                .registerTypeAdapter(
                    RegisterData::class.java,
                    ObjectTypeAdapter(Gson(), RegisterData::class.java)
                )
                .registerTypeAdapter(
                    AppResponse::class.java,
                    ObjectTypeAdapter(Gson(), AppResponse::class.java)
                )
                .create()
        NetworkConfig.gson = gson
        NetworkConfig.addInterceptor(HeaderInterceptor())

    }

    fun enterInApp() {
        Log.e("BaseApplication", "enterInApp: ")
        initBugly()
        initLive()
        TXLiveBase.setListener(object : TXLiveBaseListener() {
            override fun onLicenceLoaded(result: Int, reason: String) {
                Log.i("TAG", "onLicenceLoaded: result:$result, reason:$reason")
                if (result == 0) {
                    setData("TXLiveBaseLicence", 1)
                } else {
                    initLive()
                }
            }
        })
    }

    private fun initImageLoader() {
        GlideImageLoader.getDefault().diskCacheOptions.withContext(this).loadPlaceholderRes(-1)
            .loadErrorRes(-2).loadFallbackRes(-3)
            .setDiskCacheDirPath(getExternalFilesDir("Cache")?.path ?: filesDir.path)
            .setDiskCacheFolderName("Img").setDiskCacheSize(2 * 1024 * 1024).setBitmapPoolSize(2.0f)
            .bindBaseUrl(BuildConfig.IMG_HOST).setMemoryCacheSize(1.5f).build()
    }


    //携程下载
    private fun initDownload() {
        SimpleDownload.instance.init(this)
    }


    private fun initLive() {
        val licenseURL =
            "https://license.vod2.myqcloud.com/license/v2/1353990201_1/v_cube.license" // 获取到的 license url
        val licenseKey = "d025b928c9f91abb9a3a354cad87af4b" // 获取到的 license key
        TXLiveBase.getInstance().setLicence(this, licenseURL, licenseKey)
    }

}