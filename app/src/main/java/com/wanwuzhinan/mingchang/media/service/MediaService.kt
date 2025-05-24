package com.wanwuzhinan.mingchang.media.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wanwuzhinan.mingchang.media.MediaManager

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 16:35
 *
 * Des   :AudioService
 */

@OptIn(UnstableApi::class)
class MediaService : MediaSessionService(), Player.Listener {
    private var mediaSession: MediaSession? = null

    //以向其他客户端授予对在创建服务时构建的媒体会话的访问权限。
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession
    override fun onCreate() {
        super.onCreate()
        // 使用MediaManager的player实例（需确保已初始化）
        mediaSession =
            MediaSession.Builder(this, MediaManager.player).setCallback(mediaSessionCallback)
                .build().also {
                    it.player.playWhenReady = true // 恢复播放状态
                }
    }

    // 后台任务移除时的智能处理
    override fun onTaskRemoved(rootIntent: Intent?) {
        if (mediaSession?.player?.isPlaying == true) {
            // 保持服务运行但移除前台状态
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopSelf()
        }
    }

    override fun onDestroy() {
        // 严格按顺序释放资源
        mediaSession?.let { session ->
            session.player.playWhenReady = false // 先暂停播放
            session.release() // 释放MediaSession
            mediaSession = null
        }
        super.onDestroy()
    }

    private val mediaSessionCallback = object : MediaSession.Callback {
        override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
            if (session.connectedControllers.isEmpty()) {
                stopSelf() // 无连接客户端时自动停止服务
            }
        }
    }
}