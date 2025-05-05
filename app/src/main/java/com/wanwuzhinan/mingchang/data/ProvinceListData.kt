package com.wanwuzhinan.mingchang.data


data class ProvinceListData(
    val children: ArrayList<ProvinceListData> = ArrayList<ProvinceListData>(),
    val value: Int = 0,
    val label: String = "-"
)