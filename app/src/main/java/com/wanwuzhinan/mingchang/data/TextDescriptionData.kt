package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class TextDescriptionData(
    var code: Int,
    var msg: String,
    var result: Result
): BaseModel(){

    data class Result(
        var set_title: String,
        var set_cname: String,
        var set_cvalue: String
    ): BaseModel()
}
