package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentPasswordBinding
import com.wanwuzhinan.mingchang.vm.LoginViewModelV2

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 19:47
 *
 * Des   :PasswordFragment
 */
class PasswordFragment : AppFragment<FragmentPasswordBinding, LoginViewModelV2>() {
    companion object {
        const val PWD_LENGTH_MIN = 6
        const val PWD_LENGTH_MAX = 20
    }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getConfig()

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            updateButton()
        }

        private fun updateButton() {

        }

    }
}