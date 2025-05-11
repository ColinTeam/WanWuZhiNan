package com.wanwuzhinan.mingchang.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentPasswordBinding
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_FIND_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.fragment.LoginFragment.Companion.PHONE_LENGTH
import com.wanwuzhinan.mingchang.ui.pop.TipsDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
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
        const val EXTRA_LOGIN_STATE = "EXTRA_LOGIN_STATE"

        @JvmStatic
        fun navigate(fragment: Fragment, login: Boolean = false) {
//            fragment.findNavController().apply {
//                navigate(
//                    R.id.action_toPassword, Bundle().apply {
//                        putBoolean(EXTRA_LOGIN_STATE, login)
//                    }, NavOptions.Builder().build()
//                )
//            }
        }
    }

    private var isLogin = false
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
//        isLogin = bundle?.getBoolean(EXTRA_LOGIN_STATE, false) == true
        val phone = MMKVUtils.decodeString(Constant.USER_MOBILE)
        isLogin = ConfigApp.token.isNotEmpty() && phone.isNotEmpty()
        viewBinding.apply {
            etPhone.setText(if (isLogin) phone else "")
            etPhone.addTextChangedListener(textWatcher)
            etSMS.addTextChangedListener(textWatcher)
            etPassword.addTextChangedListener(textWatcher)
            etPasswordConfirm.addTextChangedListener(textWatcher)
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
            showToast.observe {
                Log.d("showToast:$it")
                if (it.code == HTTP_CONFIRM) {
                    showConfirmDialog(it.msg)
                } else if (it.code == HTTP_SUCCESS) {
                    HomeFragment.navigate(this@PasswordFragment)
                }
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
                    HomeFragment.navigate(this@PasswordFragment)
                }
            }
        }
        updateButton()
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

        viewModel.loginByPassword(
            phone, sms, pwd, confirmPWD, HTTP_ACTION_LOGIN_FIND_PWD, type, confirm
        )
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
            if (isAdded && !isDetached && isVisible) {
                viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
            }

        }, onFinish = {
            if (isAdded && !isDetached && isVisible) {
                viewModel.updateSuccess(false)
            }
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