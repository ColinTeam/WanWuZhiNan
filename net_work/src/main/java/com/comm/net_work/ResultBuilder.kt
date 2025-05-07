package com.comm.net_work

import com.comm.net_work.entity.ApiResponse

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net.base
 * ClassName: ResultBuilder
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 17:27
 * Email:shiming024@163.com
 * Description:
 */
fun <T> ApiResponse<T>.parseData(result: ResultBuilder<T>.() -> Unit) {

    val builder = ResultBuilder<T>().also(result)
    when (this) {
        is ApiResponse.ApiDataEmptyResponse -> builder.onDataEmpty2(this.msg)
        is ApiResponse.ApiSuccessResponse -> builder.onSuccess(this.response,this.msg)
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

    var onSuccess: (data: T?,msg: String) -> Unit = {_, _ ->  }

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