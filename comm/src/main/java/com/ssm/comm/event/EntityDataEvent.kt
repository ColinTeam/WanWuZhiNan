package com.ssm.comm.event

import com.ssm.comm.ui.base.BaseModel

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
data class EntityDataEvent constructor(var type: Int, var data: BaseModel) {

    companion object {

    }

}