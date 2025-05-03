package com.wanwuzhinan.mingchang.app

import androidx.lifecycle.ViewModel
import com.colin.library.android.network.NetworkHelper
import com.wanwuzhinan.mingchang.net.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-13
 *
 * Des   :AppViewModel
 */
open class AppViewModel : ViewModel() {
    val service: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }

    /*加载状态*/
    private val _showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /*加载状态*/
    val showLoading: Flow<Boolean> = _showLoading.asStateFlow()


    /*加载状态*/
    /**
     * 公共异常消息处理
     */
    suspend fun action(it: Throwable) {

    }

    suspend fun loading(loading: Boolean = false) {
        _showLoading.emit(loading)
    }
}