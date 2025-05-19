package com.colin.library.android.network.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-07 12:42
 *
 * Des   :StringTypeAdapter
 */
class StringTypeAdapter : TypeAdapter<String>() {

    override fun write(write: JsonWriter, value: String?) {
        if (value == null) write.nullValue() else write.value(value)
    }

    override fun read(read: JsonReader): String {
        return try {
            if (read.peek() == JsonToken.NULL) {
                read.nextNull()
                ""
            } else {
                val json = read.nextString()
                if (json == "null") "" else json
            }
        } catch (e: Exception) {
            // 处理可能的异常，例如 JsonSyntaxException
            ""
        }
    }
}
