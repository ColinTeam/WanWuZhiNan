package com.wanwuzhinan.mingchang.ui.phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.colin.library.android.utils.ext.onClick
import com.hpplay.glide.load.resource.gif.GifDrawable
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.AnswerPracticeOptionAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.databinding.ActivityAnswerPracticeBinding
import com.wanwuzhinan.mingchang.ext.invisible
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.ui.SettingTabActivity
import com.wanwuzhinan.mingchang.ui.pop.AnswerExplainPop
import com.wanwuzhinan.mingchang.ui.pop.AudioCardPop
import com.wanwuzhinan.mingchang.ui.pop.CompassNumPop
import com.wanwuzhinan.mingchang.vm.UserViewModel

//答题 视频题库
class VideoAnswerActivity :
    BaseActivity<ActivityAnswerPracticeBinding, UserViewModel>(UserViewModel()) {
    companion object{
        @JvmStatic
        fun start(context: Context,id: Int) {
            val starter = Intent(context, VideoAnswerActivity::class.java)
                .putExtra(ConfigApp.INTENT_ID,id)
            context.startActivity(starter)
        }
    }
    var mId = 0
    var mPosition = 0
    var mSelectPosition = 0
    var mType = 1//1选择状态 2显示状态
    lateinit var mQuestionList: MutableList<QuestionListData.QuestionBean>
    lateinit var mAdapter: AnswerPracticeOptionAdapter
    lateinit var mMediaPlayer: MediaPlayer

    lateinit var logModel: QuestionLogData

    override fun initView() {
        mId = intent.getIntExtra(ConfigApp.INTENT_ID,0)
        mMediaPlayer = MediaPlayer()

        initQuestionList()
        showBaseLoading()
        mViewModel.getLessonInfo(mId)
        mDataBinding.tvErrorNum.text = "${ConfigApp.question_compass}"
    }

    private fun initQuestionList() {
        mQuestionList = mutableListOf()
        mAdapter = AnswerPracticeOptionAdapter()

        mDataBinding.reOption.adapter = mAdapter
        mAdapter.setOnDebouncedItemClick { adapter, view, position ->

            if (mType == 2) return@setOnDebouncedItemClick

//            //1单选 2多选
//            if(mQuestionList.get(mPosition).typeid==1){
//                for(i in mAdapter.items.indices){
//                    mAdapter.getItem(i)!!.select=false
//                }
//                mAdapter.getItem(position)!!.select=true
//            }else{
//                mAdapter.getItem(position)!!.select=!mAdapter.getItem(position)!!.select
//            }
//
            showBaseLoading("答题中···")
            mSelectPosition = position
            mViewModel.questionAdd(
                mQuestionList.get(mPosition).id,
                mQuestionList.get(mPosition).answersArr.get(position).key
            )
        }
    }

    override fun initClick() {
        onClick(
            mDataBinding.linExplain,
            mDataBinding.tvPrevious,
            mDataBinding.tvSubmit,
            mDataBinding.tvNext,
            mDataBinding.tvReport
        ) {
            when (it) {
                mDataBinding.linExplain -> {//
                    AnswerExplainPop(this).showPop(
                        mDataBinding.linExplain,
                        mQuestionList!!.get(mPosition).answer_desc
                    )
                }

                mDataBinding.tvPrevious -> {//
                    mPosition = mPosition - 1
                    mType = 1
                    changeQuestion(mPosition)
                }

                mDataBinding.tvSubmit -> {//
                }

                mDataBinding.tvNext -> {//

                    if (mPosition + 1 > mQuestionList.size - 1) {

                        CompassNumPop(mActivity, onSure = {
                            finish()
                        }, onCancel = {
                            finish()
                        }).showPop(
                            "完成阶段训练",
                            if (logModel.compass_total.toInt() > 0) "恭喜你" else "别气馁，下次再来！",
                            logModel.compass_total
                        )
                    } else {
                        mPosition++
                        mType = 1
                        changeQuestion(mPosition)
                    }
                }

                mDataBinding.tvReport -> {
                    SettingTabActivity.start(this@VideoAnswerActivity,3)
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.getLessonInfoLiveData.observeState(this) {
            onSuccess = { data, msg ->
                if (data!!.info.questionsList.isNotEmpty()) {
                    mPosition = 0
                    mQuestionList = data.info.questionsList
                    initQuestionArray()
                } else {
                    mDataBinding.llNoData.visibility = View.VISIBLE
                    mDataBinding.llBg.visibility = View.GONE
                }
            }

            onFailed = { code, msg ->
                mDataBinding.llNoData.visibility = View.VISIBLE
                mDataBinding.llBg.visibility = View.GONE
            }
            onError = { e ->
                mDataBinding.llNoData.visibility = View.VISIBLE
                mDataBinding.llBg.visibility = View.GONE
            }
        }

        mViewModel.questionAddLiveData.observeState(this) {
            onSuccess = { data, msg ->
                for (obj in mQuestionList.get(mPosition).answersArr) {
                    if (obj.is_true == 1) {
                        obj.answerType = 1
                    }
                }
                var model = mQuestionList.get(mPosition).answersArr.get(mSelectPosition)
                if (model.is_true != 1) {
                    model.answerType = 2
                }
                if (model.is_true == 1) {
                    playAudio(mQuestionList.get(mPosition).answer_mp3_true)
                    var mediaPlayer = MediaPlayer.create(mActivity, R.raw.answer_true)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                        mediaPlayer = null
                    }

                } else {
                    var mediaPlayer = MediaPlayer.create(mActivity, R.raw.answer_error)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                        mediaPlayer = null
                    }
                    playAudio(mQuestionList.get(mPosition).answer_mp3_false)
                }
                if (data != null) {
                    mDataBinding.tvAdd.visibility = View.VISIBLE
                    mDataBinding.tvAdd.text = data.compass_this
                    mDataBinding.tvErrorNum.text = data.compass_total
                    ConfigApp.question_compass = data.compass_total.toInt()
                }
                mType = 2
                logModel = data!!
                playAudio(mQuestionList.get(mPosition).answer_mp3)
                changeButtonState()
                mAdapter.notifyDataSetChanged()

                if (data.medalCardList.isNotEmpty()) {
                    AudioCardPop(mActivity, onSure = {
                    }).showPop(data.medalCardList.get(0).name, data.medalCardList.get(0).image, 1)
                } else {
                    if (data.medalList.isNotEmpty()) {
                        AudioCardPop(mActivity, onSure = {
                        }).showPop(data.medalList.get(0).name, data.medalList.get(0).image, 2)
                    }
                }
            }

            onFailed = { code, msg ->
                toastError(msg.toString())
            }
            onComplete = {
                dismissBaseLoading()
            }
        }
    }

    private fun initQuestionArray() {
        changeQuestion(mPosition)
    }


    //四个按钮的显示状态
    private fun changeButtonState() {
        if (mQuestionList.isNullOrEmpty()) return
//        if(mType==1){
//            mDataBinding.tvSubmit.text=if(mPosition!=mQuestionList.size-1) "提交答案" else "提交全部答案"
//        }else{
//            mDataBinding.tvSubmit.text="已完成"
//        }

//        mDataBinding.tvPrevious.visible(mPosition!=0&&mType==2)
//        mDataBinding.tvNext.visible(mPosition!=mQuestionList.size-1&&mType==2)

//        mDataBinding.tvSubmit.visible(mType==1||mType==2&&mPosition==mQuestionList.size-1)

        mDataBinding.linExplain.visible(mType == 2)
        mDataBinding.tvNext.invisible(mType != 2)
    }

    //是否选中答案 返回选中的数量
    private fun getNumber(): Int {
        var number = 0
        for (i in mAdapter.items.indices) {
            if (mAdapter.getItem(i)!!.select) {
                number = number + 1
            }
        }
        return number
    }


    //切换题目
    @SuppressLint("SetTextI18n")
    private fun changeQuestion(position: Int) {
        mDataBinding.tvAdd.visibility = View.INVISIBLE
        changeButtonState()
        if (mQuestionList.size - 1 < position) return

        mQuestionList[mPosition].apply {
            //${mPosition+1}、 ${getUserInfo().truename}
            mDataBinding.tvTitle.text = title
            mAdapter.submitList(answersArr)
            playAudio(mQuestionList[mPosition].title_mp3)
        }
        mDataBinding.tvNum.text = "共${mQuestionList.size}题 已完成${mPosition + 1}题"
    }


    //播放标题
    private fun playAudio(url: String) {
        if (TextUtils.isEmpty(url)) return

        try {
            mMediaPlayer.reset()
        } catch (e: IllegalStateException) {

        }

        mMediaPlayer.setDataSource(url)
        mMediaPlayer.isLooping = false
        mMediaPlayer.prepareAsync() //异步准备

        //播放准备完成回调
        mMediaPlayer.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start() //播放
            if (mDataBinding.ivPrintGif.drawable is GifDrawable) {
                var drawable = mDataBinding.ivPrintGif.drawable as GifDrawable
                drawable.start()
            } else {
                Glide.with(this).asGif().load(R.raw.answer_practice_print)
                    .into(mDataBinding.ivPrintGif)
            }
        }
        mMediaPlayer.setOnCompletionListener {
            if (mDataBinding.ivPrintGif.drawable is GifDrawable) {
                var drawable = mDataBinding.ivPrintGif.drawable as GifDrawable
                drawable.stop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
        mMediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        try {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace() // 可选：记录日志方便排查
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_answer_practice
    }

}