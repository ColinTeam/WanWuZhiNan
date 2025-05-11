package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.widget.wheel.ArrayWheelAdapter
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogChooseBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 09:22
 *
 * Des   :ChooseDialog
 */
class ChooseDialog private constructor(
    private val title: CharSequence, private val array: List<String>
) : AppDialogFragment<DialogChooseBinding>() {

    var sure: ((Int) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    companion object {
        const val TYPE_SEX = 0
        const val TYPE_GRADE = 1
        const val EXTRAS_TITLE = "EXTRAS_TITLE"

        @JvmStatic
        fun newInstance(title: CharSequence, array: List<String>): ChooseDialog {
            val bundle = Bundle().apply {
                putCharSequence(EXTRAS_TITLE, title)
            }
            val fragment = ChooseDialog(title, array)
            fragment.arguments = bundle
            fragment.height = 0.75f
            fragment.gravity = Gravity.BOTTOM
            return fragment
        }
    }

    private var selected = Constants.INVALID
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            textTitle.text = title
            wheel.setAdapter(ArrayWheelAdapter<String>(array))
            wheel.setOnItemSelectedListener { position ->
                selected = position
            }
        }
        onClick(viewBinding.textCancel, viewBinding.textSure) {
            when (it) {
                viewBinding.textCancel -> {
                    dismiss()
                    cancel.invoke(it)
                }

                viewBinding.textSure -> {
                    sure.invoke(if (selected == Constants.INVALID) viewBinding.wheel.getSelectedPosition() else selected)
                    dismiss()
                }
            }
        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }
}