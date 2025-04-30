package com.wanwuzhinan.mingchang.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.wanwuzhinan.mingchang.service.MediaService
import kotlinx.coroutines.Runnable

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-30 10:56
 *
 * Des   :AppMediaManager
 */
object AppMediaManager {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var player: Player
    fun initMediaController(context: Context) {
        val token = SessionToken(context, ComponentName(context, MediaService::class.java))
        val controller =
            MediaController.Builder(context, token).buildAsync().also { controllerFuture = it }
        val listener = Runnable { player = controller.get() }
        controller.addListener(listener, MoreExecutors.directExecutor())
    }

    fun getPlayer() = player
    fun release() = MediaController.releaseFuture(controllerFuture)
}