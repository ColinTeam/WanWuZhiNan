package com.wanwuzhinan.mingchang.data

import com.colin.library.android.widget.picker.IPickerText


data class ProvinceListData(
    val children: ArrayList<ProvinceListData> = ArrayList<ProvinceListData>(),
    val value: Int = 0,
    val label: String = "-"
) : IPickerText {

    override fun showText(): String {
        return this.label
    }
}