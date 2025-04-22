package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class PayData(
    var order_sn: String,//
    var res: String//
): BaseModel(){

   /* data class resBean(
        var appid: String,
        var time: String,
        var nonce_str: Boolean,
        var packages: Boolean,
        var trade_type: Boolean,
        var sign: Boolean
    ):BaseModel()*/

}
