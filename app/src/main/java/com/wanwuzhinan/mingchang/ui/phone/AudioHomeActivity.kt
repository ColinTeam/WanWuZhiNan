package com.wanwuzhinan.mingchang.ui.phone

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.utils.countDown
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.databinding.ActivityAudioHomeBinding
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.comm.net_work.sign.AESDecryptor
import com.ssm.comm.config.Constant
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.dismissLoadingExt
import com.ssm.comm.ext.getAllScreenHeight
import com.ssm.comm.ext.getAllScreenWidth
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.global.AppActivityManager
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.adapter.AudioHomeCoverAdapter
import com.wanwuzhinan.mingchang.adapter.AudioHomeListAdapter
import com.wanwuzhinan.mingchang.adapter.CatePhoneAdapter
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.entity.CourseInfoData
import com.wanwuzhinan.mingchang.ext.launchAudioPlayInfoActivity
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ext.showCardImage
import com.wanwuzhinan.mingchang.ui.pop.AudioCardPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.utils.AnimationUtils
import com.wanwuzhinan.mingchang.utils.getAudioData
import com.wanwuzhinan.mingchang.utils.setData
import java.text.SimpleDateFormat

//音频主页
class AudioHomeActivity : BaseActivity<ActivityAudioHomeBinding, UserViewModel>(UserViewModel()) {
    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val starter = Intent(activity, AudioHomeActivity::class.java)
            activity.startActivity(starter)
        }
    }

    var mList: MutableList<SubjectListData>? = null
    var mPosition = 0
    var mChildPosition = 0
    lateinit var mCateAdapter: CatePhoneAdapter
    lateinit var mAdapter: AudioHomeCoverAdapter
    lateinit var mListAdapter: AudioHomeListAdapter

    var mAudioList: MutableList<SubjectListData>? = null
    var mPlayAudioList: MutableList<SubjectListData.lessonBean>? = null
    var mPlayPage = 0
    var mPlayData: CourseInfoData? = null

    var mIsSeekbarChange = false //互斥变量，防止进度条和定时器冲突。
    var mIsOpen = true//播放or暂停歌曲
    var mIsMute = false//静音
    var mMediaPlayer: MediaPlayer? = null
    var speed = 1.0f //播放速率
    lateinit var mAnimater: ObjectAnimator


    override fun initView() {
        initList()

        // 获取窗口的布局参数
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        showBaseLoading()
        mViewModel.courseSubject(ConfigApp.TYPE_AUDIO)

        mMediaPlayer = MediaPlayer()
        mAnimater = AnimationUtils.rotationAnimation(mDataBinding.ivFile)
        countDown(86400, start = {

        }, end = {

        }, next = {
            if (!mIsSeekbarChange && mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                mDataBinding.seekPaint.progress = mMediaPlayer!!.currentPosition
                mDataBinding.seekPaintBig.progress = mMediaPlayer!!.currentPosition
            }
        })
        Glide.with(this).asGif().load(R.raw.audio_1).into(mDataBinding.ivGif1)
        Glide.with(this).asGif().load(R.raw.audio_1).into(mDataBinding.ivGif2)
        Glide.with(this).asGif().load(R.raw.audio_2).into(mDataBinding.ivGif3)

    }

    private fun initList() {

        //右侧 科目
        mCateAdapter = CatePhoneAdapter()
        mDataBinding.reCateList.adapter = mCateAdapter
        mCateAdapter.setOnDebouncedItemClick { adapter, view, position ->
            mChildPosition = position
            mDataBinding.sclCateList.visibility = View.GONE
            setSelectList()
        }

        mAdapter = AudioHomeCoverAdapter()
        mDataBinding.reList.adapter = mAdapter
        mAdapter.setOnDebouncedItemClick { adapter, view, position ->
            if (mAudioList == null) return@setOnDebouncedItemClick
            Log.e("TAG", "initList: " + position)
            mPosition = position
            mAdapter.selectIndex = mPosition
            mAdapter.notifyDataSetChanged()
            mListAdapter.submitList(mAudioList?.get(mPosition)?.lessonList)
        }

        mListAdapter = AudioHomeListAdapter()
        mDataBinding.reAudioList.adapter = mListAdapter
        mListAdapter.setOnDebouncedItemClick { adapter, view, position ->
            if (mAudioList == null) return@setOnDebouncedItemClick
            if (mPosition >= mAudioList!!.size) return@setOnDebouncedItemClick
            if (mAudioList?.get(mPosition)?.lessonList!!.get(position).has_power != "1") {
                ExchangeCoursePop(AppActivityManager.getInstance().topActivity).showPop(onSure = {
                    launchExchangeActivity()
                }, onContact = {
                    ExchangeContactPop(AppActivityManager.getInstance().topActivity).showHeightPop()
                })
                return@setOnDebouncedItemClick
            }
            mPlayAudioList = mAudioList?.get(mPosition)?.lessonList
            mPlayPage = position

            if (mPlayPage < mPlayAudioList!!.size) {
                getLessonInfo()
            }
        }
    }

    override fun initRequest() {
        mViewModel.courseSubjectLiveData.observeState(this, false) {
            onSuccess = { data, msg ->
                if (!data!!.list.isNullOrEmpty()) {
                    mList = data.list!!
                    mCateAdapter.submitList(mList)

                    if (mList!!.size > 0) {
                        mChildPosition = 0
                        if (getAudioData(Constant.AUDIO_PLAY_PAGE) != null) {
                            mChildPosition = getAudioData(Constant.AUDIO_PLAY_PAGE)!!
                        }
                        setSelectList()
                        if (mList!!.size == 1) {
                            mDataBinding.clChange.visibility = View.GONE
                        }
                    } else {
                        finish()
                    }
                }
            }
            onFailed = { code, msg ->
                dismissBaseLoading()
            }
        }

        mViewModel.courseQuarterListLiveData.observeState(this) {
            SkeletonUtils.hideList()
            onSuccess = { data, msg ->
                if (!data!!.list.isNullOrEmpty()) {
                    mAudioList = data.list
                    mAdapter.submitList(mAudioList)
                    if (data.list!!.size > 0) {
                        mPosition = 0
                        mPlayPage = 0
                        Log.e("TAG", "initRequest: " + getAudioData(Constant.AUDIO_PLAY_ID))
                        data.list!!.forEachIndexed { index, obj ->
                            obj.lessonList.forEachIndexed { childIndex, lessonBean ->
                                if (lessonBean.id.toInt() == getAudioData(Constant.AUDIO_PLAY_ID)) {
                                    mPlayPage = childIndex
                                    mPosition = index
                                }
                            }
                        }

                        mAdapter.selectIndex = mPosition
                        mAdapter.notifyDataSetChanged()

                        mListAdapter.submitList(mAudioList?.get(mPosition)?.lessonList)

                        mPlayAudioList = mAudioList?.get(mPosition)?.lessonList

                        if (mPlayPage < mPlayAudioList!!.size) {
                            if (mPlayAudioList!!.get(mPlayPage).has_power.toInt() != 1) {
                                ExchangeCoursePop(AppActivityManager.getInstance().topActivity).showPop(onSure = {
                                        launchExchangeActivity()
                                    },
                                    onContact = {
                                        ExchangeContactPop(AppActivityManager.getInstance().topActivity).showHeightPop()
                                    })
                            } else {
                                getLessonInfo()
                            }
                        }
                    }
                } else {
                    mAudioList?.clear()
                    mAdapter.submitList(emptyList())
                    mListAdapter.submitList(emptyList())
                }
            }
        }

        mViewModel.getLessonInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                if (data != null) {
                    mPlayData = data
                    setData(Constant.AUDIO_PLAY_ID, data.info.id.toInt())
                    setData(Constant.AUDIO_PLAY_PAGE, mChildPosition)
                    changeMusic(data)
                }
            }

            onFailed = { code, msg ->
            }
        }

        mViewModel.courseStudyLiveData.observeForever {
            if (mActivity!!.isFinishing || mActivity!!.isDestroyed) return@observeForever
            dismissLoadingExt()
            if (it.data != null) {
                var data = it.data
                if (data != null) {
                    if (data.medalCardList.size > 0) {

                        showCardImage(data.medalCardList.get(0).image_selected, complete = {
                            post(MessageEvent.UPDATE_NIGHT)
                            nextPlay()
                        })
                    } else {
                        if (data.medalList.size > 0) {
                            showCardImage(data.medalList.get(0).image, complete = {
                                post(MessageEvent.UPDATE_NIGHT)
                                nextPlay()
                            })
                        } else {
                            post(MessageEvent.UPDATE_NIGHT)
                            nextPlay()
                        }
                    }
                }
            } else {
                post(MessageEvent.UPDATE_NIGHT)
                nextPlay()
            }
        }
    }

    private fun setSelectList() {
        if (mList!!.size > 0) {
            mViewModel.courseQuarterList(mList!!.get(mChildPosition).id, 1)
            mCateAdapter.curPage = mChildPosition
            mCateAdapter.notifyDataSetChanged()

            val constraintLayout = findViewById<ConstraintLayout>(R.id.cl_list_bg)
            val constraintSet = ConstraintSet()

            // 克隆现有布局
            constraintSet.clone(constraintLayout)
            // 修改具体视图的约束
            val imageView = findViewById<ImageView>(R.id.iv_ding_top_1)
            Log.e("TAG", "${getAllScreenHeight()} ${getAllScreenWidth()} ")
            val top = ((60 + (56 * mChildPosition)) / 375.0 * getAllScreenHeight()).toInt()
            Log.e("TAG", "mChildPosition : ${mChildPosition} --- ${top}")
            constraintSet.connect(
                imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top
            )  // 设置距离父布局顶部 100px
            // 应用新的约束
            constraintSet.applyTo(constraintLayout)

        } else {
            finish()
        }
    }

    //音乐
    override fun initClick() {
        mDataBinding.seekPaint.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mDataBinding.tvCurrentTime.text = format(mMediaPlayer!!.currentPosition.toLong())
                mDataBinding.tvCurrentTimeBig.text = format(mMediaPlayer!!.currentPosition.toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                mIsSeekbarChange = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mIsSeekbarChange = false
                mMediaPlayer!!.seekTo(p0!!.progress);//在当前位置播放
                mDataBinding.tvCurrentTime.text = format(mMediaPlayer!!.currentPosition.toLong())
                mDataBinding.tvCurrentTimeBig.text = format(mMediaPlayer!!.currentPosition.toLong())
            }
        })

        mDataBinding.seekPaintBig.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mDataBinding.tvCurrentTime.text = format(mMediaPlayer!!.currentPosition.toLong())
                mDataBinding.tvCurrentTimeBig.text = format(mMediaPlayer!!.currentPosition.toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                mIsSeekbarChange = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mIsSeekbarChange = false
                mMediaPlayer!!.seekTo(p0!!.progress);//在当前位置播放
                mDataBinding.tvCurrentTime.text = format(mMediaPlayer!!.currentPosition.toLong())
                mDataBinding.tvCurrentTimeBig.text = format(mMediaPlayer!!.currentPosition.toLong())
            }
        })

        mDataBinding.toolBarBig.getLeftBack().setOnClickListener {
            mDataBinding.llBig.visibility = View.GONE
        }

        setOnClickNoRepeat(
            mDataBinding.ivVolume,
            mDataBinding.ivPrevious,
            mDataBinding.ivStart,
            mDataBinding.ivNext,
            mDataBinding.ivPrevious15,
            mDataBinding.ivNext15,
            mDataBinding.ivVolume,
            mDataBinding.ivRate,
            mDataBinding.rivImage,
            mDataBinding.rivImageBig,
            mDataBinding.ivPreviousBig,
            mDataBinding.ivStartBig,
            mDataBinding.ivNextBig,
            mDataBinding.ivPrevious15Big,
            mDataBinding.ivNext15Big,
            mDataBinding.ivVolumeBig,
            mDataBinding.ivRateBig,
            mDataBinding.llAudio,
            mDataBinding.clBig,
            mDataBinding.ivChange,

            ) {
            when (it) {
                mDataBinding.rivImageBig, mDataBinding.clBig -> {
                    if (mPlayData!!.info.content != null) {
                        launchAudioPlayInfoActivity(mPlayData!!.info.content, mPlayData!!.info.name)
                    }
                }

                mDataBinding.rivImage, mDataBinding.llAudio -> {
                    if (mPlayData != null) {
                        mDataBinding.llBig.visibility = View.VISIBLE
                    }
                }

                mDataBinding.ivVolume, mDataBinding.ivVolumeBig -> {//
                    mIsMute = !mIsMute
                    changeMuteState()
                }

                mDataBinding.ivPrevious, mDataBinding.ivPreviousBig -> {//
                    mPlayPage--
                    if (mPlayPage >= 0) {
                        if (mPlayPage < mPlayAudioList!!.size) {
                            getLessonInfo()
                        }
                    }
                }

                mDataBinding.ivStart, mDataBinding.ivStartBig -> {//
                    mIsOpen = !mIsOpen
                    changeState()
                }

                mDataBinding.ivNext, mDataBinding.ivNextBig -> {//
                    nextPlay()
                }

                mDataBinding.ivNext15, mDataBinding.ivNext15Big -> {
                    var pro = mDataBinding.seekPaint.progress + 15000
                    mMediaPlayer?.seekTo(pro)
                }

                mDataBinding.ivPrevious15, mDataBinding.ivPrevious15Big -> {
                    var pro = mDataBinding.seekPaint.progress - 15000
                    if (pro <= 0) {
                        pro = 0
                    }
                    mMediaPlayer?.seekTo(pro)
                }

                mDataBinding.ivRate, mDataBinding.ivRateBig -> {

                    val playbackParams = mMediaPlayer?.playbackParams
                    if (speed == 1.0f) {
                        speed = 1.25f
                        playbackParams?.speed = 1.25f
                        mDataBinding.ivRate.setImageResource(R.mipmap.ic_rate_b_1)
                        mDataBinding.ivRateBig.setImageResource(R.mipmap.ic_rate_w_1_2)
                    } else if (speed == 1.25f) {
                        speed = 1.5f
                        playbackParams?.speed = 1.5f
                        mDataBinding.ivRate.setImageResource(R.mipmap.ic_rate_b_1)
                        mDataBinding.ivRateBig.setImageResource(R.mipmap.ic_rate_w_1_5)
                    } else if (speed == 1.5f) {
                        speed = 0.8f
                        playbackParams?.speed = 0.8f
                        mDataBinding.ivRate.setImageResource(R.mipmap.ic_rate_b_1)
                        mDataBinding.ivRateBig.setImageResource(R.mipmap.ic_rate_w_0_8)
                    } else {
                        speed = 1.0f
                        playbackParams?.speed = 1.0f
                        mDataBinding.ivRate.setImageResource(R.mipmap.ic_rate_b_1)
                        mDataBinding.ivRateBig.setImageResource(R.mipmap.ic_rate_w_1)
                    }
                    mMediaPlayer?.playbackParams = playbackParams!!
                }

                mDataBinding.ivChange -> {
                    if (mDataBinding.sclCateList.visibility == View.VISIBLE) {
                        mDataBinding.sclCateList.visibility = View.GONE
                    } else {
                        mDataBinding.sclCateList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun changeState() {
        mDataBinding.ivStart.setImageResource(if (mIsOpen == false) R.mipmap.play_mian else R.mipmap.pause_mian)
        mDataBinding.ivStartBig.setImageResource(if (mIsOpen == false) R.mipmap.play_w else R.mipmap.pause_w)
        if (mIsOpen) {
            mAnimater.resume()
            mMediaPlayer!!.start()
        } else {
            mAnimater.pause()
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.pause()
            }
        }
    }

    private fun changeMuteState() {
        mDataBinding.ivVolume.setImageResource(if (mIsMute) R.mipmap.ic_volume_b_n else R.mipmap.ic_volume_b)
        mDataBinding.ivVolumeBig.setImageResource(if (mIsMute) R.mipmap.ic_volume_w_n else R.mipmap.ic_volume_w)
        if (mIsMute) {
            mMediaPlayer!!.setVolume(0F, 0F)
        } else {
            mMediaPlayer!!.setVolume(1F, 1F)
        }
    }

    private fun nextPlay() {
        if (mPlayAudioList != null) {
            if (mPlayPage + 1 < mPlayAudioList!!.size) {
                mPlayPage++
                getLessonInfo()
            } else {
                mPlayPage = 0
                getLessonInfo()
            }
        }
    }

    private fun getLessonInfo() {
        if (mPlayAudioList!!.get(mPlayPage).has_power.toInt() != 1) {
            ExchangeCoursePop(AppActivityManager.getInstance().topActivity).showPop(onSure = {
                launchExchangeActivity()
            }, onContact = {
                ExchangeContactPop(AppActivityManager.getInstance().topActivity).showHeightPop()
            })
        } else {
            mViewModel.getLessonInfo(mPlayAudioList!!.get(mPlayPage).id)
        }
    }

    //切换歌曲
    private fun changeMusic(data: CourseInfoData) {
        mPlayData = data

        mDataBinding.llAudio.visibility = View.VISIBLE
        mDataBinding.tvTitle.text = mPlayData!!.info.name
        mDataBinding.tvBigTitle.text = mPlayData!!.info.name

        GlideImgManager.get()
            .loadImg(mPlayData!!.info.image, mDataBinding.rivImage, R.drawable.img_default_icon)
        GlideImgManager.get()
            .loadImg(mPlayData!!.info.image, mDataBinding.rivImageBig, R.drawable.img_default_icon)

        mListAdapter.playId = data.info.id
        mListAdapter.notifyDataSetChanged()
        if (mMediaPlayer == null) {
            return
        }
        mMediaPlayer!!.reset()
        mMediaPlayer!!.setDataSource(
            AESDecryptor.decryptAES(
                data.info.videoAes, "W1a2n3W4u5Z6h7i8N9a0n"
            )
        )
        mMediaPlayer!!.isLooping = false
        mMediaPlayer!!.prepareAsync() //异步准备

        //播放准备完成回调
        mMediaPlayer!!.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start() //播放
            if (mediaPlayer!!.duration == -1) return@setOnPreparedListener
            mDataBinding.tvEndTime.text = format(mediaPlayer.duration.toLong())
            mDataBinding.tvEndTimeBig.text = format(mediaPlayer.duration.toLong())
            mDataBinding.seekPaint.max = mediaPlayer.duration
            mDataBinding.seekPaintBig.max = mediaPlayer.duration
            mDataBinding.ivStart.setImageResource(R.mipmap.pause_mian)
            mDataBinding.ivStartBig.setImageResource(R.mipmap.pause_w)
            if (data.info.study_end_second < data.info.video_duration - 10) {
                mMediaPlayer?.seekTo(data.info.study_end_second * 1000)
            } else {
                mMediaPlayer?.seekTo(0)
            }
        }

        //播放结束回调
        mMediaPlayer!!.setOnCompletionListener {

            if (mPlayData != null) {
                mViewModel.courseStudy(
                    mPlayData!!.info.id.toString(),
                    mMediaPlayer!!.duration,
                    mMediaPlayer!!.currentPosition
                )
            }
        }
    }

    override fun finish() {
        super.finish()
        if (mPlayData != null) {
            post(
                MessageEvent.UPDATE_PROGRESS, UploadProgressEvent(
                    mPlayData!!.info.id.toString(),
                    mPlayData!!.info.video_duration,
                    ((mMediaPlayer?.currentPosition ?: 0) / 1000)
                )
            )
        }

        if (mMediaPlayer != null) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun format(position: Long): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(position)
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayer?.isPlaying == true) {
            mIsOpen = !mIsOpen
            changeState()
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_audio_home
    }

}