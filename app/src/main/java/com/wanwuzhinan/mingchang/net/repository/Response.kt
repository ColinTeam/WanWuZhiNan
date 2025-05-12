package com.wanwuzhinan.mingchang.net.repository

import java.io.Serializable

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-12 23:14
 *
 * Des   :Response
 */

open class ApiInfoResponse<T>(
    open val info: T? = null
) : Serializable {


}
open class ApiListResponse<T>(
    open val list: MutableList<T>? = null
) : Serializable {
}