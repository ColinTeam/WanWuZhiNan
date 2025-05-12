package com.comm.net_work.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable


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
    data class ApiSuccessResponse<T>(val response: T,override val msg: String) : ApiResponse<T>(data = response,msg = msg)
    class ApiEmptyResponse<T> (override val msg: String): ApiResponse<T>(msg = msg)
    class ApiDataEmptyResponse<Any> (override val msg: String): ApiResponse<Any>(msg = msg)
    class ApiFailEmptyResponse<Any> (override val msg: String): ApiResponse<Any>(msg = msg)
    data class ApiFailedResponse<T>(override val code: Int?, override val msg: String) : ApiResponse<T>(code = code, msg = msg)
    data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>(error = throwable)
}
