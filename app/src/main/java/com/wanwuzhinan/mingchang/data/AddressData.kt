package com.wanwuzhinan.mingchang.data

import android.os.Parcelable
import com.ssm.comm.ui.base.BaseModel

@kotlinx.parcelize.Parcelize
data class AddressData(
    var address_id: String,//
    var provinceCode: String,//
    var provinceName: String,//
    var cityCode: String,//
    var cityName: String,//
    var areaCode: String,//
    var areaName: String,//
    var name: String,//
    var mobile: String,//
    var cont: String,//
    var is_moren: Int,//是否默认 1默认 0不是
): BaseModel(), Parcelable
