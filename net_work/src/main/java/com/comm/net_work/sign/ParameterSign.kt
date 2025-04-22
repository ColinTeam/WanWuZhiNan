package com.comm.net_work.sign

import android.util.Log
import com.comm.net_work.BuildConfig
import com.comm.net_work.gson.GsonManager
import java.util.*

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.sign
 * ClassName: ParameterSign
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:00
 * Email:shiming024@163.com
 * Description:
 */
object ParameterSign {

    //map 集合参数MD5签名
    fun signMd5(map: MutableMap<String, Any>): String {
        if (map.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        val sortMap = sortMapByKey(map)
        sortMap.forEach { (key, value) ->
            sb.append(key).append(value)
        }
        sb.append(BuildConfig.APP_API_SIGNATURE)
        if(BuildConfig.IS_ENABLE_LOG){
            Log.e("signMd5","签名参数字符串:$sb")
        }
        return Md5Utils.md5(sb.toString())
    }

    //map 接口KEY MD5签名
    fun signMd5(): String {
        return Md5Utils.md5(BuildConfig.APP_API_SIGNATURE)
    }


    //对集合Map进行排序
    private fun sortMapByKey(bodyParams: MutableMap<String, Any>): MutableMap<String, Any> {
        val sortMap = TreeMap<String, Any>(MapKeyComparator())
        sortMap.putAll(bodyParams)
        return sortMap
    }

    //根据可变参数AES加密
    private fun getSignStr(vararg pairs: Pair<String, Any>): String {
        return getSignStr(mutableMapOf(*pairs))
    }

    //根据map参数AES加密
    fun getSignStr(map: Map<String, Any>): String {
        val json = GsonManager.get().getGson().toJson(map)
        if (BuildConfig.IS_ENABLE_LOG) {
            Log.e("ParameterSign", "JSON:$json")
            map.forEach { (key, value) ->
                Log.e("ParameterSign", "KEY:$key")
                Log.e("ParameterSign", "VALUE:$value")
            }
        }
        return AESUtils.encrypt(json)
    }
}