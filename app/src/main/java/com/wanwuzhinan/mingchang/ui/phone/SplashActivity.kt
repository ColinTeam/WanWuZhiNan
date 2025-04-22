package com.wanwuzhinan.mingchang.ui.phone

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.clearAllData
import com.ssm.comm.ext.isShowPrivacy
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.setData
import com.ssm.comm.ui.base.BaseActivity
import com.ssm.comm.utils.NavigationBarUtil
import com.ssm.comm.utils.StatusBarUtils
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.BaseApplication
import com.wanwuzhinan.mingchang.databinding.ActivityViewSplashBinding
import com.wanwuzhinan.mingchang.ext.launchLoginActivity
import com.wanwuzhinan.mingchang.ext.launchMainActivity
import com.wanwuzhinan.mingchang.ext.performLaunchPrivacy
import com.wanwuzhinan.mingchang.ext.performLaunchUserAgreement
import com.wanwuzhinan.mingchang.net.RetrofitClient
import com.wanwuzhinan.mingchang.ui.pop.PrivacyPop
import com.wanwuzhinan.mingchang.vm.UserViewModel
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

class SplashActivity : BaseActivity<ActivityViewSplashBinding, UserViewModel>(UserViewModel()) {

    lateinit var gsyVideoOptionBuilder: GSYVideoOptionBuilder

    override fun initView() {
        StatusBarUtils.translucentStatusBar(mActivity, true, true)
        NavigationBarUtil.hideNavigationBar(window)

        startVideo()
    }

    private fun startVideo(){
        var path="android.resource://"+mActivity.packageName+"/"+R.raw.splash1

        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)

        gsyVideoOptionBuilder = GSYVideoOptionBuilder()
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
        GSYVideoType.disableMediaCodec()//关闭硬解码，播放前设置
        GSYVideoType.disableMediaCodecTexture()//关闭硬解码渲染优化
        gsyVideoOptionBuilder.setAutoFullWithSize(true)
            .setIsTouchWiget(false)
            .setUrl(path)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    showPricePop()
                }
            }).build(mDataBinding.video)

        mDataBinding.video.startPlayLogic()

        mViewModel.getConfig()

        mViewModel.getConfigLiveData.observeState(this){
            onSuccess = {data, msg ->

                Log.e("TAG", "startVideo: ${data?.info?.apple_is_audit}" )
                if (("huawei".equals(Build.BRAND, ignoreCase = true) ||
                            "huawei".equals(Build.MANUFACTURER, ignoreCase = true) ||
                            "honor".equals(Build.BRAND, ignoreCase = true) ||
                            "honor".equals(Build.MANUFACTURER, ignoreCase = true))
                ) {

                }else{
                    data?.info?.apple_is_audit = 0
                }
                setData(Constant.CONFIG_DATA, Gson().toJson(data?.info))
            }
            onFailed = {code, msg ->
                if (code == 2 || code == 4){
                    clearAllData()
                    RetrofitClient.removeOkHttpCommBuilder(Constant.TOKEN)
                    mViewModel.getConfig()
                }
            }
        }
    }

    private fun showPricePop() {
        if (isShowPrivacy()) {
            jumpActivity()
        } else {
            PrivacyPop(this).showPrivacyPop {
                when (it) {
                    1 -> {
                        performLaunchUserAgreement()
                    }

                    2 -> {
                        performLaunchPrivacy()
                    }

                    3 -> {
                        jumpActivity()
                    }
                }
            }
        }
    }

    private fun jumpActivity() {

        enterApp()
//        if (getConfigData().android_code.toInt() > getCurrentVersionCode()) {
//            NetErrorPop(mActivity).showUpdate(getConfigData().android_update,onSure = {
//                enterApp()
//            }, onCancel = {
//                enterApp()
//            })
//        }else{
//            enterApp()
//        }
    }

    private fun enterApp(){
        (application as BaseApplication).enterInApp()
        if (Constant.isLogin()) {
            launchMainActivity()
        } else {
            launchLoginActivity()
        }
        this@SplashActivity.finish()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_view_splash
    }
}