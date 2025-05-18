package com.wanwuzhinan.mingchang.utils

import com.tencent.mmkv.MMKV

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.uitil
 * ClassName: MMKVUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/12 13:01
 * Email:shiming024@163.com
 * Description:工具类封装 https://blog.csdn.net/weixin_52837854/article/details/117950286
 */
object MMKVUtils {

    private val mv by lazy { MMKV.defaultMMKV() }


//    fun <T> set(key: String, value: T): Boolean {
//        return when (value) {
//            is Int -> mv.encode(key, value) == true
//            is Long -> mv.encode(key, value) == true
//            is Float -> mv.encode(key, value) == true
//            is Double -> mv.encode(key, value) == true
//            is Boolean -> mv.encode(key, value) == true
//            is String -> mv.encode(key, value) == true
//            else -> false
//        }
//    }

    fun getInt(key: String, def: Int = 0): Int = mv.decodeInt(key, def)
    fun getLong(key: String, def: Long = 0): Long = mv.decodeLong(key, def)
    fun getFloat(key: String, def: Float = 0F): Float = mv.decodeFloat(key, def)
    fun getDouble(key: String, def: Double = 0.toDouble()): Double = mv.decodeDouble(key, def)
    fun getBoolean(key: String, def: Boolean = false): Boolean = mv.decodeBool(key, def)
    fun getString(key: String, def: String? = null): String? = mv.decodeString(key, def)

    fun <T> set(key: String, value: T): Boolean {
        return when (value) {
            is Int -> mv.encode(key, value) == true
            is Long -> mv.encode(key, value) == true
            is Float -> mv.encode(key, value) == true
            is Double -> mv.encode(key, value) == true
            is Boolean -> mv.encode(key, value) == true
            is String -> mv.encode(key, value) == true
            else -> mv.encode(key, value.toString()) == true
        }
    }

    /*** 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 */
    fun encode(key: String, any: Any): Boolean {
        val success: Boolean
        when (any) {
            is String -> {
                success = mv.encode(key, any) == true
            }

            is Int -> {
                success = mv.encode(key, any) == true
            }

            is Boolean -> {
                success = mv.encode(key, any) == true
            }

            is Float -> {
                success = mv.encode(key, any) == true
            }

            is Long -> {
                success = mv.encode(key, any) == true
            }

            is Double -> {
                success = mv.encode(key, any) == true
            }

            is ByteArray -> {
                success = mv.encode(key, any) == true
            }

            else -> {
                success = mv.encode(key, any.toString()) == true
            }
        }
        return success
    }


    /**得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值*/
    fun decodeInt(key: String?): Int? {
        return mv.decodeInt(key, 0)
    }

    fun decodeBoolean(key: String?): Boolean {
        return mv.decodeBool(key, false) == true
    }


    fun decodeString(key: String?): String {
        return mv.decodeString(key, "").toString()
    }

    /*** 移除某个key对 ** @param key */
    fun removeKey(key: String) {
        mv.removeValueForKey(key)
    }

    /*** 清除所有key */
    fun clearAll() {
        mv.clearAll()
    }


}