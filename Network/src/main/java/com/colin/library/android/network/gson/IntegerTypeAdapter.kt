package com.colin.library.android.network.gson

import com.colin.library.android.utils.Constants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-07 12:38
 *
 * Des   :IntegerTypeAdapter
 */
class IntegerTypeAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {
    override fun serialize(
        src: Int, typeOfSrc: Type, context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?
    ): Int {
        //定义为int类型,如果后台返回""或者null,则返回0
        try {
            if (json.asString == "" || json.asString == "null") return Constants.ZERO
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return try {
            json.asInt
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }
}