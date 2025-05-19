package com.wanwuzhinan.mingchang.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.core.widget.doAfterTextChanged
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.utils.Log
import com.colin.library.android.utils.ToastUtil
import com.colin.library.android.utils.countDown
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppActivity
import com.wanwuzhinan.mingchang.databinding.ActivityPasswordBinding
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_FIND_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_CONFIRM
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_PHONE
import com.wanwuzhinan.mingchang.entity.HTTP_LOGIN_DEVICE_TABLET
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.pop.ImageTipsDialog
import com.wanwuzhinan.mingchang.utils.removeChinese
import com.wanwuzhinan.mingchang.vm.LoginViewModel
import java.util.regex.Pattern

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 20:33
 *
 * Des   :PasswordActivity
 */
class PasswordActivity : AppActivity<ActivityPasswordBinding, LoginViewModel>() {
    // 自定义 InputFilter，过滤中文字符
    val filterChinese = InputFilter { source, start, end, dest, dstart, dend ->
        val regex = "[\\u4e00-\\u9fa5]" // 匹配中文字符的正则表达式
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(source)
        if (matcher.find()) {
            "" // 如果包含中文字符，返回空字符串
        } else {
            null // 否则保留输入
        }
    }
    private val filters = arrayOf(filterChinese)
    private var phone: String? = null
    private var pwd: String? = null
    private var login: Boolean = false
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        phone = if (savedInstanceState != null) savedInstanceState.getString(EXTRAS_PHONE)
        else bundle?.getString(EXTRAS_PHONE)
        pwd = if (savedInstanceState != null) savedInstanceState.getString(EXTRAS_PWD)
        else bundle?.getString(EXTRAS_PWD)
        login = savedInstanceState?.getBoolean(EXTRAS_LOGIN, false) ?: (bundle?.getBoolean(
            EXTRAS_LOGIN
        ) == true)
        viewBinding.apply {
            etPhone.apply {
                setText(phone ?: "")
                //登录不能编辑，非登录可以编辑
                if (login) this.clearFocus()
                else this.doAfterTextChanged { updateButton() }
                isFocusableInTouchMode = !login
                isCursorVisible = !login
                isEnabled = !login
            }
            etPassword.setText(pwd ?: "")
            etPasswordConfirm.setText(pwd ?: "")
            etSMS.doAfterTextChanged { updateButton() }
            etPassword.doAfterTextChanged { updateButton() }
            etPassword.doAfterTextChanged { it.removeChinese() }
            etPasswordConfirm.doAfterTextChanged { updateButton() }
            etPassword.filters = filters
            etPasswordConfirm.filters = filters
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
                if (it.status == HTTP_SUCCESS) {
                    passwordSuccess()
                }
            }
        }
        updateButton()
    }

    override fun interceptorHttpAction(action: HttpResult.Action): Boolean {
        if (action.code == HTTP_CONFIRM) {
            showConfirmDialog(action.msg)
            return true
        }
        return false
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
        viewModel.login(phone, sms, pwd, type, HTTP_ACTION_LOGIN_FIND_PWD, confirm)
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
        ImageTipsDialog.newInstance(ImageTipsDialog.TYPE_PASSWORD_CONFIRM, bgMsg = smg).apply {
            isCancelable = false
            sure = { startConfirm(1) }
        }.show(this)
    }

    private fun passwordSuccess() {
        val phone = viewBinding.etPhone.text.toString().trim()
        val pwd = viewBinding.etPasswordConfirm.text.toString().trim()
        val intent = Intent().apply {
            putExtra(EXTRAS_PHONE, phone)
            putExtra(EXTRAS_PWD, pwd)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val PWD_LENGTH_MIN = 6
        const val PWD_LENGTH_MAX = 20
        const val PHONE_LENGTH = 11
        const val EXTRAS_PHONE = "phone"
        const val EXTRAS_PWD = "pwd"
        const val EXTRAS_LOGIN = "login"

        @JvmStatic
        fun start(context: Context, phone: String?) {
            context.startActivity(getIntent(context, phone, null, true))
        }

        @JvmStatic
        fun getIntent(
            context: Context, phone: String? = null, pwd: String? = null, login: Boolean = false
        ): Intent {
            return Intent(context, PasswordActivity::class.java).apply {
                putExtra(EXTRAS_LOGIN, login)
                phone?.let { putExtra(EXTRAS_PHONE, it) }
                pwd?.let { putExtra(EXTRAS_PWD, it) }
            }
        }
    }
}