package com.wanwuzhinan.mingchang.ui.phone

import android.os.Vibrator
import android.util.Log
import com.colin.library.android.utils.VibratorUtil
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ext.toastError
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.AnswerAskAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.QuestionListData
import com.wanwuzhinan.mingchang.databinding.ActivityAnswerAskBinding
import com.wanwuzhinan.mingchang.ui.pop.QuestionCheckPop
import com.wanwuzhinan.mingchang.ui.pop.QuestionCheckPop.OnCheckListener
import com.wanwuzhinan.mingchang.ui.pop.QuestionScreenPop
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import kotlin.math.floor

//答题 龙门科举
class AnswerPracticeActivity :
    BaseActivity<ActivityAnswerAskBinding, UserViewModel>(UserViewModel()) {

    var mId = ""
    var mPosition = -1
    lateinit var mQuestionList: MutableList<QuestionListData.questionBean>
    lateinit var mAdapter: AnswerAskAdapter

    lateinit var infoModel: QuestionListData

    private var vibrator: Vibrator? = null

    lateinit var screenPop: QuestionScreenPop
    lateinit var checkPop: QuestionCheckPop

    override fun initView() {
        mId = intent.getStringExtra(ConfigApp.INTENT_ID).toString()

        initList()
        checkPop = QuestionCheckPop(mActivity)
        checkPop.setOnCheckListener(object : OnCheckListener {
            override fun click(cur: Int, postion: Int) {
                mDataBinding.reOption.scrollToPosition(cur * 20 + postion)
            }
        })
        screenPop = QuestionScreenPop(mActivity, { finishS, noFinishS, trueS, falseS, selectList ->
            mQuestionList.clear()

            for (obj in infoModel.questionsList) {
                if (trueS || falseS) {
                    if (!finishS) {
                        continue;
                    }
                }
                if (selectList.size > 0) {
                    Log.e(
                        "selectList",
                        "selectList: " + selectList + "----" + floor(obj.index / 20.0).toInt()
                    )
                    if (!selectList.contains(floor(obj.index / 20.0).toInt())) {
                        continue
                    }
                }
                var isFinish = finishS
                if (trueS || falseS) {
                    isFinish = true
                }
                if (!noFinishS && !isFinish) {
                    mQuestionList.add(obj)
                } else {
                    if (isFinish && obj.answer_res_answer.isNotEmpty()) {
                        if (!trueS && !falseS) {
                            mQuestionList.add(obj)
                        } else {
                            if (trueS && obj.answer_res_true.toInt() == 1) {
                                mQuestionList.add(obj)
                            }
                            if (falseS && obj.answer_res_true.isNotEmpty() && obj.answer_res_true.toInt() == 0) {
                                mQuestionList.add(obj)
                            }
                        }
                    }

                    if (noFinishS && obj.answer_res_answer.isEmpty()) {
                        mQuestionList.add(obj)
                    }
                }
            }
            if (mQuestionList.size == 0) {
                toastError("没有结果呦")
            }
            mAdapter.submitList(mQuestionList)
            mAdapter.notifyDataSetChanged()

        })

        SkeletonUtils.showList(
            mDataBinding.reOption, mAdapter, R.layout.item_answer_ask_option_skeleton
        )
        mViewModel.questionDetail(mId)
    }

    private fun initList() {
        mQuestionList = mutableListOf()
        mAdapter = AnswerAskAdapter()
        mDataBinding.reOption.adapter = mAdapter

        mAdapter.setOnAnswerListener(object : AnswerAskAdapter.OnAnswerListener {
            override fun click(questionIndex: Int, answerIndex: Int) {
                if (!mQuestionList[questionIndex].is_select) {
                    VibratorUtil.startVibrator(mActivity)
                    mQuestionList[questionIndex].is_select = true

                    for (obj in mQuestionList.get(questionIndex).answersArr) {
                        if (obj.is_true == 1) {
                            obj.answerType = 1
                        }
                    }
                    var model = mQuestionList.get(questionIndex).answersArr.get(answerIndex)
                    if (model.is_true != 1) {
                        model.answerType = 2
                    }
                    mQuestionList.get(questionIndex).answer_res_answer = model.key
                    mQuestionList.get(questionIndex).answer_res_true = "${model.is_true}"
                    mViewModel.questionAdd(
                        mQuestionList.get(questionIndex).id,
                        mQuestionList.get(questionIndex).answersArr.get(answerIndex).key
                    )
                    var count = 0
                    for (obj in infoModel.questionsList) {
                        if (obj.answer_res_answer.isNotEmpty()) {
                            count++
                        }
                    }
                    mDataBinding.tvNum.text =
                        "共${infoModel.questionsList.count()}题 已完成${count}题"
                    mDataBinding.progress.progress = count
//                    if (count != 0 && count == mQuestionList.size){
//                        mDataBinding.tvNext.visibility = View.VISIBLE
//                    }else{
//                        mDataBinding.tvNext.visibility = View.GONE
//                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun initClick() {

        setOnClickNoRepeat(
            mDataBinding.tvNext, mDataBinding.tvScreen, mDataBinding.tvCheck, mDataBinding.tvReplay
        ) {
            when (it) {
                mDataBinding.tvNext -> {
//                    mChildPosition ++
//                    if (mChildPosition >= infoModel.children.size){
//                        mChildPosition = 0
//                        for (obj in infoModel.children){
//                            for (model in obj.questionsList){
//                                model.is_select = false
//                                for (jj in model.answersArr){
//                                    jj.answerType = 0
//                                }
//                            }
//                        }
//                    }
//                    mDataBinding.tvNext.visibility = View.INVISIBLE
//                    mQuestionList = infoModel.children.get(mChildPosition).questionsList
//                    mAdapter.submitList(mQuestionList)
                }

                mDataBinding.tvScreen -> {
                    screenPop.showPop()
                }

                mDataBinding.tvCheck -> {
                    checkPop.showPop()
                }

                mDataBinding.tvReplay -> {
                    showBaseLoading()
                    mViewModel.questionClear()
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.questionDetailLiveData.observeState(this) {
            SkeletonUtils.hideList()
            onSuccess = { data, msg ->
                infoModel = data!!.info!!
                mPosition = 0
//                for (model in infoModel.questionsList){
//                    if (model.id.toInt() == infoModel.answerLastQuestionId){
//                        mPosition = infoModel.questionsList.indexOf(model)
//                        mPosition ++
//                    }
//                }
                mQuestionList.clear()
                if (infoModel.questionsList.size > 0) {

                    infoModel.questionsList.forEachIndexed { index, obj ->
                        obj.index = index
                        if (obj.answer_res_answer.isNotEmpty()) {
                            obj.is_select = true
                            for (option in obj.answersArr) {
                                if (option.is_true == 1) {
                                    option.answerType = 1
                                }
                                if (option.key.equals(obj.answer_res_answer)) {
                                    if (option.is_true != 1) {
                                        option.answerType = 2
                                    }
                                }
                            }
                        }
                        mQuestionList.add(obj)
                    }

                    mAdapter.submitList(mQuestionList)
                    var count = 0
                    for (obj in infoModel.questionsList) {
                        if (obj.answer_res_answer.isNotEmpty()) {
                            count++
                        }
                    }
                    mDataBinding.tvNum.text =
                        "共${infoModel.questionsList.count()}题 已完成${count}题"
                    mDataBinding.progress.max = infoModel.questionsList.count()
                    mDataBinding.progress.progress = count
                    screenPop.setList(infoModel.questionsList)
                    checkPop.setList(infoModel.questionsList)

                } else {
                    finish()
                    toastError("暂无题目")
                }

            }

            onFailed = { code, msg ->
                finish()
                toastError("暂无题目")
            }
            onError = { e ->
                finish()
                toastError("暂无题目")
            }
        }

        mViewModel.questionClearLiveData.observeState(this) {
            onSuccess = { data, msg ->
                dismissBaseLoading()
                mViewModel.questionDetail(mId)
            }
            onFailed = { code: Int?, msg: String? ->
                if (code == 0) {
                    dismissBaseLoading()
                    mViewModel.questionDetail(mId)
                }
            }
            onDataEmpty2 = {
                dismissBaseLoading()
                mViewModel.questionDetail(mId)
            }
            onDataEmpty = {
                dismissBaseLoading()
                mViewModel.questionDetail(mId)
            }

        }
    }


    override fun finish() {
        super.finish()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_answer_ask
    }
}