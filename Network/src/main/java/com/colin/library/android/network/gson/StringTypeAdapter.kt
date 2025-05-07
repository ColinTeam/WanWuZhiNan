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

    override fun write(out: JsonWriter, value: String?) {
        if (value == null) out.nullValue() else out.value(value)
    }

    override fun read(`in`: JsonReader?): String {
        if (`in`!!.peek() == JsonToken.NULL) {
            `in`.nextNull()
            //原先是返回Null，这里改为返回空字符串
            return ""
        }
        val json = `in`.nextString()
        return if (json == "null") "" else json
    }
}