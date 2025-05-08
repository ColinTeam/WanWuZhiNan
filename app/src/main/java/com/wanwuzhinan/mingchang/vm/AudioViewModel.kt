package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.Config
import com.wanwuzhinan.mingchang.entity.UserInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/8 09:25
 *
 * Des   :AudioViewModel
 */
class AudioViewModel : AppViewModel() {

    private val _closeAD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _configData: MutableLiveData<Config> = MutableLiveData(Config())

    val closeAD: LiveData<Boolean> = _closeAD
    val userInfo: LiveData<UserInfo?> = _userInfo
    val configData: LiveData<Config> = _configData

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


    fun updateAD(close: Boolean) {
        if (_closeAD.value != close) {
            _closeAD.value = close
        }
    }

    fun getConfigValue() = configData.value
    fun getAdStateValue() = closeAD.value == true
}