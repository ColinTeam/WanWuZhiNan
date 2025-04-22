package com.wanwuzhinan.mingchang.data

import com.hpplay.common.asyncmanager.AsyncFileParameter.In


data class ExchangeCodeData(
    var title: String="",
    var price: String="",
    var activity_name: String="",
    var statusName: String="",
    var is_goods: Int,
    var user_start: Long,
    var user_end: Long,
    var goods_idArr :  List<String>
)