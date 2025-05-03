package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import com.colin.library.android.utils.ext.onClick
import com.google.android.material.tabs.TabLayout
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.databinding.FragmentLoginBinding
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class LoginFragment : AppFragment<FragmentLoginBinding, LoginViewModel>() {
    companion object {
        const val TAB_SMS = 0
        const val TAB_PWD = 1
    }

    private var tabIndex = TAB_SMS

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            onClick(
                tvSmsTips,
                tvPwdTips,
                tvSmsSend,
                buttonLogin,
                tvProtocolLink1,
                tvProtocolLink2,
                tvProtocolLink3
            ) {
                when (it) {
                    tvSmsTips -> {
                        // TODO:
                    }

                    tvPwdTips -> {
                        // TODO:
                    }

                    tvSmsSend -> {
                        // TODO:
                    }

                    buttonLogin -> {
                        // TODO:
                    }

                    tvProtocolLink1 -> {
                        // TODO:USER_AGREEMENT
                    }

                    tvProtocolLink2 -> {
                        // TODO:PRIVACY_POLICY
                    }

                    tvProtocolLink3 -> {
                        // TODO:PRIVACY_CHILD
                    }
                }
            }

            tabLogin.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val isSms = tab?.text == getString(R.string.login_by_sms)
                    tvSmsTips.isVisible = isSms
                    etSMS.isVisible = isSms
                    tvSmsSend.isVisible = isSms

                    tvPwdTips.isVisible = !isSms
                    etPassword.isVisible = !isSms
                    tabIndex = if (isSms) TAB_SMS else TAB_PWD
                    updateButton()
                }


                override fun onTabUnselected(position: TabLayout.Tab?) {
                }

                override fun onTabReselected(position: TabLayout.Tab?) {
                }

            })

        }
        updateButton()
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {


    }

    private fun updateButton() {
        viewBinding.apply {
            val basic = rbProtocolTips.isChecked && (etPhone.text?.length ?: 0) > 0
            if (tabIndex == TAB_SMS) {
                buttonLogin.isEnabled = basic && (etSMS.text?.length ?: 0) > 0
            } else {
                buttonLogin.isEnabled =
                    basic && (etPassword.text?.length ?: 0) > PasswordFragment.PWD_LENGTH_MIN
            }
        }
    }
}