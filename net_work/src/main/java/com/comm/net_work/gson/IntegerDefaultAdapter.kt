package com.comm.net_work.gson

import com.google.gson.*
import java.lang.reflect.Type

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.gson
 * ClassName: IntegerDefaultAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/9/4 15:49
 * Email:shiming024@163.com
 * Description:
 */
class IntegerDefaultAdapter : JsonSerializer<Int>,JsonDeserializer<Int> {

    override fun serialize(
        src: Int?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int {
        //定义为int类型,如果后台返回""或者null,则返回0
        try {
            if (json!!.asString == "" || json.asString == "null") {
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return try {
            json!!.asInt
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }
}