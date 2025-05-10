package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
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
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.HomeActivity.Companion.EXTRAS_POSITION
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.LoginViewModelV2

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:34
 *
 * Des   :LoginFragment
 */
class LoginFragment : AppFragment<FragmentLoginBinding, LoginViewModelV2>() {
    companion object {
        const val TAB_SMS = 0
        const val TAB_PWD = 1
        const val PHONE_LENGTH = 11

        @JvmStatic
        fun navigate(fragment: Fragment) {
            navigate(fragment.findNavController())
        }

        @JvmStatic
        fun navigate(activity: AppCompatActivity) {
            navigate(activity.findNavController(R.id.nav_host))
        }

        @JvmStatic
        fun navigate(controller: NavController) {
            val option =
                NavOptions.Builder().setPopUpTo(controller.graph.startDestinationId, true, true)
                    .setLaunchSingleTop(true).build()
            controller.navigate(R.id.action_toLogin, null, option)
        }
    }

    private var tabIndex = TAB_SMS

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            etPhone.setText(MMKVUtils.decodeString(Constant.USER_MOBILE).toString())
            etPhone.addTextChangedListener(textWatcher)
            etSMS.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
            onClick(
                tvSmsTips,
                tvPwdTips,
                tvSmsSend,
                btLogin,
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

                    btLogin -> {
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
                    updateTab(if (isSms) TAB_SMS else TAB_PWD)
                }


                override fun onTabUnselected(position: TabLayout.Tab?) {
                }

                override fun onTabReselected(position: TabLayout.Tab?) {
                }

            })
            rbProtocolTips.setOnCheckedChangeListener { _, _ -> updateButton() }
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
                Log.e("registerData:$it")
                ConfigApp.token = it.token
                MMKVUtils.encode(Constant.TOKEN, it.token)
                MMKVUtils.encode(
                    Constant.USER_MOBILE, viewBinding.etPhone.text.toString().toString()
                )
                HomeFragment.navigate(this@LoginFragment)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRAS_POSITION, tabIndex)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            updateTab(savedInstanceState.getInt(EXTRAS_POSITION, tabIndex))
        } else {
            updateTab(tabIndex)
            viewModel.getConfig()
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun updateTab(index: Int) {
        this.tabIndex = index
        val isSms = index == TAB_SMS
        viewBinding.tvSmsTips.isVisible = isSms
        viewBinding.etSMS.isVisible = isSms
        viewBinding.tvSmsSend.isVisible = isSms
        viewBinding.tvPwdTips.isVisible = !isSms
        viewBinding.etPassword.isVisible = !isSms
        viewBinding.tabLogin.getTabAt(tabIndex)?.select()
        updateButton()
    }

    private fun updateButton() {
        viewBinding.apply {
            val phoneNotEmpty = etPhone.text.toString().trim().isNotEmpty()
            if (tabIndex == TAB_SMS) {
                tvSmsSend.isEnabled = phoneNotEmpty
                btLogin.isSelected = phoneNotEmpty && etSMS.text.toString().trim()
                    .isNotEmpty() && rbProtocolTips.isChecked
            } else {
                btLogin.isSelected = phoneNotEmpty && etPassword.text.toString().trim()
                    .isNotEmpty() && rbProtocolTips.isChecked
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
        val type = if (isPhone()) HTTP_LOGIN_DEVICE_PHONE else HTTP_LOGIN_DEVICE_TABLET
        if (tabIndex == TAB_SMS) {
            viewModel.loginBySms(phone, text, type)
        } else {
            viewModel.loginByPassword(phone, null, text, text, HTTP_ACTION_LOGIN_PWD, type, 0)
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
            if (isAdded && !isDetached && isVisible) {
                viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
            }
        }, onFinish = {
            if (isAdded && !isDetached && isVisible) {
                viewModel.updateSuccess(false)
            }
        })
    }


}