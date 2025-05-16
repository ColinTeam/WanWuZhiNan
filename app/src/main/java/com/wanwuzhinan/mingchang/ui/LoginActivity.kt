package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.google.android.material.tabs.TabLayout
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.config.ConfigApp.EXTRAS_POSITION
import com.wanwuzhinan.mingchang.databinding.FragmentLoginBinding
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM_1
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.fragment.LoginFragment
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 17:58
 *
 * Des   :LoginActivity
 */
class LoginActivity : AppActivity<FragmentLoginBinding, LoginViewModel>() {


    private var tabIndex = LoginFragment.Companion.TAB_SMS

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            etPhone.setText(MMKVUtils.decodeString(Constant.USER_MOBILE).toString())
            etPhone.doAfterTextChanged { updateButton() }
            etSMS.doAfterTextChanged { updateButton() }
            etPassword.doAfterTextChanged { updateButton() }
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
                        WebViewActivity.start(
                            this@LoginActivity, url = getConfigData().code_verification
                        )
                    }

                    tvPwdTips -> {
                        toPassword()
                    }

                    tvSmsSend -> {
                        startSendSms(etPhone.text.toString().trim())
                    }

                    btLogin -> {
                        startLogin()
                    }

                    tvProtocolLink1 -> {
                        WebViewActivity.start(
                            this@LoginActivity,
                            url = ConfigApp.USER_AGREEMENT,
                            title = getString(R.string.login_protocol_link_1)
                        )
                    }

                    tvProtocolLink2 -> {
                        WebViewActivity.start(
                            this@LoginActivity,
                            url = ConfigApp.PRIVACY_POLICY,
                            title = getString(R.string.login_protocol_link_2)
                        )
                    }

                    tvProtocolLink3 -> {
                        WebViewActivity.start(
                            this@LoginActivity,
                            url = ConfigApp.PRIVACY_CHILD,
                            title = getString(R.string.login_protocol_link_3)
                        )
                    }
                }
            }

            tabLogin.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val isSms = tab?.text == getString(R.string.login_by_sms)
                    updateTab(if (isSms) LoginFragment.Companion.TAB_SMS else LoginFragment.Companion.TAB_PWD)
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
                Log.i("configData:$it")
            }
            smsSuccess.observe {
                Log.i("smsSuccess:$it")
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
                ConfigApp.token = it.token
                MMKVUtils.encode(Constant.TOKEN, it.token)
                MMKVUtils.encode(Constant.USER_MOBILE, viewBinding.etPhone.text.toString())
                HomeActivity.start(this@LoginActivity)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRAS_POSITION, tabIndex)
    }

    override fun interceptorHttpAction(action: HttpResult.Action): Boolean {
        if (action.code == HTTP_CONFIRM || action.code == HTTP_CONFIRM_1) {
            showConfirmDialog(action.msg)
            return true
        }
        return false
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            updateTab(savedInstanceState.getInt(EXTRAS_POSITION, tabIndex))
        } else {
            updateTab(tabIndex)
            viewModel.getConfig()
        }
    }

    private fun updateTab(index: Int) {
        this.tabIndex = index
        val isSms = index == LoginFragment.Companion.TAB_SMS
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
            if (tabIndex == LoginFragment.Companion.TAB_SMS) {
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
        if (phone.length < LoginFragment.Companion.PHONE_LENGTH) {
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
        if (phone.length < LoginFragment.Companion.PHONE_LENGTH) {
            ToastUtil.show(R.string.login_toast_short)
            return
        }

        if (!viewBinding.rbProtocolTips.isChecked) {
            ToastUtil.show(R.string.login_toast_protocol)
            return
        }
        val text = if (tabIndex == LoginFragment.Companion.TAB_SMS) {
            viewBinding.etSMS.text.toString().trim()
        } else viewBinding.etPassword.text.toString().trim()
        if (text.isEmpty()) {
            ToastUtil.show(if (tabIndex == LoginFragment.Companion.TAB_SMS) R.string.login_toast_sms else R.string.login_toast_pwd)
            return
        }
        val type = if (isPhone()) HTTP_LOGIN_DEVICE_PHONE else HTTP_LOGIN_DEVICE_TABLET
        if (tabIndex == LoginFragment.Companion.TAB_SMS) {
            viewModel.loginBySms(phone, text, type)
        } else {
            viewModel.loginByPassword(phone, null, text, text, HTTP_ACTION_LOGIN_PWD, type, 0)
        }

    }

    private fun startCountDown() {
        countDown(60, onNext = {
            viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
        }, onFinish = {
            viewModel.updateSuccess(false)
        })
    }

    companion object {
        const val TAB_SMS = 0
        const val TAB_PWD = 1
        const val PHONE_LENGTH = 11

        @JvmStatic
        fun start(context: Context, position: Int = TAB_SMS) {
            val starter = Intent(context, LoginActivity::class.java).putExtra(
                ConfigApp.EXTRAS_POSITION, position
            )
            context.startActivity(starter)
        }
    }

    private fun showConfirmDialog(smg: String) {
        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_PASSWORD, msg = smg).apply {
            sure = { toPassword() }
        }.show(this)
    }

    fun toPassword() {
        launcher.launch(
            PasswordActivity.getIntent(
                this@LoginActivity,
                viewBinding.etPhone.text.toString().trim(),
                viewBinding.etPassword.text.toString().trim()
            )
        )
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.e("result:${result.resultCode} $result")
        if (result.resultCode == Activity.RESULT_OK) {
            val phone = result.data?.getStringExtra(Constant.USER_MOBILE) ?: ""
            if (!phone.isEmpty()) {
                viewBinding.etPhone.setText(phone)
                MMKVUtils.encode(Constant.USER_MOBILE, phone)
            }
        }
    }
}