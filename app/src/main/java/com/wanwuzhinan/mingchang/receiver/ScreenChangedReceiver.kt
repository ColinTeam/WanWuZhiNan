package com.wanwuzhinan.mingchang.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.colin.library.android.utils.Log
import com.colin.library.android.widget.base.BaseReceiver
import com.colin.library.android.widget.base.OnReceiverListener

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-25
 *
 * Des   :屏幕息屏动作，监听
 */
class ScreenChangedReceiver(listener: OnScreenChangedListener) : BaseReceiver(listener) {
    companion object {
        /*Activity 绑定广播*/
        fun bind(listener: OnScreenChangedListener): ScreenChangedReceiver {
            return ScreenChangedReceiver(listener)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ScreenChangedReceiver", "onReceive:${intent?.action}")
        val action = intent?.action ?: return
        val listener = listenerRef.get() ?: return
        (listener as OnScreenChangedListener).screenChanged(action)
    }

    /*屏幕息屏动作接口回调*/
    interface OnScreenChangedListener : OnReceiverListener {
        fun screenChanged(action: String)
    }

    override fun getIntentFilter() = IntentFilter().apply {
        addAction(Intent.ACTION_SCREEN_ON)
        addAction(Intent.ACTION_SCREEN_OFF)
        addAction(Intent.ACTION_USER_PRESENT)
    }
}