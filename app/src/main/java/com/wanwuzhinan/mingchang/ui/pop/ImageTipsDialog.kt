package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogImageTipsBinding
import com.wanwuzhinan.mingchang.ext.visible


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 15:02
 *
 * Des   :TipsDialog
 */
class ImageTipsDialog private constructor(
    private val type: Int,
    private val title: CharSequence? = null,
    private val msg: CharSequence? = null
) : AppDialogFragment<DialogImageTipsBinding>() {
    var sure: ((View) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            displayText(tvTitle, title ?: getTitle(type))
            displayText(tvSubtitle, getSubtitle(type))
            displayText(tvMsg, msg ?: getMsg(type))
            displayText(tvOther, getOther(type))
            displayText(btSure, getSureText(type))
            displayText(btCancel, getCancelText(type))
            onClick(ivCancel, btSure, btCancel) {
                when (it) {
                    ivCancel -> {
                        dismiss()
                    }

                    btCancel -> {
                        dismiss()
                        cancel.invoke(it)
                    }

                    btSure -> {
                        dismiss()
                        sure.invoke(it)
                    }
                }
            }
        }
    }

    private fun displayText(view: TextView, title: CharSequence?) {
        view.text = title ?: ""
        view.visible(!title.isNullOrEmpty())
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    private fun getSureText(type: Int): CharSequence? {
        return if (type == TYPE_LOGOUT) getString(R.string.confirm)
        else if (type == TYPE_QUESTION) getString(R.string.dialog_i_know)
        else if (type == TYPE_EXCHANGE) getString(R.string.dialog_exchange_sure)
        else if (type == TYPE_PASSWORD) getString(R.string.sure)
        else null
    }

    private fun getCancelText(type: Int): CharSequence? {
        return if (type == TYPE_EXCHANGE) getString(R.string.dialog_exchange_cancel)
        else if (type == TYPE_PASSWORD) getString(R.string.cancel)
        else null
    }


    private fun getTitle(type: Int): CharSequence? {
        return if (type == TYPE_LOGOUT) getString(R.string.dialog_logout_title)
        else if (type == TYPE_PASSWORD) getString(R.string.dialog_question_title)
        else if (type == TYPE_QUESTION) getString(R.string.dialog_question_title)
        else if (type == TYPE_UPGRADE) getString(R.string.dialog_upgrade_title)
        else null
    }

    private fun getSubtitle(type: Int): CharSequence? {
        return if (type == TYPE_LOGOUT) getString(R.string.dialog_logout_subtitle)
        else if (type == TYPE_QUESTION) getString(R.string.dialog_question_subtitle)
        else if (type == TYPE_UPGRADE) getString(R.string.dialog_upgrade_subtitle)
        else null

    }

    private fun getMsg(type: Int): CharSequence? {
        return if (type == TYPE_LOGOUT) getString(R.string.dialog_logout_msg)
        else if (type == TYPE_QUESTION) getString(R.string.dialog_question_msg)
        else if (type == TYPE_EXCHANGE) getString(R.string.dialog_question_subtitle)
        else if (type == TYPE_UPGRADE) getString(R.string.dialog_upgrade_msg)
        else null
    }

    private fun getOther(type: Int): CharSequence? {
        return if (type == TYPE_EXCHANGE) {
            val builder = SpannableStringBuilder(getString(R.string.dialog_exchange_other_pro))
            val coloredPart = SpannableString(getString(R.string.dialog_exchange_title))
            coloredPart.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.color_active, null)),
                0,
                coloredPart.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append(coloredPart).append(getString(R.string.dialog_exchange_other_nex))
        } else null
    }


    companion object {
        const val TYPE_LOGOUT = 0   //退出登录提示
        const val TYPE_PASSWORD = 1 //修改密码，后台引起弹框提示
        const val TYPE_QUESTION = 2 //回答问题，点击异常提示
        const val TYPE_EXCHANGE = 3 //兑换课程提示
        const val TYPE_UPGRADE = 4

        @JvmStatic
        fun newInstance(
            type: Int, title: CharSequence? = null, msg: CharSequence? = null
        ): ImageTipsDialog {
            return ImageTipsDialog(type, title, msg)
        }

    }


}
