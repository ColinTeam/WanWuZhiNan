package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.utils.Constants
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_SMS
import com.wanwuzhinan.mingchang.entity.RegisterData

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :LoginViewModel
 */
class LoginViewModel : AppViewModel() {
    private val _registerData: MutableLiveData<RegisterData?> = MutableLiveData(null)
    val registerData: LiveData<RegisterData?> = _registerData
    private val _smsSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val smsSuccess: LiveData<Boolean> = _smsSuccess


    fun getSms(phone: String) {
        request(
            loading = true,
            request = { service.newCode(phone) },
            success = { it -> _smsSuccess.postValue(true) },
            delay = Constants.ONE_SECOND.toLong()
        )
    }

    fun loginBySms(phone: String, code: String, type: Int, action: Int = HTTP_ACTION_LOGIN_SMS) {
        request(
            loading = true,
            request = { service.newLogin(phone, code, type, action) },
            success = { it -> it?.let { _registerData.postValue(it) } },
            delay = Constants.ONE_SECOND.toLong()
        )
    }

    fun loginByPassword(
        phone: String,
        sms: String?,
        pwd: String,
        pwds: String,
        action: Int = HTTP_ACTION_LOGIN_PWD,
        type: Int,
        confirm: Int
    ) {
        request(
            loading = true,
            request = { service.newLoginPwd(phone, sms, pwd, pwds, action, type, confirm) },
            success = { it -> _registerData.postValue(it) },
            delay = Constants.ONE_SECOND.toLong()
        )
    }


    fun updateSuccess(state: Boolean = false) {
        if (smsSuccess.value != state) _smsSuccess.value = state
    }


}