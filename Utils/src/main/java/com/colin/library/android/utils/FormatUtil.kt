package com.colin.library.android.utils

import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-11-13
 *
 * Des   :各种格式化
 */
object FormatUtil {
    private const val INDENT_SPACES = 4
    private val LINE_SEPARATOR = System.lineSeparator()

    /**
     * 格式化Json 字符串
     */
    @JvmStatic
    fun formatJson(json: Any?): String? {
        if (json == null) return null
        return when (json) {
            is JSONObject -> json.toString(INDENT_SPACES)
            is JSONArray -> json.toString(INDENT_SPACES)
            is String -> {
                when {
                    isJSONObject(json) -> JSONObject(json).toString(INDENT_SPACES)
                    isJSONArray(json) -> JSONArray(json).toString(INDENT_SPACES)
                    else -> json // Return the string as is if it's not JSON
                }
            }

            else -> "$json" // If it's not a JSONObject, JSONArray, or String, convert it to string
        }

    }

    @JvmStatic
    fun formatXml(xml: String?): String? {
        if (xml == null) return null
        try {
            val xmlInput = StreamSource(StringReader(xml))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
            transformer.transform(xmlInput, xmlOutput)
            return xmlOutput.writer.toString().replaceFirst((">").toRegex(), ">$LINE_SEPARATOR")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return xml
    }

    fun isJSONObject(json: String): Boolean {
        return json.startsWith('{') and json.endsWith('}')
    }

    fun isJSONArray(json: String): Boolean {
        return json.startsWith('[') and json.endsWith(']')
    }

}