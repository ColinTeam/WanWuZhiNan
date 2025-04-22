package com.comm.net_work.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.gson
 * ClassName: GsonManager
 * Author:ShiMing Shi
 * CreateDate: 2022/9/4 15:45
 * Email:shiming024@163.com
 * Description:
 */
class GsonManager private constructor() {

    companion object{
        @Volatile
        private var instance: GsonManager? = null
           get(){
               synchronized(GsonManager::class.java){
                   if(field == null){
                       field = GsonManager()
                   }
               }
               return field
           }
        @Synchronized
        fun get() : GsonManager{
            return instance!!
        }
    }

    private var mGson: Gson? = null

    init {
        mGson = createGson()
    }

    fun getGson() : Gson{
        return mGson!!
    }

    private fun createGson() : Gson = GsonBuilder()
        .registerTypeAdapter(Int::class.java,IntegerDefaultAdapter())
        .registerTypeAdapter(String::class.java,StringNullAdapter()).create()


    /**
     * json 解析 成List
     * @param json json
     * @param clazz 类
     * @return list
     */
    fun <T> jsonToArrayList(json: String, clazz: Class<T>): ArrayList<T> {
        val type = object : TypeToken<ArrayList<JsonObject?>?>() {}.type
        val jsonObjects = mGson?.fromJson<ArrayList<JsonObject>>(json, type)
        val arrayList = ArrayList<T>()
        if (jsonObjects != null) {
            for (jsonObject in jsonObjects) {
                mGson?.fromJson(jsonObject, clazz)?.let { arrayList.add(it) }
            }
        }
        return arrayList
    }


}