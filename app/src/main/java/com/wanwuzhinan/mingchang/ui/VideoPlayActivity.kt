package com.wanwuzhinan.mingchang.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.encrypt.DecryptUtil
import com.google.gson.reflect.TypeToken
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentVideoBinding
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonInfo
import com.wanwuzhinan.mingchang.entity.MediaInfo
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.vm.MediaViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 16:43
 *
 * Des   :VideoHomeActivity
 */
class VideoPlayActivity : AppActivity<FragmentVideoBinding, MediaViewModel>() {

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        if (savedInstanceState != null) parse(savedInstanceState)
        else parse(bundle)
        viewBinding.apply {
            viewPlayer.setViewTreeLifecycleOwner(this@VideoPlayActivity)
            viewPlayer.setSuperPlayerListener(superPlayerListener)
            viewPlayer.setPlayerViewCallback(superPlayerViewCallback)
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            positionData.observe {
                Log.d("position:$it")
                play(it, viewModel.getLessons())
            }
            lessonInfo.observe {
                Log.d("lessonInfo:$it")
                play(it)
            }
        }
    }

    private fun play(position: Int, lessons: List<Lesson?>? = viewModel.getLessons()) {
        Log.d("position:$position lessons:${lessons}")
        if (lessons == null || lessons.isEmpty()) {
            finish()
            return
        }
        var selected = lessons.indexOfFirst { (it?.id ?: 0) == position }
        if (selected < 0) selected = 0
        play(lessons[position]!!)
    }

    private fun play(lesson: Lesson) {
        if (lesson.has_power == "0") {
            showTips()
        } else {
            viewModel.getLessonInfo(lesson.id.toInt())
        }
    }

    private fun showTipsView(): MediaInfo? {
        TODO("Not yet implemented")
    }

    private fun play(lesson: LessonInfo?) {
        if (lesson == null || lesson.info.videoAes.isEmpty()) {
            showTipsView()
            return
        }
        val url = DecryptUtil.aes(lesson.info.videoAes, ConfigApp.VIDEO_AES_KEY)
        val seek = if (lesson.info.study_end_second < lesson.info.video_duration) {
            lesson.info.study_end_second * 1000L
        } else 0
        play(url, seek)
    }

    private fun play(url: String?, seek: Long = 0L) {
        Log.d("play url:$url seek:${seek}")
        val model = SuperPlayerModel().apply {
            this.url = url
        }
        viewBinding.viewPlayer.apply {
            playWithModelNeedLicence(model)
            setStartTime(seek.toDouble())
        }
    }

    private fun showTips() {
        ExchangeCoursePop(this).showPop(onSure = {
            ExchangeActivity.start(this)
            finish()
        }, onContact = {
            ExchangeContactPop(this).showHeightPop()
        })
    }

    private fun parse(bundle: Bundle?) {
        bundle?.let {
            val position = it.getInt(ConfigApp.INTENT_ID, 0)
            val listType = object : TypeToken<List<Lesson>>() {}.type
            val list = NetworkConfig.gson.fromJson<List<Lesson>>(
                it.getString(ConfigApp.INTENT_DATA, null), listType
            )
            viewModel.updatePosition(position)
            viewModel.updateLessons(list)
        }
    }


    override fun onResume() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
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
            Log.e("onStartFullScreenPlay")
        }

        override fun onStopFullScreenPlay() {
            Log.e("onStopFullScreenPlay")
        }

        override fun onClickFloatCloseBtn() {
            Log.e("onClickFloatCloseBtn")
            finish()
        }

        override fun onClickSmallReturnBtn() {
            Log.e("onClickSmallReturnBtn")
            finish()
        }

        override fun onStartFloatWindowPlay() {
            Log.e("onStartFloatWindowPlay")
        }

        override fun onPlaying() {
            Log.e("onPlaying")
        }

        override fun onPlayEnd() {
            Log.e("onPlayEnd")
        }

        override fun onError(code: Int) {
            Log.e("onError code:$code")
        }

        override fun onShowCacheListClick() {
            Log.e("onShowCacheListClick")
        }

    }
}