package com.ssm.comm.response

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 23:41
 *
 * Des   :BaseBaseRepository
 */
open class BaseRepository {

    suspend fun <T> executeHttp(block : suspend() -> ApiResponse<T>) : ApiResponse<T> {
        delay(500)
        kotlin.runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            return handleHttpOk(data)
        }.onFailure { e ->
            when (e) {
                is JsonSyntaxException -> {
                    Log.e("TAG", "executeHttp: "+ block)
                    Log.e("TAG", "executeHttp111: "+ block.invoke())
//                    val jsonData = e.message // 如果是 Gson 的 JsonSyntaxException，message 可能包含原始数据
//                    val jsonObject = JsonParser().parse(jsonData).asJsonObject
//                    val code = jsonObject.get("code").asInt
//                    if (code != 0) {
//                        return ApiResponse.ApiDataEmptyResponse(jsonObject.get("msg").asString)
//                    }else{
//                        return ApiResponse.ApiFailEmptyResponse(jsonObject.get("msg").asString)
//                    }
                }
                else -> {
                    return handleHttpError(e)
                }
            }
        }
        return ApiResponse.ApiEmptyResponse("请稍后重试")
    }


    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable) : ApiResponse.ApiErrorResponse<T>{
//        if(BuildConfig.IS_ENABLE_LOG){
//            e.printStackTrace()
//        }
        return ApiResponse.ApiErrorResponse(e)
    }


    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>) : ApiResponse<T> {
        return if(data.isSuccess){
            getHttpSuccessResponse(data)
        }else if (data.code == 2 || data.code == 4){
            ApiResponse.ApiFailedResponse(data.code,data.msg)
        } else{
            ApiResponse.ApiFailedResponse(data.code,data.msg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>) : ApiResponse<T> {
        val data = response.data
        return if(data == null || data is List<*> && (data as List<*>).isEmpty()){
            ApiResponse.ApiEmptyResponse(response.msg)
        }else{
            ApiResponse.ApiSuccessResponse(data,response.msg)
        }
    }
}
