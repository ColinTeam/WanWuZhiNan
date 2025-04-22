package com.wanwuzhinan.mingchang.ui.phone

import android.media.MediaPlayer
import android.text.TextUtils
import android.view.View
import com.ad.img_load.setOnClickNoRepeat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.hpplay.glide.load.resource.gif.GifDrawable
import com.ssm.comm.event.MessageEvent
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.AnswerPracticeOptionAdapter
import com.wanwuzhinan.mingchang.adapter.AnswerPracticeSelectAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.databinding.ActivityAnswerPracticeBinding
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.ui.pop.AnswerExplainPop
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.data.QuestionLogData
import com.wanwuzhinan.mingchang.ext.getUserInfo
import com.wanwuzhinan.mingchang.ext.launchSettingActivity
import com.wanwuzhinan.mingchang.ext.showCardImage
import com.wanwuzhinan.mingchang.ui.pop.AudioCardPop
import com.wanwuzhinan.mingchang.ui.pop.CompassNumPop
import com.wanwuzhinan.mingchang.ui.pop.QuePhaseFinishPop

//答题 龙门题库
class AnswerAskActivity : BaseActivity<ActivityAnswerPracticeBinding, UserViewModel>(UserViewModel()) {

    var mId=""
    var mPosition=0
    var mSelectPosition=0
    var mType=1//1选择状态 2显示状态
    lateinit var mQuestionList:MutableList<QuestionListData.questionBean>
    lateinit var mAdapter: AnswerPracticeOptionAdapter
    lateinit var mMediaPlayer: MediaPlayer

    lateinit var infoModel: QuestionListData
    lateinit var logModel: QuestionLogData

    override fun initView() {
        mId=intent.getStringExtra(ConfigApp.INTENT_ID).toString()
        mMediaPlayer = MediaPlayer()

        initQuestionList()
        showBaseLoading()
        mViewModel.questionDetail(mId)

        mDataBinding.tvErrorNum.text = "${ConfigApp.question_compass}"
    }

