package com.ad.img_load.pickerview.bean

import com.ad.img_load.pickerview.wheelview.interfaces.IPickerViewData

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.ad.img_load.pickerview.bean
 * ClassName: CityData
 * Author:ShiMing Shi
 * CreateDate: 2022/10/2 18:44
 * Email:shiming024@163.com
 * Description:
 */
data class CityData(
    var name: String,
    var city: MutableList<CityListData>,
) : IPickerViewData {

    override fun getPickerViewText(): String {
        return this.name
    }
}

data class CityListData(
    var name: String,
    var area: MutableList<String>,
)