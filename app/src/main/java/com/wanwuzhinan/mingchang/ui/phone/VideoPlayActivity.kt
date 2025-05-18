package com.wanwuzhinan.mingchang.ui.phone

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.encrypt.DecryptUtil
import com.colin.library.android.utils.ext.onClick
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ui.base.BaseActivity
import com.tencent.liteav.demo.superplayer.SuperPlayerCode
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView.OnSuperPlayerViewCallback
import com.tencent.liteav.demo.superplayer.model.ISuperPlayerListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayer
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.databinding.ActivityVideoPlayBinding
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ext.launchQuestionListActivity
import com.wanwuzhinan.mingchang.ext.launchVideoAnswerActivity
import com.wanwuzhinan.mingchang.ext.showCardImage
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.vm.UserViewModel


class VideoPlayActivity : BaseActivity<ActivityVideoPlayBinding, UserViewModel>(UserViewModel()) {

    val TAG = "VideoPlayActivity"
    var mPosition = 0
    var mVideoList: ArrayList<Lesson>? = null
    var seek = 0L
    var mData: CourseInfoData? = null

    var errorPro = 0.0F

    val timer = object : CountDownTimer(6000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            mDataBinding.tvDown.text = "${millisUntilFinished / 1000 + 1}S"
        }

        override fun onFinish() {
            playNextVideo()
        }
    }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        val videoCourseId = intent.getStringExtra(ConfigApp.INTENT_ID)
        val json = intent.getStringExtra(ConfigApp.INTENT_DATA)
        val listType = object : TypeToken<ArrayList<Lesson>>() {}.type
        mVideoList = Gson().fromJson(json, listType)
        mVideoList?.removeIf { it.is_video.toInt() == 0 }
        mPosition = mVideoList?.indexOfFirst { it.id == videoCourseId } ?: 0

        if (mPosition < 0) {
            mPosition = 0
        }
        if (mVideoList?.isEmpty() == true) {
            viewModel.postError(error = "mPosition,$json")
            finish()
        } else {
            val videoData = mVideoList?.get(mPosition)
            if (videoData?.has_power == "0") {
                ExchangeCoursePop(mActivity).showPop(onSure = {
                    finish()
                    launchExchangeActivity()
                }, onContact = {
                    ExchangeContactPop(mActivity).showHeightPop()
                })
                return
            }
            showBaseLoading()
            mViewModel.getLessonInfo(videoData?.id ?: "")
            initVideo()
        }
    }

    override fun initClick() {
        onClick(
            mDataBinding.tvPlay,
            mDataBinding.llMenu,
            mDataBinding.tvQuestion,
            mDataBinding.tvSure,
            mDataBinding.tvReload,
            mDataBinding.netErrorBack,
            mDataBinding.ivVideoBack,
            mDataBinding.tvVideoReload
        ) {
            when (it) {
                mDataBinding.llMenu -> {

                }

                mDataBinding.tvPlay -> {
                    timer.cancel()
                    if (mPosition >= mVideoList!!.size - 1) {
                        viewModel.postError(error = "mPosition >= mVideoList!!.size - 1->$mData")
                        finish()
                        return@onClick
                    }
                    playNextVideo()
                }

                mDataBinding.tvQuestion -> {
                    timer.cancel()
                    viewModel.postError(error = "tvQuestion->$mData")
                    if (mPosition >= mVideoList!!.size - 1) {
                        finish()
                        launchQuestionListActivity(ConfigApp.TYPE_ASK)
                        return@onClick
                    }
                    val videoData = mVideoList?.get(mPosition)
                    launchVideoAnswerActivity(videoData!!.id)
                    finish()
                }


                mDataBinding.tvSure -> {
                    mDataBinding.llTips.visibility = View.GONE
                    mDataBinding.detailPlayer.onResume()
                }

                mDataBinding.tvReload -> {
                    mDataBinding.clNoNet.visibility = View.GONE
                    val videoData = mVideoList?.get(mPosition)
                    if (videoData?.has_power == "0") {
                        viewModel.postError(error = "has_power->$videoData")
                        ExchangeCoursePop(mActivity).showPop(onSure = {
                            finish()
                            launchExchangeActivity()
                        }, onContact = {
                            ExchangeContactPop(mActivity).showPop()
                        })
                    } else {
                        showBaseLoading()
                        mViewModel.getLessonInfo(videoData?.id ?: "")
                    }
                }

                mDataBinding.netErrorBack -> {
                    viewModel.postError(error = "netErrorBack->$mData")
                    finish()
                }

                mDataBinding.ivVideoBack -> {
                    viewModel.postError(error = "ivVideoBack->$mData")
                    finish()
                }

                mDataBinding.tvVideoReload -> {
                    mDataBinding.clVideoNoNet.visibility = View.GONE
                    mDataBinding.detailPlayer.resetPlayer()
                    val model = SuperPlayerModel()
                    model.url = DecryptUtil.aes(mData?.info?.videoAes!!, ConfigApp.VIDEO_AES_KEY)
                    mDataBinding.detailPlayer.playWithModelNeedLicence(model)
                    mDataBinding.detailPlayer.seek(errorPro)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mDataBinding.detailPlayer.onPause()
//        post(
//            MessageEvent.UPDATE_PROGRESS, UploadProgressEvent(
//                mData!!.info.id.toString(),mData!!.info.video_duration,(mDataBinding.detailPlayer.currentPositionWhenPlaying /1000).toInt()
//            )
//        )
    }

    private fun initVideo() {

        mDataBinding.detailPlayer.setSuperPlayerListener(object : ISuperPlayerListener {
            override fun onVodPlayEvent(player: TXVodPlayer?, event: Int, param: Bundle?) {
                viewModel.postError(error = "onVodPlayEvent event->$event param->$param")
                if (event == 2013) {

                }
                if (event == 2006) {
                    if (mData != null) {
                        mViewModel.courseStudy(
                            mData!!.info.id.toString(),
                            mData!!.info.video_duration,
                            mData!!.info.video_duration
                        )
                    } else {
                        getVideo()
                    }
                }

                if (event == 2005) {
                    val progress = param!!.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS)
                    val duration = param!!.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS)
                    val playable = param!!.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS)
                    if (mData != null) {
                        for (info in mData!!.info.photos_tipsArr) {
                            if (!info.isShow) {
                                if ((progress / 1000).toInt() == info.sort.toInt()) {
                                    mDataBinding.llTips.visibility = View.VISIBLE
                                    GlideImgManager.get()
                                        .loadDefaultImg(info.path, mDataBinding.ivTipsCover, 0)
                                    mDataBinding.detailPlayer.onPause()
                                    info.isShow = true
                                }
                            }
                        }
                    }
                }

//                if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
//                    // 显示当前网速
//                    mDataBinding.tvSpeed.visibility = View.VISIBLE
//                } else if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END) {
//                    // 对显示当前网速的 view 进行隐藏
//                    mDataBinding.tvSpeed.visibility = View.GONE
//                }
            }

            override fun onVodNetStatus(player: TXVodPlayer?, status: Bundle?) {
                viewModel.postError(error = "onVodNetStatus status->$status")
                // 获取实时速率, 单位：kbps
//                val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
//                Log.e(TAG, "onLiveNetStatus: $speed" )
//                mDataBinding.tvSpeed.text = "${speed}kbps"
            }

            override fun onLivePlayEvent(event: Int, param: Bundle?) {
                viewModel.postError(error = "onLivePlayEvent event->$event param->$param")
            }

            override fun onLiveNetStatus(status: Bundle?) {
                viewModel.postError(error = "onLiveNetStatus status->$status")
            }
        })

        mDataBinding.detailPlayer.setPlayerViewCallback(object : OnSuperPlayerViewCallback {
            override fun onStartFullScreenPlay() {
                viewModel.postError(error = "onStartFullScreenPlay")
            }

            override fun onStopFullScreenPlay() {
                viewModel.postError(error = "onStopFullScreenPlay")
            }

            override fun onClickFloatCloseBtn() {
                viewModel.postError(error = "onClickFloatCloseBtn")
                finish()
            }

            override fun onClickSmallReturnBtn() {
                viewModel.postError(error = "onClickSmallReturnBtn")
                finish()
            }

            override fun onStartFloatWindowPlay() {
                viewModel.postError(error = "onStartFloatWindowPlay")
            }

            override fun onPlaying() {
                viewModel.postError(error = "onPlaying")
            }

            override fun onPlayEnd() {
                viewModel.postError(error = "onPlayEnd")
            }

            override fun onError(code: Int) {
                viewModel.postError(error = "onError $code")
                if (code == SuperPlayerCode.NET_ERROR) {
                    mDataBinding.clVideoNoNet.visibility = View.VISIBLE
                    errorPro =
                        (mDataBinding.detailPlayer.progress / (mDataBinding.detailPlayer.duration * 0.1)).toFloat()
                }
            }

            override fun onShowCacheListClick() {
                viewModel.postError(error = "onShowCacheListClick")
            }

        })
    }

    override fun initRequest() {
        super.initRequest()
        mViewModel.getLessonInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                if (data != null) {
                    mData = data
                    startPlayVideo()
                }
            }

            onFailed = { code, msg ->
                getVideo()
            }
            onError = { e ->
                mDataBinding.clNoNet.visibility = View.VISIBLE
            }
        }
        mViewModel.courseStudyLiveData.observeForever {
            if (mActivity!!.isFinishing || mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()
            if (it.data != null) {
                var data = it.data
                post(
                    MessageEvent.UPDATE_NIGHT, UploadProgressEvent(
                        "0", 0, 0
                    )
                )
                if (data!!.medalCardList.size > 0) {
                    showCardImage(data.medalCardList.get(0).image_selected, complete = {
                        getVideo()
                    })
                } else {
                    if (data.medalList.size > 0) {
                        showCardImage(data.medalList.get(0).image, complete = {
                            getVideo()
                        })
                    } else {
                        getVideo()
                    }
                }
            } else {
                getVideo()
            }
        }
    }

    private fun playNextVideo() {
        mDataBinding.llMenu.visibility = View.GONE
        if (mPosition >= mVideoList?.size!! - 1) {
            launchQuestionListActivity(ConfigApp.TYPE_ASK)
            finish()
            return
        }
        mPosition++
        val videoData = mVideoList?.get(mPosition)
        if (videoData?.has_power == "0") {
            ExchangeCoursePop(mActivity).showPop(onSure = {
                finish()
                launchExchangeActivity()
            }, onContact = {
                ExchangeContactPop(mActivity).showPop()
            })
            return
        }
        if (!this.isFinishing && !this.isDestroyed) showBaseLoading()
        mViewModel.getLessonInfo(videoData?.id ?: "")
        initVideo()
    }

    private fun startPlayVideo() {

        mDataBinding.llMenu.visibility = View.GONE
        timer.cancel()

        val model = SuperPlayerModel()
        model.url = DecryptUtil.aes(mData?.info?.videoAes!!, ConfigApp.VIDEO_AES_KEY)
        mDataBinding.detailPlayer.playWithModelNeedLicence(model)
        seek = 0
        seek = if (mData?.info?.study_end_second!! < mData?.info?.video_duration!!) {
            (mData?.info?.study_end_second!! * 1000).toLong()
        } else {
            0
        }
//        mDataBinding.detailPlayer.startPlayLogic()
    }

    @SuppressLint("SetTextI18n")
    private fun getVideo() {
        if (mPosition >= mVideoList?.size!! - 1) {
            mDataBinding.llMenu.visibility = View.VISIBLE
            mDataBinding.llName.visibility = View.GONE
            mDataBinding.tvPlay.text = "返回首页"
            mDataBinding.tvQuestion.text = "挑战${getConfigData().home_title3}"
            mDataBinding.tvDown2.text = "自动进入${getConfigData().home_title3}"
            mDataBinding.ivCover.setImageResource(R.mipmap.ic_video_finish);
            timer.start()
        } else {
            val videoData = mVideoList?.get(mPosition + 1)
            mDataBinding.llMenu.visibility = View.VISIBLE
            mDataBinding.tvTitle.text = videoData!!.name
            GlideImgManager.get()
                .loadImg(videoData.image, mDataBinding.ivCover, R.drawable.img_default_icon)
            timer.start()
        }
    }


    override fun onDestroy() {
        mDataBinding.detailPlayer.resetPlayer()
        super.onDestroy()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_play
    }


}