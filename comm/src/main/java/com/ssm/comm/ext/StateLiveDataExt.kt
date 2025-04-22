package com.ssm.comm.ext

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.comm.net_work.ResultBuilder
import com.comm.net_work.entity.ApiResponse
import com.comm.net_work.parseData
import com.ssm.comm.event.MessageEvent

typealias StateLiveData<T> = LiveData<ApiResponse<T>>

typealias StateMutableLiveData<T> = MutableLiveData<ApiResponse<T>>

@MainThread
fun <T> StateMutableLiveData<T>.observeState(
    owner: LifecycleOwner, dismiss: Boolean = true, listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    observe(owner) { apiResponse ->
        apiResponse.parseData(listenerBuilder)
        when (apiResponse) {
            //请求失败
            is ApiResponse.ApiFailedResponse<T> -> {
                when (apiResponse.code) {
                    //登录过期、账户被管理员限制登录、账号在其他设备登录
                    2,4 -> {
                        clearAllData()
//                        toastNormal(apiResponse.msg)
                        post(MessageEvent.LOGIN_EXPIRED, apiResponse.msg)
                        post(MessageEvent.APP_VERSION_UPDATE, apiResponse.msg)
                        toastNormal("当前账户已从其他设备登录，为保障安全，请重新登录")
                    }
                    else -> {
                        toastNormal(apiResponse.msg)
                    }
                }
            }
            //数据为空
            is ApiResponse.ApiEmptyResponse<T> -> {
                //toastNormal(apiResponse.msg)
            }
            //请求错误
            is ApiResponse.ApiErrorResponse<T> -> {
//                toastNormal(apiResponse.error?.message!!)
                toastNormal("哎呀没有网络啦！请退出重新加载！")
            }
        }
        if (dismiss && owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            dismissLoadingExt()
        }
    }
}

@MainThread
fun <T> LiveData<ApiResponse<T>>.observeState(
    owner: LifecycleOwner, dismiss: Boolean = true, listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    observe(owner) { apiResponse ->
        apiResponse.parseData(listenerBuilder)
        Log.e("TAG", "observeState: " )
        when (apiResponse) {
            //请求失败
            is ApiResponse.ApiFailedResponse<T> -> {
                when (apiResponse.code) {
                    //登录过期、账户被管理员限制登录、账号在其他设备登录
                    2,4 -> {
                        clearAllData()
//                        toastNormal(apiResponse.msg)
                        post(MessageEvent.LOGIN_EXPIRED, apiResponse.msg)
                        post(MessageEvent.APP_VERSION_UPDATE, apiResponse.msg)
                        toastNormal("当前账户已从其他设备登录，为保障安全，请重新登录")
                    }
                    else -> {
                        toastNormal(apiResponse.msg)
                    }
                }
            }
            //数据为空
            is ApiResponse.ApiEmptyResponse<T> -> {
                toastNormal(apiResponse.msg)
            }
            //请求错误
            is ApiResponse.ApiErrorResponse<T> -> {
//                toastNormal(apiResponse.msg)
                toastNormal("哎呀没有网络啦！请退出重新加载！")
            }
        }
        if (dismiss && owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            dismissLoadingExt()
        }
    }
}