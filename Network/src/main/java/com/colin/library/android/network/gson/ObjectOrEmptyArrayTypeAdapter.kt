package com.colin.library.android.network.gson

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-10 15:38
 *
 * Des   :ObjectOrEmptyArrayTypeAdapter
 */
class ObjectOrEmptyArrayTypeAdapter() : TypeAdapter<Any>() {
    private val delegate = Gson().getAdapter(Any::class.java)

    @Throws(IOException::class)
    override fun write(out: JsonWriter?, value: Any?) {
        delegate.write(out, value)
    }


    override fun read(reader: JsonReader): Any? {
        when (reader.peek()) {
            JsonToken.BEGIN_OBJECT -> return delegate.read(reader)
            JsonToken.BEGIN_ARRAY -> {
                reader.beginArray()
                if (!reader.hasNext()) {
                    // 空数组 []
                    reader.endArray()
                    return null
                } else {
                    // 非空数组，不符合预期结构，抛异常
                    reader.endArray()
                    throw JsonSyntaxException("Expected a JSON object but found an array")
                }
            }

            JsonToken.STRING -> {
                val str = reader.nextString().trim()
                if (str == "[]" || str == "[{}]") {
                    return null
                }
                throw JsonSyntaxException("Unexpected string value: $str")
            }

            else -> {
                // 其他类型也视为错误
                reader.skipValue()
                throw JsonSyntaxException("Expected JSON object or empty array but found: ${reader.peek()}")
            }
        }
    }

}


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

    override fun write(out: JsonWriter?, value: T?) {
        gson.toJson(value, clazz, out)
    }
}

class ObjectTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        if (rawType == Any::class.java) {
            return ObjectTypeAdapter(gson, rawType as Class<T>)
        }
        return null
    }
}