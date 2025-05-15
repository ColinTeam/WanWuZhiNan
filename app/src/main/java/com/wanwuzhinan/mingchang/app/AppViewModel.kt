package com.wanwuzhinan.mingchang.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.network.NetworkHelper
import com.colin.library.android.network.data.NetworkResult
import com.wanwuzhinan.mingchang.data.ErrorLog
import com.wanwuzhinan.mingchang.net.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    protected val _showToast: MutableLiveData<NetworkResult.Failure?> = MutableLiveData(null)
    val showToast: LiveData<NetworkResult.Failure?> = _showToast
    protected val _showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading


    fun postError(type: Int = 0, error: String) {
        viewModelScope.launch(Dispatchers.IO) {
            service.newErrorLog(
                NetworkConfig.gson.toJson(ErrorLog(type = type, error = error))
            )
        }
    }


}