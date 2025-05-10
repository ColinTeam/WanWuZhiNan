package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.view.View
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogTipsBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 15:02
 *
 * Des   :TipsDialog
 */
class TipsDialog private constructor(
    private val title: CharSequence, private val tips: CharSequence
) : AppDialogFragment<DialogTipsBinding>() {
    var sure: ((View) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            tvTitle.text = title
            tvTips.text = tips
            onClick(btCancel, btSure) {
                when (it) {
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

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    companion object {
        const val TYPE_UNREGISTER = 0
        const val TYPE_VERSION = 1
        internal const val EXTRAS_TITLE = "extras_title"
        internal const val EXTRAS_TIPS = "extras_tips"

        @JvmStatic
        fun newInstance(type: Int): TipsDialog {
            val value = getTips(type)
            val bundle = Bundle().apply {
                putCharSequence(EXTRAS_TITLE, value.first)
                putCharSequence(EXTRAS_TIPS, value.second)
            }
            val fragment = TipsDialog(value.first, value.second)
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        fun newInstance(title: String, smg: String): TipsDialog {
            val bundle = Bundle().apply {
                putCharSequence(EXTRAS_TITLE, title)
                putCharSequence(EXTRAS_TIPS, smg)
            }
            val fragment = TipsDialog(title, smg)
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        private fun getTips(type: Int): Pair<CharSequence, CharSequence> {
            return Pair(
                ResourcesUtil.getString(R.string.dialog_tips_title),
                ResourcesUtil.getString(R.string.dialog_unregister)
            )
//            return when (type) {
//                TYPE_UNREGISTER -> Pair(
//                    ResourcesUtil.getString(R.string.dialog_tips_title),
//                    ResourcesUtil.getString(R.string.dialog_unregister)
//                )
//            }
        }
    }


}
