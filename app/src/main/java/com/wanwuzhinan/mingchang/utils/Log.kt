package com.wanwuzhinan.mingchang.utils

import android.util.Log
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
 * Des   :简化log输出
 */
object Log {

    private const val VM_STACK = "VMStack.java"
    private var mEnabled: Boolean = true
    private var mLevel: Int = Log.VERBOSE
    private var mTag: String? = null

    /**
     * 设置Log是否开启
     *
     * @param enable 是否开启
     */
    @JvmStatic
    fun setEnabled(enable: Boolean) {
        mEnabled = enable
    }

    /**
     * 设置Log 全局 Tag
     *
     * @param tag 全局Tag
     */
    @JvmStatic
    fun setTag(tag: String?) {
        mTag = tag
    }

    /**
     * 设置打印等级,只有高于该打印等级的log会被打印<br>
     * 打印等级从低到高分别为: Log.VERBOSE < Log.DEBUG < Log.INFO < Log.WARN < Log.ERROR < Log.ASSERT
     *
     * @param level 打印等级
     */
    @JvmStatic
    fun setLevel(level: Int) {
        mLevel = level
    }

    @JvmStatic
    fun v(msg: Any?) = print(Log.VERBOSE, mTag, msg)

    @JvmStatic
    fun v(tag: String, msg: Any?) = print(Log.VERBOSE, tag, msg)

    @JvmStatic
    fun d(msg: Any?) = print(Log.DEBUG, mTag, msg)

    @JvmStatic
    fun d(tag: String, msg: Any?) = print(Log.DEBUG, tag, msg)

    @JvmStatic
    fun i(msg: Any?) = print(Log.INFO, mTag, msg)

    @JvmStatic
    fun i(tag: String, msg: Any?) = print(Log.INFO, tag, msg)

    @JvmStatic
    fun w(msg: Any?) = print(Log.WARN, mTag, msg)

    @JvmStatic
    fun w(tag: String, msg: Any?) = print(Log.WARN, tag, msg)

    @JvmStatic
    fun e(msg: Any?) = print(Log.ERROR, mTag, msg)

    @JvmStatic
    fun e(tag: String, msg: Any?) = print(Log.ERROR, tag, msg)

    @JvmStatic
    fun a(msg: Any?) = print(Log.ASSERT, mTag, msg)

    @JvmStatic
    fun a(tag: String, msg: Any?) = print(Log.ASSERT, tag, msg)

    @JvmStatic
    fun json(json: Any?) = print(mLevel, mTag, formatJson(json))

    @JvmStatic
    fun json(tag: String, json: Any?) = print(mLevel, tag, formatJson(json))

    @JvmStatic
    fun xml(xml: String?) = print(mLevel, mTag, formatXml(xml))

    @JvmStatic
    fun xml(tag: String, xml: String?) = print(mLevel, tag, formatXml(xml))

    @JvmStatic
    fun log(t: Throwable?) = print(Log.ERROR, mTag, Log.getStackTraceString(t))

    @JvmStatic
    fun log(msg: Any?) = print(mLevel, mTag, msg)

    private fun print(level: Int, tag: String?, msg: Any?): Int {
        if (!mEnabled || level < mLevel) return -1
        val logTag = tag ?: getTag(Thread.currentThread().stackTrace)
        return when (level) {
            Log.VERBOSE -> Log.v(logTag, "$msg")
            Log.DEBUG -> Log.d(logTag, "$msg")
            Log.INFO -> Log.i(logTag, "$msg")
            Log.WARN -> Log.w(logTag, "$msg")
            Log.ERROR -> Log.e(logTag, "$msg")
            Log.ASSERT -> Log.wtf(logTag, "$msg")
            else -> -1
        }
    }

    private fun getTag(traces: Array<StackTraceElement>): String {
        val index = if (traces.getOrNull(0)?.fileName == VM_STACK) 4 else 3
        return classInfo(traces.getOrElse(index) { traces[3] })
    }

    private fun classInfo(element: StackTraceElement): String {
        return "${element.fileName}:${element.lineNumber}"
    }

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