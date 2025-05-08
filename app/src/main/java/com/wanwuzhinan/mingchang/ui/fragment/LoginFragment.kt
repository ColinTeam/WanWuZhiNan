package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.ext.onClick
import com.google.android.material.tabs.TabLayout
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentLoginBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.vm.LoginViewModelV2

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :HomeFragment
 */
class LoginFragment : AppFragment<FragmentLoginBinding, LoginViewModelV2>() {
    companion object {
        const val TAB_SMS = 0
        const val TAB_PWD = 1


    }

    override fun bindViewModelStore(): ViewModelStore {
        return requireActivity().viewModelStore
    }

    private var tabIndex = TAB_SMS

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })
        viewBinding.apply {
            etPhone.addTextChangedListener(textWatcher)
            etSMS.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
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
                        WebFragment.navigate(
                            this@LoginFragment, url = getConfigData().code_verification
                        )
                    }

                    tvPwdTips -> {
                        findNavController().navigate(R.id.action_toPassword)
                    }

                    tvSmsSend -> {
                        startSendSms(etPhone.text.toString().trim())
                    }

                    buttonLogin -> {
                        startLogin()
                    }

                    tvProtocolLink1 -> {
                        WebFragment.navigate(
                            this@LoginFragment,
                            url = ConfigApp.USER_AGREEMENT,
                            title = getString(R.string.login_protocol_link_1)
                        )
                    }

                    tvProtocolLink2 -> {
                        WebFragment.navigate(
                            this@LoginFragment,
                            url = ConfigApp.PRIVACY_POLICY,
                            title = getString(R.string.login_protocol_link_2)
                        )
                    }

                    tvProtocolLink3 -> {
                        WebFragment.navigate(
                            this@LoginFragment,
                            url = ConfigApp.PRIVACY_CHILD,
                            title = getString(R.string.login_protocol_link_3)
                        )
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
        viewModel.apply {
            configData.observe {
                Log.d("configData:$it")
            }
            showLoading.observe {
                Log.d("showLoading:$it")
                showLoading(it)
            }
        }
        viewModel.getConfig()
    }

    private fun updateButton() {
        viewBinding.apply {
            val basic = rbProtocolTips.isChecked && (etPhone.text?.length ?: 0) > 0
            if (tabIndex == TAB_SMS) {
                tvSmsSend.isEnabled = (etSMS.text?.length ?: 0) > 0
                buttonLogin.isSelected = basic && (etSMS.text?.length ?: 0) > 0
            } else {
                buttonLogin.isSelected =
                    basic && (etPassword.text?.length ?: 0) > PasswordFragment.PWD_LENGTH_MIN
            }
        }
    }

    private fun startSendSms(phone: String) {
        if (phone.isEmpty()) {
            ToastUtil.show("请输入手机号码")
            return
        }
        if (phone.length < 11) {
            ToastUtil.show("请输入正确手机号码")
            return
        }
        viewModel.getSms(phone)
    }

    private fun startLogin() {
        val phone = viewBinding.etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            ToastUtil.show("请输入手机号码")
            return
        }
        if (phone.length < 11) {
            ToastUtil.show("请输入正确手机号码")
            return
        }

        if (!viewBinding.rbProtocolTips.isChecked) {
            ToastUtil.show("请阅读并同意《用户协议》和《隐私协议》")
            return
        }
        val text = if (tabIndex == TAB_SMS) {
            viewBinding.etSMS.text.toString().trim()
        } else viewBinding.etPassword.text.toString().trim()
        if (text.isEmpty()) {
            ToastUtil.show("请输入对应内容")
            return
        }
        val type = if(isPhone()) "1" else "2"
        if (tabIndex == TAB_SMS) {
            viewModel.loginBySms(phone, text, type)
        } else {
            viewModel.loginByPassword(phone, text, type)
        }

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            updateButton()
        }

    }

}