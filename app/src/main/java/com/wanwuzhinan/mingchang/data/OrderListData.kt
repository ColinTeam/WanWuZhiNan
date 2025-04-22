package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class OrderListData(
    var more: String,
    var data: MutableList<dataBean>
): BaseModel(){

    data class dataBean(
        var order_id: String,//
        var orderno: String,//
        var order_sn: String,//
        var goods_id: String,//
        var thumb: String,//
        var num: String,//
        var address_name: String,//
        var address_mobile: String,//
        var address_info: String,//
        var price_all: String,//
        var other_all: String,//
        var goods_name: String,//
        var sku_name: String,//
        var status: Int,//状态 1待支付 2待发货 3待收货 4已完成 5已取消
        var status2: String,//状态 0没有 1待评价 2已评价 3申请退款 4已收货 5已退款
        var time: String,//	下单时间
        var paytype: Int,//	支付方式 1支付宝 2微信 3银行卡 4积分 5微信小程序
        var paytime: String,//支付时间
        var wuliu_name: String,//
        var wuliu_num: String,//
        var ok_time: String,//完成时间
        var wuliu_fahuo_time: String,//	发货时间
        var shouhuo_auto: String,//自动收货时间
        var time_buy: String//待支付时间
    ):BaseModel()
}
