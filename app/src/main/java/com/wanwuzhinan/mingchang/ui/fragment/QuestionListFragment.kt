package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentQuestionListBinding
import com.wanwuzhinan.mingchang.entity.Question
import com.wanwuzhinan.mingchang.vm.QuestionViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-15 09:31
 *
 * Des   :QuestionHomeFragment
 * 错题集 id==-1
 * 正常答题
 */
class QuestionListFragment : AppFragment<FragmentQuestionListBinding, QuestionViewModel>() {
    private var id = 0
    private var title = ""
    private var position = -1
    private var optionSelected = -1
    override fun bindViewModelStore() = requireActivity().viewModelStore

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        id = getExtrasPosition(bundle, savedInstanceState)
        title = bundle?.getString(ConfigApp.EXTRAS_TITLE) ?: ""
        viewBinding.apply {

        }
    }


    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            questions.observe {
                Log.i("questions:$it")
                showQuestion(it)

            }
            questionErrorList.observe {
                Log.i("questionErrorList:$it")
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        requestQuestion(position, 0)
    }

    private fun showQuestion(question: Question) {
        if (question.questionsList.isEmpty()) {
            // TODO: 空布局,显示完成弹框
            if (position > 0) {
                showCompleteDialog()
            }
            return
        }



    }

    private fun showCompleteDialog() {

    }

    private fun requestQuestion(position: Int, question: Int) {
        viewBinding.tvQuestion.text = ""
        if (position != Constants.INVALID) {
            viewModel.getQuestions(position, question)
        } else {
            viewModel.getQuestionErrorList()
        }


    }

    private fun showExchangeCurseDialog() {

    }

    companion object {
        fun navigate(
            fragment: Fragment, type: Int = 0, title: String = ""
        ) {
//            fragment.findNavController().apply {
//                navigate(
//                    R.id.action_toQuestionList, Bundle().apply {
//                        putInt(ConfigApp.EXTRAS_POSITION, type)
//                        putString(ConfigApp.EXTRAS_TITLE, title)
//                    }, NavOptions.Builder().build()
//                )
//            }
        }
    }

}