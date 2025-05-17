package com.wanwuzhinan.mingchang.ui

import android.annotation.SuppressLint
import android.os.Bundle
import com.colin.library.android.widget.motion.MotionTouchLister
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.wanwuzhinan.mingchang.BuildConfig
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentSplashBinding
import com.wanwuzhinan.mingchang.ui.pop.PrivacyDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.HomeViewModel
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-16 22:50
 *
 * Des   :SplashActivity
 */
class SplashActivity : AppActivity<FragmentSplashBinding, HomeViewModel>() {
    var playCompleted = false
    var builder: GSYVideoOptionBuilder? = null
    override fun goBack(): Boolean {
        if (playCompleted) {
            doNextAction()
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.ivBack.setOnTouchListener(MotionTouchLister())
        viewBinding.ivBack.isEnabled = false
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        val path = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.splash1
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
        GSYVideoType.disableMediaCodec()
        GSYVideoType.disableMediaCodecTexture()
        builder = GSYVideoOptionBuilder().apply {
            setAutoFullWithSize(true)
            setIsTouchWiget(false)
            setUrl(path)
            setVideoAllCallBack(callback)
        }
        builder!!.build(viewBinding.video)
        viewBinding.video.startPlayLogic()

    }

    override fun loadData(refresh: Boolean) {
        viewModel.getConfig()
    }

    override fun onStop() {
        super.onStop()
        builder?.setVideoAllCallBack(null)
        builder = null
        viewBinding.video.release()
    }

    private fun doNextAction() {
        if (MMKVUtils.getBoolean(ConfigApp.MMKY_KEY_PRIVACY)) {
            startActivity()
        } else {
            showPrivacyDialog()
        }
    }

    private fun showPrivacyDialog() {
        PrivacyDialog.newInstance().apply {
            sure = {
                MMKVUtils.set(ConfigApp.MMKY_KEY_PRIVACY, true)
                startActivity()
            }
            cancel = {
                this@SplashActivity.finish()
            }
        }.show(this)
    }


    private fun startActivity() {
        if (ConfigApp.token.isEmpty()) {
            LoginActivity.start(this)
        } else {
            HomeActivity.start(this)
        }
        finish()
    }

    private val callback = object : GSYSampleCallBack() {
        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            playCompleted = true
            viewBinding.ivBack.isEnabled = true
            doNextAction()
        }

        override fun onPlayError(url: String?, vararg objects: Any?) {
            playCompleted = true
            viewBinding.ivBack.isEnabled = true
            viewModel.postError(error = "SplashFragment $url $objects")
            doNextAction()
        }
    }

}