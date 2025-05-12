package com.wanwuzhinan.mingchang.net.repository.comm

import com.colin.library.android.network.NetworkHelper
import com.ssm.comm.config.Constant
import com.ssm.comm.response.BaseRepository
import com.wanwuzhinan.mingchang.net.ApiService


open class CommRepository : BaseRepository() {

    companion object {
        const val PAGE_SIZE = Constant.PAGE_SIZE
    }
    val mService: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }

}