package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel


data class GoodsOrderData(
    var goods_id: String,
    var sku_id: Int,//
    var num: String,//
    var goods_car_id: Int//
): BaseModel()