package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.databinding.DialogLoadingBinding

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 09:22
 *
 * Des   :ChooseDialog
 */
class LoadingDialog private constructor() : AppDialogFragment<DialogLoadingBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(cancellable: Boolean = false): LoadingDialog {
            val fragment = LoadingDialog()
            fragment.isCancelable = cancellable
            return fragment
        }
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {

    }
}