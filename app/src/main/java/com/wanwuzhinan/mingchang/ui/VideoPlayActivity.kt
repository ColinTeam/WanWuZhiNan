package com.wanwuzhinan.mingchang.ui

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.encrypt.DecryptUtil
import com.colin.library.android.utils.ext.onClick
import com.google.gson.reflect.TypeToken
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.post
import com.tencent.liteav.demo.superplayer.SuperPlayerCode
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.databinding.FragmentVideoBinding
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.MediaInfo
import com.wanwuzhinan.mingchang.ext.showCardImage
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.ui.phone.ExchangeActivity
import com.wanwuzhinan.mingchang.ui.phone.QuestionListAskActivity
import com.wanwuzhinan.mingchang.ui.phone.VideoAnswerActivity
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.utils.load
import com.wanwuzhinan.mingchang.vm.MediaViewModel
import kotlinx.coroutines.Job

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 16:43
 *
 * Des   :VideoHomeActivity
 */
class VideoPlayActivity : AppActivity<FragmentVideoBinding, MediaViewModel>() {
    var timer: Job? = null
    private var errorSeek = -0.0F
    private var position = -1
    private var lessons: List<Lesson>? = null
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
        viewBinding.videoPlayer.setNeedToPause(false)
    }

    override fun onPause() {
        viewBinding.videoPlayer.onPause()
        viewBinding.videoPlayer.setNeedToPause(true)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
        super.onPause()
    }

    override fun onDestroy() {
        viewBinding.videoPlayer.release()
        timer?.cancel()
        errorSeek = -0.0F
        position = -1
        lessons = null
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        showLoading(true)
        parse(intent.extras)
        loadData(true)
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_SECURE)
        if (savedInstanceState != null) parse(savedInstanceState)
        else parse(bundle)
        viewBinding.apply {
            videoPlayer.setSuperPlayerListener(superPlayerListener)
            videoPlayer.setPlayerViewCallback(superPlayerViewCallback)
            onClick(
                tvPlay, tvQuestion, tvSure, tvReload, netErrorBack, tvVideoReload
            ) {
                when (it) {
                    tvPlay -> {
                        val lesson = getCurrentLesson()
                        if (lesson == null) {
                            finish()
                            return@onClick
                        }
                        checkLessonPlayComplete(toAsk = true)
                    }

                    tvSure -> {
                        llTips.visible(false)
                        videoPlayer.onResume()
                    }

                    tvReload -> {
                        clNoNet.visible(false)
                        checkLessonPlayComplete(0)
                    }

                    tvVideoReload -> {
                        clVideoNoNet.visible(false)
                        videoPlayer.resetPlayer()
                        play(viewModel.getMediaInfoValue(), errorSeek)
                    }

                    netErrorBack -> {
                        finish()
                    }

                    tvQuestion -> {
                        checkLessonPlayComplete(offset = 0, toAnswer = true, toAsk = true)
                    }
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            mediaInfoData.observe {
                Log.d("mediaInfoData:$it")
                play(it)
            }
            studyLog.observe {
                Log.d("studyLog:$it")
                post(MessageEvent.UPDATE_NIGHT, UploadProgressEvent("0", 0, 0))
                if (it.medalCardList.isNotEmpty()) {
                    showCardImage(it.medalCardList[0].image_selected)
                } else if (it.medalList.isNotEmpty()) {
                    showCardImage(it.medalList[0].image)
                } else showEmptyView()
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        viewModel.getConfig()
        checkLessonPlayComplete()
    }

    private fun play(lesson: Lesson) {
        if (lesson.has_power == 0) {
            showPowerTips()
        } else {
            viewModel.getLessonInfo(lesson.id)
        }
    }

    override fun interceptorHttpAction(action: HttpResult.Action): Boolean {
        Log.i("interceptorHttpAction:$action")
        if (action.code < 0) {
            viewBinding.clNoNet.visibility = View.VISIBLE
            return true
        }
        return false
    }

    private fun play(
        info: MediaInfo? = viewModel.getMediaInfoValue(), progress: Float = 0F
    ) {
        val aes = info?.videoAes
        val url = if (!aes.isNullOrEmpty()) DecryptUtil.aes(aes, ConfigApp.VIDEO_AES_KEY) else null
        if (url.isNullOrEmpty()) {
            showEmptyView()
            return
        }
        val seekProgress = if (progress > 0F) progress
        else if (info!!.study_end_second < info.video_duration) info.study_end_second * 1000F
        else 0F
        play(url, info!!.image, seekProgress)
    }

    private fun play(url: String, image: String?, progress: Float = 0f) {
        Log.d("play url:$url  image:${image} progress:${progress}")
        val model = SuperPlayerModel().apply {
            this.url = url
        }
        viewBinding.videoPlayer.apply {
            //默认自动播放
            playWithModelNeedLicence(model)
            seek(progress)
        }
    }

    private fun showEmptyView(config: ConfigData? = viewModel.getConfigValue()?.info) {
        val size = lessons?.size ?: 0
        if (position >= size - 1 || position < 0) {
            val text = config?.home_title3 ?: ""
            viewBinding.llMenu.visibility = View.VISIBLE
            viewBinding.llName.visibility = View.GONE
            viewBinding.tvPlay.text = getString(R.string.video_empty_question)
            viewBinding.tvQuestion.text = getString(R.string.video_empty_question, text)
            viewBinding.tvDown2.text = getString(R.string.video_empty_action, text)
            viewBinding.ivCover.setImageResource(R.mipmap.ic_video_finish);
            startCountDown()
        } else {
            val lesson = lessons!![position]
            viewBinding.llMenu.visibility = View.VISIBLE
            viewBinding.tvTitle.text = lesson.name
            viewBinding.ivCover.load(lesson.image, R.drawable.img_default_icon)
            startCountDown()
        }
    }

    private fun startCountDown() {
        timer?.cancel()
        timer = countDown(6, onNext = {
            viewBinding.tvDown.text = getString(R.string.video_empty_countdown, (it + 1))
        }, onFinish = {
            viewBinding.llMenu.visible(false)
            checkLessonPlayComplete(toAsk = true)
        })
    }


    private fun showCardImage(image: String) {
        showCardImage(image, complete = {
            showEmptyView()
        })
    }

    private fun showPowerTips() {
        ExchangeCoursePop(this).showPop(onSure = {
            ExchangeActivity.start(this)
            finish()
        }, onContact = {
            ExchangeContactPop(this).showHeightPop()
        })
    }


    private fun parse(bundle: Bundle?) {
        bundle?.let {
            val id = it.getInt(ConfigApp.INTENT_ID, 0)
            val listType = object : TypeToken<List<Lesson>>() {}.type
            lessons = NetworkConfig.gson.fromJson<List<Lesson>>(
                it.getString(ConfigApp.INTENT_DATA, null), listType
            ).filter { lesson -> lesson.is_video != 0 }
            position = lessons?.indexOfFirst { it.id == id } ?: -1
        }
    }

    private fun showDisplayPhoto(
        info: MediaInfo? = viewModel.getMediaInfoValue(), progress: Int
    ) {
        if (info == null) return
        val sort = progress / 1000
        for (photo in info.photos_tipsArr) {
            if (!photo.isShow && sort == info.sort) {
                viewBinding.llTips.visible(true)
                viewBinding.ivTipsCover.load(photo.path, 0)
                viewBinding.videoPlayer.onPause()
                photo.isShow = true
            }
        }
    }

    private fun getCurrentLesson(): Lesson? {
        if (!lessons.isNullOrEmpty() && lessons!!.size > position && position > 0) {
            return lessons!![position]
        }
        return null
    }

    private fun checkLessonPlayComplete(
        offset: Int = 1, toAnswer: Boolean = false, toAsk: Boolean = false
    ) {
        timer?.cancel()
        position += offset
        val lesson = getCurrentLesson()
        Log.d("position:$position lesson:$lesson")
        if (lesson == null) {
            if (toAsk) QuestionListAskActivity.start(this)
            finish()
            return
        }
        if (toAnswer) {
            VideoAnswerActivity.start(this, lesson.id)
            finish()
        }
        play(lesson)
    }

    private val superPlayerListener = object : ISuperPlayerListener {
        override fun onVodPlayEvent(
            player: TXVodPlayer?, event: Int, param: Bundle?
        ) {
            Log.i("onVodPlayEvent event:$event\t param:$param")
            when (event) {
                2005 -> {
                    showDisplayPhoto(
                        viewModel.getMediaInfoValue(),
                        param?.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS) ?: 0
                    )
                }

                2006 -> {
                    val info = viewModel.getMediaInfoValue()
                    if (info != null) {
                        viewModel.study(info.id, info.video_duration)
                    } else {
                        showEmptyView()
                    }
                }
            }
        }


        override fun onVodNetStatus(
            player: TXVodPlayer?, status: Bundle?
        ) {
            // 获取实时速率, 单位：kbps
            val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
            Log.i("onVodNetStatus status:$status network speed:$speed")
        }

        override fun onLivePlayEvent(event: Int, param: Bundle?) {
            Log.i("onLivePlayEvent event:$event\t param:$param")
        }

        override fun onLiveNetStatus(status: Bundle?) {
            val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
            Log.i("onLiveNetStatus speed:$speed")
        }
    }

    private val superPlayerViewCallback = object : SuperPlayerView.OnSuperPlayerViewCallback {
        override fun onStartFullScreenPlay() {
            Log.i("onStartFullScreenPlay")
        }

        override fun onStopFullScreenPlay() {
            Log.i("onStopFullScreenPlay")
        }

        override fun onClickFloatCloseBtn() {
            Log.i("onClickFloatCloseBtn")
            finish()
        }

        override fun onClickSmallReturnBtn() {
            Log.i("onClickSmallReturnBtn")
            finish()
        }

        override fun onStartFloatWindowPlay() {
            Log.i("onStartFloatWindowPlay")
        }

        override fun onPlaying() {
            Log.i("onPlaying")
            showLoading(false)
        }

        override fun onPlayEnd() {
            Log.i("onPlayEnd")
        }

        override fun onError(code: Int) {
            Log.i("onError code:$code")
            if (code == SuperPlayerCode.NET_ERROR) {
                viewBinding.apply {
                    clVideoNoNet.visible(true)
                    errorSeek = videoPlayer.progress / (videoPlayer.duration * 0.1F)
                }
            }
            showLoading(false)
        }

        override fun onShowCacheListClick() {
            Log.i("onShowCacheListClick")
        }

    }
}


