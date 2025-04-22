package com.ssm.comm.data

import com.google.gson.annotations.SerializedName
import com.ssm.comm.ui.base.BaseModel

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.entity
 * ClassName: OrdeWeChatrInfoData
 * Author:ShiMing Shi
 * CreateDate: 2022/9/26 12:25
 * Email:shiming024@163.com
 * Description:
 */
data class OrderWXData(
    var wechat: Wechat
) : BaseModel()

data class Wechat(
    var appId: String,
    var timeStamp: Int,
    var nonceStr: String,
    var prepayid: String,
    var sign: String,
    @SerializedName("package")
    var packageStr: String,
    var partnerid: String,
) : BaseModel()
