package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentPasswordBinding
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.fragment.LoginFragment.Companion.PHONE_LENGTH
import com.wanwuzhinan.mingchang.ui.pop.TipsDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 19:47
 *
 * Des   :PasswordFragment
 */
class PasswordFragment : AppFragment<FragmentPasswordBinding, LoginViewModel>() {
    companion object {
        const val PWD_LENGTH_MIN = 6
        const val PWD_LENGTH_MAX = 20
        const val EXTRA_LOGIN_STATE = "EXTRA_LOGIN_STATE"
    }

    private var isLogin = false
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        val phone = MMKVUtils.decodeString(Constant.USER_MOBILE)
        isLogin = ConfigApp.token.isNotEmpty() && phone.isNotEmpty()
        viewBinding.apply {
            etPhone.setText(if (isLogin) phone else "")
            etPhone.doAfterTextChanged { updateButton() }
            etSMS.doAfterTextChanged { updateButton() }
            etPassword.doAfterTextChanged { updateButton() }
            etPasswordConfirm.doAfterTextChanged { updateButton() }

            onClick(tvSmsSend, btConfirm) {
                when (it) {
                    tvSmsSend -> startSendSms(etPhone.text.toString().trim())
                    btConfirm -> startConfirm()
                }

            }
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
//                    HomeFragment.navigate(this@PasswordFragment)
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
            val enable = etPhone.text.toString().trim().isNotEmpty()
            tvSmsSend.isEnabled = enable
            val smsNotEmpty = etSMS.text.toString().trim().isNotEmpty()
            val pwdNotEmpty = etPassword.text.toString().trim().isNotEmpty()
            val confirmNotEmpty = etPasswordConfirm.text.toString().trim().isNotEmpty()
            btConfirm.isSelected = enable && smsNotEmpty && pwdNotEmpty && confirmNotEmpty
        }
    }

    private fun startConfirm(confirm: Int = 0) {
        val phone = viewBinding.etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            ToastUtil.show(R.string.login_toast_phone)
            return
        }
        if (phone.length < PHONE_LENGTH) {
            ToastUtil.show(R.string.login_toast_short)
            return
        }
        val sms = viewBinding.etSMS.text.toString().trim()
        if (sms.isEmpty()) {
            ToastUtil.show(R.string.login_toast_sms)
            return
        }

        val pwd = viewBinding.etPassword.text.toString().trim()
        if (pwd.isEmpty()) {
            ToastUtil.show(R.string.login_toast_pwd)
            return
        }
        if (pwd.length < PWD_LENGTH_MIN) {
            ToastUtil.show(R.string.login_toast_pwd_min)
            return
        }
        if (pwd.length > PWD_LENGTH_MAX) {
            ToastUtil.show(R.string.login_toast_pwd_max)
            return
        }
        val confirmPWD = viewBinding.etPasswordConfirm.text.toString().trim()
        if (confirmPWD.isEmpty()) {
            ToastUtil.show(R.string.login_toast_pwd)
            return
        }
        if (confirmPWD != pwd) {
            ToastUtil.show(R.string.login_toast_same)
            return
        }
        val type = if (isPhone()) HTTP_LOGIN_DEVICE_PHONE else HTTP_LOGIN_DEVICE_TABLET

//        viewModel.loginByPassword(
//            phone, sms, pwd, confirmPWD, HTTP_ACTION_LOGIN_FIND_PWD, type, confirm
//        )
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

    private fun startCountDown() {
        countDown(60, onNext = {
            viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
        }, onFinish = {
            viewModel.updateSuccess(false)
        })
    }

    private fun showConfirmDialog(smg: String) {
        TipsDialog.newInstance(getString(R.string.dialog_tips_title), smg).apply {
            sure = {
                startConfirm(1)
            }
        }.show(this)
    }
}