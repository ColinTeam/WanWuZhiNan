package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_FIND_PWD
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
            showToast = true,
            request = { service.newCode(phone) },
            success = { it -> _smsSuccess.postValue(true) },
            delay = 1000L
        )
    }

    fun login(
        phone: String,
        sms: String,
        pwd: String,
        type: Int,
        action: Int = HTTP_ACTION_LOGIN_SMS,
        confirm: Int = 0
    ) {
        request(
            loading = true,
            showToast = true,
            request = { service.newLogin(phone, sms, pwd, pwd, type, action, confirm) },
            success = { it -> it?.let { _registerData.postValue(it) } },
            delay = 1000L
        )
    }

    fun confirm(
        phone: String,
        sms: String,
        pwd: String,
        type: Int,
        action: Int = HTTP_ACTION_LOGIN_FIND_PWD,
        confirm: Int = 1
    ) {
        request(
            request = { service.newLogin(phone, sms, pwd, pwd, type, action, confirm) },
            success = { it -> it?.let { _registerData.postValue(it) } }
        )
    }

    fun updateSuccess(state: Boolean = false) {
        if (smsSuccess.value != state) _smsSuccess.value = state
    }


}