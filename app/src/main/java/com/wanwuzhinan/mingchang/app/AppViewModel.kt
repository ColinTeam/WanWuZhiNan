package com.wanwuzhinan.mingchang.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.library.android.network.NetworkConfig
import com.colin.library.android.network.NetworkHelper
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.network.data.INetworkResponse
import com.colin.library.android.network.requestImpl
import com.wanwuzhinan.mingchang.data.ErrorLog
import com.wanwuzhinan.mingchang.entity.Config
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
interface INetworkFeedback {
    fun onStart(show: Boolean = false, start: HttpResult.Start)
    fun onToast(toast: HttpResult.Toast)
    fun onAction(action: HttpResult.Action)
    fun onFinish(show: Boolean = false, finish: HttpResult.Finish)
}

open class AppViewModel : ViewModel(), INetworkFeedback {

    val service: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }
    protected val _showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading
    protected val _showToast: MutableLiveData<HttpResult.Toast?> = MutableLiveData(null)
    val showToast: LiveData<HttpResult.Toast?> = _showToast
    protected val _httpAction: MutableLiveData<HttpResult.Action?> = MutableLiveData(null)
    val httpAction: LiveData<HttpResult.Action?> = _httpAction
    private val _configData: MutableLiveData<Config?> = MutableLiveData(null)
    val configData: LiveData<Config?> = _configData

    /*app 配置*/
    fun getConfig() {
        request(request = { service.newConfig() }, success = { it ->
            _configData.postValue(it)
        })
    }

    fun postError(type: Int = 0, error: String) {
        viewModelScope.launch(Dispatchers.IO) {
            service.newErrorLog(
                NetworkConfig.gson.toJson(ErrorLog(type = type, error = error))
            )
        }
    }

    override fun onStart(show: Boolean, start: HttpResult.Start) {
        if (show) _showLoading.postValue(true)
    }

    override fun onToast(toast: HttpResult.Toast) {
        if (toast.code != 0 && toast.msg.isNotEmpty() == true) {
            postError(toast.code, toast.msg)
        }
        _showToast.postValue(toast)
    }

    override fun onAction(action: HttpResult.Action) {
        _httpAction.postValue(action)
    }

    override fun onFinish(show: Boolean, finish: HttpResult.Finish) {
        if (show) _showLoading.postValue(false)
    }

    fun <T> ViewModel.request(
        request: suspend () -> INetworkResponse<T>, success: (suspend (T?) -> Unit)
    ) = requestImpl(
        viewModelScope,
        request = request,
        success = success,
        start = { onStart(false, it) },
        toast = { onToast(it) },
        action = { onAction(it) },
        finish = { onFinish(false, it) })

    fun <T> ViewModel.request(
        loading: Boolean = true,
        request: suspend () -> INetworkResponse<T>,
        success: (suspend (T?) -> Unit),
        delay: Long = 500L
    ) = requestImpl(
        viewModelScope,
        request = request,
        success = success,
        start = { onStart(loading, it) },
        toast = { onToast(it) },
        action = { onAction(it) },
        finish = { onFinish(loading, it) },
        delay = delay
    )
}