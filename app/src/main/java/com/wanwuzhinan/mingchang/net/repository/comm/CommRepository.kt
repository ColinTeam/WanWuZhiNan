package com.wanwuzhinan.mingchang.net.repository.comm

import com.colin.library.android.network.NetworkHelper
import com.comm.net_work.base.BaseRepository
import com.comm.net_work.sign.ParameterSign
import com.ssm.comm.config.Constant
import com.ssm.comm.utils.LogUtils
import com.wanwuzhinan.mingchang.net.ApiService
import com.wanwuzhinan.mingchang.net.RetrofitClient

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

    //    protected val mService by lazy {
//        NetworkHelper.retrofit.create(ApiService::class.java)
//        RetrofitClient.service
//    }
    val mService: ApiService by lazy {
        NetworkHelper.create(ApiService::class.java)
    }

    fun addOkHttpCommBuilder(key: String = "", value: String = "") {
        LogUtils.e("Header参数================>$value")
        RetrofitClient.addOkHttpCommBuilder(key, value)
    }


    fun setSignStr(vararg pairs: Pair<String, Any>): String {
        val map = mutableMapOf<String, Any>()
        for (i in pairs.indices) {
            map[pairs[i].first] = pairs[i].second
        }
        return setSignStr(map)
    }

    fun setSignStr(map: MutableMap<String, Any>): String {
        return ParameterSign.signMd5(map)
    }

    fun setSignStr(): String {
        return ParameterSign.signMd5()
    }
}