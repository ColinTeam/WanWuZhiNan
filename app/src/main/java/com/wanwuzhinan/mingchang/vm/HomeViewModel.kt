package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wanwuzhinan.mingchang.app.AppViewModel

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-23 22:10
 *
 * Des   :HomeViewModel
 */
class HomeViewModel : AppViewModel() {

    private val _closeAD: MutableLiveData<Boolean> = MutableLiveData(false)

    val closeAD: LiveData<Boolean> = _closeAD

    fun updateAD(close: Boolean) {
        if (_closeAD.value != close) {
            _closeAD.value = close
        }
    }

    fun getAdStateValue() = closeAD.value == true

}