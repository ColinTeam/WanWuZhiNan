package com.wanwuzhinan.mingchang.app

import androidx.lifecycle.ViewModel
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

    /*加载状态*/
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /*加载状态*/
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()






    /**
     * 公共异常消息处理
     */
    open fun action(it: Throwable) {

    }
}