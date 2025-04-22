package com.comm.net_work.entity

import java.io.Serializable

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net.entity
 * ClassName: ApiResponse
 * Author:ShiMing Shi
 * CreateDate: 2022/9/3 17:27
 * Email:shiming024@163.com
 * Description:
 */
open class ApiBaseResponse<T>(
    open val code: Int? = null,
    open val msg: String = "",
    open val error: Throwable? = null,
) : Serializable {

    val isSuccess: Boolean get() = code == 0


}