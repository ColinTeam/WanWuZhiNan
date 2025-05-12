package com.comm.net_work.entity

import java.io.Serializable

open class ApiListResponse<T>(
    open val list: MutableList<T>? = null
) : Serializable {
}