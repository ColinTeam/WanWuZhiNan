package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.colin.library.android.utils.Constants
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_PWD
import com.wanwuzhinan.mingchang.entity.HTTP_ACTION_LOGIN_SMS
import com.wanwuzhinan.mingchang.entity.RegisterData
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.utils.MMKVUtils
import kotlinx.coroutines.delay

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :LoginViewModelV2
 */
class LoginViewModelV2 : AppViewModel() {
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _configData: MutableLiveData<Config> = MutableLiveData(Config())
    val configData: LiveData<Config> = _configData
    private val _RegisterData: MutableLiveData<RegisterData> = MutableLiveData(RegisterData())
    val registerData: LiveData<RegisterData> = _RegisterData
    private val _smsSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val smsSuccess: LiveData<Boolean> = _smsSuccess

    fun getConfig() {
        request({
            service.newConfig()
        }, success = {
            if (it != configData.value) {
                _configData.postValue(it)
            }
        }, failure = {
            _showError.postValue(it)
        })
    }

    //获取用户信息
    fun getUserInfo() {
        request({
            service.newUserInfo()
        }, success = {
            _userInfo.postValue(it)
        }, failure = {
            _showError.postValue(it)
        })
    }

    fun getSms(phone: String) {
        _showLoading.postValue(true)
        request({
            delay(Constants.ONE_SECOND.toLong())
            service.newCode(phone)
        }, success = {
            _showLoading.postValue(false)
            _smsSuccess.postValue(true)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
            _smsSuccess.postValue(false)
        })
    }

    fun loginBySms(phone: String, code: String, type: Int, action: Int = HTTP_ACTION_LOGIN_SMS) {
        _showLoading.postValue(true)
        request({
            delay(Constants.ONE_SECOND.toLong())
            service.newLogin(phone, code, type, action)
        }, success = {
            if (it.token.isEmpty()) {
                ConfigApp.token = it.token
                MMKVUtils.encode(Constant.TOKEN, it.token)
                MMKVUtils.encode(Constant.USER_MOBILE, phone)
                delay(Constants.TIMEOUT_CLICK.toLong())
            }
            _RegisterData.postValue(it)
            _showLoading.postValue(false)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
        })
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
        _showLoading.postValue(true)
        request({
            delay(Constants.ONE_SECOND.toLong())
            service.newLoginPwd(phone, sms, pwd, pwds, action, type, confirm)
        }, success = {
            if (it.token.isEmpty()) {
                ConfigApp.token = it.token
                MMKVUtils.encode(Constant.TOKEN, it.token)
                MMKVUtils.encode(Constant.USER_MOBILE, phone)
                delay(Constants.TIMEOUT_CLICK.toLong())
            }
            _RegisterData.postValue(it)
            _showLoading.postValue(false)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
        })
    }

    fun updateSuccess(state: Boolean = false) {
        if (_smsSuccess.value != state) _smsSuccess.value = state
    }


}