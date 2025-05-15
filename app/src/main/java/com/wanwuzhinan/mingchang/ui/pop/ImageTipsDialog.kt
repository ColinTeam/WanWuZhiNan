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
    private val type: Int, private val title: CharSequence? = null
) : AppDialogFragment<DialogImageTipsBinding>() {
    var sure: ((View) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            displayText(tvTitle, getTitle(type))
            displayText(tvSubtitle, getSubtitle(type))
            displayText(tvMsg, getMsg(type))
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
        if (type == TYPE_LOGOUT) getString(R.string.confirm)
        if (type == TYPE_QUESTION) getString(R.string.dialog_i_know)
        if (type == TYPE_EXCHANGE) getString(R.string.dialog_exchange_sure)
        return null
    }

    private fun getCancelText(type: Int): CharSequence? {
        if (type == TYPE_EXCHANGE) return getString(R.string.dialog_exchange_cancel)
        return null
    }


    private fun getTitle(type: Int): CharSequence? {
        return if (type == TYPE_LOGOUT) getString(R.string.dialog_logout_title)
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
        else if (type == TYPE_UPGRADE) getString(R.string.dialog_upgrade_msg)
        else null
    }

    private fun getOther(type: Int): CharSequence? {
        return if (type == TYPE_EXCHANGE) {
            //请先兑换课程后再学习
            val builder = SpannableStringBuilder("请先")
            val coloredPart = SpannableString("兑换课程")
            coloredPart.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.color_active, null)),
                0,
                coloredPart.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append(coloredPart).append("后再学习")
        } else null
    }

    companion object {
        const val TYPE_LOGOUT = 0
        const val TYPE_QUESTION = 1
        const val TYPE_EXCHANGE = 2
        const val TYPE_UPGRADE = 3

        @JvmStatic
        fun newInstance(type: Int, title: CharSequence? = null): ImageTipsDialog {
            val fragment = ImageTipsDialog(type, title)
            return fragment
        }

    }


}
