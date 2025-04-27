package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.wanwuzhinan.mingchang.app.AppViewModel
import kotlinx.coroutines.flow.Flow
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
    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _token: MutableStateFlow<String?> = MutableStateFlow(null)

    val isLogin: Flow<Boolean> = _isLogin.asStateFlow()
    val token: Flow<String?> = _token.asStateFlow()

    fun update(isLogin: Boolean) {
        viewModelScope.launch { _isLogin.value = isLogin }
    }

    fun update(token: String) {
        viewModelScope.launch { _token.value = token }
    }
}