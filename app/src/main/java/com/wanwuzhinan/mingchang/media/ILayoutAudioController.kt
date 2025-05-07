package com.wanwuzhinan.mingchang.media

import androidx.lifecycle.Lifecycle
import androidx.media3.common.Player

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-06 21:02
 *
 * Des   :ILayoutAudioController
 */
interface ILayoutAudioController : Player.Listener {
    fun bind(lifecycle: Lifecycle, player: Player)
    fun play()
    fun pause()
    fun toggle()
    fun toggleRepeatMode()
    fun seekTo(progress: Long)
}