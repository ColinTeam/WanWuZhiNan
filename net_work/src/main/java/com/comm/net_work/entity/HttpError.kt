package com.comm.net_work.entity

import android.util.Log
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net
 * ClassName: HttpError
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 16:51
 * Email:shiming024@163.com
 * Description:异常的枚举类
 */
enum class HttpError(val code : Int,val msg : String){
    TOKEN_EXPIRE(3001, "token is expired"),
    PARAMS_ERROR(4003, "params is error")
}

internal fun handlingApiExceptions(code: Int?, errorMsg: String?) = when (code) {
    HttpError.TOKEN_EXPIRE.code -> errorMsg?.let { Log.e("HttpError", it) }
    HttpError.PARAMS_ERROR.code -> errorMsg?.let { Log.e("HttpError", it) }
    else -> errorMsg?.let { }
}

internal fun handlingExceptions(e: Throwable) = when (e) {
    is HttpException -> Log.e("HttpError",e.message())

    is CancellationException -> {

    }
    is SocketTimeoutException -> {

    }
    is JsonParseException -> {

    }
    else -> {

    }
}