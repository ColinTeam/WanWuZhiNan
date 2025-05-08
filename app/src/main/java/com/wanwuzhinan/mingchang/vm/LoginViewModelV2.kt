package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.UserInfo

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
        _showLoading.postValue(true)
        request({
            service.newUserInfo()
        }, success = {
            _userInfo.postValue(it)
            _showLoading.postValue(false)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
        })
    }

    fun getSms(phone: String) {
        _showLoading.postValue(true)
        request({
            service.newCode(phone)
        }, success = {
            Log.e("newCode:$it")
            _showLoading.postValue(false)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
        })
    }

    fun loginBySms(phone: String, code: String, type: String) {
        _showLoading.postValue(true)
        request({
            service.newLogin(phone, code, type)
        }, success = {
            Log.e("newCode:$it")
            _showLoading.postValue(false)
        }, failure = {
            _showError.postValue(it)
            _showLoading.postValue(false)
        })
    }

    fun loginByPassword(phone: String, password: String, type: String) {

    }

    fun getConfigValue() = configData.value


}