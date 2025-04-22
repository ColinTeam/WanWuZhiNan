package com.ssm.comm.event

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
data class MessageEvent constructor(var type: Int, var msg: String) {

    companion object {
        const val UPDATE_PERSONAL_CENTER = 1000
        const val LOGIN_EXPIRED = 1001
        const val APP_VERSION_UPDATE = 1002
        const val SELECT_CITY = 1003//选择城市
        const val ADDRESS_MANAGE = 1004//选择地址
        const val ADDRESS_EDIT = 1005//编辑地址列表 关心
        const val UPDATE_PROGRESS = 1006//更新学习进度
        const val UPDATE_NIGHT = 1007//学完
        const val EXCHANGE_COURSE = 1008//兑换课程
        const val UPDATE_USERINFO = 1009//个人信息
    }

}