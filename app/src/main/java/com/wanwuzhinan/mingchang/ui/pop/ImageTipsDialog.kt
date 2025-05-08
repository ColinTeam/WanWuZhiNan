package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.view.View
import com.colin.library.android.utils.Constants
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
    private val type: Int, private val extra: Int = Constants.INVALID
) : AppDialogFragment<DialogImageTipsBinding>() {
    var sure: ((View) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            if (type == TYPE_LOGOUT) {
                tvTitle.text = getString(R.string.dialog_logout_title)
                tvSubTitle.text = getString(R.string.dialog_logout_subtitle)
                tvTips.text = getString(R.string.dialog_logout_tips)
                btSure.text = getString(R.string.confirm)
            } else {
                tvTitle.text = getString(R.string.dialog_upgrade_title)
                tvSubTitle.text = getString(R.string.dialog_upgrade_subtitle)
                tvTips.text = getString(R.string.dialog_upgrade_tips)
                btSure.text = getString(R.string.dialog_i_know)
                val hide = extra == 1
                ivCancel.visible(!hide)
                btSure.visible(!hide)
            }

            onClick(ivCancel, btSure) {
                when (it) {
                    ivCancel -> {
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
        const val TYPE_LOGOUT = 0
        const val TYPE_UPGRADE = 1

        @JvmStatic
        fun newInstance(type: Int): ImageTipsDialog {
            val fragment = ImageTipsDialog(type)
            return fragment
        }

    }


}
