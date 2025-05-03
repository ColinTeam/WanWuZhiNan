package com.wanwuzhinan.mingchang.data

import com.colin.library.android.network.data.INetworkResponse
import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 15:38
 *
 * Des   :UserInfoResponse
 */

open class AppResponse<T>(
    private val data: T? = null,
    private val code: Int = Constants.INVALID,
    private val msg: String = "",
) : INetworkResponse<T> {
    override fun isSuccess() = getCode() == Constants.ZERO

    override fun getCode() = code

    override fun getMsg() = msg

    override fun getData() = data

    override fun toString(): String {
        return "AppResponse(data=$data, code=$code, msg='$msg')"
    }


}

class UserInfoResponse() : AppResponse<UserInfo>()

data class UserInfo(
    val id: String = "",//
    val headimg: String = "",//
    val account: String = "",//
    val truename: String = "",//
    val nickname: String = "",//
    val sex: String = "",//
    val province_name: String = "",//
    val city_name: String = "",//
    val area_name: String = "",//
    val school_name: String = "",//
    val grade_name: String = "",//
    val question_count_error: String = "", val question_compass: String = ""
)