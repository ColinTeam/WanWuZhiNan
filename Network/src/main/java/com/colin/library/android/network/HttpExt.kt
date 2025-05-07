package com.colin.library.android.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.colin.library.android.network.data.AppResponse
import com.colin.library.android.network.data.HTTP_ERROR
import com.colin.library.android.network.data.INetworkResponse
import com.colin.library.android.network.data.NetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketException


/**
 * 在 ViewModel 中发起一个挂起请求，并处理请求的成功和失败回调。
 *
 * @param request 挂起函数，用于发起请求并返回 `AppResponse<T>` 类型的结果。
 * @param success 可选的挂起函数，用于处理请求成功时的逻辑，默认不执行任何操作。
 * @param failure 可选的挂起函数，用于处理请求失败时的逻辑，默认不执行任何操作。
 * @return 无返回值，该函数通过回调处理请求结果。
 *
 * 该函数通过 `requestImpl` 实现具体的请求逻辑，并使用 `viewModelScope` 确保请求在 ViewModel 的生命周期内执行。
 */
fun <T> ViewModel.request(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T) -> Unit) = { },
    failure: (suspend (NetworkResult.Failure) -> Unit) = { }
) = requestImpl(viewModelScope, request, success, failure)


/**
 * 在 Activity/Fragment 中发起一个挂起请求，并处理请求的成功和失败回调。
 *
 * @param request 挂起函数，用于发起请求并返回 `AppResponse<T>` 类型的结果。
 * @param success 可选的挂起函数，用于处理请求成功时的逻辑，默认不执行任何操作。
 * @param failure 可选的挂起函数，用于处理请求失败时的逻辑，默认不执行任何操作。
 * @return 无返回值，该函数通过回调处理请求结果。
 *
 * 该函数通过 `requestImpl` 实现具体的请求逻辑，并使用 `lifecycleScope` 确保请求在 ViewModel 的生命周期内执行。
 */
suspend fun <T> LifecycleOwner.request(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T) -> Unit) = { },
    failure: (suspend (NetworkResult.Failure) -> Unit) = { }
) = requestImpl(lifecycleScope, request, success, failure)

/**
 * 在 CoroutineScope 中发起一个挂起请求，并处理请求的成功和失败回调。
 *
 * @param request 挂起函数，用于发起请求并返回 `AppResponse<T>` 类型的结果。
 * @param success 可选的挂起函数，用于处理请求成功时的逻辑，默认不执行任何操作。
 * @param failure 可选的挂起函数，用于处理请求失败时的逻辑，默认不执行任何操作。
 * @return 无返回值，该函数通过回调处理请求结果。
 *
 * 该函数通过 `requestImpl` 实现具体的请求逻辑，并使用 `viewModelScope` 确保请求在 ViewModel 的生命周期内执行。
 */
suspend fun <T> CoroutineScope.request(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T) -> Unit) = { },
    failure: (suspend (NetworkResult.Failure) -> Unit) = { }
) = requestImpl(this, request, success, failure)

/**
 * 执行网络请求的实现函数，处理请求的成功和失败回调。
 * 该函数在指定的协程作用域中启动一个IO线程来执行请求，并处理可能的异常。
 *
 * @param scope 协程作用域，用于启动和管理协程的生命周期。
 * @param request 挂起函数，执行实际的网络请求并返回[AppResponse]类型的结果。
 * @param success 挂起函数，当请求成功时调用，接收请求结果[T]作为参数。
 * @param failure 挂起函数，当请求失败时调用，接收[NetworkResult.Failure]作为参数。
 */
fun <T> requestImpl(
    scope: CoroutineScope,
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T) -> Unit) = {},
    failure: (suspend (NetworkResult.Failure) -> Unit) = {},
    retry: Int = NetworkConfig.retry,
    delay: Long = 2000
) {
    scope.launch(Dispatchers.IO) {
        try {
            requestImpl(request, success, failure, retry, delay)
        } catch (e: Exception) {
            //scope cancel 说明不需要了，所以直接退出即可
            if (e is CancellationException) return@launch
            failure.invoke(NetworkResult.failure(HTTP_ERROR, "$e"))
        }
    }
}

/**
 * 执行网络请求的实现函数，支持重试机制。
 *
 * @param request 挂起函数，用于执行实际的网络请求，返回[AppResponse]对象。
 * @param success 挂起函数，当请求成功时调用，接收请求成功的数据[T]。
 * @param failure 挂起函数，当请求失败时调用，接收[NetworkResult.Failure]对象。
 * @param retry 请求失败时的重试次数，默认为[NetworkConfig.retry]。
 * @param delay 重试之间的延迟时间（毫秒），默认为2000毫秒。
 * @throws Exception 如果所有重试都失败，抛出最后一次捕获的异常或自定义的[SocketException]。
 */
@Suppress("UNCHECKED_CAST")
suspend fun <T> requestImpl(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T) -> Unit) = {},
    failure: (suspend (NetworkResult.Failure) -> Unit) = {},
    retry: Int = NetworkConfig.retry,
    delay: Long = 2000
) {
    var response: INetworkResponse<T>? = null
    var exception: Exception? = null
    //尝试执行请求，最多重试[retry]次
    for (i in 0 until retry) {
        try {
            response = request()
            break
        } catch (e: Exception) {
            exception = e
            //// 如果是SocketException或包含"reset"信息，则延迟[delay]毫秒后重试
            if (e is SocketException || e.message?.contains("reset") == true) delay(delay)
            else break
        }
    }
    // 如果所有重试都失败，抛出异常
    if (response == null) throw exception ?: SocketException("Request failed after $delay retries")
    // 根据请求结果调用相应的回调函数
    when {
        !response.isSuccess() -> failure.invoke(
            NetworkResult.failure(
                response.getCode(), response.getMsg()
            )
        )

        response.getData() == null -> failure.invoke(NetworkResult.empty())
        else -> success.invoke(response.getData()!!)
    }
}



