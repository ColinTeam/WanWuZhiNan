package com.wanwuzhinan.mingchang.net.repository

import com.comm.net_work.entity.ApiResponse
import com.wanwuzhinan.mingchang.data.AddressData
import com.wanwuzhinan.mingchang.data.CityListData
import com.wanwuzhinan.mingchang.net.repository.comm.CommRepository

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
class ShopRepository : CommRepository() {

    //获取我的收货地址
    suspend fun getAddressList(): ApiResponse<MutableList<AddressData>> {
        val signature = setSignStr()
        return executeHttp { mService.getAddressList(signature) }
    }

    //添加我的收货地址
    suspend fun addAddress(map: MutableMap<String, Any>): ApiResponse<MutableList<String>> {
        val signature = setSignStr(map)
        return executeHttp { mService.addAddress(signature, map) }
    }

    //获取我的默认收货地址
    suspend fun getDefaultAddress(): ApiResponse<AddressData> {
        val signature = setSignStr()
        return executeHttp { mService.getDefaultAddress(signature) }
    }

    //获取系统城市列表
    suspend fun getCityList(city_id: String): ApiResponse<MutableList<CityListData>> {
        val signature = setSignStr(Pair("city_id", city_id))
        return executeHttp { mService.getCityList(signature, city_id) }
    }

}