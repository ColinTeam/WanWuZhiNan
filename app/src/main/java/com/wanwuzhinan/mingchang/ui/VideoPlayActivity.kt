package com.wanwuzhinan.mingchang.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.encrypt.DecryptUtil
import com.colin.library.android.utils.ext.onClick
import com.google.gson.reflect.TypeToken
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.post
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.databinding.FragmentVideoBinding
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonInfo
import com.wanwuzhinan.mingchang.ext.showCardImage
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListAskActivity
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
    val timer = countDown(6, onNext = {

    }, onFinish = {
        playNext()
    })


    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        if (savedInstanceState != null) parse(savedInstanceState)
        else parse(bundle)
        viewBinding.apply {
            videoPlayer.setViewTreeLifecycleOwner(this@VideoPlayActivity)
            videoPlayer.setSuperPlayerListener(superPlayerListener)
            videoPlayer.setPlayerViewCallback(superPlayerViewCallback)
            onClick(
                tvPlay, tvQuestion, tvSure, tvReload, netErrorBack, tvVideoReload
            ) {
                when (it) {
                    tvPlay -> {
                        timer.cancel()
                        checkMore()
                    }

                    tvSure -> {
                        llTips.visibility = View.GONE
                        videoPlayer.onResume()
                    }

                    tvReload -> {

                    }

                    tvVideoReload -> {
                        clVideoNoNet.visibility = View.GONE
                        videoPlayer.resetPlayer()
                        play(viewModel.getLessonInfoValue())
                    }

                    netErrorBack -> {
                        finish()
                    }

                    tvQuestion -> {
                        timer.cancel()
                        checkMore(toAsk = true)
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            positionData.observe {
                Log.d("position:$it")
                play(viewModel.getLessonsValue(), it)
            }
            lessonsData.observe {
                Log.d("lessonsData:$it")
                play(it, viewModel.getPositionValue())
            }
            lessonInfo.observe {
                Log.d("lessonInfo:$it")
                play(it)
            }
            studyLog.observe {
                Log.d("studyLog:$it")
                post(MessageEvent.UPDATE_NIGHT, UploadProgressEvent("0", 0, 0))
                if (it.medalCardList.isNotEmpty()) {
                    showCardImage(it.medalCardList[0].image_selected)
                } else if (it.medalList.isNotEmpty()) {
                    showCardImage(it.medalList[0].image)
                } else checkMore()
            }
        }
    }

    private fun showCardImage(image: String) {
        showCardImage(image, complete = {
            checkMore()
        })
    }

    private fun playNext(
        lessons: List<Lesson>? = viewModel.getLessonsValue(),
        position: Int = viewModel.getPositionValue()
    ) {
        play(lessons, position + 1)
    }

    private fun play(
        lessons: List<Lesson>? = viewModel.getLessonsValue(),
        position: Int = viewModel.getPositionValue()
    ) {
        Log.d("position:$position  lessons:${lessons} ")
        if (lessons.isNullOrEmpty()) {
            return
        }
        var selected = position
        if (selected < 0) selected = 0
        else if (position >= lessons.size) selected = lessons.size - 1
        play(lessons[selected])
    }


    private fun play(lesson: Lesson) {
        if (lesson.has_power == 0) {
            showPowerTips()
        } else {
            viewModel.getLessonInfo(lesson.id)
        }
    }


    private fun play(lesson: LessonInfo? = viewModel.getLessonInfoValue()) {
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
        viewBinding.videoPlayer.apply {
            playWithModelNeedLicence(model)
            setStartTime(seek.toDouble())
        }
    }

    private fun showPowerTips() {
        ExchangeCoursePop(this).showPop(onSure = {
            ExchangeActivity.start(this)
            finish()
        }, onContact = {
            ExchangeContactPop(this).showHeightPop()
        })
    }

    private fun showTipsView() {
    }

    private fun parse(bundle: Bundle?) {
        bundle?.let {
            val id = it.getInt(ConfigApp.INTENT_ID, 0)
            val listType = object : TypeToken<List<Lesson>>() {}.type
            val list = NetworkConfig.gson.fromJson<List<Lesson>>(
                it.getString(ConfigApp.INTENT_DATA, null), listType
            ).filter { lesson -> lesson.is_video != 0 }
            val position = list.indexOfFirst { it.id == id }
            viewModel.updatePosition(position)
            viewModel.updateLessons(list)
        }
    }

    private fun checkMore(
        lessions: List<Lesson>? = viewModel.getLessonsValue(),
        postion: Int = viewModel.getPositionValue(),
        toAsk: Boolean = false
    ) {
        val size = lessions?.size ?: 0
        if (size > postion) {
            playNext()
        } else if (toAsk) {
            QuestionListAskActivity.start(this, ConfigApp.TYPE_ASK)
            finish()
        } else finish()
    }

    override fun onResume() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private val superPlayerListener = object : ISuperPlayerListener {
        override fun onVodPlayEvent(
            player: TXVodPlayer?, event: Int, param: Bundle?
        ) {
            Log.e("onVodPlayEvent event:$event\t param:$param")
            when (event) {
                2005 -> {
                    val progress = param?.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS) ?: 0

                }

                2006 -> {
                    val info = viewModel.getLessonInfoValue()?.info
                    if (info != null) {
                        viewModel.study(info.id, info.video_duration)
                    } else {
                        checkMore()
                    }
                }
            }
        }

        override fun onVodNetStatus(
            player: TXVodPlayer?, status: Bundle?
        ) {
            // 获取实时速率, 单位：kbps
            val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
            Log.e("onVodNetStatus status:$status network speed:$speed")
        }

        override fun onLivePlayEvent(event: Int, param: Bundle?) {
            Log.e("onLivePlayEvent event:$event\t param:$param")
        }

        override fun onLiveNetStatus(status: Bundle?) {
            val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
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
//            viewModel.postError(code,)
        }

        override fun onShowCacheListClick() {
            Log.e("onShowCacheListClick")
        }

    }
}


