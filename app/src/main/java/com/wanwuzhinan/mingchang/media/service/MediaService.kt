package com.wanwuzhinan.mingchang.media.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wanwuzhinan.mingchang.media.MediaHolder

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 16:35
 *
 * Des   :AudioService
 */

@OptIn(UnstableApi::class)
class MediaService : MediaSessionService(), Player.Listener {
    //    private lateinit var player: ExoPlayer
    private var mediaSession: MediaSession? = null
    //以向其他客户端授予对在创建服务时构建的媒体会话的访问权限。
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        MediaHolder.audioSessionID = player.audioSessionId
    }

    //除了在后台继续播放之外，您还可以在用户关闭应用时停止服务：
    //使用 isPlaybackOngoing() 检查播放是否被视为正在进行，以及前台服务是否已启动。
    override fun onTaskRemoved(rootIntent: Intent?) {
        pauseAllPlayersAndStopSelf()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}