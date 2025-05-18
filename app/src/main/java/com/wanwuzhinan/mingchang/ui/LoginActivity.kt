package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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
import com.wanwuzhinan.mingchang.databinding.ActivityLoginBinding
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_SMS
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ext.visible
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 17:58
 *
 * Des   :LoginActivity
 */
class LoginActivity : AppActivity<ActivityLoginBinding, LoginViewModel>() {


    private var tabIndex = TAB_SMS

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
                finish()
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

    private fun updateTab(index: Int) {
        this.tabIndex = index
        val isSms = index == TAB_SMS
        viewBinding.apply {
            tvSmsSend.isVisible = isSms
            tvSmsTips.isVisible = isSms
            tvPwdTips.isVisible = !isSms
            setActivated(etSMS, isSms)
            setActivated(etPassword, !isSms)
            tabLogin.getTabAt(tabIndex)?.select()
        }
        updateButton()
    }

    private fun setActivated(
        view: AppCompatEditText, activated: Boolean
    ) {
        view.isFocusableInTouchMode = activated
        view.isCursorVisible = activated
        view.isEnabled = activated
        view.visible(activated)
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
        val sms = viewBinding.etSMS.text.toString().trim()
        if (tabIndex == TAB_SMS && sms.isEmpty()) {
            ToastUtil.show(getString(R.string.login_toast_sms))
            return
        }
        val pwd = viewBinding.etPassword.text.toString().trim()
        if (tabIndex == TAB_PWD && pwd.isEmpty()) {
            ToastUtil.show(getString(R.string.login_toast_pwd))
            return
        }
        val type = if (isPhone()) HTTP_LOGIN_DEVICE_PHONE else HTTP_LOGIN_DEVICE_TABLET
        val action = if (tabIndex == TAB_SMS) HTTP_ACTION_LOGIN_SMS else HTTP_ACTION_LOGIN_PWD
        Log.i("phone:$phone sms:$sms pwd:$pwd type:$type action:$action")
        viewModel.login(phone, sms, pwd, type, action)
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
            val phone = result.data?.getStringExtra(PasswordActivity.EXTRAS_PHONE) ?: ""
            val pwd = result.data?.getStringExtra(PasswordActivity.EXTRAS_PWD) ?: ""
            viewBinding.etPhone.setText(phone)
            viewBinding.etPassword.setText(pwd)
        }
    }
}