package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class ShopDetailsData(
    var goods_id: String,//
    var name: String,//名称
    var namemin: String,//名称 副标题
    var buy_type: String,//类型 1现金 2积分 3现金+积分
    var price: String,//价格 buy_type=1或2使用
    var jifen: String,//积分 buy_type=3时展示积分数量用此字段
    var wallet: String,//赠送的东西
    var price_original: String,//原价
    var maichu_num: String,//销量
    var thumb: String,//
    var levelnum: Int,//库存
    var consumptionValue: String,//消费值
    var thumbs: MutableList<String>,//商品轮播图
    var detail: MutableList<String>//商品详情图
): BaseModel()
