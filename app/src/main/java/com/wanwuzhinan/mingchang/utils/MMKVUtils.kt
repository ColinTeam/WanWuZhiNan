package com.wanwuzhinan.mingchang.utils

import android.os.Parcelable
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
object  MMKVUtils {

    var mv: MMKV? = null

    init{
        mv = MMKV.defaultMMKV()
    }

    /*** 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 */
    fun encode(key: String,any: Any): Boolean{
        val success: Boolean
        when(any){
            is String ->{
                success = mv?.encode(key, any) == true
            }
            is Int ->{
                success = mv?.encode(key, any)  == true
            }
            is Boolean ->{
                success = mv?.encode(key, any) == true
            }
            is Float ->{
                success = mv?.encode(key, any) == true
            }
            is Long ->{
                success = mv?.encode(key, any) == true
            }
            is Double ->{
                success = mv?.encode(key, any) == true
            }
            is ByteArray ->{
                success = mv?.encode(key, any) == true
            }
            else ->{
                success = mv?.encode(key, any.toString()) == true
            }
        }
        return success
    }

    fun encodeSet(key: String?,sets: Set<String?>?){
        mv?.encode(key,sets)
    }

    fun encodeParcelable(key: String?,obj: Parcelable?){
        mv?.encode(key,obj)
    }

    /**得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值*/
    fun decodeInt(key: String?): Int?{
        return mv?.decodeInt(key,0)
    }

    fun decodeDouble(key: String?): Double{
        return mv?.decodeDouble(key,0.00)!!
    }

    fun decodeLong(key: String?): Long{
        return mv?.decodeLong(key,0L)!!
    }

    fun decodeBoolean(key: String?): Boolean{
        return mv?.decodeBool(key,false) == true
    }

    fun decodeBooleanTure(key: String?, defaultValue: Boolean): Boolean {
        return mv?.decodeBool(key, defaultValue) == true
    }


    fun decodeFloat(key: String?): Float{
        return mv?.decodeFloat(key,0.0f)!!
    }

    fun decodeString(key: String?): String{
        return mv?.decodeString(key,"").toString()
    }

    fun decodeStringDef(key: String?, defaultValue: String): String {
        return mv?.decodeString(key, defaultValue).toString()
    }

    fun decodeStringSet(key: String?): Set<String>{
        return mv?.decodeStringSet(key,emptySet()) as Set<String>
    }

    fun <T: Parcelable> decodeParcelable(key: String,tClass: Class<T>): T?{
        return mv?.decodeParcelable(key,tClass)
    }

    /*** 移除某个key对 ** @param key */
    fun removeKey(key: String){
        mv?.removeValueForKey(key)
    }

    /*** 清除所有key */
    fun clearAll(){
        mv?.clearAll()
    }



}