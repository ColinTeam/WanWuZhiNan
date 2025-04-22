package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class OrderInitData(
    var address: AddressData,
    var wallet: String,//用户积分
    var all_price: String,//总共所需现金 就是真实支付 支付宝/微信
    var goods: MutableList<goodsBean>,//
    var jifen: String,//总共所需总积分 不包含运费
    var all_yunfei: String//运费
): BaseModel(){

    data class goodsBean(
        var name: String,//
        var thumb: String,
        var price: String,//
        var price_all: String,//
        var num: String//
    ):BaseModel()
}
