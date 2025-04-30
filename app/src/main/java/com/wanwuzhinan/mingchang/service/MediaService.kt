package com.wanwuzhinan.mingchang.service

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-04-30 10:49
 *
 * Des   :MediaService
 */
class MediaService : MediaSessionService(), Player.Listener {
    private lateinit var mediaSession: MediaSession
    private val player by lazy {
        ExoPlayer.Builder(this@MediaService).build()
    }

    override fun onGetSession(info: MediaSession.ControllerInfo) = mediaSession

    override fun onCreate() {
        super.onCreate()
        player.audioSessionId
    }
}