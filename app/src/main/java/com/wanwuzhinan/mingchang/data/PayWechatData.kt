package com.wanwuzhinan.mingchang.data

import com.ssm.comm.data.Wechat
import com.ssm.comm.ui.base.BaseModel

data class PayWechatData(
    var order_sn: String,//
    var res: Wechat//
): BaseModel()
