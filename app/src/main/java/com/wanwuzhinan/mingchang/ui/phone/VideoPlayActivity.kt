package com.wanwuzhinan.mingchang.ui.phone

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.encrypt.DecryptUtil
import com.colin.library.android.utils.ext.onClick
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hpplay.sdk.source.api.LelinkSourceSDK
import com.hpplay.sdk.source.browse.api.LelinkServiceInfo
import com.hpplay.sdk.source.easycast.IEasyCastListener
import com.hpplay.sdk.source.easycast.bean.EasyCastBean
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
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.databinding.ActivityVideoPlayBinding
import com.wanwuzhinan.mingchang.entity.CourseInfoData
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
    var mVideoList: ArrayList<SubjectListData.lessonBean>? = null
    var seek = 0L
    lateinit var startBtn: ImageView

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
        val listType = object : TypeToken<ArrayList<SubjectListData.lessonBean>>() {}.type
        mVideoList = Gson().fromJson(intent.getStringExtra(ConfigApp.INTENT_DATA), listType)
        mVideoList?.removeIf { it.is_video.toInt() == 0 }
        mPosition = mVideoList?.indexOfFirst { it.id == videoCourseId } ?: 0

        if (mPosition < 0) {
            mPosition = 0
        }
        if (mVideoList?.isEmpty() == true) {
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
            mDataBinding.ivShare,
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
                        finish()
                        return@onClick
                    }
                    playNextVideo()
                }

                mDataBinding.tvQuestion -> {
                    timer.cancel()
                    if (mPosition >= mVideoList!!.size - 1) {
                        finish()
                        launchQuestionListActivity(ConfigApp.TYPE_ASK)
                        return@onClick
                    }
                    val videoData = mVideoList?.get(mPosition)
                    launchVideoAnswerActivity(videoData!!.id)
                    finish()
                }

                mDataBinding.ivShare -> {
                    initShare()
                }

                mDataBinding.tvSure -> {
                    mDataBinding.llTips.visibility = View.GONE
                    mDataBinding.detailPlayer.onResume()
                }

                mDataBinding.tvReload -> {
                    mDataBinding.clNoNet.visibility = View.GONE
                    val videoData = mVideoList?.get(mPosition)
                    if (videoData?.has_power == "0") {
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
                    finish()
                }

                mDataBinding.ivVideoBack -> {
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

    fun initShare() {
        mDataBinding.detailPlayer.onPause()
        mDataBinding.llShare.visibility = View.VISIBLE

        LelinkSourceSDK.getInstance()
            .setSdkInitInfo(getApplicationContext(), "23294", "250b751fc44ca5a443fd9e55679e67b1")
            .easyPush(mDataBinding.llShare)

        LelinkSourceSDK.getInstance().setEasyCastListener(object : IEasyCastListener {
            override fun onCast(p0: LelinkServiceInfo?): EasyCastBean {
                val bean = EasyCastBean()
                bean.url = DecryptUtil.aes(mData?.info?.videoAes!!, ConfigApp.VIDEO_AES_KEY)
                return bean
            }

            override fun onCastError(p0: LelinkServiceInfo?, p1: EasyCastBean?, p2: Int, p3: Int) {
            }

            override fun onCastLoading(p0: LelinkServiceInfo?, p1: EasyCastBean?) {

            }

            override fun onCastPause(p0: LelinkServiceInfo?, p1: EasyCastBean?) {

            }

            override fun onCastStart(p0: LelinkServiceInfo?, p1: EasyCastBean?) {

            }

            override fun onCastCompletion(p0: LelinkServiceInfo?, p1: EasyCastBean?) {

            }

            override fun onCastPositionUpdate(
                p0: LelinkServiceInfo?, p1: EasyCastBean?, p2: Long, p3: Long
            ) {
            }

            override fun onCastStop(p0: LelinkServiceInfo?, p1: EasyCastBean?) {
            }

            override fun onDismiss() {
                mDataBinding.llShare.visibility = View.GONE
            }

        })
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
                // 获取实时速率, 单位：kbps
//                val speed: Int = status?.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) ?: 0
//                Log.e(TAG, "onLiveNetStatus: $speed" )
//                mDataBinding.tvSpeed.text = "${speed}kbps"
            }

            override fun onLivePlayEvent(event: Int, param: Bundle?) {

            }

            override fun onLiveNetStatus(status: Bundle?) {
            }
        })

        mDataBinding.detailPlayer.setPlayerViewCallback(object : OnSuperPlayerViewCallback {
            override fun onStartFullScreenPlay() {

            }

            override fun onStopFullScreenPlay() {

            }

            override fun onClickFloatCloseBtn() {
                finish()
            }

            override fun onClickSmallReturnBtn() {
                finish()
            }

            override fun onStartFloatWindowPlay() {
            }

            override fun onPlaying() {
            }

            override fun onPlayEnd() {
            }

            override fun onError(code: Int) {
                if (code == SuperPlayerCode.NET_ERROR) {
                    mDataBinding.clVideoNoNet.visibility = View.VISIBLE
                    errorPro =
                        (mDataBinding.detailPlayer.progress / (mDataBinding.detailPlayer.duration * 0.1)).toFloat()
                }
            }

            override fun onShowCacheListClick() {
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
        super.onDestroy()

        mDataBinding.detailPlayer.resetPlayer()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_play
    }


    fun initRouter() {

        var mediaRouter = MediaRouter.getInstance(applicationContext)


        // 定义要查找的媒体路由类型
        var mediaRouteSelector =
            MediaRouteSelector.Builder().addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
                .build()

        // 设置MediaRouteButton
        mDataBinding.btnMediaRoute.routeSelector = mediaRouteSelector

        // 创建回调处理路由选择事件
        var mediaRouterCallback = object : MediaRouter.Callback() {
            @Suppress("DEPRECATION")
            override fun onRouteSelected(router: MediaRouter, info: MediaRouter.RouteInfo) {
                super.onRouteSelected(router, info)
                // 当用户选择了投屏目标时触发

                Log.d("MediaRouter", "Selected route: " + info.name)
                // 在这里你可以开始投屏过程

            }

            @Suppress("DEPRECATION")
            override fun onRouteUnselected(router: MediaRouter, info: MediaRouter.RouteInfo) {
                super.onRouteUnselected(router, info)
                // 当用户取消选择投屏目标时触发
                Log.d("MediaRouter", "Unselected route: " + info.name)
                // 在这里你可以停止投屏过程
            }
        }

        mediaRouter.addCallback(mediaRouteSelector, mediaRouterCallback)

    }
}