package com.colin.library.android.network.gson

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-10 15:38
 *
 * Des   :ObjectOrEmptyArrayTypeAdapter
 */

class ObjectTypeAdapter<T>(private val gson: Gson, private val clazz: Class<T>) : TypeAdapter<T>() {
    override fun read(reader: JsonReader): T? {
        return when (reader.peek()) {
            JsonToken.BEGIN_OBJECT -> gson.fromJson(reader, clazz)
            JsonToken.BEGIN_ARRAY -> {
                reader.beginArray()
                if (!reader.hasNext()) {
                    reader.endArray()
                    null
                } else {
                    // 确保 reader 状态被正确清理
                    reader.endArray()
                    throw JsonSyntaxException("Expected a JSON object but found an array")
                }
            }

            JsonToken.NULL -> {
                reader.nextNull()
                null
            }

            else -> throw JsonSyntaxException("Expected a JSON object or empty array")
        }
    }

    override fun write(out: JsonWriter, value: T?) {
        gson.toJson(value, clazz, out)
    }
}

@Suppress("UNCHECKED_CAST")
class ObjectTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        if (rawType == Any::class.java) {
            return ObjectTypeAdapter(gson, rawType as Class<T>)
        }
        return null
    }
}



