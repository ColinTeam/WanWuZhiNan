package com.comm.net_work.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ApiConfigInfoResponse<T>(

    open val info: T? = null,
    open val user_count: Int = 0,
    open val user_countShow: Int = 0
) : Serializable {


}