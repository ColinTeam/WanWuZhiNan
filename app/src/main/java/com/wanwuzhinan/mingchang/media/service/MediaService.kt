package com.wanwuzhinan.mingchang.media.service

import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 16:35
 *
 * Des   :AudioService
 */

@OptIn(UnstableApi::class)
class MediaService : MediaSessionService(), Player.Listener {
    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        player.audioSessionId
    }
}