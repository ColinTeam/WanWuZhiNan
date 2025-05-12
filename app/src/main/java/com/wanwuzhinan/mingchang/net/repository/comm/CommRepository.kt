package com.wanwuzhinan.mingchang.net.repository.comm

import com.colin.library.android.network.NetworkHelper
import com.comm.net_work.base.BaseRepository
import com.ssm.comm.config.Constant
import com.wanwuzhinan.mingchang.net.ApiService

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.net
 * ClassName: CommRepository
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:33
 * Email:shiming024@163.com
 * Description:
 */
open class CommRepository : BaseRepository() {

    companion object {
        const val PAGE_SIZE = Constant.PAGE_SIZE
    }

    val mService: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }


}