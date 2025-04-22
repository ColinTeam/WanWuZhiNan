package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class SettingData(
    var image: Int,
    var title: String,
    var subTitle: String = "",
    var select: Boolean=false,
): BaseModel()