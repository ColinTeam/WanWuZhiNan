package com.wanwuzhinan.mingchang.net.repository.comm

import com.comm.net_work.base.BaseRepository
import com.comm.net_work.sign.ParameterSign
import com.wanwuzhinan.mingchang.net.RetrofitClient
import com.ssm.comm.config.Constant
import com.ssm.comm.utils.LogUtils

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

    companion object{
        const val PAGE_SIZE = Constant.PAGE_SIZE
    }

    protected val mService by lazy {
        RetrofitClient.service
    }

    fun addOkHttpCommBuilder(key: String = "",value: String = ""){
        LogUtils.e("Header参数================>$value")
        RetrofitClient.addOkHttpCommBuilder(key, value)
    }


    fun setSignStr(vararg pairs: Pair<String, Any>): String{
        val map = mutableMapOf<String,Any>()
        for(i in pairs.indices){
            map[pairs[i].first] = pairs[i].second
        }
        return setSignStr(map)
    }

    fun setSignStr(map: MutableMap<String,Any>): String{
        //val signature = ParameterSign.signMd5(map)
        //addOkHttpCommBuilder("signature",signature)
        return ParameterSign.signMd5(map)
    }

    fun setSignStr(): String{
        //val signature = ParameterSign.signMd5()
        return ParameterSign.signMd5()
    }
}