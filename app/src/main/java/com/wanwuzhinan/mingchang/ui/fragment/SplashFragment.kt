package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.colin.library.android.widget.motion.MotionTouchLister
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.wanwuzhinan.mingchang.BuildConfig
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentSplashBinding
import com.wanwuzhinan.mingchang.vm.HomeViewModel
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :SplashFragment
 */
class SplashFragment : AppFragment<FragmentSplashBinding, HomeViewModel>() {
    var playCompleted = false
    override fun onBackPressed(): Boolean {
        if (playCompleted) {
            toHome()
            return true
        } else {
            return false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.ivBack.setOnTouchListener(MotionTouchLister())
        val path = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.splash1
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
        GSYVideoType.disableMediaCodec()
        GSYVideoType.disableMediaCodecTexture()
        GSYVideoOptionBuilder().setAutoFullWithSize(true).setIsTouchWiget(false).setUrl(path)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    playCompleted = true
                    toHome()
                }

            }).build(viewBinding.video)

        viewBinding.video.startPlayLogic()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    private fun toHome() {
        findNavController().navigate(R.id.fragment_home)
//        val option = NavOptions.Builder().setPopUpTo(R.id.fragment_splash, inclusive = true)
//            .setLaunchSingleTop(true).setRestoreState(false).build()
//        findNavController().navigate(R.id.fragment_home, null, option)
    }

}