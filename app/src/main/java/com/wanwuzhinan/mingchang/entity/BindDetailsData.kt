package com.wanwuzhinan.mingchang.entity

import com.ssm.comm.ui.base.BaseModel
import java.util.ArrayList

data class BindDetailsData(
    var alipay: MutableList<dataBean>,//
    var wechat: ArrayList<dataBean>,//
    var bank: MutableList<dataBean>//
): BaseModel(){

    data class dataBean(
        var auth_name: String,//
        var account: String,//
        var binding_id: String,//
        var bank_name: String,//
        var type: Int
    ):BaseModel()
}
