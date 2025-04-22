package com.comm.net_work.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.gson
 * ClassName: StringNullAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/9/4 15:51
 * Email:shiming024@163.com
 * Description:
 */
class StringNullAdapter : TypeAdapter<String>(){

    override fun write(out: JsonWriter?, value: String?) {
        if (value == null) {
            out!!.nullValue()
            return
        }
        out!!.value(value)
    }

    override fun read(`in`: JsonReader?): String {
        if (`in`!!.peek() == JsonToken.NULL) {
            `in`.nextNull()
            //原先是返回Null，这里改为返回空字符串
            return ""
        }
        val json = `in`.nextString()
        return if (json == "null") {
            ""
        } else json
    }
}