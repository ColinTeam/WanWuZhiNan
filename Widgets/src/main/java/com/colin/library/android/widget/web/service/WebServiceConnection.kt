package com.colin.library.android.widget.web.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.colin.library.android.widget.ICallbackFromWebProcessInterface
import com.colin.library.android.widget.IWebProcessInterface
import com.colin.library.android.widget.web.ScrollWebView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 13:23
 *
 * Des   :WebServiceConnection
 */
class WebServiceConnection private constructor() : ServiceConnection {
    private var webProcessInterface: IWebProcessInterface? = null

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WebServiceConnection()
        }
    }

    fun initAidlConnection(context: Context) {
        val intent = Intent(context, WebProcessService::class.java)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        webProcessInterface = IWebProcessInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        webProcessInterface = null
//        initAidlConnection()
    }

    override fun onBindingDied(name: ComponentName?) {
        webProcessInterface = null
//        initAidlConnection()
    }

    fun executeCommand(commandName: String, params: String?, webView: ScrollWebView) {
        try {
            webProcessInterface?.handleWebCommand(
                commandName, params, object : ICallbackFromWebProcessInterface.Stub() {
                    override fun onResult(callbackname: String, response: String?) {
                        webView.handleCallback(callbackname, response)
                    }
                })
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}