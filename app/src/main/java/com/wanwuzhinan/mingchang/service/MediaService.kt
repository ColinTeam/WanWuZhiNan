package com.wanwuzhinan.mingchang.service

import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.wanwuzhinan.mingchang.media.AppMediaManager

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-30 10:49
 *
 * Des   :MediaService
 */
@OptIn(UnstableApi::class)
class MediaService : MediaSessionService(), Player.Listener {
    private lateinit var mediaSession: MediaSession
    private val player by lazy {
        ExoPlayer.Builder(this@MediaService).build()
    }

    override fun onGetSession(info: MediaSession.ControllerInfo) = mediaSession

    override fun onCreate() {
        super.onCreate()
        AppMediaManager.audioSessionId = player.audioSessionId
        mediaSession = MediaSession.Builder(this,player).build()
        player.addListener(this)
    }

    override fun onDestroy() {
        mediaSession.release()
        player.setVideoSurface(null)
        player.release()
        super.onDestroy()
    }
}