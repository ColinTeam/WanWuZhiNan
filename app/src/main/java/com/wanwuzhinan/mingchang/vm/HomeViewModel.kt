package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.library.android.network.request
import com.colin.library.android.utils.Log
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.data.UserInfo
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
    private val _UserInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)

    val user: LiveData<RegisterData> = _user
    val userFlow = _userFlow.asStateFlow()
    val msg: LiveData<String> = _msg
    val userInfo: LiveData<UserInfo?> = _UserInfo

    fun update(isLogin: Boolean) {

    }

     suspend fun getUserInfo() {
        request({ service.newUserInfo() }, {
            Log.e("success it:${it}")
        }, {
            Log.e("failure it:${it}")
        })
    }

    suspend fun login(phone: String, password: String) {

    }
}