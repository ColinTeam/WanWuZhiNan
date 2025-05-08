package com.colin.library.android.widget.web.service

import com.colin.library.android.widget.ICallbackFromWebProcessInterface
import com.colin.library.android.widget.IWebProcessInterface
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.ServiceLoader

data class JsParam(
    val name: String, val json: JsonObject
)

interface Command {
    fun name(): String
    fun execute(
        parameters: Map<*, *>?, callback: ICallbackFromWebProcessInterface? = null
    )
}

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/8 13:24
 *
 * Des   :WebProcessManager
 */
class WebProcessManager private constructor() : IWebProcessInterface.Stub() {
    private val mCommands: HashMap<String, Command> = HashMap<String, Command>()

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WebProcessManager()
        }
    }


    init {
        // 收集使用了注解的类
        val serviceLoader = ServiceLoader.load(Command::class.java)
        for (command in serviceLoader) {
            if (!mCommands.containsKey(command.name())) {
                mCommands[command.name()] = command
            }
        }
    }


    override fun handleWebCommand(
        commandName: String, jsonParams: String?, callback: ICallbackFromWebProcessInterface?
    ) {
        executeCommand(
            commandName, Gson().fromJson<Map<*, *>>(
                jsonParams, MutableMap::class.java
            ), callback
        )
    }


    private fun executeCommand(
        commandName: String, params: Map<*, *>?, callback: ICallbackFromWebProcessInterface? = null
    ) {
        mCommands[commandName]?.execute(params, callback)
    }

}