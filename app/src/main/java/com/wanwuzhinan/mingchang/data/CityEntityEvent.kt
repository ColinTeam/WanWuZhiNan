package com.wanwuzhinan.mingchang.data

import android.os.Parcelable
import com.ssm.comm.ui.base.BaseModel
import kotlinx.android.parcel.Parcelize

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.event
 * ClassName: MessageEvent
 * Author:ShiMing Shi
 * CreateDate: 2022/9/20 16:11
 * Email:shiming024@163.com
 * Description:
 */
@Parcelize
data class CityEntityEvent (
    var ProvinceCode: String = "",
    var Province: String = "",
    var cityCode: String = "",
    var city: String = "",
    var areaCode: String = "",
    var area: String = ""
) : BaseModel(), Parcelable
