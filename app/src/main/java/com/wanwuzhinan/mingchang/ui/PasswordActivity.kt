package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.core.widget.doAfterTextChanged
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.FragmentPasswordBinding
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_FIND_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import com.wanwuzhinan.mingchang.vm.LoginViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 20:33
 *
 * Des   :PasswordActivity
 */
class PasswordActivity : AppActivity<FragmentPasswordBinding, LoginViewModel>() {

    private var phone: String? = null
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        phone = if (savedInstanceState != null) savedInstanceState.getString(ConfigApp.EXTRAS_TITLE)
        else bundle?.getString(ConfigApp.EXTRAS_TITLE)
        viewBinding.apply {
            etPhone.apply {
                val isEnable = !phone.isNullOrEmpty()
                this.setText(phone ?: "")
                if (!isEnable) this.inputType = InputType.TYPE_NULL
                else this.doAfterTextChanged { updateButton() }
                this.setFocusableInTouchMode(isEnable)
                this.isClickable = isEnable
                this.isEnabled = isEnable
            }
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
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewModel.apply {
            showLoading.observe {
                Log.i("showLoading:$it")
                showLoading(it)
            }
            showToast.observe {
                Log.i("showToast:$it")
                if (it.code == HTTP_CONFIRM) {
                    showConfirmDialog(it.msg)
                } else if (it.code == HTTP_SUCCESS) {
                    passwordSuccess()
                }
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
                if (it.status == 0) {
                    passwordSuccess()
                }
            }
        }
        updateButton()
    }


    override fun loadData(refresh: Boolean) {
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
            viewBinding.tvSmsSend.text = getString(R.string.login_sms_countdown, it)
        }, onFinish = {
            viewModel.updateSuccess(false)
        })
    }

    private fun showConfirmDialog(smg: String) {
        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_PASSWORD, msg = smg).apply {
            sure = { startConfirm(1) }
        }.show(this)
    }

    private fun passwordSuccess() {
        val phone = viewBinding.etPhone.text.toString().trim()
        MMKVUtils.set(Constant.USER_MOBILE, phone)
        val intent = Intent().apply {
            putExtra(Constant.USER_MOBILE, phone)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val PWD_LENGTH_MIN = 6
        const val PWD_LENGTH_MAX = 20
        const val PHONE_LENGTH = 11
        const val REQUEST_CODE_PASSWORD = 1001

        @JvmStatic
        fun start(context: Context, phone: String) {
            val starter = Intent(context, PasswordActivity::class.java).putExtra(
                ConfigApp.EXTRAS_TITLE, phone
            )
            context.startActivity(starter)
        }

        @JvmStatic
        fun getIntent(context: Context, phone: String?): Intent {
            return Intent(context, PasswordActivity::class.java).apply {
                putExtra(ConfigApp.EXTRAS_TITLE, phone)
            }
        }
    }
}