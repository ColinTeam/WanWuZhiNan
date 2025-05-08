package com.colin.library.android.widget.web.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/8 13:23
 *
 * Des   :WebProcessService
 */
class WebProcessService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return WebProcessManager.Companion.instance
    }
}