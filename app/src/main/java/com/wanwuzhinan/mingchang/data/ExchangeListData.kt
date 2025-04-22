package com.wanwuzhinan.mingchang.data


data class ExchangeListData(
    var name: String="",
    var id: String="",
    var image: String="",
    var has_power_start: Long,
    var has_power_end: Long,
    var title: String="",
    var price: String = ""
)