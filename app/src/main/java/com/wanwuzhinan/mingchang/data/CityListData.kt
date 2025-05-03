package com.wanwuzhinan.mingchang.data

import android.os.Parcelable
import com.wanwuzhinan.mingchang.view.indexlib.indexbar.bean.BaseIndexPinyinBean
import kotlinx.parcelize.Parcelize


@Parcelize
data class CityListData(
    var city_id: String,
    var level: String,
    var name: String,
    var pid: String,
    var select: Boolean
) : BaseIndexPinyinBean(), Parcelable {
    override fun getTarget(): String {
        return name
    }
}
