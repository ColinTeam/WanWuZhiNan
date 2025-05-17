package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

/**
 * author:
 * date: 2024/8/20
 * constraints:
 */
data class UploadProgressEvent(
    var lesson_id: String = "0",
    var start_second: Int = 0,
    var end_second: Int = 0,
) : BaseModel()