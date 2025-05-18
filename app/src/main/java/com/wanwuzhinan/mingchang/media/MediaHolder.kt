package com.wanwuzhinan.mingchang.media

import android.content.ComponentName
import android.content.Context
import android.media.AudioFormat
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.wanwuzhinan.mingchang.media.service.MediaService

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 16:49
 *
 * Des   :MediaHolder
 */
object MediaHolder {
    private lateinit var future: ListenableFuture<MediaController>
    private lateinit var player: Player
    var audioSessionID = AudioFormat.ENCODING_INVALID
    fun initialize(context: Context) {
        val token = SessionToken(context, ComponentName(context, MediaService::class.java))
        val cf = MediaController.Builder(context, token).buildAsync().also { future = it }
        val listener = Runnable { player = cf.get() }
        cf.addListener(listener, ContextCompat.getMainExecutor(context))
    }

    fun getPlayer() = player

    fun release() = MediaController.releaseFuture(future)
}