package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.google.android.material.tabs.TabLayout
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentLoginBinding
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.utils.setData
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
        const val PHONE_LENGTH = 11
        const val DEVICE_PHONE = "1"
        const val DEVICE_OTHER = "2"

        @JvmStatic
        fun navigate(fragment: Fragment) {
            val controller = fragment.findNavController()
            val option =
                NavOptions.Builder().setPopUpTo(controller.graph.startDestinationId, true, false)
                    .setLaunchSingleTop(true).build()
            controller.navigate(R.id.action_toLogin, null, option)
        }

        @JvmStatic
        fun navigate(activity: AppCompatActivity) {
            val controller = activity.findNavController(R.id.nav_host)
            val option =
                NavOptions.Builder().setPopUpTo(controller.graph.startDestinationId, true, false)
                    .setLaunchSingleTop(true).build()
            controller.navigate(R.id.action_toLogin, null, option)
        }
    }

    private var tabIndex = TAB_SMS

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
        viewBinding.apply {
            etPhone.setText(MMKVUtils.decodeString(Constant.USER_MOBILE).toString())
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
        viewModel.apply {
            configData.observe {
                Log.d("configData:$it")
            }
            showLoading.observe {
                Log.d("showLoading:$it")
                showLoading(it)
            }
            smsSuccess.observe {
                Log.d("smsSuccess:$it")
                if (it) {
                    viewBinding.tvSmsSend.isEnabled = false
                    viewBinding.tvSmsSend.alpha = 0.3F
                    startCountDown()
                } else {
                    viewBinding.tvSmsSend.apply {
                        isEnabled = true
                        text = getString(R.string.login_sms_action)
                        alpha = 1.0F
                    }
                }
            }
            registerData.observe {
                if (it.token.isNotEmpty()) {
                    setData(Constant.USER_MOBILE, viewBinding.etPhone.text.toString().trim())
                    setData(Constant.TOKEN, it.token)
                    HomeFragment.navigate(this@LoginFragment)
                }
            }
        }
        updateButton()
    }


    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.getConfig()
    }

    private fun updateButton() {
        viewBinding.apply {
            val basic = rbProtocolTips.isChecked && etPhone.text.toString().trim().isNotEmpty()
            if (tabIndex == TAB_SMS) {
                val enable = etPhone.text.toString().trim().isNotEmpty()
                tvSmsSend.isEnabled = enable
                buttonLogin.isSelected = basic && enable
            } else {
                buttonLogin.isSelected = basic && etPassword.text.toString()
                    .trim().length > PasswordFragment.PWD_LENGTH_MIN
            }
        }
    }

    private fun startSendSms(phone: String) {
        if (phone.isEmpty()) {
            ToastUtil.show(R.string.login_toast_phone)
            return
        }
        if (phone.length < PHONE_LENGTH) {
            ToastUtil.show(R.string.login_toast_short)
            return
        }
        viewModel.getSms(phone)
    }

    private fun startLogin() {
        val phone = viewBinding.etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            ToastUtil.show(R.string.login_toast_phone)
            return
        }
        if (phone.length < PHONE_LENGTH) {
            ToastUtil.show(R.string.login_toast_short)
            return
        }

        if (!viewBinding.rbProtocolTips.isChecked) {
            ToastUtil.show(R.string.login_toast_protocol)
            return
        }
        val text = if (tabIndex == TAB_SMS) {
            viewBinding.etSMS.text.toString().trim()
        } else viewBinding.etPassword.text.toString().trim()
        if (text.isEmpty()) {
            ToastUtil.show(if (tabIndex == TAB_SMS) R.string.login_toast_sms else R.string.login_toast_pwd)
            return
        }
        val type = if (isPhone()) DEVICE_PHONE else DEVICE_OTHER
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

    private fun startCountDown() {
        countDown(60, onNext = {
            viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
        }, onFinish = {
            viewModel.updateSuccess(false)
        })
    }
}