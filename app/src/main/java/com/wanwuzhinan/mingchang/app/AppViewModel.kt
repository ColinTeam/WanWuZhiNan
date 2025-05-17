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
import com.wanwuzhinan.mingchang.entity.HTTP_SUCCESS
import com.wanwuzhinan.mingchang.entity.UserInfo
import com.wanwuzhinan.mingchang.net.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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
    fun onToast(showToast: Boolean, toast: HttpResult.Toast)
    fun onAction(action: HttpResult.Action)
    fun onFinish(show: Boolean = false, finish: HttpResult.Finish)
}

open class AppViewModel : ViewModel(), INetworkFeedback {

    val service: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }
    protected val _showLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading
    protected val _showToast: MutableSharedFlow<HttpResult.Toast> = MutableSharedFlow(1, 10)
    val showToast = _showToast.asSharedFlow().distinctUntilChanged()
    protected val _httpAction: MutableSharedFlow<HttpResult.Action> = MutableSharedFlow(1, 10)
    val httpAction = _httpAction.asSharedFlow().distinctUntilChanged()
    private val _configData: MutableLiveData<Config?> = MutableLiveData(null)
    val configData: LiveData<Config?> = _configData
    private val _userInfo: MutableLiveData<UserInfo?> = MutableLiveData(null)
    val userInfo: LiveData<UserInfo?> = _userInfo

    /*app 配置*/
    fun getConfig() {
        request(request = { service.newConfig() }, success = { it ->
            _configData.postValue(it)
        })
    }

    //获取用户信息
    fun getUserInfo() {
        request(request = { service.newUserInfo() }, success = { it ->
            it?.info?.let { _userInfo.postValue(it) }
        })
    }

    //异常反馈
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

    override fun onToast(showToast: Boolean, toast: HttpResult.Toast) {
        if (!showToast && toast.code == HTTP_SUCCESS) {
            return
        }
        viewModelScope.launch {
            _showToast.emit(toast)
        }
    }

    override fun onAction(action: HttpResult.Action) {
        if (action.code != 0 && action.msg.isNotEmpty() == true) {
            postError(action.code, action.msg)
        }
        viewModelScope.launch {
            _httpAction.emit(action)
        }
    }

    override fun onFinish(show: Boolean, finish: HttpResult.Finish) {
        if (show) _showLoading.postValue(false)
    }

//    fun <T> ViewModel.request(
//        request: suspend () -> INetworkResponse<T>, success: (suspend (T?) -> Unit)
//    ) = requestImpl(
//        viewModelScope,
//        request = request,
//        success = success,
//        start = { onStart(false, it) },
//        toast = { onToast(false, it) },
//        action = { onAction(it) },
//        finish = { onFinish(false, it) })
//
//    fun <T> ViewModel.request(
//        loading: Boolean = true,
//        successToast: Boolean = false,
//        request: suspend () -> INetworkResponse<T>,
//        success: (suspend (T?) -> Unit),
//        delay: Long = 1000L
//    ) = requestImpl(
//        viewModelScope,
//        request = request,
//        success = success,
//        start = { onStart(loading, it) },
//        toast = { onToast(successToast, it) },
//        action = { onAction(it) },
//        finish = { onFinish(loading, it) },
//        delay = delay
//    )

    fun <T> ViewModel.request(
        loading: Boolean = false,
        showToast: Boolean = false,
        request: suspend () -> INetworkResponse<T>,
        success: (suspend (T?) -> Unit),
        delay: Long = 0L
    ) = requestImpl(
        viewModelScope,
        request = request,
        success = success,
        start = { onStart(loading, it) },
        toast = { onToast(showToast, it) },
        action = { onAction(it) },
        finish = { onFinish(loading, it) },
        delay = delay
    )

    fun getConfigValue() = configData.value
    fun getUserInfoValue() = userInfo.value
}