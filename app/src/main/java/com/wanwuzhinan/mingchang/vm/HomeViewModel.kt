package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.entity.UserInfo

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class HomeViewModel : AppViewModel() {

    private val _closeAD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)

    val closeAD: LiveData<Boolean> = _closeAD
    val userInfo: LiveData<UserInfo?> = _userInfo

    //获取用户信息
    fun getUserInfo() {
        request(request = { service.newUserInfo() }, success = { it ->
            it?.info?.let { _userInfo.postValue(it) }
        })
    }

    fun updateAD(close: Boolean) {
        if (_closeAD.value != close) {
            _closeAD.value = close
        }
    }

    fun getConfigValue() = configData.value
    fun getUserInfoValue() = userInfo.value
    fun getAdStateValue() = closeAD.value == true


}