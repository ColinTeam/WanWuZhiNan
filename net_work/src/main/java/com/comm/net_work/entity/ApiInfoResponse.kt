package com.comm.net_work.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ApiInfoResponse<T>(

    open val info: T? = null
) : Serializable {


}