package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
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
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (playCompleted) {
                        findNavController().navigate(R.id.action_toHome)
                    }
                }

            })
        val path = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.splash1
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
        GSYVideoType.disableMediaCodec()
        GSYVideoType.disableMediaCodecTexture()
        GSYVideoOptionBuilder().setAutoFullWithSize(true).setIsTouchWiget(false).setUrl(path)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    playCompleted = true
                    findNavController().navigate(R.id.action_toHome)
                }

            }).build(viewBinding.video)

        viewBinding.video.startPlayLogic()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }
}