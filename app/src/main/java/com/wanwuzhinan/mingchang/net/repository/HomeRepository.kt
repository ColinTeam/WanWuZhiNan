package com.wanwuzhinan.mingchang.net.repository

import com.comm.net_work.entity.ApiResponse
import com.wanwuzhinan.mingchang.net.repository.comm.CommRepository
import com.ssm.comm.data.VersionData

/**
 * Company:AD
 * ProjectName: 绿色生态
 * Package: com.green.ecology.net.repository
 * ClassName: UserRepository
 * Author:ShiMing Shi
 * CreateDate: 2022/12/31 17:48
 * Email:shiming024@163.com
 * Description:
 */
class HomeRepository : CommRepository() {

    //获取版本号
    suspend fun getVersion(): ApiResponse<VersionData> {
        val signature = setSignStr()
        return executeHttp { mService.getVersion(signature) }
    }

}