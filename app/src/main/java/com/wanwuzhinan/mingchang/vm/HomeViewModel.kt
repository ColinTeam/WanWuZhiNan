package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.entity.ConfigData
import com.wanwuzhinan.mingchang.entity.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class HomeViewModel : AppViewModel() {

    private val _user: MutableLiveData<RegisterData> = MutableLiveData()
    private val _userFlow: MutableStateFlow<RegisterData?> = MutableStateFlow(null)
    private val _msg: MutableLiveData<String> = MutableLiveData("")
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    private val _configData: MutableLiveData<ConfigData> = MutableLiveData(ConfigData())

    val user: LiveData<RegisterData> = _user
    val userFlow = _userFlow.asStateFlow()
    val msg: LiveData<String> = _msg
    val userInfo: LiveData<UserInfo?> = _userInfo
    val configData: LiveData<ConfigData> = _configData


    //获取用户信息
    fun getUserInfo() {
        request({
            service.newUserInfo()
        }, success = {
            Log.e("http success:$it")
            _userInfo.postValue(it)
        }, failure = {
            Log.e("http failure:$it")
            _msg.postValue(it.msg)
        })
    }

    fun getConfig() {
        request({
            service.newConfig()
        }, success = {
            Log.e("http success:$it")
            if (it.info != null && it.info != configData.value) {
                _configData.postValue(it.info)
            }
        }, failure = {
            Log.e("http failure:$it")
            _msg.postValue(it.msg)
        })
    }
}