package com.ssm.comm.data

import com.ssm.comm.ui.base.BaseModel

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.entity
 * ClassName: VersionData
 * Author:ShiMing Shi
 * CreateDate: 2022/9/20 19:38
 * Email:shiming024@163.com
 * Description:
 */
data class VersionData(
    var ios: Ios,
    var android: Android
): BaseModel()

data class Ios(
    var version_name: String,
    var version_code: String,
    var version_url: String,
    var version_desc: String,
    var version_upgrade: String
): BaseModel()

data class Android(
    var version_name: String,
    var version_code: String = "1.0.0",
    var version_url: String,
    var version_desc: String,
    var version_upgrade: String = "1"
): BaseModel()


