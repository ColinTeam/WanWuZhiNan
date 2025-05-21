package com.colin.library.android.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colin.library.android.network.data.HTTP_ERROR
import com.colin.library.android.network.data.HttpResult
import com.colin.library.android.network.data.INetworkResponse
import com.colin.library.android.utils.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketException


/**
 * 发起一个网络请求，并在请求的不同阶段执行相应的回调函数。
 *
 * 该函数是一个挂起函数，用于在 ViewModel 中发起网络请求，并根据请求的不同阶段（如开始、成功、失败等）执行相应的回调操作。
 * 通过传入的 `request` 函数发起网络请求，并根据请求结果执行 `success`、`start`、`toast`、`action` 和 `finish` 等回调。
 *
 * @param T 网络请求返回的数据类型。
 * @param request 一个挂起函数，用于发起网络请求并返回 `INetworkResponse<T>` 类型的结果。
 * @param success 一个挂起函数，当请求成功时执行，接收请求返回的数据 `T?` 作为参数。
 * @param start 一个挂起函数，当请求开始时执行，接收 `HttpResult.Start` 作为参数。
 * @param toast 一个挂起函数，当需要显示 Toast 消息时执行，接收 `HttpResult.Toast` 作为参数。
 * @param action 一个挂起函数，当需要执行特定操作时执行，接收 `HttpResult.Action` 作为参数。
 * @param finish 一个挂起函数，当请求结束时执行，接收 `HttpResult.Finish` 作为参数。
 *
 * 该函数内部调用了 `requestImpl` 函数，将 `viewModelScope` 作为协程作用域，并将所有回调函数传递给 `requestImpl` 进行处理。
 */
fun <T> ViewModel.request(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T?) -> Unit) = { },
    start: (suspend (HttpResult.Start) -> Unit) = { },
    toast: (suspend (HttpResult.Toast) -> Unit) = { },
    action: (suspend (HttpResult.Action) -> Unit) = { },
    finish: (suspend (HttpResult.Finish) -> Unit) = { }
) = requestImpl(viewModelScope, request, success, start, toast, action, finish)

inline fun <T> CoroutineScope.request(
    crossinline request: suspend () -> INetworkResponse<T>,
    crossinline success: (T) -> Unit = { },
    crossinline error: (code: Int, msg: String) -> Unit = { _, _ -> }
): Job {
    return launch {
        try {
            val response = withContext(Dispatchers.IO) { request() }
            withContext(Dispatchers.Main) {
                if (response.isSuccess() && response.getData() != null) {
                    success(response.getData()!!)
                } else {
                    error(response.getCode(), response.getMsg())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) { error(-1, "$e") }
        }
    }
}

/**
 * 执行网络请求的实现函数，支持重试机制和延迟启动。
 *
 * @param scope 协程作用域，用于控制请求的生命周期。
 * @param request 挂起函数，执行实际的网络请求并返回 [INetworkResponse] 类型的结果。
 * @param success 挂起函数，当请求成功时调用，接收请求结果 [T] 作为参数。
 * @param start 挂起函数，在请求开始时调用，接收 [HttpResult.Start] 作为参数。
 * @param toast 挂起函数，当需要显示提示信息时调用，接收 [HttpResult.Toast] 作为参数。
 * @param action 挂起函数，当需要执行特定操作时调用，接收 [HttpResult.Action] 作为参数。
 * @param finish 挂起函数，在请求结束时调用，接收 [HttpResult.Finish] 作为参数。
 * @param retry 请求失败时的重试次数，默认为 [NetworkConfig.retry]。
 * @param delay 请求启动前的延迟时间，默认为 [NetworkConfig.delay]。
 * @return 返回一个 [Job] 对象，用于控制请求的取消和状态。
 */
fun <T> requestImpl(
    scope: CoroutineScope,
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T?) -> Unit) = { },
    start: (suspend (HttpResult.Start) -> Unit) = { },
    toast: (suspend (HttpResult.Toast) -> Unit) = { },
    action: (suspend (HttpResult.Action) -> Unit) = { },
    finish: (suspend (HttpResult.Finish) -> Unit) = { },
    retry: Int = NetworkConfig.retry,
    delay: Long = NetworkConfig.delay
): Job {
    return scope.launch(Dispatchers.IO) {
        try {
            // 通知请求开始，并记录开始时间
            start.invoke(HttpResult.Start(System.currentTimeMillis()))
            // 如果设置了延迟，则等待指定时间
            if (delay > 0L) delay(delay)
            // 执行实际的网络请求逻辑
            requestImpl(request, success, toast, action, retry, delay)
        } catch (e: Exception) {
            // 如果协程被取消，则直接退出
            if (e is CancellationException) return@launch
            // 处理异常，显示提示信息并执行相关操作
            toast.invoke(HttpResult.Toast(HTTP_ERROR, "$e"))
            action.invoke(HttpResult.Action(HTTP_ERROR, "$e"))
        } finally {
            // 无论请求成功或失败，最终都会通知请求结束，并记录结束时间
            finish.invoke(HttpResult.Finish(System.currentTimeMillis()))
        }
    }
}

/**
 * 执行网络请求的实现函数，支持重试机制，并根据请求结果调用相应的回调函数。
 *
 * @param request 实际的网络请求函数，返回一个[INetworkResponse]对象。
 * @param success 请求成功时的回调函数，接收请求返回的数据[T]。
 * @param toast 请求完成后显示提示信息的回调函数，接收[HttpResult.Toast]对象。
 * @param action 请求完成后执行特定操作的回调函数，接收[HttpResult.Action]对象。
 * @param retry 请求失败时的重试次数，默认为[NetworkConfig.retry]。
 * @param delay 请求失败后的重试延迟时间（毫秒），默认为[NetworkConfig.delay]。
 *
 * @throws Exception 如果所有重试都失败，抛出最后一次捕获的异常或默认的[SocketException]。
 */
suspend fun <T> requestImpl(
    request: suspend () -> INetworkResponse<T>,
    success: (suspend (T?) -> Unit) = {},
    toast: (suspend (HttpResult.Toast) -> Unit) = {},
    action: (suspend (HttpResult.Action) -> Unit) = {},
    retry: Int = NetworkConfig.retry,
    delay: Long = NetworkConfig.delay
) {
    var response: INetworkResponse<T>? = null
    var exception: Exception? = null

    // 尝试执行请求，最多重试[retry]次
    for (i in 0 until retry) {
        try {
            response = request()
            break
        } catch (e: Exception) {
            exception = e
            // 如果是SocketException或包含"reset"信息，则延迟[delay]毫秒后重试
            if (e is SocketException || e.message?.contains("reset") == true) delay(500L)
            else break
        }
    }

    // 如果所有重试都失败，抛出异常
    if (response == null) throw exception ?: SocketException("Request failed after $delay retries")
    Log.i("okhttp-->>$response")

    // 根据请求结果调用相应的回调函数
    if (response.isSuccess()) success.invoke(response.getData())
    toast.invoke(HttpResult.Toast(response.getCode(), response.getMsg()))
    action.invoke(HttpResult.Action(response.getCode(), response.getMsg()))
}



