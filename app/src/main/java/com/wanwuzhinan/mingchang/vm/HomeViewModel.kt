package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wanwuzhinan.mingchang.app.AppViewModel
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.net.ApiService
import com.wanwuzhinan.mingchang.network.NetworkHelper
import com.wanwuzhinan.mingchang.network.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private val _token: MutableLiveData<String> = MutableLiveData("")

    val user: LiveData<RegisterData> = _user
    val userFlow = _userFlow.asStateFlow()
    val msg: LiveData<String> = _msg
    val token: LiveData<String> = _token
    val service: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }

    fun update(isLogin: Boolean) {

    }

    fun update(token: String) {
        viewModelScope.launch { _token.value = token }
    }

    suspend fun login(phone: String, password: String) {
        request({ service.login(phone, password) }, {
            _userFlow.emit(it)
        }, {})
    }
}