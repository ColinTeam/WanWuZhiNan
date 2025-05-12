package com.ssm.comm.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 23:40
 *
 * Des   :ApiResponse
 */
open class ApiResponse<T>(
    @SerializedName("data")
    open val data: T? = null,
    open val code: Int? = null,
    open val msg: String = "",
    open val error: Throwable? = null,
) : Serializable {
    val isSuccess: Boolean get() = code == 0
    override fun toString(): String {
        return "ApiResponse(data=$data, code=$code, msg=$msg, error=$error)"
    }

    data class ApiSuccessResponse<T>(val response: T, override val msg: String) :
        ApiResponse<T>(data = response, msg = msg)

    class ApiEmptyResponse<T>(override val msg: String) : ApiResponse<T>(msg = msg)
    class ApiDataEmptyResponse<Any>(override val msg: String) : ApiResponse<Any>(msg = msg)
    class ApiFailEmptyResponse<Any>(override val msg: String) : ApiResponse<Any>(msg = msg)
    data class ApiFailedResponse<T>(override val code: Int?, override val msg: String) :
        ApiResponse<T>(code = code, msg = msg)

    data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>(error = throwable)
}

fun <T> ApiResponse<T>.parseData(result: ResultBuilder<T>.() -> Unit) {

    val builder = ResultBuilder<T>().also(result)
    when (this) {
        is ApiResponse.ApiDataEmptyResponse -> builder.onDataEmpty2(this.msg)
        is ApiResponse.ApiSuccessResponse -> builder.onSuccess(this.response, this.msg)
        is ApiResponse.ApiEmptyResponse -> {
            builder.onDataEmpty()
            builder.onDataEmpty2(this.msg)
        }

        is ApiResponse.ApiFailedResponse -> builder.onFailed(this.code, this.msg)
        is ApiResponse.ApiErrorResponse -> builder.onError(this.throwable)
    }
    builder.onComplete
}


class ResultBuilder<T> {

    var onSuccess: (data: T?, msg: String) -> Unit = { _, _ -> }

    var onDataEmpty: () -> Unit = {}

    var onDataEmpty2: (msg: String) -> Unit = { }

    var onFailed: (code: Int?, msg: String?) -> Unit = { _, _ ->
        //msg?.let { }
    }

    var onError: (e: Throwable) -> Unit = {
        it.message?.let { }
    }
    var onComplete: () -> Unit = {}
}