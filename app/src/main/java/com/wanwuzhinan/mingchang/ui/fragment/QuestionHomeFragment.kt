package com.wanwuzhinan.mingchang.ui.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.motion.MotionTouchLister
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentQuestionHomeBinding
import com.wanwuzhinan.mingchang.entity.Question
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.vm.QuestionViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-15 09:31
 *
 * Des   :QuestionHomeFragment
 * 物理万问
 * 龙门题库
 */
class QuestionHomeFragment : AppFragment<FragmentQuestionHomeBinding, QuestionViewModel>() {
    private var position = 0
    private var title = ""
    override fun bindViewModelStore() = requireActivity().viewModelStore

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        position = getExtrasPosition(bundle, savedInstanceState)
        title = bundle?.getString(ConfigApp.EXTRAS_TITLE, "") ?: ""
        viewBinding.apply {
            layout.background = getBg()
            ivQuestion.setImageResource(getBackground())
            val showError = position == ConfigApp.TYPE_ASK
            ivErrorBg.visible(showError)
            ivError.visible(showError)
            tvError.visible(showError)
            if (showError) ivErrorBg.setOnTouchListener(MotionTouchLister())

            onClick(one, two, three, four, five, ivErrorBg) {
                when (it) {
                    one, two, three, four, five -> check(getTag(it))
                    ivErrorBg -> {
                        QuestionListFragment.navigate(this@QuestionHomeFragment, -1, title = title)
                    }
                }
            }
        }
    }

    private fun getTag(view: View): Int {
        return when (val tag = view.tag) {
            is Int -> tag
            is String -> tag.toIntOrNull() ?: 0
            else -> 0
        }
    }

    //"哎呀，来早啦", "暂时还不能探索"
    private fun check(position: Int, list: List<Question>? = viewModel.getQuestionListValue()) {
        if (list == null || list.isEmpty() || position >= list.size) {
            showDialog()
            return
        }
        val question = list[position]
        if (question.has_power != 1) {
            showExchangeCurseDialog()
            return
        }
        if (question.questionsCount <= 0) {
            showDialog()
            return
        }
        QuestionListFragment.navigate(this, question.id, title = title)
    }

    private fun getBg(): Drawable? {
        return if (position == ConfigApp.TYPE_ASK) {
            ResourcesCompat.getDrawable(resources, R.mipmap.bg_ask_home, context?.theme)
        } else {
            ResourcesCompat.getDrawable(resources, R.mipmap.bg_practice_home, context?.theme)
        }
    }

    private fun getBackground(): Int {
        return if (position == ConfigApp.TYPE_ASK) {
            R.mipmap.ask_ti
        } else {
            R.mipmap.practice_ti
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            userInfo.observe {
                viewBinding.tvError.text = "${it.question_count_error}"
            }
        }
    }

    override fun loadData(refresh: Boolean) {
        viewModel.getUserInfo()
        viewModel.getQuestionList(position)
    }

    private fun showExchangeCurseDialog() {
        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_EXCHANGE).apply {
            cancel = {

            }
            sure = {

            }
        }.show(this)
    }

    private fun showDialog() {
        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_QUESTION).apply {
            sure = {

            }
        }.show(this)
    }

    companion object {
        fun navigate(
            fragment: Fragment, type: Int = ConfigApp.TYPE_ASK, title: String = ""
        ) {
            fragment.findNavController().apply {
                navigate(
                    R.id.action_toQuestionHome, Bundle().apply {
                        putInt(ConfigApp.EXTRAS_POSITION, type)
                        putString(ConfigApp.EXTRAS_TITLE, title)
                    }, NavOptions.Builder().build()
                )
            }
        }
    }

}