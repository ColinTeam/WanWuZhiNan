package com.colin.library.android.widget.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colin.library.android.utils.Log
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 *
 * Des   :仅适用于动态广播，静态广播不适用
 */
abstract class BaseReceiver (listener: OnReceiverListener) : BroadcastReceiver(),
    LifecycleEventObserver {
    val listenerRef: WeakReference<out OnReceiverListener>

    init {
        listener.lifecycle.addObserver(this)
        listenerRef = WeakReference(listener)
    }


    @SuppressLint("InlinedApi")
    override fun onStateChanged(source: LifecycleOwner, event: Event) {
        Log.d("BroadcastReceiver registerReceiver $event")
        when (event) {
            Event.ON_CREATE -> {
                val context = listenerRef.get()?.getContext() ?: return
                context.registerReceiver(this, getIntentFilter(), Context.RECEIVER_NOT_EXPORTED)
            }

            Event.ON_DESTROY -> {
                val context = listenerRef.get()?.getContext() ?: return
                context.unregisterReceiver(this)
            }

            else -> {
            }
        }
    }

    /*子类重写，进行动作捕获监听*/
    abstract fun getIntentFilter(): IntentFilter
}


interface OnReceiverListener : LifecycleOwner {
    fun getContext(): Context?
}