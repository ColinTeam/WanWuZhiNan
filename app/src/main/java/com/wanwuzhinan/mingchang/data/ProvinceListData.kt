package com.wanwuzhinan.mingchang.data

import com.ad.img_load.pickerview.wheelview.interfaces.IPickerViewData

data class ProvinceListData(
    val children: ArrayList<ProvinceListData> = ArrayList<ProvinceListData>(),
    val value: Int = 0,
    val label: String = "-"
): IPickerViewData {

    override fun getPickerViewText(): String {
        return this.label
    }
}