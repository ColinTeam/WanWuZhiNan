package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import com.colin.library.android.utils.Log
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoBinding
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :AudioFragment
 */
class VideoFragment : AppFragment<FragmentVideoBinding, MediaViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            playerView.setSuperPlayerListener(superPlayerListener)
            playerView.setPlayerViewCallback(superPlayerViewCallback)
        }
        viewModel.apply {
            lessonInfo.observe {
                Log.i("lessonInfo:$it")
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

        play(ConfigApp.VIDEO_DEMO_2)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.playerView.onResume()
    }


    override fun onPause() {
        viewBinding.playerView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        viewBinding.playerView.apply {
            resetPlayer()
            release()
        }
        super.onDestroyView()
    }

    private fun play(url: String) {
        val model = SuperPlayerModel().apply {
            this.url = url
        }
        viewBinding.playerView.apply {
            playWithModelNeedLicence(model)
        }
    }

    private val superPlayerListener = object : ISuperPlayerListener {
        override fun onVodPlayEvent(
            player: TXVodPlayer?, event: Int, param: Bundle?
        ) {
            Log.e("onVodPlayEvent event:$event\t param:$param")
        }

        override fun onVodNetStatus(
            player: TXVodPlayer?, status: Bundle?
        ) {
            Log.e("onVodNetStatus status:$status")
        }

        override fun onLivePlayEvent(event: Int, param: Bundle?) {
            Log.e("onLivePlayEvent event:$event\t param:$param")
        }

        override fun onLiveNetStatus(status: Bundle?) {
            Log.e("onLiveNetStatus status:$status")
        }
    }

    private val superPlayerViewCallback = object : SuperPlayerView.OnSuperPlayerViewCallback {
        override fun onStartFullScreenPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onStopFullScreenPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onClickFloatCloseBtn() {
            Log.e("onShowCacheListClick")
        }

        override fun onClickSmallReturnBtn() {
            Log.e("onShowCacheListClick")
        }

        override fun onStartFloatWindowPlay() {
            Log.e("onShowCacheListClick")
        }

        override fun onPlaying() {
            Log.e("onShowCacheListClick")
        }

        override fun onPlayEnd() {
            Log.e("onShowCacheListClick")
        }

        override fun onError(code: Int) {
            Log.e("onError code:$code")
        }

        override fun onShowCacheListClick() {
            Log.e("onShowCacheListClick")
        }

    }
}