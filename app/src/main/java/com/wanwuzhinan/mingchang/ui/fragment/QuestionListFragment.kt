package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentQuestionListBinding
import com.wanwuzhinan.mingchang.vm.QuestionViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-15 09:31
 *
 * Des   :QuestionHomeFragment
 * 错题集 position==-1
 * 正常答题
 */
class QuestionListFragment : AppFragment<FragmentQuestionListBinding, QuestionViewModel>() {
    private var position = 0
    private var title = ""
    override fun bindViewModelStore() = requireActivity().viewModelStore

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {

        }
    }


    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        position = getExtrasPosition(bundle, savedInstanceState)
        viewModel.apply {

        }
    }

    override fun loadData(refresh: Boolean) {
        viewModel.getUserInfo()
        viewModel.getQuestionList(position)
    }

    private fun showExchangeCurseDialog() {

    }

    companion object {
        fun navigate(
            fragment: Fragment, type: Int = 0, title: String = ""
        ) {
            fragment.findNavController().apply {
                navigate(
                    R.id.action_toQuestionList, Bundle().apply {
                        putInt(ConfigApp.EXTRAS_POSITION, type)
                        putString(ConfigApp.EXTRAS_TITLE, title)
                    }, NavOptions.Builder().build()
                )
            }
        }
    }

}