    private fun initQuestionList(){
        mQuestionList= mutableListOf()
        mAdapter= AnswerPracticeOptionAdapter()

        mDataBinding.reOption.adapter=mAdapter
        mAdapter.setOnDebouncedItemClick{adapter, view, position ->

            if(mType==2) return@setOnDebouncedItemClick

            mSelectPosition = position
            mViewModel.questionAdd(mQuestionList.get(mPosition).id,mQuestionList.get(mPosition).answersArr.get(position).key)
        }
    }

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.linExplain,mDataBinding.tvPrevious,mDataBinding.tvSubmit,mDataBinding.tvNext,mDataBinding.tvReport) {
            if(mQuestionList==null) return@setOnClickNoRepeat
            when(it){
                mDataBinding.linExplain->{//
                    AnswerExplainPop(this).showPop(mDataBinding.linExplain,mQuestionList!!.get(mPosition).answer_desc)
                }
                mDataBinding.tvPrevious->{//
                    mPosition=mPosition-1
                    mType=1
                    changeQuestion(mPosition)
                }
                mDataBinding.tvSubmit->{//
                }
                mDataBinding.tvNext->{//
                    mMediaPlayer.pause()
                    if (mPosition+1 >  mQuestionList.size-1) {

                        CompassNumPop(mActivity, onSure = {
                            finish()
                        }, onCancel = {
                            finish()
                        }).showPop("完成阶段训练", if (logModel.compass_total.toInt() > 0) "恭喜你" else "别气馁，下次再来！", logModel.compass_total)
                    }else{
                        mPosition ++
                        mType=1
                        changeQuestion(mPosition)
                    }
                }
                mDataBinding.tvReport ->{
                    launchSettingActivity(2)
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.questionDetailLiveData.observeState(this){
            onSuccess={data, msg ->

                infoModel = data!!.info!!

                for (obj in infoModel.questionsList){
                    if (obj.id.toInt() == infoModel.answerLastQuestionId){
                        mPosition = infoModel.questionsList.indexOf(obj)
                        mPosition ++
                    }
                }

                if (infoModel.questionsList.size > 0){
                    if (mPosition >= infoModel.questionsList.size){
                        mPosition = 0
                    }
                    mQuestionList = infoModel.questionsList
                    initQuestionArray()
                }else{
                   mDataBinding.llNoData.visibility = View.VISIBLE
                    mDataBinding.llBg.visibility = View.GONE
                }
            }

            onFailed = {code, msg ->
                mDataBinding.llNoData.visibility = View.VISIBLE
                mDataBinding.llBg.visibility = View.GONE
            }
            onError = {e ->
                mDataBinding.llNoData.visibility = View.VISIBLE
                mDataBinding.llBg.visibility = View.GONE
            }
        }

        mViewModel.questionAddLiveData.observeState(this){
            onSuccess={data, msg ->
                for (obj in mQuestionList.get(mPosition).answersArr){
                    if (obj.is_true == 1){
                        obj.answerType = 1
                    }
                }
                var model = mQuestionList.get(mPosition).answersArr.get(mSelectPosition)
                if (model.is_true != 1){
                    model.answerType = 2
                }
                if (model.is_true == 1){
                    playAudio(mQuestionList.get(mPosition).answer_mp3_true)
                    var mediaPlayer = MediaPlayer.create(mActivity, R.raw.answer_true)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                        mediaPlayer = null
                    }

                }else{
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
                    mDataBinding.tvAdd.text = "${data.compass_this}"
                    mDataBinding.tvErrorNum.text = "${data.compass_total}"
                    ConfigApp.question_compass = data.compass_total.toInt()
                }

                mType = 2
                logModel = data!!
                playAudio(mQuestionList.get(mPosition).answer_mp3)
                changeButtonState()
                mAdapter.notifyDataSetChanged()
                if (data.medalCardList.size > 0) {
                    showCardImage(data.medalCardList.get(0).image_selected, complete = {
                    })
                }else{
                    if (data.medalList.size > 0) {
                        showCardImage(data.medalList.get(0).image, complete = {
                        })
                    }
                }
            }
        }
    }

    private fun initQuestionArray(){
        mQuestionList = infoModel.questionsList
        changeQuestion(mPosition)
    }


    //四个按钮的显示状态
    private fun changeButtonState(){
        if(mQuestionList.isNullOrEmpty()) return
//        if(mType==1){
//            mDataBinding.tvSubmit.text=if(mPosition!=mQuestionList.size-1) "提交答案" else "提交全部答案"
//        }else{
//            mDataBinding.tvSubmit.text="已完成"
//        }

//        mDataBinding.tvPrevious.visible(mPosition!=0&&mType==2)
//        mDataBinding.tvNext.visible(mPosition!=mQuestionList.size-1&&mType==2)

//        mDataBinding.tvSubmit.visible(mType==1||mType==2&&mPosition==mQuestionList.size-1)

        mDataBinding.linExplain.visible(mType==2)
        mDataBinding.tvNext.visibility = if (mType == 2) View.VISIBLE else View.INVISIBLE
    }

    //是否选中答案 返回选中的数量
    private fun getNumber():Int{
        var number=0
        for(i in mAdapter.items.indices){
            if(mAdapter.getItem(i)!!.select){
                number=number+1
            }
        }
        return number
    }


    //切换题目
    private fun changeQuestion(position: Int){
        mDataBinding.tvAdd.visibility = View.INVISIBLE
        changeButtonState()
        if(mQuestionList==null) return
        if(mQuestionList.size-1<position) return

        mQuestionList.get(mPosition).apply {
            //${mPosition+1}、 ${getUserInfo().truename}
            mDataBinding.tvTitle.text="${title}"
            mAdapter.submitList(answersArr)
            playAudio(mQuestionList.get(mPosition).title_mp3)
        }
        mDataBinding.tvNum.text = "共${mQuestionList.size}题 已完成${mPosition+1}题"
    }


    //播放标题
    private fun playAudio(url:String){
        if(TextUtils.isEmpty(url)) return

        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(url)
        mMediaPlayer.isLooping = false
        mMediaPlayer.prepareAsync() //异步准备

        //播放准备完成回调
        mMediaPlayer.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.start() //播放
            if (mDataBinding.ivPrintGif.drawable is  GifDrawable){
                var drawable = mDataBinding.ivPrintGif.drawable as GifDrawable
                drawable.start()
            }else {
                Glide.with(this).asGif().load(R.raw.answer_practice_print)
                    .into(mDataBinding.ivPrintGif)
            }
        }
        mMediaPlayer.setOnCompletionListener {
            if (mDataBinding.ivPrintGif.drawable is  GifDrawable){
                var drawable = mDataBinding.ivPrintGif.drawable as GifDrawable
                drawable.stop()
            }
        }
    }

    override fun finish() {
        super.finish()
        if (mMediaPlayer!= null) {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.stop()
            }
            mMediaPlayer.release()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_answer_practice
    }

}