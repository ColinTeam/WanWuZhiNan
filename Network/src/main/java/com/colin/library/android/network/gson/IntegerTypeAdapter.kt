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
        json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext?
    ): Int {
        // 处理json为null的情况
        if (json == null) return Constants.ZERO

        // 处理空字符串或"null"的情况
        val jsonString = try {
            json.asString
        } catch (e: IllegalStateException) {
            return Constants.ZERO
        }

        if (jsonString == "" || jsonString == "null") return Constants.ZERO

        // 尝试将json转换为Int
        return try {
            json.asInt
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }
}
